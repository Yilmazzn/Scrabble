package net.message;

/** @author vkaczmar Message Class for signaling, that you are ready when you are in a lobby */
public class PlayerReadyMessage extends Message {
  private boolean ready = false;
  private String username;

  /**
   * Constructor for creation
   *
   * @param ready true, if player is ready
   * @param username username, from client, who sets ready flag
   */
  public PlayerReadyMessage(boolean ready, PlayerProfile player) {
    super(MessageType.PLAYERREADY);
    this.ready = ready;
    this.username = username;
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
  public Player getPlayer(int i){
    return this.players[i];
  }
}
