package game.players;

import game.Game;
import game.components.Board;
import game.components.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public abstract class Player {

    /**
     * @author yuzun
     * <p>
     * Players are actors in a game
     * Players are loaded when they join the game lobby
     * when the game starts, players receive tiles and can make moves when they are in turn
     */

    private static final int RACK_SIZE = 7; // amount of tiles a player can hold
    private LinkedList<Tile> rack;          // collection of tiles the player can directly interact with
    private boolean human;                  // true if player is human
    private boolean turn = false;           // true if it is the player's turn
    private Game game;                      // the game which the player takes part in
    private Board board;                    // Board the player can sees and can manipulate
    private int score;                      // Score
    private ArrayList<String> wordsFound;   // Words the player found


    public Player(Game game, boolean human) {
        this.human = human;
        this.game = game;
        rack = new LinkedList<>();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Player can pass his round if it is player's turn
     */
    public void pass() {
        if (turn) {
            game.pass();
        }
    }

    /**
     * Exchanges given tiles, if it is player's turn & the bag holds enough tiles
     * The game returns equivalent amount of different tiles from the bag which are placed back on to the rack
     */
    public void exchange(Tile... tiles) {
        if (turn && game.getBagSize() >= tiles.length) {  // Player's turn and enough tiles in bag
            Arrays.stream(tiles).forEach(t -> rack.remove(t));      // remove from player's rack
            LinkedList<Tile> exchangeTiles = game.exchange(this, tiles);    // pass to game and get new tiles
            rack.addAll(exchangeTiles);                     // put new tiles to the rack
        }
    }

    /**
     * Makes a play when it is the player's turn
     * The board is checked before-hand, if the board state is invalid then the move is not passed to the game and the player's board
     * is set back to the state before the player made his placements
     */
    public void play() {
        if (turn) {
            if (board.check()) { // Board state is valid
                score += game.place(this, board);    // Pass board to game and get evaluated score
            } else {            // Board state is invalid
                updateBoard(game.getBoard());
            }
        }
    }

    /**
     * Adds given tiles to rack if the number of tiles is still below accepted amount
     */
    public void addTilesToRack(Tile... tiles) {
        if (rack.size() + tiles.length <= RACK_SIZE) {
            Arrays.stream(tiles).forEach(t -> rack.add(t));
        }
    }

    /**
     * Removes given Tile from rack if it exists in the rack
     */
    public void removeTilesFromRack(Tile tile) {
        rack.remove(tile);
    }

    /**
     * Returns amount of tiles on the rack of the player
     */
    public int getRackSize() {
        return rack.size();
    }

    /**
     * Updates the player's board to the current state of the board
     */
    public void updateBoard(Board gameBoard) {
        this.board = gameBoard.clone();
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isHuman() {
        return human;
    }

    public int getScore() {
        return score;
    }

    /**
     * Adds a word to the player's list of found words
     */
    public void addFoundWord(String word) {
        wordsFound.add(word);
    }

    public Board getBoard() {
        return board;
    }


}
