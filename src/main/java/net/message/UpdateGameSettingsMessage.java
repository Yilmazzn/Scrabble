package net.message;

/**
 * Message Class for sending and receiving the current game Settings
 *
 * @author ygarip
 */
public class UpdateGameSettingsMessage extends Message {
  private int[] tileScores;
  private int[] tileDistributions;
  private String dictionary;

  /**
   * Constructor for Creating an UpdateGameSettingsMessage instance
   *
   * @param tileScores requires the tileScores settings
   * @param tileDistributions requires tileDistributions settings
   * @param dictionary requires dictionary as string
   */
  public UpdateGameSettingsMessage(int[] tileScores, int[] tileDistributions, String dictionary) {
    super(MessageType.UPDATEGAMESETTINGS);
    this.tileScores = tileScores;
    this.tileDistributions = tileDistributions;
    this.dictionary = dictionary;
  }

  /** @return returns tileScores */
  public int[] getTileScores() {
    return tileScores;
  }

  /** @return returns dictionary */
  public String getDictionary() {
    return this.dictionary;
  }

  /** @return returns tile distribution */
  public int[] getTileDistributions() {
    return this.tileDistributions;
  }
}
