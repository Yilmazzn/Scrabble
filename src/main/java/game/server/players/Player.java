package game.server.players;

import game.components.Board;
import game.components.Tile;
import game.server.Game;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author yuzun
 * <p>
 * The primary actor in the game on the server-side
 */

public abstract class Player {

    private final String username;
    private final boolean human;              // true if player is human
    private final ArrayList<String> foundWords = new ArrayList<>();    // words the player found in the current game session
    private boolean turn;               // true if it is player's turn
    private Game game;                  // the game the player participates in
    private int sessionScore;                  // score accumulated since the game round started
    private int lobbyScore;                    // score accumulated since joining the server

    public Player(String username, boolean human) {
        this.username = username;
        this.human = human;
    }

    /**
     * Makes the player join the game so it can interact with the game
     */
    public void joinGame(Game game) {
        this.game = game;
    }


    /**
     * ============= Called by the game
     */
    public abstract void addTilesToRack(Collection<Tile> tiles);         // Adds tiles to player's rack

    public abstract void removeTilesFromRack(Collection<Tile> tiles);    // Removes tiles from player's tile

    public abstract void updateBoard(Board board);              // Update player's board

    public abstract int getRackSize();                          // Returns the amount of tiles on the player's rack


    /**
     * ============= Called by the player
     */
    public void pass() {
        if (turn) {
            game.passRound(this);
        }
    }

    /**
     * Returns true if player is human
     */
    public boolean isHuman() {
        return human;
    }

    /**
     * Returns the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns if it is the player's turn
     */
    public boolean isTurn() {
        return turn;
    }

    /**
     * Sets turn of player
     */
    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    /**
     * Adds to the session and lobby score of the player
     */
    public void addScore(int score) {
        sessionScore += score;
        lobbyScore += score;
    }

    /**
     * Returns the session score
     */
    public int getSessionScore() {
        return sessionScore;
    }

    /**
     * Returns the lobby score
     */
    public int getLobbyScore() {
        return lobbyScore;
    }

    /**
     * Add words to the user's list of found words
     */
    public void addFoundWords(String... words) {
        for (String word : words) {
            foundWords.add(word);
        }
    }

    /**
     * Returns the list of words the player has found in the current session
     */
    public ArrayList<String> getFoundWords() {
        return foundWords;
    }
}
