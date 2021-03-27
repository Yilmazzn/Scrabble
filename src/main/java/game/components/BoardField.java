package game.components;

/**
 * @author yuzun
 * <p>
 * Single tile on the board which can hold a tile
 */

public class BoardField {

    private final FieldType type;     // Type of field
    private Tile tile;          // Tile placed on this field

    /**
     * Initialize the field with the type of field this instance represents
     */
    public BoardField(FieldType type) {
        this.type = type;
    }

    /**
     * Returns true if field is empty
     */
    public boolean isEmpty() {
        return tile == null;
    }

    /**
     * Returns the tile this field holds (null if none)
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Places the given tile on this field
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /** Returns the type of this field */
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
        NORMAL, STAR, DWS, DLS, TLS, TWS
    }
}
