import game.components.Board;
import game.components.Tile;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BoardTest {

    private final static int BOARD_SIZE = 15;
    // public Dictionary dictionaryS
    public Board board;

    @BeforeAll
    public static void setUp() {
        // dictionary = new Dictionary(path);
    }

    @BeforeEach
    public void init() {
        board = new Board();
    }

    @Test
    @DisplayName("Correct board placement")
    public void checkTestSuccess() throws IOException {
        configureBoard("boardStateCorrect1.txt");
        Assertions.assertTrue(board.check());
    }

    @Test
    @DisplayName("Start field not covered")
    public void checkTestFail1() throws IOException {
        configureBoard("boardStateFail1.txt");
        Assertions.assertFalse(board.check());
        Assertions.assertFalse(board.getField(5, 9).isValid());
    }

    @Test
    @DisplayName("Start field covered, but non-adjacent tiles exist")
    public void checkTestFail2() throws IOException {
        configureBoard("boardStateFail2.txt");
        Assertions.assertFalse(board.check());

    }

    @Test
    @DisplayName("Placements correct, but formed words does not exist")
    @Disabled
    public void checkTestFail3() throws IOException {
        configureBoard("boardStateFail3.txt");
        Assertions.assertFalse(board.check());
    }


    private void configureBoard(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + "/src/test/resources/BoardTest/" + filename)));

        for (int i = 0; i < BOARD_SIZE; i++) {
            String line = reader.readLine();
            String[] letters = line.split(" ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (letters[j].charAt(0) != '-') {
                    Tile tile = new Tile(letters[j].charAt(0), 0);
                    board.placeTile(tile, i, j);
                }
            }
        }
    }


}
