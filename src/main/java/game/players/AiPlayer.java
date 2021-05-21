package game.players;

import client.PlayerProfile;
import game.components.Tile;

import java.time.LocalDate;
import java.util.*;

/**
 * @author yuzun
 *     <p>AI player/actor in game. Provides method 'think' which is the main feature
 */
public abstract class AiPlayer extends Player {

  private static final List<String> botNames =
      new LinkedList<>(
          Arrays.asList(
              "Yilmaz",
              "Valentin",
              "Vincent",
              "Yasin",
              "Max",
              "Nicolas")); // bots get random names which is removed from collection then (no
  // duplicates)
  private final ArrayList<Tile> rack = new ArrayList<>();
  private final DIFFICULTY difficulty;

  /** Initializes Bot with random name of collection and removes it from list of available names */
  public AiPlayer(DIFFICULTY difficulty) {
    super(new PlayerProfile("", 0, 0, 0, 0, LocalDate.now(), LocalDate.now()), false);

    this.difficulty = difficulty;

    // Get random name
    int randomIdx = (int) (Math.random() * botNames.size()); // random number 0-5
    String botName = botNames.get(randomIdx);

    // Set name of profile to 'Bot <name>' and remove name from list
    super.getProfile().setName("Bot " + botName);
    botNames.remove(randomIdx);
  }

  /**
   * Main method which is triggered by the game instance All computations from start of round till
   * end need to be done in here!
   */
  public abstract void think();

  /** Adds given tiles to rack */
  @Override
  public void addTilesToRack(Collection<Tile> tiles) {
    rack.addAll(tiles);
  }

  /** Return Difficulty of Ai (Used to be able to differentiate, e.g. by colors blue/red) */
  public DIFFICULTY getDifficulty() {
    return difficulty;
  }

  /** Quit from game. Set username back to list of available bot names */
  public void quit() {
    // Adds botName back to 'pool' of available names
    botNames.add(super.getProfile().getName().split(" ")[1]);
  }

  public enum DIFFICULTY {
    EASY,
    HARD
  }

  @Override
  public void rejectSubmission() {}
}
