package net.message;

import java.io.Serializable;

/**
 * a abstract message class that represents communication of server and client
 *
 * @author ygarip
 */
public abstract class Message implements Serializable {
  private static final long serialVersionUID = 1L;
  private MessageType mType;

  /**
   * an abstract constructor for creating a new message
   *
   * @param type an enumtype of messageType
   */
  public Message(MessageType type) {
    this.mType = type;
  }

  /**
   * a method to get the messagetype of the message
   *
   * @return returns the messagetype
   */
  public MessageType getMessageType() {
    return this.mType;
  }
}
