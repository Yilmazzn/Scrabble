package game.players;

import game.Dictionary;
import game.Game;
import game.components.Board;
import game.components.BoardField;
import game.components.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yuzun Hard AI Player actor, interacting with game
 *     <p>Inspiration by - "The World's Fastest Scrabble Program" by ANDREW W. APPEL AND GUY J.
 *     JACOBSON
 */
public class HardAiPlayer extends AiPlayer {

  private static Node
      root; // Root of Word tree built by dictionary (see below) if multiple hard bots exist, only
  // // once created !
  // Only used for hard ai, I dont know what Nico will use :)
  private final List<String> possibleWords = new ArrayList<>(); // possible words in turn

  private List<Placement> bestPlacements = new ArrayList<>(); // best placement

  private long startTime; // Start time of think method (used to track time it took)
  // once created !

  public HardAiPlayer() {
    super(DIFFICULTY.HARD);
  }

  /** */
  @Override
  public void joinGame(Game game) {
    super.joinGame(game); // assign game to this player
    if (root == null) { // create only first time
      root = buildTree(game.getDictionary()); // Build word tree
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
    startTime = System.currentTimeMillis(); // Init start time

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
        placement -> {
          board.placeTile(null, placement.getRow(), placement.getColumn());
        });
  }

  /**
   * Builds a word tree & Reduces its complexity identifying duplicate subtrees (MUUUCH SMALLER)
   * Runtime: n words in Dictionary, k is max amount of letters in a word, 26 characters in alphabet
   * --> O(n * k * 26)
   */
  private Node buildTree(Dictionary dictionary) {
    String[] words = dictionary.getWordsAsArray();
    Node root = new Node(Character.MIN_VALUE, false); // Empty character literal for root

    // BUILD TREE
    for (String word : words) {

      Node helper = root; // traverses over nodes

      // iterate over every character of word
      for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        Node child = helper.getChild(c);
        if (child == null) { // if no child --> create one
          child = new Node(c, i == word.length() - 1);
          helper.add(child);
        }
        helper = child;
      }
    }

    // TODO DUPLICATE SUBTREES
    return root;
  }

  /**
   * Called if first round. Tries to build word with tiles on hand
   *
   * @param board copy of game board
   */
  private void computeFirstRound(Board board) {
    List<Character> characters = new ArrayList<>();
    rack.forEach(tile -> characters.add(tile.getLetter())); // fill list of characters

    possibleWords.clear();
    calculatePossibleWords(root, characters, new ArrayList<>(), 7);
    System.out.println("Found " + possibleWords.size() + " possible words");

    // sort list by length to check longest ones first
    Collections.sort(possibleWords, (word1, word2) -> word2.length() - word1.length());

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
  private void compute(Board board) {}

  /**
   * Recursive Method, checks which words are possible
   *
   * @param node Node at which we start
   * @param characters characters representing tiles on hand (which can be used to complete the
   *     word)
   * @param path list of characters already visited
   * @param depth Max characters which can be attached
   * @return list of words possible with given prefix and RACK
   */
  private void calculatePossibleWords(
      Node node, List<Character> characters, List<Character> path, int depth) {
    if (depth <= 0) {
      return;
    }

    // if marked as end --> add to possible words
    if (node.isEnd()) {
      StringBuilder builder = new StringBuilder();
      path.forEach(c -> builder.append(c));
      possibleWords.add(builder.toString());
    }

    for (int i = 0; i < characters.size(); i++) {
      Node child = node.getChild(characters.get(i));
      if (child != null) { // If child exists --> calculate of it
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

  /** A tree built by the bot at the start of game (PREFIX-/SUFFIX-TREE) */
  private class Node {
    private final char letter; // Letter the node represents
    private final List<Node> children; // Children nodes
    private final boolean end; // true if word (can) end here

    Node(char letter, boolean end) {
      children = new ArrayList<>();
      this.letter = letter;
      this.end = end;
    }

    public char getLetter() {
      return letter;
    }

    /** Adds new node with given letter to children */
    public void add(Node n) {
      children.add(n);
    }

    /** returns child which contains given letter (null if none) */
    public Node getChild(char letter) {
      for (Node childNode : children) {
        if (childNode.getLetter() == letter) {
          return childNode;
        }
      }
      return null;
    }

    /** returns children */
    public List<Node> getChildren() {
      return children;
    }

    /** true if word (can) end here */
    public boolean isEnd() {
      return end;
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
