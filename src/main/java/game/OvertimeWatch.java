package game;

import gui.controllers.GameViewController;
import javafx.application.Platform;

/**
 * @author yuzun Thread which runs parallel to the game application Counts down from 10mins and ends
 *     the associated game object if player runs out of time If player made a move before then, then
 *     thread is interrupted
 */
public class OvertimeWatch extends Thread {

  private long overtime = 600000L; // 10 mins of overtime in ms
  private boolean running = false;

  private GameViewController
      gameViewController; // Controller to interact with GUI and update time every second

  public OvertimeWatch(GameViewController gameViewController, int overtime) {
    this.gameViewController = gameViewController;
  }

  /**
   * Waits for 10mins after calling and ends game if not stopped till then Thread is interrupted by
   * game if player made a move
   */
  @Override
  public void run() {
    running = true;
    try {
      while (overtime > 0 && running) { // start after each nextRound()
        Platform.runLater(
            () -> {
              gameViewController.updateTime(overtime);
            });

        Thread.sleep(500); // wait for 1/2th sec
        overtime = overtime - 500;
      }
      if (running) { // overtime exceeded and turn still true
        // TODO player.sendMesEXCEEDEDEDED
      }
    } catch (InterruptedException e) {
    } // catch exception and do nothing with it since expected
  }

  /** Stops countdown by interrupting and killing this thread */
  public void stopCountdown() {
    running = false;
    gameViewController.updateTime(overtime);
  }
}
