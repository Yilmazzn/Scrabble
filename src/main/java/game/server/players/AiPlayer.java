package game.server.players;

import game.components.Board;
import game.components.Tile;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author yuzun
 */

public abstract class AiPlayer extends Player {

    private static final int RACK_SIZE = 7;
    private final LinkedList<Tile> rack = new LinkedList<>();     // rack of the player

    public AiPlayer() {
        super("AI", false);
    }

    /**
     * This method makes the ai evaluate the board and think of its next play
     * Depending on the subclass, this method is overwritten in a way, that represents the intelligence of the bot
     */
    public abstract void think();


    /**
     * Adds tiles to the rack of the ai player
     */
    @Override
    public void addTilesToRack(Collection<Tile> tiles) {
        rack.addAll(tiles);
    }

    /**
     * Removes given tiles form the rack of the ai player
     */
    @Override
    public void removeTilesFromRack(Collection<Tile> tiles) {
        rack.removeAll(tiles);
    }

    @Override
    public void updateBoard(Board board) {

    }

    @Override
    public int getRackSize() {
        return rack.size();
    }
}
