package game.players;

/**
 * @author yuzun
 * <p>
 * Simple Ai Player
 * Only implements think method
 */

public class EasyAiPlayer extends AiPlayer {

    public EasyAiPlayer() {
        super(DIFFICULTY.EASY);
    }

    /**
     * Main method which is triggered by the game instance All computations from start of round till
     * end need to be done in here!
     */
    @Override
    public void think() {
    }
}
