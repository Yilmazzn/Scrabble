package game;

import game.components.Board;
import game.components.BoardField;
import game.components.Tile;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

/**
 * @author nsiebler
 * the test is about the words hallo, world and middle, and test the various cases
 *     that can occur while playing
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameTest {

  Tile letterH;
  Tile letterL;
  Tile letterO;
  Tile letterW;
  Tile letterR;
  Tile letterD;
  Tile letterM;
  Tile letterI;
  Tile letterE;
  Tile letterN;
  Tile letterP;
  Tile letterG;
  Tile letterT;
  Tile letterA;
  Tile letterS;


  Board board = new Board();
  List<BoardField> placements = new ArrayList<>();

  @BeforeAll
  void setUp() throws Exception {
    // Initialize all the tiles for the test words
    letterH = new Tile('H', 4);
    letterL = new Tile('L', 1);
    letterO = new Tile('O', 1);
    letterW = new Tile('W', 4);
    letterR = new Tile('R', 1);
    letterD = new Tile('D', 2);
    letterM = new Tile('M', 3);
    letterI = new Tile('I', 1);
    letterE = new Tile('E', 1);
    letterN = new Tile('N', 1);
    letterP = new Tile('P', 3);
    letterG = new Tile('G', 2);
    letterT = new Tile('T', 1);
    letterA = new Tile('A', 1);
    letterS = new Tile('S', 1);
  }

  @Test
  @DisplayName("Placing 'HELLO' (H on DWS, O on DLS)")
  void evaluateScoreTest1() {
    // First word
    place(letterH, 7, 7);
    place(letterE, 7, 8);
    place(letterL, 7, 9);
    place(letterL, 7, 10);
    place(letterO, 7, 11);

    Assertions.assertEquals(18, board.evaluateScore(placements));
    placements.clear();
  }

  @Test
  @DisplayName("Placing 'ING' (Placements: HELLOING)")
  void evaluateScoreTest2() {
    // second word
    place(letterI, 7, 12);
    place(letterN, 7, 13);
    place(letterG, 7, 14);
    Assertions.assertEquals(36, board.evaluateScore(placements));
    placements.clear();
  }

  @Test
  @DisplayName("Placing 'ELMETED' (Placements: HELMETED)")
  void evaluateScoreTest3() {
    // third word
    place(letterE, 8, 7);
    place(letterL, 9, 7);
    place(letterM, 10, 7);
    place(letterE, 11, 7);
    place(letterT, 12, 7);
    place(letterE, 13, 7);
    place(letterD, 14, 7);
    Assertions.assertEquals(95, board.evaluateScore(placements));
    placements.clear();
  }

  @Test
  @DisplayName("Placing 'AT' (Placements: EAT)")
  void evaluateScoreTest4() {
    place(letterA, 11, 8);
    place(letterT, 11, 9);

    Assertions.assertEquals(3, board.evaluateScore(placements));
    placements.clear();
  }

  @Test
  @DisplayName("Placing 'OW' (Placements: LOW)")
  void evaluateScoreTest5() {
    place(letterO, 8, 9);
    place(letterW, 9, 9);

    Assertions.assertEquals(14, board.evaluateScore(placements));
    placements.clear();
  }

  @Test
  @DisplayName("Placing 'EST' (Placements: LOWEST)")
  void evaluateScoreTest6() {
    place(letterE, 10, 9);
    place(letterS, 11, 9);
    place(letterT, 12, 9);
    Assertions.assertEquals(12, board.evaluateScore(placements));
    placements.clear();
  }

  private void place(Tile tile, int row, int col) {
    board.placeTile(tile, row, col);
    placements.add(board.getField(row, col));
  }
}
