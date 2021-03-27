package game.server;

import game.components.Board;
import game.components.Tile;
import game.server.players.AiPlayer;
import game.server.players.Player;

import java.util.*;

/**
 * @author yuzun
 * <p>
 * Single instance running on the hosted server
 * Handles game flow and logic
 */

public class Game {


    private final ArrayList<Player> players;                  // Players playing this game (in opposite order of their turns)   [0: last player, 1: second last player, ...]
    private final boolean running;                            // true if game is running
    private final HashMap<Character, Integer> letterScores;                // Map representing letter's score
    private final LinkedList<Tile> bag = new LinkedList<>();           // bag of tiles in the game
    private final ArrayList<String> wordsFound = new ArrayList<>();  // a list of words found up to this point
    private Board board;                                // Game Board
    private int roundNum;                                   // amount of total rounds since start
    private int roundsSinceLastScore;                   // last n amount of rounds without points
    private Object dictionary;                              // Dictionary this game relies on   TODO Dictionary class, getter&setter
    private long startTime;     // startTime
    private Player playerInTurn;            // Player who is in turn

    /**
     * A game instance is created by the server, when the host decided to start the game
     * A game expects
     * a list of players,
     * an array in which the nth element represents the amount of tiles of the (n+1)th letter in the alphabet  (26th element is the joker tile)
     * an array in which the nth element represents the score of the (n+1)th letter in the alphabet            (26th element is the joker tile)
     * <p>
     * When a game is created
     * it sets the status 'running' to true
     * creates a board
     * creates the tiles (amount & score determined by the given arrays), shuffles them and distributes them to the players
     */
    public Game(ArrayList<Player> players, HashMap<Character, Integer> letterDistribution, HashMap<Character, Integer> letterScores) {
        this.letterScores = letterScores;

        // Players participating
        this.players = players;
        players.forEach(player -> player.joinGame(this));

        // Set running to true
        this.running = true;

        // Set up a board
        Board board = new Board();

        // Setup letter scores
        // Create tiles, put them into the bag and shuffle
        for (char letter : letterScores.keySet()) {
            for (int i = 0; i < letterDistribution.get(letter); i++) {
                bag.add(new Tile(letter, letterScores.get(letter)));
            }
        }
        Collections.shuffle(bag);

        // Give each player 7 tiles and remove them from the bag
        for (Player player : players) {
            LinkedList<Tile> tilesToDistribute = new LinkedList<>();
            for (int i = 0; i < 7; i++) {
                tilesToDistribute.add(bag.pop());
            }
            player.addTilesToRack(tilesToDistribute);
        }
    }


    /**
     * Called to start a new round
     * increments round number
     * assigns turn, if it is the turn of an AI player, then trigger it to to think and make a move
     */
    public void nextRound() {
        players.get((roundNum - 1) % players.size()).setTurn(false);     // set turn of last round's player to false
        playerInTurn = players.get(roundNum % players.size());          // Player, whose turn it is
        playerInTurn.setTurn(true);

        roundNum++;     // increment round number

        // Trigger Ai to think
        if (!playerInTurn.isHuman()) { // player is not human
            AiPlayer ai = (AiPlayer) playerInTurn;
            ai.think();
        }
    }

    /**
     * Passes the round if it is the player's turn, else it sends an appropriate error message
     */
    public void passRound(Player player) {
        if (player.isTurn()) {
            System.out.println(playerInTurn.getUsername() + " passed the round");
            nextRound();
        } else {
            // Send Error Message
        }
    }

    /**
     * Removes the tiles given to the game from the player's rack
     * Gets equal amount of tiles out of the bag and adds them to the rack of the player
     * The new tiles from the player are put back in the bag and the bag gets shuffled
     *
     * @param player Player who made the move
     * @param tiles  collection of tiles the player wants to exchange
     */
    public void exchange(Player player, Tile... tiles) {
        List<Tile> tilesFromPlayer = new LinkedList<>();  // tiles the player wants to exchange
        List<Tile> tilesFromBag = new LinkedList<>();     // tiles that are to be given back

        for (int i = 0; i < tiles.length; i++) {
            tilesFromBag.add(bag.pop());
        }
        for (Tile tile : tiles) {
            tilesFromPlayer.add(tile);
        }

        player.removeTilesFromRack(tilesFromPlayer);
        player.addTilesToRack(tilesFromBag);
        Collections.shuffle(bag);
        nextRound();
    }

    public void play() {

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
