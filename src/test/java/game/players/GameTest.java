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
  Tile g;
  Tile t;
  Tile a;
  Tile s;
  Board board = new Board();
  List<BoardField> placements = new ArrayList<>();

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
    g = new Tile('G', 2);
    t = new Tile('T',1);
    a = new Tile('A',1);
    s = new Tile('S',1);
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

    Assertions.assertEquals(18, board.evaluateScore(placements));
    placements.clear();
  }

  @Test
  @DisplayName("Placing 'ING' (Placements: HELLOING)")
  void evaluateScoreTest2() {
    // second word
    place(i, 7, 12);
    place(n, 7, 13);
    place(g, 7, 14);
    Assertions.assertEquals(36, board.evaluateScore(placements));
    placements.clear();
  }

    @Test
    @DisplayName("Placing 'ELMETED' (Placements: HELMETED)")
    void evaluateScoreTest3() {
      // third word
      place(e, 8, 7);
      place(l, 9, 7);
      place(m, 10, 7);
      place(e, 11, 7);
      place(t, 12, 7);
      place(e, 13, 7);
      place(d, 14, 7);
      Assertions.assertEquals(63, board.evaluateScore(placements));
      placements.clear();
    }

    @Test
    @DisplayName("Placing 'AT' (Placements: EAT)")
    void evaluateScoreTest4() {
      place(a, 11, 8);
      place(t, 11, 9);

      Assertions.assertEquals(3, board.evaluateScore(placements));
      placements.clear();
    }

  @Test
  @DisplayName("Placing 'AT' (Placements: EAT)")
  void evaluateScoreTest5() {
    place(o, 8, 9);
    place(w, 9, 9);

    Assertions.assertEquals(14, board.evaluateScore(placements));
    placements.clear();
  }

  @Test
  @DisplayName("Placing 'OESS' (Placements: LOESS)")
  void evaluateScoreTest6() {
    place(o, 8, 9);
    place(e, 9, 9);
    place(s, 10, 9);
    place(s, 11, 9);
    Assertions.assertEquals(21, board.evaluateScore(placements));
    placements.clear();
  }
  private void place(Tile tile, int row, int col) {
    board.placeTile(tile, row, col);
    placements.add(board.getField(row, col));
  }
}
