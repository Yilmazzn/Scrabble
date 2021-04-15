package game.components;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author yuzun
 * <p>
 * The board is the main object in the game of scrabble
 * This class handles all interactions with the board itself -- Integrated Game Logic --
 */

public class Board {

    private static final int BOARD_SIZE = 15;   // width and height of board
    private BoardField[][] fields;      // 2D array to represent the board fields

    /**
     * Initializes an empty board
     */
    public Board() {
        fields = new BoardField[BOARD_SIZE][BOARD_SIZE];

        // Initializing one half of the board and then mirroring
        fields[7][7] = new BoardField(BoardField.FieldType.STAR, 7, 7);

        fields[0][0] = new BoardField(BoardField.FieldType.TWS, 0, 0);
        fields[0][7] = new BoardField(BoardField.FieldType.TWS, 0, 7);
        fields[7][0] = new BoardField(BoardField.FieldType.TWS, 7, 0);
        fields[14][0] = new BoardField(BoardField.FieldType.TWS, 14, 0);
        fields[14][7] = new BoardField(BoardField.FieldType.TWS, 14, 7);

        fields[1][1] = new BoardField(BoardField.FieldType.DWS, 1, 1);
        fields[13][1] = new BoardField(BoardField.FieldType.DWS, 13, 1);
        fields[2][2] = new BoardField(BoardField.FieldType.DWS, 2, 2);
        fields[12][2] = new BoardField(BoardField.FieldType.DWS, 12, 2);
        fields[3][3] = new BoardField(BoardField.FieldType.DWS, 3, 3);
        fields[11][3] = new BoardField(BoardField.FieldType.DWS, 11, 3);
        fields[4][4] = new BoardField(BoardField.FieldType.DWS, 4, 4);
        fields[10][4] = new BoardField(BoardField.FieldType.DWS, 10, 4);

        fields[1][5] = new BoardField(BoardField.FieldType.TLS, 1, 5);
        fields[5][1] = new BoardField(BoardField.FieldType.TLS, 5, 1);
        fields[5][5] = new BoardField(BoardField.FieldType.TLS, 5, 5);
        fields[9][1] = new BoardField(BoardField.FieldType.TLS, 9, 1);
        fields[9][5] = new BoardField(BoardField.FieldType.TLS, 9, 5);
        fields[13][5] = new BoardField(BoardField.FieldType.TLS, 13, 5);

        fields[0][3] = new BoardField(BoardField.FieldType.DLS, 0, 3);
        fields[2][6] = new BoardField(BoardField.FieldType.DLS, 2, 6);
        fields[3][0] = new BoardField(BoardField.FieldType.DLS, 3, 0);
        fields[3][7] = new BoardField(BoardField.FieldType.DLS, 3, 7);
        fields[6][2] = new BoardField(BoardField.FieldType.DLS, 6, 2);
        fields[6][6] = new BoardField(BoardField.FieldType.DLS, 6, 6);
        fields[7][3] = new BoardField(BoardField.FieldType.DLS, 7, 3);
        fields[8][2] = new BoardField(BoardField.FieldType.DLS, 8, 2);
        fields[8][6] = new BoardField(BoardField.FieldType.DLS, 8, 6);
        fields[11][0] = new BoardField(BoardField.FieldType.DLS, 11, 0);
        fields[11][7] = new BoardField(BoardField.FieldType.DLS, 11, 7);
        fields[12][6] = new BoardField(BoardField.FieldType.DLS, 12, 6);
        fields[14][3] = new BoardField(BoardField.FieldType.DLS, 14, 3);

        // Fill rest of fields with normal fields
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j <= 7; j++) {
                if (fields[i][j] == null) {
                    fields[i][j] = new BoardField(BoardField.FieldType.NORMAL, i, j);
                }
            }
        }

        // Mirror
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < 7; j++) {
                fields[i][BOARD_SIZE - j - 1] = new BoardField(fields[i][j].getType(), i, BOARD_SIZE - j - 1);
            }
        }
    }


    /**
     * Set given tile at given row and column
     */
    public void placeTile(Tile tile, int row, int col) {
        fields[row][col].setTile(tile);
    }

    /**
     * Returns true if field is empty
     */
    public boolean isEmpty(int row, int col) {
        return fields[row][col].isEmpty();
    }

    /**
     * Returns the tile at given row and column
     */
    public Tile getTile(int row, int col) {
        return fields[row][col].getTile();
    }

    /**
     * Returns true if field at given row and column is empty
     */
    public boolean fieldIsEmpty(int row, int col) {
        return fields[row][col].isEmpty();
    }

    /**
     * Returns the BoardField at given row and column counting from 0
     */
    public BoardField getField(int row, int col) {
        return fields[row][col];
    }

    /**
     * Checks validity of board state
     * checks if STAR field is covered
     * checks that there are no tiles not adjacent to others
     * checks that valid words are formed on the board based on the dictionary
     * As it checks it sets fields which hold invalid tiles as invalid (field.valid = false)
     * TODO DICTIONARY
     *
     * @param placements placements in the last turn
     * @param dictionary Dictionary the game is based on
     */
    public boolean check(List<BoardField> placements, Object dictionary) {

        // Every field is valid default
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                fields[i][j].setValid(true);
            }
        }

        // Check: Star field is covered
        //  --> mark every tile on board as invalid
        if (fields[7][7].isEmpty()) {
            placements.forEach(bf -> bf.setValid(false));
            return false;
        }

        // Check placements are all in a specific row or column
        Set<Integer> uniqueRows = new TreeSet<>();
        Set<Integer> uniqueColumns = new TreeSet<>();

        for (BoardField bf : placements) {
            uniqueRows.add(bf.getRow());
            uniqueColumns.add(bf.getColumn());
        }

        if (uniqueRows.size() > 1 && uniqueColumns.size() > 1) {
            return false;
        }

        // Check adjacency
        boolean placementCheck = true;
        for (BoardField bf : placements) {

            int i = bf.getRow();        // row
            int j = bf.getColumn();     // column

            if (!fields[i][j].isEmpty()) {
                boolean isAdjacent = false;
                if (i > 0 && !fields[i - 1][j].isEmpty()) {                 //up
                    isAdjacent = true;
                }
                if (i < BOARD_SIZE - 1 && !fields[i + 1][j].isEmpty()) {    //down
                    isAdjacent = true;
                }
                if (j > 0 && !fields[i][j - 1].isEmpty()) {                 //left
                    isAdjacent = true;
                }
                if (j < BOARD_SIZE - 1 && !fields[i][j + 1].isEmpty()) {    //right
                    isAdjacent = true;
                }
                fields[i][j].setValid(isAdjacent);
                if (!isAdjacent) {
                    placementCheck = false;
                }
            }
        }
        if (!placementCheck) {
            return false;
        }


        // Check: Valid words are formed (Dictionary
        // Check horizontal
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE - 1; j++) {

                // if this field or field to the right is empty then skip
                if (isEmpty(i, j) || isEmpty(i, j + 1)) {
                    continue;
                }

                // traverse to the right until field is empty or out of bounds
                String word = "";    // horizontal word
                int k = j;      // column of first tile of the word
                while (k < BOARD_SIZE && !isEmpty(i, k)) {
                    word += getTile(i, k++);        // Append tile letter to found word
                }

                /* TODO UNCOMMENT DICTIONARY CHECK
                if (!dictionary.wordExists(word)) {
                    return false;
                }
                */
                j = k;  // sets j to the field after the word (out of bounds or empty)
            }
        }

        // Check vertical
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE - 1; j++) {

                // if this field or field to the right is empty then skip
                if (isEmpty(j, i) || isEmpty(j + 1, i)) {
                    continue;
                }

                // traverse to the right until field is empty or out of bounds
                String word = "";    // horizontal word
                int k = j;      // row of first tile of the word
                while (k < BOARD_SIZE && !isEmpty(k, i)) {
                    word += getTile(k++, i);        // Append tile letter to found word
                }

                /* TODO UNCOMMENT DICTIONARY CHECK
                if (!dictionary.wordExists(word)) {
                    return false;
                }
                */
                j = k;  // sets j to the field after the word (out of bounds or empty)
            }
        }


        return true;
    }

}
