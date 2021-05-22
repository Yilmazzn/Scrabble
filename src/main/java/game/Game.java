package game;

import game.components.Board;
import game.components.BoardException;
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

  private final List<Player>
      players; // Players playing this game (in opposite order of their turns)   [0: last player, 1:
  // second last player, ...]
  private final boolean running; // true if game is running
  private final List<String> wordsFound =
      new ArrayList<>(); // a list of words found up to this point
  private final Board board; // Game Board
  private final LinkedList<Tile> bag; // bag of tiles in the game
  private final int roundsSinceLastScore = 0; // last n amount of rounds without points
  private final Dictionary
      dictionary; // Dictionary this game relies on   TODO Dictionary class, getter&setter
  private Board
      lastValidBoard; // Game Board which was last accepted as valid to reset after invalid player
  private int roundNum = -1; // amount of total rounds since start
  private Player playerInTurn; // Player whose turn it is
  private List<BoardField> placementsInTurn =
      new LinkedList<>(); // Placements on the board in the last turn
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
  public Game(List<Player> players, LinkedList<Tile> bag, Dictionary dictionary) {

    this.dictionary = dictionary;
    this.bag = bag;

    // Players participating
    this.players = players;
    players.forEach(player -> player.joinGame(this));

    // Set running to true
    this.running = true;

    // Set up a board
    this.board = new Board();

    // Shuffle bag for certainty
    Collections.shuffle(bag);

    // Give each player 7 tiles and remove them from the bag
    for (Player player : players) {
      LinkedList<Tile> tilesToDistribute = new LinkedList<>();
      for (int i = 0; i < 7; i++) {
        tilesToDistribute.add(bag.pop());
      }
      player.addTilesToRack(tilesToDistribute);
    }
  }

  /**
   * Called to start a new round increments round number assigns turn, if it is the turn of an AI
   * player, then trigger it to to think and make a move
   */
  public void nextRound() {

    // increment round number
    roundNum++;
    // TODO Remove
    if (roundNum > 10) {
      System.out.println("GAME END\t\t| limited to 10 rounds");
    }
    System.out.println("Round Number: " + (roundNum + 1));

    // Save last valid board state
    this.lastValidBoard = new Board(board); // deep copy

    // reassign turns
    // TODO Handle with care, check for 3 and 4 players
    players.get(Math.abs(roundNum - 1) % players.size()).setTurn(false);
    playerInTurn = players.get(roundNum % players.size());
    playerInTurn.setTurn(true);

    placementsInTurn = new LinkedList<>();

    // If player is Ai, then trigger it to think
    if (!playerInTurn.isHuman()) { // player is not human
      AiPlayer ai = (AiPlayer) playerInTurn;
      ai.think(board, dictionary);
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
    }
  }

  /**
   * Ends game (can also be called by incomin EXCEEDED_TIME_MESSAGE or HOST_DISCONNECT_MESSAGE) If
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
    try { // Try checking board which throws BoardException if any checks fail
      board.check(placementsInTurn, dictionary);

      // if not thrown error by now then board state valid
      int score = evaluateScore();
      System.out.println("SCORE: " + score);
      List<Tile> tileRefill = new LinkedList<>();
      for (int i = 0; i < Math.min(placementsInTurn.size(), bag.size()); i++) {
        tileRefill.add(bag.pop());
      }
      playerInTurn.addTilesToRack(tileRefill);

      nextRound();
    } catch (BoardException e) {
      // Send SubmitMoveMessage back, acts as RejectMessage
      System.out.println("BOARD_ERROR: " + e.getMessage());
      playerInTurn.rejectSubmission(
          e.getMessage()); // Reject player's submission with reason of exception
    }
  }

  /***
   * Evaluates the score of the play in the last turn. Iterates over placements in last turn and only ever starts evaluating if placement is to the most top/left placement of all placements in last turn of the specific word it is forming
   * @return score of play in last turn
   */
  public int evaluateScore() {
    return board.evaluateScore(placementsInTurn);
  }

  public int getBagSize() {
    return bag.size();
  }

  public Board getBoard() {
    return board;
  }

  public Dictionary getDictionary() {
    return this.dictionary;
  }

  public int getRoundNumber() {
    return roundNum + 1;
  }
}
