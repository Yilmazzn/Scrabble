package net.message;

import game.components.Board;

/** @author ygarip Message class for updating points */
public class UpdatePointsMessage extends Message {
  private Board currentState;
  private Board previousState;
  private int id;

  /**
   * constructor for creating a Message to update the points
   *
   * @param currentState of the Board
   * @param previousState of the Board
   * @param id of the client
   */
  public UpdatePointsMessage(Board currentState, Board previousState, int id) {
    super(MessageType.UPDATEPOINTS);
    this.currentState = currentState;
    this.previousState = previousState;
    this.id = id;
  }

  /** @return returns the currentBoardState */
  public Board getCurrentState() {
    return currentState;
  }

  /** @return returns previous boardState from the beginning of the turn */
  public Board getPreviousState() {
    return previousState;
  }

  /** @return returns username */
  public int getId() {
    return id;
  }
}
