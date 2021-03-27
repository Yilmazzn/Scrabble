package game.server.players;

import game.components.Board;
import game.components.Tile;

import java.util.Collection;

/**
 * @author yuzun
 */

public class HumanPlayer extends Player {

    //private ClientProtocol connection;        // connection of player's client to the server
    //private PlayerProfile profile;            // Player Profile the client used to host/join the game
    private final boolean host;

    /**
     * Initialize a player with connection to the server and the profile used by the client
     */
    public HumanPlayer(boolean host) {
        super("Human1", true);
        this.host = host;
    }

    @Override
    public void addTilesToRack(Collection<Tile> tiles) {

    }

    @Override
    public void removeTilesFromRack(Collection<Tile> tiles) {

    }

    @Override
    public void updateBoard(Board board) {

    }

    @Override
    public int getRackSize() {
        return 0;
    }

    public boolean isHost() {
        return host;
    }
}
