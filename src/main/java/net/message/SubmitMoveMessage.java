package net.message;

import game.components.Board;

/** @author vkaczmar Message class for checking if a move is valid or not */
public class SubmitMoveMessage extends Message {
  private Board board;

  /** Constructor for creating a SubmitMoveMassage
   * Only created for sending from client to server */
  public SubmitMoveMessage() {
    super(MessageType.SUBMITMOVE);
  }

  /**
   * Constructor for creating a SubmitMoveMassage with board instance
   * Only created for sending from Server to Client
   * @param board Requires board to be set
   */
  public SubmitMoveMessage(Board board) {
    super(MessageType.SUBMITMOVE);
    this.board = board;
  }

  /** @return Returns board */
  public Board getBoard() {
    return board;
  }

}
