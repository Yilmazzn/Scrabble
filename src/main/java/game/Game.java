package game;

import game.components.Board;
import game.components.BoardException;
import game.components.BoardField;
import game.components.Tile;
import game.players.AiPlayer;
import game.players.Player;
import game.players.RemotePlayer;
import net.message.ChatMessage;
import net.message.Message;
import net.message.PlaceTileMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yuzun
 *     <p>Single instance running on the hosted server Handles game flow and logic
 */
public class Game {

  private final List<Player>
      players; // Players playing this game (in opposite order of their turns)   [0: last player, 1:
  // second last player, ...]
  private final boolean running; // true if game is running
  private final Board board; // Game Board
  private final LinkedList<Tile> bag; // bag of tiles in the game
  private int roundsSinceLastScore = 0; // last n amount of rounds without points
  private final Dictionary
      dictionary; // Dictionary this game relies on   TODO Dictionary class, getter&setter
  private Board
      lastValidBoard; // Game Board which was last accepted as valid to reset after invalid player
  private int roundNum = -1; // amount of total rounds since start
  private Player playerInTurn; // Player whose turn it is
  private List<BoardField> placementsInTurn =
      new LinkedList<>(); // Placements on the board in the last turn
  private Scoreboard scoreboard; // Scoreboard containing game statistics
  private int amountFoundWords = 0; // counts number of found words

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
    System.out.println("Round Number: " + (roundNum + 1));

    // Check if game is endable
    if (roundsSinceLastScore >= 6 || bag.size() == 0) {
      // TODO SEND ENDABLE MESSAGE
    }

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

    Collections.shuffle(bag);
    Collection<Tile> tilesFromBag = new LinkedList<>(); // tiles that are to be given back

    for (int i = 0; i < tilesFromPlayer.size(); i++) {
      tilesFromBag.add(bag.pop());
    }

    bag.addAll(tilesFromPlayer);
    playerInTurn.addTilesToRack(tilesFromBag);

    // Notify other about this event
    String message = playerInTurn.getProfile().getName() + " exchanged tiles!";
    notify(new ChatMessage(message, null));

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
      notify(new PlaceTileMessage(tile, row, col));
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
      notify(new PlaceTileMessage(null, row, col));
    }
  }

  /**
   * @author ygarip Ends game (can also be called by incoming EXCEEDED_TIME_MESSAGE or
   *     HOST_DISCONNECT_MESSAGE) If ended abruptly, then because of a user running out of time -->
   *     removing pending placements from the board Else just end normally and show every human
   *     player scoreboard. No checks here
   */
  public void end() {
    // Remove placements in this turn
    placementsInTurn.forEach(bf -> bf.setTile(null));

    // Gather stats
    // Score list & Winner
    int[] scores = new int[players.size()];
    int highestScore = 0;
    int winnerIdx = 0;
    for (int i = 0; i < scores.length; i++) {
      scores[i] = players.get(i).getScore();
      if (highestScore < scores[i]) {
        highestScore = scores[i];
        winnerIdx = i;
      }
    }

    // Found words list 2D Array
    String[][] foundWords = new String[players.size()][];
    for (int i = 0; i < foundWords.length; i++) {
      List<String> words = players.get(i).getFoundWords();
      foundWords[i] = new String[words.size()];
      for (int j = 0; j < foundWords.length; j++) {
        foundWords[i][j] = words.get(i);
      }
    }

    // TODO Update Scoreboard and .....
  }

  /** TODO change some things maybe... */
  public void submit() {
    try { // Try checking board which throws BoardException if any checks fail
      if (placementsInTurn.size() != 0) {
        board.check(placementsInTurn, dictionary); // dont check if no placements
      }

      // add new found words to player
      List<String> foundWords =
          board.getFoundWords().subList(amountFoundWords, board.getFoundWords().size());
      playerInTurn.addFoundWords(foundWords);
      amountFoundWords = board.getFoundWords().size();

      // if not thrown error by now then board state valid
      int score = evaluateScore();
      if (score == 0) {
        roundsSinceLastScore++;
      }

      // Notify which words were found & Score
      if (score > 0) { // if words found
        String message = playerInTurn.getProfile().getName() + " found: ";
        for (String word : foundWords) {
          message += word + ", ";
        }
        message = message.substring(0, message.length() - 2); // Cut away last ", "
        message += "\n --> scored " + score + " points!";
        notify(new ChatMessage(message, null));
      } else { // Passed
        String message = playerInTurn.getProfile().getName() + " passed";
        notify(new ChatMessage(message, null));
      }

      List<Tile> tileRefill = new LinkedList<>();
      for (int i = 0; i < Math.min(placementsInTurn.size(), bag.size()); i++) {
        tileRefill.add(bag.pop());
      }
      playerInTurn.addTilesToRack(tileRefill);
      playerInTurn.addScore(score);

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
   * @author yuzun
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

  /**
   * Creates System message This method is only used by the AI to flex with possible placements,
   * processing time, etc. ;)
   */
  public void notify(Message m) {
    for (Player player : players) {
      if (player.isHuman()) {
        RemotePlayer remotePlayer = (RemotePlayer) player;
        remotePlayer.getConnection().sendToClient(m);
      }
    }
  }

  public List<Player> getPlayers() {
    return players;
  }
}
