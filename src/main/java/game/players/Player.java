package game.players;

import client.PlayerProfile;
import game.Game;
import game.components.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author yuzun
 * <p>Actor interacting with game (exists only on the server). Subclasses are AiPlayer,
 * RemotePlayer!
 */
public abstract class Player {

  private final boolean human;
  private final PlayerProfile profile;
  private final ArrayList<String> foundWords = new ArrayList<>();
  private Game game;
  private boolean turn;
  private int score;

  public Player(PlayerProfile profile, boolean human) {
    this.profile = profile;
    this.human = human;
  }

  // Starter

  /**
   * Called by game when game is instantiated to bind both objects
   */
  public void joinGame(Game game) {
    this.game = game;
  }

  // ========= interactions TO game ===================

  /**
   * Places tile on game board (only called if it is player's turn)
   */
  public void placeTile(Tile tile, int row, int col) {
    if (turn) {
      game.placeTile(tile, row, col);
    }
  }

  /**
   * Removes tile from game board if turn
   */
  public void removeTile(int row, int col) {
    if (turn) {
      game.removeTile(row, col);
    }
  }

  /**
   * Submit game turn if turn
   */
  public void submit() {
    if (turn) {
      game.submit();
    }
  }

  /**
   * Exchange tiles. Game calls addTilesToRack method and adds tiles on its own
   */
  public void exchange(Tile... tiles) {
    if (turn) {
      game.exchange(Arrays.asList(tiles));
    }
  }

  /**
   * Quit from game. Player is sent all relevant informations
   */
  public abstract void quit();

  // ======== interactions FROM game ==================

  /**
   * Sets turn of player
   */
  public void setTurn(boolean turn) {
    this.turn = turn;
  }

  /**
   * Add tile to player's rack
   */
  public abstract void addTilesToRack(Collection<Tile> tiles);

  /**
   * Adds score
   */
  public void addScore(int score) {
    this.score += score;
  }

  /**
   * Returns score
   */
  public int getScore() {
    return score;
  }

  /**
   * Add to found words. If list is size 0, this operation is skipped
   */
  public void addFoundWords(Collection<String> words) {
    if (words.size() > 0) {
      foundWords.addAll(words);
    }
  }

  /**
   * Get list of found words
   */
  public Collection<String> getFoundWords() {
    return foundWords;
  }

  /**
   * Returns true if player is human
   */
  public boolean isHuman() {
    return human;
  }

  /**
   * Returns profile
   */
  public PlayerProfile getProfile() {
    return profile;
  }
}
