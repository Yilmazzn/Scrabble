package net.message;

import game.players.Player;
import game.players.RemotePlayer;

import java.io.Serializable;
import java.util.List;

/** @author vkaczmar Message Class for signaling, that you are ready when you are in a lobby */
public class PlayerReadyMessage extends Message {
  private boolean ready = false;
  private RemotePlayer[] players;

  /**
   * Constructor for creation
   *
   * @param ready true, if player is read
   */
  public PlayerReadyMessage(boolean ready) {
    super(MessageType.PLAYERREADY);
    this.ready = ready;
  }

  /** @return returns readyState */
  public boolean getReady() {
    return this.ready;
  }

  /**
   * a method to set the ready state of the player
   *
   * @param ready Requires the boolean value of ready
   */
  public void setReady(boolean ready) {
    this.ready = ready;
  }

  /**
   * Sets players list to paramter
   *
   * @param players Requires the connected players
   */
  public void setPlayers(RemotePlayer[] players) {
    this.players = players;
  }

  /** @return returns the player List */
  public RemotePlayer[] getPlayers() {
    return this.players;
  }
}
