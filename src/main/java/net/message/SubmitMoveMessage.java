package net.message;

import game.components.Board;

/** @author vkaczmar Message class for checking if a move is valid or not */
public class SubmitMoveMessage extends Message {
  private String username;
  private boolean valid;
  private Board board;

  // TODO Remove
  /**
   * constructor for creating the SumbitMoveMessage Class
   *
   * @param username username of client, who wants to submit
   */
  public SubmitMoveMessage(String username, Board board) {
    super(MessageType.SUBMITMOVE);
    this.username = username;
    this.board = board;
  }

  /** @return returns the username */
  public String getUsername() {
    return username;
  }

  /** @return returns the valid attribute */
  public boolean getValid() {
    return valid;
  }

  /**
   * Method for setting the valid attribute
   *
   * @param valid sets the attribute valid
   */
  public void setValid(boolean valid) {
    this.valid = valid;
  }

  /** @return returns board */
  public Board getBoard() {
    return board;
  }
}
