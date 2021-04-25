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
  public PlayerReadyMessage(boolean ready, String username) {
    super(MessageType.PLAYERREADY);
    this.ready = ready;
    this.username = username;
  }

  /** @return returns readyState */
  public boolean getReady() {
    return this.ready;
  }

  /** @return returns username */
  public String getUsername() {
    return this.username;
  }
}
