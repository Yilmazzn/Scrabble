package net.message;

import client.PlayerProfile;

/**
 * a message class to send chat messages
 *
 * @author ygarip
 */
public class ChatMessage extends Message {
  String msg;
  PlayerProfile profile;

  /**
   * a constructor for creating a new ChatMessage
   *
   * @param message the chatmessage that should be send
   * @param profile Requires PlayerProfile as sender of msg
   */
  public ChatMessage(String message, PlayerProfile profile) {
    super(MessageType.CHATMESSAGE);
    this.msg = message;
    this.profile = profile;
  }

  /** @return returns the msg */
  public String getMsg() {
    return this.msg;
  }

  /** @return returns the user */
  public PlayerProfile getProfile() {
    return this.profile;
  }
}
