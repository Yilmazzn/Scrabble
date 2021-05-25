package net.message;

/**
 * We're in the Endgame now ~ Avengers
 *
 * @author ygarip
 */
public class EndGameMessage extends Message {
  private int type;
  private int winnerScore;
  private String[][] foundWords;

  /**
   * Constructor for creating EndGameMessage
   *
   * @param type
   */
  public EndGameMessage(int type) {
    super(MessageType.ENDGAME);
    this.type = type;
  }

  /**
   * Constructor for creating EndGameMessage
   *
   * @param type Requires type to be send
   * @param winnerScore Requires winnerScore to be send
   * @param foundWords Requires all foundWords in 2D-String array
   */
  public EndGameMessage(int type, int winnerScore, String[][] foundWords) {
    super(MessageType.ENDGAME);
    this.type = type;
    this.winnerScore = winnerScore;
    this.foundWords = foundWords;
  }

  /** @return Returns type of message */
  public int getType() {
    return this.type;
  }

  /** @return Returns winnerScore */
  public int getWinnerScore() {
    return winnerScore;
  }

  /** @return returns foundWords */
  public String[][] getFoundWords() {
    return this.foundWords;
  }
}
