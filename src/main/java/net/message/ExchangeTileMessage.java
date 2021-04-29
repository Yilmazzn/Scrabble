package net.message;

import game.components.Tile;

/**
 * a message class to request a tiles exchange with the server bag
 *
 * @author vkaczmar
 */
public class ExchangeTileMessage extends Message {
  private Tile[] oldTiles;
  private Tile[] newTiles;
  private String message;

  /**
   * a constructor to create a ExchangeTileMessage instance
   *
   * @param oldTiles Requires tiles array with tiles you want to remove
   */
  public ExchangeTileMessage(Tile[] oldTiles) {
    super(MessageType.EXCHANGETILES);
    this.oldTiles = new Tile[oldTiles.length];
    for (int i = 0; i < this.oldTiles.length; i++) {
      this.oldTiles[i] = oldTiles[i];
    }
  }

  /** @return returns the requested tiles to exchange */
  public Tile[] getOldTiles() {
    return oldTiles;
  }

  /** @return returns the new Tiles that will swap with the old tiles */
  public Tile[] getNewTiles() {
    return newTiles;
  }

  /**
   * a method to set the new Tiles that should be added to the rack of the client
   *
   * @param newTiles requires the newTiles from the bag
   */
  public void setNewTiles(Tile[] newTiles) {
    this.newTiles = new Tile[newTiles.length];
    for (int i = 0; i < this.newTiles.length; i++) {
      this.newTiles[i] = newTiles[i];
    }
  }

  /** @return Returns message from Message */
  public String getError() {
    return message;
  }

  /**
   * a method to set the message which is a error string
   *
   * @param message requires the message
   */
  public void setError(String message) {
    this.message = message;
  }
}
