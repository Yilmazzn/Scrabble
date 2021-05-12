package game.players;

import game.Game;
import game.components.Board;
import game.components.Tile;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * @author nsiebler the test is about the words hallo, world and middle, and test the various cases
 *     that can occur while playing
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameTest {

  Tile h;
  Tile l;
  Tile o;
  Tile w;
  Tile r;
  Tile d;
  Tile m;
  Tile i;
  Tile e;
  Tile n;
  Tile p;
  Player p1;
  Player p2;
  Player p3;
  Player p4;
  Game g;
  ArrayList<Player> a1;
  HashMap<Character, Integer> letterDistribution;
  HashMap<Character, Integer> letterScores;

  @BeforeAll
  void setUp() throws Exception {
    // Initialize all the tiles for the test words
    h = new Tile('H', 4);
    l = new Tile('L', 1);
    o = new Tile('O', 1);
    w = new Tile('W', 4);
    r = new Tile('R', 1);
    d = new Tile('D', 2);
    m = new Tile('M', 3);
    i = new Tile('I', 1);
    e = new Tile('E', 1);
    n = new Tile('N', 1);
    p = new Tile('P', 3);
    // Initialize the players
    a1 = new ArrayList<>();
    p1 =
        new Player(true) {
          @Override
          public void updateBoard(Board board) {}
        };
    p2 =
        new Player(true) {
          @Override
          public void updateBoard(Board board) {}
        };
    p3 =
        new Player(true) {
          @Override
          public void updateBoard(Board board) {}
        };
    p4 =
        new Player(true) {
          @Override
          public void updateBoard(Board board) {}
        };
    a1.add(p1);
    a1.add(p2);
    a1.add(p3);
    a1.add(p4);

    // Initialize the Hash Maps
    letterDistribution = new HashMap<>();
    letterDistribution.put('A', 9);
    letterDistribution.put('B', 2);
    letterDistribution.put('C', 2);
    letterDistribution.put('D', 4);
    letterDistribution.put('E', 12);
    letterDistribution.put('F', 2);
    letterDistribution.put('G', 3);
    letterDistribution.put('H', 2);
    letterDistribution.put('I', 9);
    letterDistribution.put('J', 1);
    letterDistribution.put('K', 1);
    letterDistribution.put('L', 4);
    letterDistribution.put('M', 2);
    letterDistribution.put('N', 6);
    letterDistribution.put('O', 8);
    letterDistribution.put('P', 2);
    letterDistribution.put('Q', 1);
    letterDistribution.put('R', 6);
    letterDistribution.put('S', 4);
    letterDistribution.put('T', 6);
    letterDistribution.put('U', 4);
    letterDistribution.put('V', 2);
    letterDistribution.put('W', 2);
    letterDistribution.put('X', 1);
    letterDistribution.put('Y', 2);
    letterDistribution.put('Z', 1);
    letterDistribution.put(' ', 2);

    letterScores = new HashMap<>();
    letterScores.put('A', 1);
    letterScores.put('B', 3);
    letterScores.put('C', 3);
    letterScores.put('D', 2);
    letterScores.put('E', 1);
    letterScores.put('F', 4);
    letterScores.put('G', 2);
    letterScores.put('H', 4);
    letterScores.put('I', 1);
    letterScores.put('J', 8);
    letterScores.put('K', 5);
    letterScores.put('L', 4);
    letterScores.put('M', 3);
    letterScores.put('N', 1);
    letterScores.put('O', 1);
    letterScores.put('P', 3);
    letterScores.put('Q', 10);
    letterScores.put('R', 1);
    letterScores.put('S', 1);
    letterScores.put('T', 1);
    letterScores.put('U', 1);
    letterScores.put('V', 4);
    letterScores.put('W', 4);
    letterScores.put('X', 8);
    letterScores.put('Y', 4);
    letterScores.put('Z', 10);
    letterScores.put(' ', 0);
    g = new Game(a1, letterDistribution, letterScores, new game.Dictionary());
  }

  @Test
  /**
   * First round with word hello from the star tile to the right The second word is world and the
   * 'o' of the world hello is used too The third word is middle and uses the 'd' from the world
   * word The last word is wow and pene. In this turn the player sets different tiles on the
   * position of the 'w' from world and the 'e' from middle. Therefore also the functionality and
   * search capability of the ecvaluateScore is testes
   */
  void evaluateScore() {
    /**
     * TODO current issue because placed tiles of a players turn arent saved in the placementsInTurn
     * List from TODO the class Game
     */
    // First word
    g.placeTile(h, 7, 7);
    g.placeTile(e, 7, 8);
    g.placeTile(l, 7, 9);
    g.placeTile(l, 7, 10);
    g.placeTile(o, 7, 11);

    Assertions.assertEquals(18, g.evaluateScore());
    g.nextRound();

    // second word
    g.placeTile(w, 6, 11);
    g.placeTile(o, 7, 11);
    g.placeTile(r, 8, 11);
    g.placeTile(l, 9, 11);
    g.placeTile(d, 10, 11);
    Assertions.assertEquals(12, g.evaluateScore());
    g.nextRound();

    // third word
    g.placeTile(m, 10, 9);
    g.placeTile(i, 10, 10);
    g.placeTile(d, 10, 11);
    g.placeTile(d, 10, 12);
    g.placeTile(l, 10, 13);
    g.placeTile(e, 10, 14);

    Assertions.assertEquals(26, g.evaluateScore());
    g.nextRound();

    // 4th words
    g.placeTile(o, 6, 12);
    g.placeTile(w, 6, 13);

    g.placeTile(p, 7, 14);
    g.placeTile(e, 8, 14);
    g.placeTile(n, 9, 14);
    Assertions.assertEquals(30, g.evaluateScore());
    g.nextRound();
  }
}
