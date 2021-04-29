package net.message;

import game.components.Tile;

/**
 * a message class to get a Tile from the server
 *
 * @author vkaczmar
 */
public class GetTileMessage extends Message {
  private Tile tile;

  /** a constructor for creating a getTileMessage instance */
  public GetTileMessage() {
    super(MessageType.GETTILE);
  }

  /** @return returns Tile */
  public Tile getTile() {
    return this.tile;
  }

  /**
   * a method to set the tile
   *
   * @param tile requires the tile
   */
  public void setTile(Tile tile) {
    this.tile = tile;
  }
}
