package game;

import game.components.Board;
import game.components.Tile;
import game.players.HumanPlayer;
import game.players.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Game {

    /**
     * @author yuzun
     * <p>
     * Single instance running on the host's computer
     * The game flow itself, handling turns, player moves, etc.
     */

    private ArrayList<Player> players;          // Players playing this game (in opposite order of their turns)   [0: last player, 1: second last player, ...]
    private Board board;                        // Game Board
    private boolean running;                    // true if game is running
    private int roundNum;                       // amount of total rounds since start
    private int roundsSinceLastScore;           // last n amount of rounds without points
    private Object dictionary;                  // Dictionary this game relies on   TODO Dictionary class
    private int[] letterScores;                 // nth element is (n+1)th letter of alphabet's score
    private LinkedList<Tile> bag = new LinkedList<>();           // bag of tiles in the game
    private ArrayList<String> wordsFound = new ArrayList<>();  // a list of words found up to this point
    private HumanPlayer host;

    /**
     * A game expects
     * a list of players
     * an array in which the nth element represents the amount of tiles of the (n+1)th letter in the alphabet  (26th element is the joker tile)
     * an array in which the nth element represents the score of the (n+1)th letter in the alphabet            (26th element is the joker tile)
     * When a game is created
     * it sets the status 'running' to true
     * creates a board and shares them with its players
     * creates the tiles (amount & score determined by the given arrays), shuffles them and distributes them to the players
     */
    public Game(ArrayList<Player> players, int[] letterDistribution, int[] letterScores) {
        // Players participating
        this.players = players;
        players.forEach(player -> player.setGame(this));

        // Set running to true
        this.running = true;

        // Set up
        // Build a board and give every player the same starting point
        Board board = new Board();
        players.forEach(player -> player.updateBoard(board));

        // Create tiles, put them into the bag and shuffle
        for (int i = 0; i < letterDistribution.length; i++) {
            for (int j = 0; j < letterDistribution[i]; j++) {
                bag.add(new Tile(i, letterScores[i]));
            }
        }
        Collections.shuffle(bag);

        // Give each player 7 tiles and remove them from the bag
        for (Player player : players) {
            player.addTilesToRack(bag.pop(), bag.pop(), bag.pop(), bag.pop(), bag.pop(), bag.pop(), bag.pop());
        }
    }


    /**
     * Called to start a new round
     * increments round number
     * assigns turn
     */
    public void nextRound() {
        roundNum++;     // increment round number
        //players.get((roundNum -1) % players.size()).setTurn(false); // set turn of last round's player to false
        //players.get(roundNum % players.size()).setTurn(true);       // assign turn to next player
    }

    /**
     * Passes the round
     */
    public void pass() {
        nextRound();
    }

    /**
     * Gets tiles and returns equivalent amount of tiles back to the player
     * Tiles are put back into the bag and the bag is shuffled
     */
    public LinkedList<Tile> exchange(Player player, Tile... tiles) {
        LinkedList<Tile> exchangeTiles = new LinkedList<>();
        Arrays.stream(tiles).forEach(t -> exchangeTiles.add(bag.pop()));
        Arrays.stream(tiles).forEach(t -> bag.add(t));
        Collections.shuffle(bag);
        nextRound();
        return exchangeTiles;
    }

    /**
     * Called when player makes a play
     * TODO Scans words which were found new
     * TODO Evaluates the score difference of two different board states and returns difference (score of last play)
     *
     * @param player      Player who made the move
     * @param playerBoard Board of the player, who made the move
     */
    public int place(Player player, Board playerBoard) {
        ArrayList<String> newFoundWords = new ArrayList<>();    // Words discovered by this placement
        return 0;
    }


    public int getBagSize() {
        return bag.size();
    }

    public Board getBoard() {
        return board;
    }


}
