package game.players;

import game.components.Board;
import game.components.BoardField;
import game.components.Tile;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

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
  Board board = new Board();
  List<BoardField> placements = new ArrayList<>();

  @BeforeAll
  void setUp() throws Exception {
    // Initialize all the tiles for the test words
    h = new Tile('H', 4);
    l = new Tile('L', 4);
    o = new Tile('O', 1);
    w = new Tile('W', 2);
    r = new Tile('R', 1);
    d = new Tile('D', 2);
    m = new Tile('M', 3);
    i = new Tile('I', 1);
    e = new Tile('E', 1);
    n = new Tile('N', 1);
    p = new Tile('P', 3);
  }

  @Test
  /**
   * First round with word hello from the star tile to the right The second word is world and the
   * 'o' of the world hello is used too The third word is middle and uses the 'd' from the world
   * word The last word is wow and pene. In this turn the player sets different tiles on the
   * position of the 'w' from world and the 'e' from middle. Therefore also the functionality and
   * search capability of the ecvaluateScore is testes
   */
  @DisplayName("Placing 'HELLO' (H on DWS, O on DLS)")
  void evaluateScoreTest1() {
    // First word
    place(h, 7, 7);
    place(e, 7, 8);
    place(l, 7, 9);
    place(l, 7, 10);
    place(o, 7, 11);

    Assertions.assertEquals(30, board.evaluateScore(placements));
    placements.clear();
  }

  /*
    @Test
    @DisplayName("Placing 'WORLD' (Placements: WRLD)")
    void evaluateScoreTest2() {
      // second word
      g.placeTile(w, 6, 11);
      g.placeTile(o, 7, 11);
      g.placeTile(r, 8, 11);
      g.placeTile(l, 9, 11);
      g.placeTile(d, 10, 11);

      Assertions.assertEquals(12, g.evaluateScore());
      g.nextRound();
    }

    @Test
    void evaluateScoreTest3() {
      // third word
      g.placeTile(m, 10, 9);
      g.placeTile(i, 10, 10);
      g.placeTile(d, 10, 11);
      g.placeTile(d, 10, 12);
      g.placeTile(l, 10, 13);
      g.placeTile(e, 10, 14);

      Assertions.assertEquals(26, g.evaluateScore());
      g.nextRound();
    }

    @Test
    void evaluateScoreTest4() {
      // 4th words
      g.placeTile(o, 6, 12);
      g.placeTile(w, 6, 13);

      g.placeTile(p, 7, 14);
      g.placeTile(e, 8, 14);
      g.placeTile(n, 9, 14);

      Assertions.assertEquals(30, g.evaluateScore());
      g.nextRound();
    }
  */
  private void place(Tile tile, int row, int col) {
    board.placeTile(tile, row, col);
    placements.add(board.getField(row, col));
  }
}
