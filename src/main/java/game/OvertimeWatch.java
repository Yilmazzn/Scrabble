package game;

/**
 * @author yuzun
 *     <p>Thread which runs parallel to the game application Counts down from 10mins and ends the
 *     associated game object if player runs out of time
 */
public class OvertimeWatch implements Runnable {

  private static final long OVERTIME = 600000L; // 10 mins of overtime
  private long turnStartTime; // Start time of turn in ms (initialized when object is instantiated)
  private boolean running = true; // true it is still the player's turn
  private Game game; // Game which runs the thread

  public OvertimeWatch(Game game) {
    this.game = game;
  }

  /**
   * As long as stopwatch is running, stopwatch if overtime was exceeded To save resources it checks
   * every 10 seconds (to make sure thread is killed if move was made before then and running became
   * false) at the beginning If there are 15 seconds or less then it checks every 1/10th second
   */
  public void run() {
    this.turnStartTime = System.currentTimeMillis();
    while (running) {
      if (System.currentTimeMillis() - turnStartTime > OVERTIME) {
        game.end(); // Game ends
        break; // stop countdown
      }
      // Wait
      try {
        // 10s if time left > 15s, else 0.1s
        int millisToWait =
            (OVERTIME - (System.currentTimeMillis() - turnStartTime) > 15000) ? 10000 : 100;
        Thread.sleep(millisToWait);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /** Stops countdown */
  public void stop() {
    running = false;
  }

  /**
   * Returns overtime
   *
   * @return time since start of countdown in ms
   */
  public long getOvertime() {
    return System.currentTimeMillis() - turnStartTime;
  }
}
