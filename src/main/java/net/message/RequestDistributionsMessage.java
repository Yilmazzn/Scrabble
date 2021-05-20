package net.message;

/**
 * Message class for requesting TileDistributions
 *
 * @author yuzun
 */
public class RequestDistributionsMessage extends Message {
  private int[] distributions;

  /** Constructor for creating a RequestDistributionsMessage */
  public RequestDistributionsMessage() {
    super(MessageType.REQUESTDISTRIBUTIONS);
  }
  /** @param distributions Requires the tile distribution */
  public void setDistributions(int[] distributions) {
    this.distributions = distributions;
  }
  /** @return returns the tile distributions */
  public int[] getDistributions() {
    return this.distributions;
  }
}
