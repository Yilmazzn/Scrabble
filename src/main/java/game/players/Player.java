package game.players;

import game.Game;
import game.components.Board;
import game.components.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author yuzun
 *     <p>The primary actor in the game (game participant)
 */
public abstract class Player {

  private final boolean human; // true if player is human
  private final ArrayList<String> foundWords =
      new ArrayList<>(); // words the player found in the current game session
  private final ArrayList<Tile> rack =
      new ArrayList<>(); // rack which holds the tiles the user can interact with
  private boolean turn; // true if it is player's turn
  private Game game; // the game the player participates in
  private int sessionScore; // score accumulated since the game round started
  private int lobbyScore; // score accumulated since joining the server

  public Player(boolean human) {
    this.human = human;
  }

  public abstract void updateBoard(Board board); // Update player's board

  /** Makes the player join the game so it can interact with the game */
  public void joinGame(Game game) {
    this.game = game;
  }

  public void placeTile(Tile tile, int row, int col) {}

  /** Adds given tiles to player's rack */
  public void addTilesToRack(Collection<Tile> tiles) {
    rack.addAll(tiles);
  }

  /** Remove given tiles from player's rack */
  public void removeTilesFromRack(Collection<Tile> tiles) {
    rack.removeAll(tiles);
  }

  /** Returns the amount of tiles on the player's rack */
  public int getRackSize() {
    return rack.size();
  }

  /**
   * Called to submit a play (or pass if no placements) Submission is only possible if it is
   * player's turn
   *
   * @return true, if submission left board in a valid state
   */
  public boolean submit() {
    if (turn) {
      return game.submit();
    } else {
      return false;
    }
  }

  /** Called to exchange tiles with game's bag, costing the player his or her turn */
  public void exchange(Collection<Tile> tiles) {
    if (turn) {
      if (game.getBagSize() >= tiles.size()) {
        removeTilesFromRack(tiles);
        game.exchange(tiles);
      } else {
        // TODO error not enough tiles
      }
    }
  }

  /** Returns true if player is human */
  public boolean isHuman() {
    return human;
  }

  /** Returns if it is the player's turn */
  public boolean isTurn() {
    return turn;
  }

  /** Sets turn of player */
  public void setTurn(boolean turn) {
    this.turn = turn;
  }

  /** Adds to the session and lobby score of the player */
  public void addScore(int score) {
    sessionScore += score;
    lobbyScore += score;
  }

  /** Returns the session score */
  public int getSessionScore() {
    return sessionScore;
  }

  /** Returns the lobby score of the player */
  public int getLobbyScore() {
    return lobbyScore;
  }

  /** Add words to the user's list of found words */
  public void addFoundWords(List<String> words) {
    words.addAll(words);
  }

  /** Returns the list of words the player has found in the current session */
  public ArrayList<String> getFoundWords() {
    return foundWords;
  }
}
