package net.message;

/**
 * Message class for adding an ai as player.
 *
 * @author ygarip
 */
public class AddAIMessage extends Message {
  private boolean difficulty;

  /**
   * Constructor for creating an AddAIMessage.
   *
   * @param difficulty Requires difficulty to be set, true = hard, false = easy
   */
  public AddAIMessage(boolean difficulty) {
    super(MessageType.ADDAI);
    this.difficulty = difficulty;
  }

  /** @return Returns difficulty */
  public boolean getDifficulty() {
    return difficulty;
  }
}
