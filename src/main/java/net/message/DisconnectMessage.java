package net.message;

import client.PlayerProfile;

/**
 * a message class to disconnect the client from server
 *
 * @author ygarip
 */
public class DisconnectMessage extends Message {
  private PlayerProfile profile;

  /**
   * a constructor to create a disconnect message for the client
   *
   * @param profile Requires PlayerProfile to be included in DisconnectMessage
   * @param disconnectMessage Requires the 'reason' for disconnect
   */
  public DisconnectMessage(PlayerProfile profile, String disconnectMessage) {
    super(MessageType.DISCONNECT);
    this.profile = profile;
    this.disconnectMessage = disconnectMessage;
  }

  /**
   * a method to get the username
   *
   * @return returns the username
   */
  public PlayerProfile getProfile() {
    return this.profile;
  }
}
