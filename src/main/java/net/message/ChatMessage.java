package net.message;

/**
 * a message class to send chat messages
 *
 * @author ygarip
 */
public class ChatMessage extends Message {
  String msg;
  String username;

  /**
   * a constructor for creating a new ChatMessage
   *
   * @param message the chatmessage that should be send
   */
  public ChatMessage(String message, String user) {
    super(MessageType.CHATMESSAGE);
    this.msg = message;
    this.username = user;
  }

  /** @return returns the msg */
  public String getMsg() {
    return this.msg;
  }

  /** @return returns the user */
  public String getUsername() {
    return this.username;
  }
}
