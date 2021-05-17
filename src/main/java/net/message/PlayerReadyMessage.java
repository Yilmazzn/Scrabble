package net.message;

import client.PlayerProfile;
import game.players.Player;

import java.util.List;

/** @author vkaczmar Message Class for signaling, that you are ready when you are in a lobby */
public class PlayerReadyMessage extends Message {
  private boolean ready = false;
  private PlayerProfile player;
  private Player[] players; // todo vorher List<Player> ebenso set/get Methode

  /**
   * Constructor for creation
   *
   * @param ready true, if player is ready
   * @param player Requires PlayerProfile to include in message
   */
  public PlayerReadyMessage(boolean ready, PlayerProfile player) {
    super(MessageType.PLAYERREADY);
    this.ready = ready;
    this.player = player;
  }

  /** @return returns readyState */
  public boolean getReady() {
    return this.ready;
  }

  /** @return returns PlayerProfile */
  public PlayerProfile getProfile() {
    return this.player;
  }

  /**
   * Sets players list to paramter
   *
   * @param players Requires the connected players
   */
  public void setPlayers(Player[] players) {
    this.players = players;
  }

  /** @return returns the player List */
  public Player[] getPlayers() {
    return this.players;
  }

  /**
   * @param i Requires an integer i
   * @return returns the player at index i
   */
  public Player getPlayer(int i) {
    return this.players[i];
  }
}
