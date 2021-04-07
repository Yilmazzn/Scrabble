package game.components;

/**
 * @author yuzun
 *
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
        fields[7][7] = new BoardField(BoardField.FieldType.STAR);

        fields[0][0] = new BoardField(BoardField.FieldType.TWS);
        fields[0][7] = new BoardField(BoardField.FieldType.TWS);
        fields[7][0] = new BoardField(BoardField.FieldType.TWS);
        fields[14][0] = new BoardField(BoardField.FieldType.TWS);
        fields[14][7] = new BoardField(BoardField.FieldType.TWS);

        fields[1][1] = new BoardField(BoardField.FieldType.DWS);
        fields[13][1] = new BoardField(BoardField.FieldType.DWS);
        fields[2][2] = new BoardField(BoardField.FieldType.DWS);
        fields[12][2] = new BoardField(BoardField.FieldType.DWS);
        fields[3][3] = new BoardField(BoardField.FieldType.DWS);
        fields[11][3] = new BoardField(BoardField.FieldType.DWS);
        fields[4][4] = new BoardField(BoardField.FieldType.DWS);
        fields[10][4] = new BoardField(BoardField.FieldType.DWS);

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
     */
    public boolean check() {

        // Every field is valid default
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                fields[i][j].setValid(true);
            }
        }

        // Check: Star field is covered
        //  --> mark every tile on board as invalid
        if (fields[7][7].isEmpty()) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    fields[i][j].setValid(fields[i][j].isEmpty());
                }
            }
            return false;
        }

        // Check adjacency
        boolean placementCheck = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!fields[i][j].isEmpty()) {
                    boolean isAdjacent = false;
                    if (i > 0 && !fields[i - 1][j].isEmpty()) {                 //up
                        isAdjacent = true;
                        continue;
                    }
                    if (i < BOARD_SIZE - 1 && !fields[i + 1][j].isEmpty()) {    //down
                        isAdjacent = true;
                        continue;
                    }
                    if (j > 0 && !fields[i][j - 1].isEmpty()) {                 //left
                        isAdjacent = true;
                        continue;
                    }
                    if (j < BOARD_SIZE - 1 && !fields[i][j + 1].isEmpty()) {    //right
                        isAdjacent = true;
                    }
                    fields[i][j].setValid(isAdjacent);
                    if (!isAdjacent) {
                        placementCheck = false;
                        break;
                    }
                }
            }
        }
        if (!placementCheck) {
            return false;
        }

        // Check: Valid words are formed (Dictionary
        // TODO

        return true;
    }

}
