package game.components;

import java.util.ArrayList;

public class Board {

    private static final int BOARD_SIZE = 15;   // width and height of board
    /**
     * @author yuzun
     * <p>
     * The main object of the game
     * Handles all interactions with the board
     */

    private BoardField[][] fields;      // 2D array to represent the board

    /**
     * Initializes an empty board
     */
    public Board() {
        fields = new BoardField[BOARD_SIZE][BOARD_SIZE];

        // Initializing one half of the board and then mirroring
        fields[7][7] = new BoardField(BoardField.FieldType.STAR);

        fields[0][0] = new BoardField(BoardField.FieldType.TWS);
        fields[0][7] = new BoardField(BoardField.FieldType.TWS);
        fields[7][0] = new BoardField(BoardField.FieldType.TWS);
        fields[14][0] = new BoardField(BoardField.FieldType.TWS);
        fields[14][7] = new BoardField(BoardField.FieldType.TWS);

        fields[1][1] = new BoardField(BoardField.FieldType.DWS);
        fields[1][13] = new BoardField(BoardField.FieldType.DWS);
        fields[2][2] = new BoardField(BoardField.FieldType.DWS);
        fields[2][12] = new BoardField(BoardField.FieldType.DWS);
        fields[3][3] = new BoardField(BoardField.FieldType.DWS);
        fields[3][11] = new BoardField(BoardField.FieldType.DWS);
        fields[4][4] = new BoardField(BoardField.FieldType.DWS);
        fields[4][10] = new BoardField(BoardField.FieldType.DWS);

        fields[1][5] = new BoardField(BoardField.FieldType.TLS);
        fields[5][1] = new BoardField(BoardField.FieldType.TLS);
        fields[5][5] = new BoardField(BoardField.FieldType.TLS);
        fields[9][1] = new BoardField(BoardField.FieldType.TLS);
        fields[9][5] = new BoardField(BoardField.FieldType.TLS);
        fields[13][5] = new BoardField(BoardField.FieldType.TLS);

        fields[0][3] = new BoardField(BoardField.FieldType.DLS);
        fields[2][6] = new BoardField(BoardField.FieldType.DLS);
        fields[3][0] = new BoardField(BoardField.FieldType.DLS);
        fields[3][7] = new BoardField(BoardField.FieldType.DLS);
        fields[6][2] = new BoardField(BoardField.FieldType.DLS);
        fields[6][6] = new BoardField(BoardField.FieldType.DLS);
        fields[7][3] = new BoardField(BoardField.FieldType.DLS);
        fields[8][2] = new BoardField(BoardField.FieldType.DLS);
        fields[8][6] = new BoardField(BoardField.FieldType.DLS);
        fields[11][0] = new BoardField(BoardField.FieldType.DLS);
        fields[11][7] = new BoardField(BoardField.FieldType.DLS);
        fields[12][6] = new BoardField(BoardField.FieldType.DLS);
        fields[14][3] = new BoardField(BoardField.FieldType.DLS);

        // Fill rest of fields with normal fields
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j <= 7; j++) {
                if (fields[i][j] == null) {
                    fields[i][j] = new BoardField(BoardField.FieldType.NORMAL);
                }
            }
        }

        // Mirror
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < 7; j++) {
                fields[i][BOARD_SIZE - j - 1] = new BoardField(fields[i][j].getType());
            }
        }
    }

    /**
     * Set given tile at given row and column
     */
    public void setTile(Tile tile, int row, int col) {
        if (isEmpty(row, col)) {
            fields[row][col].setTile(tile);
        }
    }

    /**
     * Returns true if field is empty
     */
    public boolean isEmpty(int row, int col) {
        return fields[row][col].getTile() == null;
    }


    /**
     * Makes a deep-copy of this board and returns it
     */
    public Board clone() {
        Board clone = new Board();
        clone.fields = new BoardField[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                clone.fields[i][j] = new BoardField(this.fields[i][j].getType(), this.fields[i][j].getTile());
            }
        }
        return clone;
    }

    /**
     * TODO Checks validity of board
     * checks if STAR field is covered
     * checks that there are no tiles not adjacent to others
     */
    public boolean check() {
        return false;
    }

    /**
     * TODO Extracts every valid word placed on the board
     */
    public ArrayList<String> scan() {
        return null;
    }
}
