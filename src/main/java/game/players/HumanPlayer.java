package game.players;

import game.Game;

public class HumanPlayer extends Player {

    /**
     * @author yuzun
     * <p>
     * Human player connected
     */

    private Game game;
    private Object client;  // TODO client

    public HumanPlayer(Object client) {
        super(true);
        this.client = client;
    }

}
