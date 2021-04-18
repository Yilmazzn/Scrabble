package game;

/**
 * @author yuzun Thread which runs parallel to the game application Counts down from 10mins and ends
 *     the associated game object if player runs out of time If player made a move before then, then
 *     thread is interrupted
 */
public class OvertimeWatch extends Thread {

  private static final long OVERTIME = 600000L; // 10 mins of overtime
  private Game game; // Game which runs the thread

  public OvertimeWatch(Game game) {
    this.game = game;
  }

  /**
   * Waits for 10mins after calling and ends game if not stopped till then Thread is interrupted by
   * game if player made a move
   */
  @Override
  public void run() {
    try {
      Thread.sleep(OVERTIME);
      // only reaches here if player did not make move for 10mins since beginning of their round
      game.end();
    } catch (InterruptedException e) {
    } // catch exception and do nothing with it since expected
  }

  /** Stops countdown by interrupting and killing this thread */
  public void stopCountdown() {
    if (!this.isInterrupted()) {
      this.interrupt();
    }
  }
}
