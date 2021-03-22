package game.players;

import game.Game;

public class HumanPlayer extends Player {

    /**
     * @author yuzun
     */

    private Game game;

    public HumanPlayer(Game game) {
        super(game, true);
    }
}
