package game.players;

import game.Dictionary;
import game.Game;
import game.components.Tile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class HardAiPlayerTest {

  private final int[] tileScores = {
    1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10
  };
  private final int[] tileDistributions = {
    9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 2
  };
  private final Dictionary dictionary = new Dictionary();
  private Game game;

  @Test
  void playGameTest() { // Play till end
    // set game bag
    List<Player> players = new LinkedList<>();
    players.add(new HardAiPlayer());
    players.add(new HardAiPlayer());
    players.add(new HardAiPlayer());

    LinkedList<Tile> gameBag = new LinkedList<>();
    for (int i = 0; i < tileDistributions.length; i++) {
      for (int j = 0; j < tileDistributions[i]; j++) {
        if (i != tileDistributions.length - 1) { // Not joker
          gameBag.add(new Tile(((char) ('A' + i)), tileScores[i]));
        } else { // joker
          gameBag.add(new Tile('#', 0));
        }
      }
    }
    new Game(players, gameBag, dictionary).nextRound();
  }

  @AfterEach
  void tearDown() {}
}
