package game.players;

import game.Dictionary;
import game.Game;
import game.components.Board;
import game.components.BoardException;
import game.components.BoardField;
import game.components.Tile;
import game.players.aiplayers.LexiconTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yuzun Hard AI Player actor, interacting with game
 *     <p>Inspiration by - "The World's Fastest Scrabble Program" by ANDREW W. APPEL AND GUY J.
 *     JACOBSON
 */
public class HardAiPlayer extends AiPlayer {

  private static LexiconTree tree;
  // Root of Word Directed-Acyclic Word Graph!
  // if multiple hard bots exist, only once created!
  // Only used for hard ai up till now , I dont know what Nico will use :)
  private List<Placement> bestPlacements; // best placement

  public HardAiPlayer() {
    super(DIFFICULTY.HARD);
  }

  /** */
  @Override
  public void joinGame(Game game) {
    super.joinGame(game); // assign game to this player
    if (tree == null) { // create only first time
      tree = new LexiconTree(game.getDictionary()); // Build word tree
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
    bestPlacements = new ArrayList<>();

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
      for (int i = 0; i < Math.min(game.getBagSize(), rack.size()); i++) {
        if (rack.get(i).getLetter() == '#') { // Exchange any joker tiles
          tilesToExchange.add(rack.get(i));
        } else if (Math.random() < 0.5) { // 50 % chance to be exchanged
          tilesToExchange.add(rack.get(i));
        }
      }
      if (tilesToExchange.size() != 0) {
        exchange(tilesToExchange); // exchange
      } else {
        submit(); // pass
      }
    }
    submit();
  }

  /** Reset board */
  private void resetBoard(Board board, List<Placement> placements) {
    placements.forEach(
        placement -> board.placeTile(null, placement.getRow(), placement.getColumn()));
  }

  /**
   * Called if first round. Tries to build word with tiles on hand
   *
   * @param board copy of game board
   */
  private void computeFirstRound(Board board) {
    String wordPattern = "";
    for (int i = 0; i < rack.size(); i++) {
      wordPattern += '#';
    }
    Set<String> possibleWords = tree.calculatePossibleWords(wordPattern, rack);
    System.out.println();
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
    List<List<Placement>> possiblePlacements = new ArrayList<>();
    // Traverse through every row
    for (int row = 0; row < Board.BOARD_SIZE; row++) {
      int col = 0;
      while (col < Board.BOARD_SIZE) {
        if (board.isEmpty(row, col)) { // skip if empty
          col++;
          continue;
        }

        // space to left
        int spaceLeft = 0;
        while (col - spaceLeft >= 0 && board.isEmpty(row, col - spaceLeft)) {
          spaceLeft++;
        }

        // skip till empty field again since words have been
        while (col < Board.BOARD_SIZE && !board.isEmpty(row, col)) {
          col++;
        }
      }
    }

    System.out.println(possiblePlacements.size() + " possible Placements found");
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
   * @return score of placements (-1 if board was in an invalid state)
   */
  private int evaluatePlacement(Board board, List<Placement> placements) {
    List<BoardField> boardPlacements = new ArrayList<>();
    placements.forEach(
        placement -> {
          board.placeTile(placement.getTile(), placement.getRow(), placement.getColumn());
          boardPlacements.add(board.getField(placement.getRow(), placement.getColumn()));
        });
    try {
      board.check(boardPlacements, game.getDictionary());
    } catch (BoardException be) {
      return -1;
    }
    int score = board.evaluateScore(boardPlacements);
    resetBoard(board, placements);
    return score;
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
