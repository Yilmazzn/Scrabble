package net.message;

/**
 * a message class to send chat messages
 *
 * @author ygarip
 */
public class ChatMessage extends Message {
  private String msg;
  private PlayerProfile profile;

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

  /** @return Returns PlayerProfile attached to ChatMessage */
  public PlayerProfile getProfile() {
    return this.profile;
  }
}
