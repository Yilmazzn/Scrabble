package game.players;

import game.Dictionary;
import game.Game;
import game.components.Board;
import game.components.BoardField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
  private final List<BoardField> placements = new LinkedList<>();

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
   * end need to be done in here!
   */
  @Override
  public void think(Board gameBoard, Dictionary dictionary) {
    Board board = new Board(gameBoard); // Deep copy of game board
    placements.clear();
    startTime = System.currentTimeMillis(); // Init start time

    System.out.println("Before AI Move");
    printBoard(board);

    // fill placements with best computed solution
    if (game.getRoundNumber() == 1) { // First round --> think different since no anchors
      computeFirstRound(board); // fills placements with best computed solution
    } else {
      compute(board);
    }

    // play placements

  }

  /** TODO REMOVE */
  private void printBoard(Board board) {
    for (int i = 0; i < Board.BOARD_SIZE; i++) {
      for (int j = 0; j < Board.BOARD_SIZE; j++) {
        System.out.print(board.getTile(i, j) == null ? "-" : board.getTile(i, j).getLetter());
      }
      System.out.println();
    }
  }

  /** Reset board */
  private void resetBoard(Board board) {
    placements.forEach(
        placement -> {
          super.addTilesToRack(Arrays.asList(placement.getTile()));
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
  private void computeFirstRound(Board board) {}

  /** @param board copy of game board */
  private void compute(Board board) {}

  /** @return list of words possible with given prefix and RACK */
  private List<String> getWords(String prefix, int maxDepth) {
    List<String> possibleWord = new ArrayList<>();
    return possibleWord;
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

    /** true if word (can) end here */
    public boolean isEnd() {
      return end;
    }
  }
}
