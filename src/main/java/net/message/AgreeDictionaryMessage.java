package net.message;

/**
 * Message class for agreeing on a dictionary
 *
 * @author vkaczmar
 */
public class AgreeDictionaryMessage extends Message {
  private boolean agree;
  private String username;

  /**
   * Constructor for creating a new AgreeDictionaryMessage instance
   *
   * @param agree requires the boolean value of the agreeDictionaryMessage
   * @param username requires username of client, who sent the message
   */
  public AgreeDictionaryMessage(boolean agree, String username) {
    super(MessageType.AGREEDICTIONARY);
    this.agree = agree;
    this.username = username;
  }

  /** @return returns boolean value of the agreement */
  public boolean getAgreement() {
    return this.agree;
  }

  /** @return returns username */
  public String getUsername() {
    return username;
  }
}
