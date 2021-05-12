package game.players;

/**
 * @author yuzun
 * <p>Hard AI Player Only implements think method
 */
public class HardAiPlayer extends AiPlayer {

    public HardAiPlayer() {
        super(DIFFICULTY.HARD);
    }

    /**
     * Main method which is triggered by the game instance All computations from start of round till
     * end need to be done in here!
     */
    @Override
    public void think() {
    }
}
