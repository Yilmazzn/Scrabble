package net.message;

/**
 * Message class for indicating whether it is your turn or not
 *
 * @author vkaczmar
 */
public class TurnMessage extends Message {
  private boolean turn;
  private boolean[] turns;

  /**
   * Constructor for Creating TurnMessage
   *
   * @param turn Requires the boolean value for turn
   */
  public TurnMessage(boolean turn, boolean[] turns) {
    super(MessageType.TURN);
    this.turn = turn;
    this.turns = turns;
  }

  /** @return returns the boolean value of turn */
  public boolean getTurn() {
    return this.turn;
  }

  /** @return returns in the boolean array the player's turns */
  public boolean[] getTurns() {
    return this.turns;
  }
}
