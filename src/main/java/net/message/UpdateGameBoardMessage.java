package net.message;

import game.components.Board;

/**
 * @author ygarip Message Class for signaling, to update the board of every client, Board is part of
 *     the message
 */
public class UpdateGameBoardMessage extends Message {
  private Board board;

  /**
   * a constructor for creating the Message
   *
   * @param board send the actual Bordstate
   */
  public UpdateGameBoardMessage(Board board) {
    super(MessageType.UPDATEGAMEBOARD);
    this.board = board;
  }

  /** @return returns Board */
  public Board getBoard() {
    return this.board;
  }
}
