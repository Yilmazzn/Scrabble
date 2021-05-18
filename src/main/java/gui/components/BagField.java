package gui.components;

import game.components.Tile;

import java.io.Serializable;

/**
 * @author mnetzer
 *     <p>Single tile on the rack which can hold a tile
 */
public class BagField implements Serializable {

  private Tile tile; // Tile placed in this spot
  private int id; // Column on the rack this field is located at

  /** Initialize the field with the type of field this instance represents */
  public BagField(int id) {
    this.id = id;
  }

  /** Returns true if field is empty */
  public boolean isEmpty() {
    return tile == null;
  }

  /** Returns the tile this field holds (null if none) */
  public Tile getTile() {
    return tile;
  }

  /** Places the given tile on this field */
  public void setTile(Tile tile) {
    this.tile = tile;
  }

  public void setId(int id){
    this.id = id;
  }

  public void removeTile(){
    this.tile = null;
  }


  /** Returns column this field is located at on the board */
  public int getId() {
    return id;
  }

}
