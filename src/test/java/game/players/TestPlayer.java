package game.players;

import client.PlayerProfile;
import game.components.Tile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author yuzun
 *     <p>Player test class for tests
 */
public class TestPlayer extends Player {

  private final ArrayList<Tile> rack = new ArrayList<>();

  public TestPlayer(String username) {
    super(new PlayerProfile(username, 0, 0, 0, 0, LocalDate.now(), LocalDate.now()), true);
  }

  /** Quit from game. Player is sent all relevant informations */
  @Override
  public void quit() {
    // empty
  }

  @Override
  public void rejectSubmission(String reason) {
    // empty
  }

  /**
   * Add tile to player's rack
   *
   * @param tiles
   */
  @Override
  public void addTilesToRack(Collection<Tile> tiles) {
    tiles.addAll(tiles);
  }
}
