package game;

import game.components.Board;
import game.components.BoardField;
import game.components.Tile;
import game.players.AiPlayer;
import game.players.Player;

import java.util.*;

/**
 * @author yuzun
 *     <p>Single instance running on the hosted server Handles game flow and logic
 */
public class Game {

  private final ArrayList<Player>
          players; // Players playing this game (in opposite order of their turns)   [0: last player, 1:
  // second last player, ...]
  private final boolean running; // true if game is running
  private final HashMap<Character, Integer> letterScores; // Map representing letter's score
  private final LinkedList<Tile> bag = new LinkedList<>(); // bag of tiles in the game
  private final ArrayList<String> wordsFound =
          new ArrayList<>(); // a list of words found up to this point
  private final Board board = new Board(); // Game Board
  private final Board lastValidBoard =
          new Board(); // Game Board which was last accepted as valid to reset after invalid player
  // turns
  private int roundNum = 0; // amount of total rounds since start
  private final int roundsSinceLastScore = 0; // last n amount of rounds without points
  private final Dictionary
          dictionary; // Dictionary this game relies on   TODO Dictionary class, getter&setter
  private Player playerInTurn; // Player whose turn it is
  private List<BoardField> placementsInTurn =
          new LinkedList<>(); // Placements on the board in the last turn
  private OvertimeWatch overtime; // Thread which counts down from 10mins, is reset each turn
  private Scoreboard scoreboard; // Scoreboard containing game statistics

  /**
   * A game instance is created by the server, when the host decided to start the game A game
   * expects a list of players, an array in which the nth element represents the amount of tiles of
   * the (n+1)th letter in the alphabet (26th element is the joker tile) an array in which the nth
   * element represents the score of the (n+1)th letter in the alphabet (26th element is the joker
   * tile) When a game is created it sets the status 'running' to true creates a board creates the
   * tiles (amount & score determined by the given arrays), shuffles them and distributes them to
   * the players
   */
  public Game(
          ArrayList<Player> players,
          HashMap<Character, Integer> letterDistribution,
          HashMap<Character, Integer> letterScores,
          Dictionary dictionary) {

    this.letterScores = letterScores;
    this.dictionary = dictionary;

    // Players participating
    this.players = players;
    players.forEach(player -> player.joinGame(this));

    // Set running to true
    this.running = true;

    // Set up a board
    Board board = new Board();

    // Setup letter scores
    // Create tiles, put them into the bag and shuffle
    for (char letter : letterScores.keySet()) {
      for (int i = 0; i < letterDistribution.get(letter); i++) {
        bag.add(new Tile(letter, letterScores.get(letter)));
      }
    }
    Collections.shuffle(bag);

    // Give each player 7 tiles and remove them from the bag
    for (Player player : players) {
      LinkedList<Tile> tilesToDistribute = new LinkedList<>();
      for (int i = 0; i < 7; i++) {
        tilesToDistribute.add(bag.pop());
      }
      player.addTilesToRack(tilesToDistribute);
    }

    // Start first round
    nextRound();
  }

  /**
   * Called to start a new round increments round number assigns turn, if it is the turn of an AI
   * player, then trigger it to to think and make a move
   */
  public void nextRound() {
    // Stop overtime of last round's player (interrupt thread)
    if (roundNum > 0) {
      overtime.stopCountdown();
    }

    // increment round number
    roundNum++;

    // reassign turns
    players.get((roundNum - 1) % players.size()).setTurn(false);
    playerInTurn = players.get(roundNum % players.size());
    playerInTurn.setTurn(true);

    // reset countdown and tracked placements
    overtime = new OvertimeWatch(this);
    overtime.start();
    placementsInTurn = new LinkedList<>();

    // If player is Ai, then trigger it to think
    if (!playerInTurn.isHuman()) { // player is not human
      AiPlayer ai = (AiPlayer) playerInTurn;
      ai.think();
    }
  }

  /**
   * Removes the tiles given to the game from the player's rack Gets equal amount of tiles out of
   * the bag and adds them to the rack of the player The new tiles from the player are put back in
   * the bag and the bag gets shuffled
   *
   * @param tilesFromPlayer collection of tiles the player wants to exchange
   */
  public void exchange(Collection<Tile> tilesFromPlayer) {

    Collection<Tile> tilesFromBag = new LinkedList<>(); // tiles that are to be given back

    for (int i = 0; i < tilesFromPlayer.size(); i++) {
      tilesFromBag.add(bag.pop());
    }

    playerInTurn.addTilesToRack(tilesFromBag);
    Collections.shuffle(bag);
    nextRound();
  }

  /**
   * Places give tile on field at given row and column if field is empty. Adds placement to the list
   * of placements this turn. Checks new board state.
   */
  public void placeTile(Tile tile, int row, int col) {
    if (board.isEmpty(row, col)) {
      board.placeTile(tile, row, col);
      placementsInTurn.add(board.getField(row, col));
      board.check(placementsInTurn, dictionary);
    }
  }

  /**
   * Removes tile at given row and column. Removes placmeent from the list of placements in this
   * turn. Checks new board state. If field was empty, then nothing happens.
   */
  public void removeTile(int row, int col) {
    if (!board.isEmpty(row, col)) {
      board.placeTile(null, row, col);
      placementsInTurn.remove(board.getField(row, col));
      board.check(placementsInTurn, dictionary);
    }
  }

  /**
   * Checks whether game is in a valid state
   *
   * @return true if board state is valid
   */
  public boolean checkBoard() {
    return board.check(placementsInTurn, dictionary);
  }

  /**
   * Ends game (can also be called by running instance of OvertimeWatch if user runs out of time) If
   * ended abruptly, then because of a user running out of time --> removing pending placements from
   * the board Else just end normally and show every human player scoreboard
   */
  public void end() {
    // Remove placements in this turn
    placementsInTurn.forEach(bf -> bf.setTile(null));

    // TODO Update Scoreboard and .....
  }

  /** TODO change some things maybe... */
  public void submit() {
    if (board.check(placementsInTurn, dictionary)) {
      nextRound();
    }
  }

  /***
   * Evaluates the score of the play in the last turn. Iterates over placements in last turn and only ever starts evaluating if placement is to the most top/left placement of all placements in last turn of the specific word it is forming
   * @author yuzun
   * @return score of play in last turn
   */
  public int evaluateScore() {

    int totalScore = 0;

    // Iterate over placements of last turn
    for (BoardField bf : placementsInTurn) {


      // check to the left if other placement exists there (would be evaluated in that spec.
      // iteration)
      BoardField helper = board.getField(bf.getRow(), bf.getColumn() - 1); // left of placement
      boolean leftmostPlacement = true; // is true if only placement to the left

      boolean formsWordHorizontal =
              (bf.getColumn() - 1 >= 0 && !board.isEmpty(bf.getRow(), bf.getColumn() - 1))
                      || (bf.getColumn() + 1 < Board.BOARD_SIZE
                      && !board.isEmpty(
                      bf.getRow(),
                      bf.getColumn() + 1)); // true if left or right of placement exists tile

      // traverse to left till empty or out of bounds (if forms word horitonal)
      while (helper.getColumn() >= 0 && !helper.isEmpty() && formsWordHorizontal) {
        if (placementsInTurn.contains(helper)) {
          leftmostPlacement = false;
          break;
        }
        helper = board.getField(helper.getRow(), helper.getColumn() - 1);
      }

      // check to the top if other placement this turn exists there (would be evaluated in that
      // spec. iteration)
      helper = board.getField(bf.getRow() - 1, bf.getColumn()); // above placement
      boolean topmostPlacement = true; // is true if top of placement in formed word

      boolean formsWordVertical =
              (bf.getRow() - 1 >= 0 && !board.isEmpty(bf.getRow() - 1, bf.getColumn()))
                      || (bf.getRow() + 1 < Board.BOARD_SIZE
                      && !board.isEmpty(
                      bf.getRow() + 1,
                      bf.getColumn())); // true if above or below of placement exists tile

      // traverse up till empty or out of bounds (if forms word veritcal)
      while (helper.getRow() >= 0 && !helper.isEmpty() && formsWordVertical) {
        if (placementsInTurn.contains(helper)) {
          topmostPlacement = false;
          break;
        }
        helper = board.getField(helper.getRow() - 1, helper.getColumn());
      }

      // if leftmost placement & to the left/right tiles exist--> evaluate horizontal
      if (leftmostPlacement && formsWordHorizontal) {
        int wordScore = 0;
        int wordMult = 1;

        // traverse to the left
        helper = bf;
        while (helper.getColumn() - 1 >= 0 && !board.isEmpty(helper.getRow(), helper.getColumn() - 1)) {
          helper = board.getField(helper.getRow(), helper.getColumn() - 1);
        }

        // traverse to the right
        while (helper.getColumn() < Board.BOARD_SIZE && !helper.isEmpty()) {

          int letterScore = helper.getTile().getScore();
          int letterMult = 1;

          // check if tile was placed last turn
          if (!placementsInTurn.contains(helper)) { // tile was not placed last turn
            wordScore += letterScore;
          } else { // tile was placed last turn

            // check field type and evaluate multipliers
            switch (helper.getType()) {
              case STAR:
              case DWS:
                wordMult *= 2;
                break;
              case TWS:
                wordMult *= 3;
                break;
              case DLS:
                letterMult *= 2;
                break;
              case TLS:
                letterMult *= 3;
                break;
            }
            wordScore += letterScore * letterMult;
          }

          helper = board.getField(helper.getRow(), helper.getColumn() + 1);
        }
        totalScore += wordScore * wordMult;
      }

      // if topmost placement & tiles exist above or below --> evaluate vertical
      if (topmostPlacement && formsWordVertical) {
        int wordScore = 0;
        int wordMult = 1;

        // traverse up
        helper = bf;
        while (helper.getRow() >= 0 && !helper.isEmpty()) {
          helper = board.getField(helper.getRow() - 1, helper.getColumn());
        }

        // traverse down
        while (helper.getRow() < Board.BOARD_SIZE && !helper.isEmpty()) {

          int letterScore = helper.getTile().getScore();
          int letterMult = 1;

          // check if tile was placed last turn
          if (!placementsInTurn.contains(helper)) { // tile was not placed last turn
            wordScore += letterScore;
          } else { // tile was placed last turn

            // check field type and evaluate multipliers
            switch (helper.getType()) {
              case STAR:
              case DWS:
                wordMult *= 2;
                break;
              case TWS:
                wordMult *= 3;
                break;
              case DLS:
                letterMult *= 2;
                break;
              case TLS:
                letterMult *= 3;
                break;
            }
            wordScore += letterScore * letterMult;
          }

          helper = board.getField(helper.getRow() + 1, helper.getColumn());
        }

        totalScore += wordScore * wordMult;
      }
    }

    return totalScore;
  }

  public int getBagSize() {
    return bag.size();
  }

  public Board getBoard() {
    return board;
  }
}
