package gui.components;

import game.components.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author mnetzer
 * Rack of a player holds the RackTiles/ Tiles, that the player can place
 */
public class Rack implements Serializable {

  public static final int RACK_SIZE = 7; // width of the rack
  private final RackField[] fields; // array to represent the rack fields


  /**
   * Initializes an empty board
   */
  public Rack() {
    fields = new RackField[RACK_SIZE];

    // Initializing one half of the board and then mirroring
    fields[0] = new RackField(0);
    fields[1] = new RackField(1);
    fields[2] = new RackField(2);
    fields[3] = new RackField(3);
    fields[4] = new RackField(4);
    fields[5] = new RackField(5);
    fields[6] = new RackField(6);
  }

  /**
   * Set given tile at given column
   *
   * @param tile Tile to place
   * @param col  Column number of rack (starting from 0)
   */
  public void placeTile(Tile tile, int col) {
    fields[col].setTile(tile);
  }

  /**
   * Returns true if field is empty
   *
   * @param col Column number of field on rack (starting from 0)
   * @return true, if field at given col on rack is empty
   */
  public boolean isEmpty(int col) {
    return fields[col].isEmpty();
  }

  /**
   * Returns the tile at given column
   *
   * @param col Column number of field on rack (starting from 0)
   * @return tile at given column on rack
   */
  public Tile getTile(int col) {
    return fields[col].getTile();
  }

  /**
   * Returns the BoardField at given column counting from 0
   *
   * @param col Column number of field on rack (starting from 0)
   * @return field at given column on rack
   */
  public RackField getField(int col) {
    return fields[col];
  }

  /** Shuffles the tiles on the rack */
  public void shuffleRack(){
    for(RackField field : fields){
     field.setSelected(false);
    }

    RackField tmp;
    int rand;
    Random r = new Random();
    for (int i = 0; i < fields.length; i++) {
      rand = r.nextInt(fields.length);
      tmp = fields[i];
      fields[i] = fields[rand];
      fields[rand] = tmp;
    }
  }

  /**
   * Returns if a tile on the rack is selected for an exchange
   *
   * @param col identifies the tile
   * @return boolean if tile is selected
   */
  public boolean isSelected(int col) {
    return fields[col].isSelected();
  }

  /**
   * Add a tile to the next free spot on the rack
   *
   * @param tile tile that should be added
   */
  public void add(Tile tile) {
    for(int i = 0; i < RACK_SIZE; i++){
      if(fields[i].isEmpty()){
        fields[i].setTile(tile);
        break;
      }
    }
  }

  /**
   * Removes a tile of the rack
   *
   * @param position identifies the tile position
   */
  public void remove(int position){
    System.out.println("Position: " + position);
    fields[position].setTile(null);
    fields[position].setSelected(false);
  }

}
