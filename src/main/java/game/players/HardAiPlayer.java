package game.players;

import game.Dictionary;
import game.Game;
import game.components.Board;
import game.components.BoardField;
import game.components.Tile;

import java.util.*;

/**
 * @author yuzun Hard AI Player actor, interacting with game
 *     <p>Inspiration by - "The World's Fastest Scrabble Program" by ANDREW W. APPEL AND GUY J.
 *     JACOBSON
 */
public class HardAiPlayer extends AiPlayer {

  private static Node
      root; // Root of Word tree built by dictionary (see below) if multiple hard bots exist, only
  // once created!
  // Only used for hard ai, I dont know what Nico will use :)
  private final List<String> possibleWords = new ArrayList<>(); // possible words in turn 1
  private List<Placement> bestPlacements = new ArrayList<>(); // best placement

  public HardAiPlayer() {
    super(DIFFICULTY.HARD);
  }

  /** */
  @Override
  public void joinGame(Game game) {
    super.joinGame(game); // assign game to this player
    if (root == null) { // create only first time
      buildTree(game.getDictionary()); // Build word tree
    }
  }
  /**
   * Main method which is triggered by the game instance All computations from start of round till
   * end need to be done in here! START COMPUTATION in THREAD as to not block any functionalities of
   * server
   */
  @Override
  public void think(Board gameBoard, Dictionary dictionary) {
    new Thread(() -> thinkInternal(gameBoard, dictionary));
  }

  /**
   * Main method which is triggered by the game instance All computations from start of round till
   * end need to be done in here!
   */
  public void thinkInternal(Board gameBoard, Dictionary dictionary) {
    Board board = new Board(gameBoard); // Deep copy of game board
    bestPlacements.clear();
    possibleWords.clear();

    // fill placements with best computed solution
    if (game.getRoundNumber() == 1) { // First round --> think different since no anchors
      computeFirstRound(board); // fills placements with best computed solution
    } else {
      compute(board);
    }

    // if placement could be found --> execute it
    if (bestPlacements.size() > 0) {

      // play placements (with a little time in between to simulate single placements)
      for (Placement placement : bestPlacements) {
        placeTile(placement.getTile(), placement.getRow(), placement.getColumn());
        rack.remove(placement.getTile());
        try {
          Thread.sleep(0); // TODO INCREASE
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

    } else { // if placement could not be found --> randomly exchange tiles
      List<Tile> tilesToExchange = new ArrayList<>();
      for (int i = 0; i < game.getBagSize(); i++) {
        rack.forEach(
            tile -> {
              if (Math.random() > 0.5) { // 50 % chance to be exchanged
                tilesToExchange.add(tile);
              }
            });
      }
      exchange(tilesToExchange);
    }

    submit();
  }

  /** Reset board */
  private void resetBoard(Board board, List<Placement> placements) {
    placements.forEach(
        placement -> board.placeTile(null, placement.getRow(), placement.getColumn()));
  }

  /**
   * Builds a word tree & Reduces its complexity identifying duplicate subtrees (MUUUCH SMALLER)
   * Runtime: n words in Dictionary, k is max amount of letters in a word, 26 characters in alphabet
   * --> O(n * k * 26)
   */
  private void buildTree(Dictionary dictionary) {
    String[] words = dictionary.getWordsAsArray();
    root = new Node(false); // Empty character literal for root

    // BUILD TREE
    for (String word : words) {

      Node helper = root; // traverses over nodes

      // iterate over every character of word
      for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        Node child = helper.getChild(c);
        if (child == null) { // if no child --> create one
          helper.add(c, i == word.length() - 1);
        }
        helper = helper.getChild(c);
      }
    }

    // TODO REMOVE DUPLICATE SUBTREES --> Saving a lot of space
  }

  /**
   * Called if first round. Tries to build word with tiles on hand
   *
   * @param board copy of game board
   */
  private void computeFirstRound(Board board) {
    possibleWords.clear();

    LinkedList<Character> characters = new LinkedList<>();
    rack.forEach(tile -> characters.add(tile.getLetter())); // fill list of characters
    LinkedList<Character> pattern = new LinkedList<>();
    for (int i = 0; i < characters.size(); i++) {
      pattern.add('#');
    }
    root.calculatePossibleWords(pattern, new LinkedList<>(), characters);
    System.out.println(
        super.getProfile().getName() + " found " + possibleWords.size() + " possible words");

    // For every possible word try placing and evaluate
    int maxScore = 0;
    for (String word : possibleWords) {
      List<Placement> placements = new ArrayList<>();

      for (int i = 0; i < word.length(); i++) {
        Tile tile = getTile(word.charAt(i));
        board.placeTile(tile, 7 + i, 7); // place vertically
        placements.add(new Placement(tile, 7 + i, 7));
      }

      int score = evaluatePlacement(board, placements); // evaluate placement
      if (maxScore < score) {
        bestPlacements = placements;
        maxScore = score;
      }
    }
    System.out.println("Max Score calculated to be: " + maxScore);
  }

  /**
   * Computes best possible placement. Reduces dimensions to one by first checking only horizontal,
   * then vertical
   *
   * @param board copy of game board
   */
  private void compute(Board board) {

    // ==== COLLECT PLACEMENTS
    List<List<Placement>> allPossiblePlacements = new ArrayList<>();

    // Traverse through every row
    for (int row = 0; row < Board.BOARD_SIZE; row++) {
      int col = 0;
      while (col < Board.BOARD_SIZE
          && !board.isEmpty(row, col)) { // traverse columns till field not empty
        col++;
      }
      if (col >= Board.BOARD_SIZE) {
        continue;
      }

      // Build prefix
      List<Character> word = new ArrayList<>(); // Represents the word on board
      while (col < Board.BOARD_SIZE && !board.isEmpty(row, col)) { // fill 'word'
        word.add(board.getTile(row, col++).getLetter());
      }

      // Check what is maximum amount of tiles which can be placed after (Till out of bounds
      int spaceRemaining = Board.BOARD_SIZE - col - 1;

      // Check no placement before, only after
      Node startNode = root.traverseTo(word); // CANNOT BE NULL since word must have existed
      Node helper = startNode;

      // Check what is maximum amount of tiles which can be placed after
      // Check for no placement before but after
      List<Character> rackLetters = new ArrayList<>();
      rack.forEach(tile -> rackLetters.add(tile.getLetter()));
      calculatePossibleWords(startNode, rackLetters, word, col);
    }
  }

  /**
   * Recursive Method, checks which words are possible
   *
   * @param node Node at which we start
   * @param characters characters representing tiles on hand (which can be used to complete the
   *     word)
   * @param path list of characters already visited
   * @param depth Max characters which can be attached
   */
  private void calculatePossibleWords(
      Node node, List<Character> characters, List<Character> path, int depth) {
    if (depth <= 0) {
      return;
    }
    // if marked as end --> add to possible words
    if (node.isEnd()) {
      StringBuilder builder = new StringBuilder();
      path.forEach(builder::append);
      possibleWords.add(builder.toString());
    }

    for (int i = 0; i < characters.size(); i++) {
      Node child = node.getChild(characters.get(i));
      if (child != null) { // If child exists --> calculate possible word of it
        char c = characters.remove(i); // remove to "Simulate"
        path.add(c);
        calculatePossibleWords(child, characters, path, depth - 1);
        path.remove(path.size() - 1);
        characters.add(c);
      }
    }
  }

  /** Returns a tile with given letter */
  private Tile getTile(char letter) {
    for (Tile tile : rack) {
      if (tile.getLetter() == letter) {
        return tile;
      }
    }
    return null; // should never be reached since method only called with values known to be in hand
  }

  /**
   * Simulates placements on copy of board, evaluates score, resets board back to state prior to
   * placements
   *
   * @param board board object
   * @param placements placements to be simulated and scored
   * @return score of placements
   */
  private int evaluatePlacement(Board board, List<Placement> placements) {
    List<BoardField> boardPlacements = new ArrayList<>();
    placements.forEach(
        placement -> {
          board.placeTile(placement.getTile(), placement.getRow(), placement.getColumn());
          boardPlacements.add(board.getField(placement.getRow(), placement.getColumn()));
        });
    int score = board.evaluateScore(boardPlacements);
    resetBoard(board, placements);
    return score;
  }

  /**
   * A tree built by the bot at the start of game (PREFIX-/SUFFIX-TREE) Only used here for this bot,
   * I dont know what Nico will use :)
   */
  private class Node {
    private final Map<Character, Node> children; // Children nodes
    private final boolean end; // true if word (can) end here

    Node(boolean end) {
      children = new HashMap<>();
      this.end = end;
    }

    /** Adds new node with given letter to children */
    public void add(char letter, boolean end) {
      children.put(letter, new Node(end));
    }

    public Node getChild(char letter) {
      return children.get(letter);
    }

    /** Traverse to given prefix. Recursive */
    public Node traverseTo(List<Character> characters) {
      if (characters.size() == 0) {
        return this;
      }
      char letter = characters.get(0);
      Node child = children.get(letter);
      if (child == null) {
        return null;
      }
      characters.remove(0); // remove from prefix
      Node toReturn = child.traverseTo(characters); // search recursively
      characters.add(0, letter); // put it back at first as to preserve list
      return toReturn;
    }

    /** true if word (can) end here */
    public boolean isEnd() {
      return end;
    }

    /** PATTERN: "HE##0' */
    private void calculatePossibleWords(
        LinkedList<Character> pattern,
        LinkedList<Character> path,
        LinkedList<Character> characters) {
      if (pattern.size() == 0) { // if full add to possible words
        StringBuilder builder = new StringBuilder();
        path.forEach(builder::append);
        possibleWords.add(builder.toString());
        return;
      }

      if (end) { // if end node --> add
        StringBuilder builder = new StringBuilder();
        path.forEach(builder::append);
        possibleWords.add(builder.toString());
      }

      char letter = pattern.removeFirst(); // next letter trying to fill
      if (letter != '#') { // if already placed
        path.addLast(letter);
        Node child =
            children.get(letter); // Not null since it must have been defined in the dictionary
        child.calculatePossibleWords(pattern, path, characters);
        path.removeLast();

      } else { // if to be placed try everything out

        for (int i = 0; i < characters.size(); i++) { // try out for every child
          Node child = children.get(characters.get(i));
          if (child != null) {
            char c = characters.get(i);
            path.addLast(c);
            characters.remove(i);
            child.calculatePossibleWords(pattern, path, characters);
            characters.add(c);
            path.removeLast();
          }
        }
      }
      pattern.addFirst(letter);
    }
  }

  /** Class for single placement */
  private class Placement {

    private final int row;
    private final int column;
    private final Tile tile;

    Placement(Tile tile, int row, int column) {
      this.tile = tile;
      this.row = row;
      this.column = column;
    }

    public int getRow() {
      return row;
    }

    public int getColumn() {
      return column;
    }

    public Tile getTile() {
      return tile;
    }
  }
}
