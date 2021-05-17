package net.message;

/**
 * a message class to disconnect the client from server
 *
 * @author ygarip
 */
public class DisconnectMessage extends Message {
  private String username;

  /**
   * a constructor to create a disconnect message for the client
   *
   * @param username a username
   */
  public DisconnectMessage(PlayerProfile profile) {
    super(MessageType.DISCONNECT);
    this.username = username;
  }

  /**
   * a method to get the username
   *
   * @return returns the username
   */
  public String getUsername() {
    return this.username;
  }
}
