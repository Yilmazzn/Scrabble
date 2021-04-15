package game;

/**
 * @author yuzun
 * <p>
 * Thread which runs parallel to the game application
 * Counts down from 10mins and ends the game of associated game object if player runs out of time
 */

public class OvertimeWatch implements Runnable {

    private static final long OVERTIME = 600000L;  // 10 mins of overtime
    private long turnStartTime;             // Start time of turn in ms (initialized when object is instantiated)
    private boolean running = true;                // true it is still the player's turn
    private Game game;                      // Game which runs the thread

    public OvertimeWatch(Game game) {
        this.game = game;
    }

    public void run() {
        this.turnStartTime = System.currentTimeMillis();
        while (running) {
            if (System.currentTimeMillis() - turnStartTime > OVERTIME) {
                game.end();        // Game ends
            }
        }
    }

    /**
     * Stops countdown
     */
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
