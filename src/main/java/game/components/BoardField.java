package game.components;

public class BoardField {

    /**
     * @author yuzun
     * <p>
     * Single tile on the board which can hold a tile
     */

    private FieldType type;     // Type of field
    private Tile tile;          // Tile placed on this field

    /**
     * BoardField object is instantiated with the type of field it represents
     */
    public BoardField(FieldType type) {
        this.type = type;
    }

    /**
     * BoardField object is instantiated with the type of field it represents and the tile it holds
     */
    public BoardField(FieldType type, Tile tile) {
        this.type = type;
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public FieldType getType() {
        return type;
    }

    /**
     * @author yuzun
     * <p>
     * This class represents a single square on the board
     */

    // Types of fields
    public enum FieldType {
        NORMAL, STAR, DLS, TLS, DWS, TWS
    }
}
