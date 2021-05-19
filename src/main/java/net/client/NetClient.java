package net.client;

import client.Client;
import client.PlayerProfile;
import game.Dictionary;
import game.components.Board;
import game.components.Tile;
import gui.controllers.CreateGameController;
import gui.controllers.GameViewController;
import gui.controllers.JoinGameController;
import net.server.Server;

import java.io.File;
import java.io.IOException;

/**
 * a client class to create a client and connect with the server
 *
 * @author vkaczmar
 */
public class NetClient {
  private ClientProtocol connection;
  private String ipAdr;
  private String username; // username from playersprofile
  private int points;
  private Dictionary dictionary;
  private PlayerProfile profile;
  private boolean isAIActive;
  private final Client client;
  private Server server;

  private CreateGameController createGameController; // controls GUI
  private GameViewController gameViewController; // controls GUI Game
  private JoinGameController joinGameController;

  private PlayerProfile[] coPlayers;
  private int[] coPlayerScores;

  /**
   * Constructor to create a NetClient with client
   *
   * @param client Requires client to connect from
   */
  public NetClient(Client client) {
    this.client = client;
    try {
      this.ipAdr = Server.getLocalHostIp4Address();
      coPlayers = new PlayerProfile[] {client.getSelectedProfile()};
      coPlayerScores = new int[1];
    } catch (Exception e) {
      System.out.println("Netclient create didnt worked");
      e.printStackTrace();
    }
  }

  /**
   * @author vkaczmar method call, when "Create Game" is clicked. Happens only once, only the host
   *     calls this method
   */
  // TODO change ClientTestClass to the class, that starts everything
  public static void createGame(ClientTestClass ctc) {
    ctc.startServer();
  }

  /** Creates and starts server */
  public void createServer() {
    server = new Server();
    server.start();
  }

  /**
   * a method to connect the client to the server
   *
   * @author ygarip
   */
  public void connect() {
    this.connection = new ClientProtocol(this.ipAdr, this);
    this.connection.start();
    System.out.println(
        "Netclient " + this.getPlayerProfile().getName() + " is connected |NetClient");
  }

  /** @return Returns ClientProtocol connected to NetClient */
  public ClientProtocol getConnection() {
    return connection;
  }

  /**
   * method to set the ip
   *
   * @param ip requires the ip
   */
  public void setIp(String ip) {
    this.ipAdr = ip;
  }

  /** @return Returns up of NetClient */
  public String getIp() {
    return ipAdr;
  }

  /** @return Returns server */
  public Server getServer() {
    return this.server;
  }

  /**
   * a method to disconnect the client from the server
   *
   * @throws IOException exception occurs if disconnect didn't work
   * @author ygarip
   */
  public void disconnect() {
    try {
      connection.disconnect();
      connection = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Calls method, whenever the host changed anything in the gamesettings
   *
   * @param tileScores Requires tileScores
   * @param tileDistributions requires tileDistribution
   * @param dictionary requires dictionary File
   */
  public void updateGameSettings(int[] tileScores, int[] tileDistributions, File dictionary) {
    connection.updateGameSettings(tileScores, tileDistributions, dictionary);
  }

  /**
   * Initiates sending of Players readiness to server
   *
   * @param ready Requires ready value to be sent
   */
  public void setPlayerReady(boolean ready) {
    connection.setPlayerReady(ready);
  }

  /**
   * a method to start the game
   *
   * @author ygarip
   */
  public void startGame(File file) {
    this.connection.startGame(file);
  }

  /**
   * @author vkaczmar sends text message to all other clients
   * @param chatMessage Requires chatMessage to be sent to other players
   */
  public void sendChatMessage(String chatMessage) {
    this.connection.sendChatMessage(chatMessage);
  }

  /**
   * @param board represents the game board
   * @author vkaczmar call method, when a new tile is placed
   */
  public void updateGameBoard(Board board) {
    this.connection.updateGameBoard(board);
  }

  /**
   * @param username username who wants to check
   * @author ygarip checks if move is valid
   */
  public void submitMove(String username, Board board) {
    this.connection.submitMove(username, board);
  }

  /**
   * @param currentState requires the current state of the board
   * @param previousState requires the state from teh beginning of the turn
   * @author vkaczmar a method to update the points of the players
   */
  public void updatePoints(Board currentState, Board previousState) {
    this.connection.updatePoints(currentState, previousState);
  }

  /**
   * @return returns the username of the client
   * @author vkaczmar a method to get the username of client
   */
  public String getUsername() {
    return client.getSelectedProfile().getName();
  }

  /**
   * @return returns points
   * @author vkaczmar
   */
  public int getPoints() {
    return points;
  }

  /**
   * @param dictionary requires the dictionary to be set
   * @author ygarip a method to set the wished dictionary
   */
  public void setDictionary(Dictionary dictionary) {
    this.dictionary = dictionary;
  }

  /**
   * @param word requires the word to be tested
   * @return returns true if word exists otherwise false
   * @author vkaczmar a method to look up a word in dictionary and send back if it exists or not
   */
  public boolean wordExists(String word) {
    return dictionary.wordExists(word);
  }

  /** @author ygarip a method to send the playerdata to the server */
  public void sendPlayerData(int id) {
    connection.sendPlayerData(id);
  }

  /**
   * @return returns the players profile
   * @author vkaczmar a method to return the player profile
   */
  public PlayerProfile getPlayerProfile() {
    return client.getSelectedProfile();
  }

  /** @author ygarip a method to get the tile from server */
  public void getTile() {
    connection.getTile();
  }

  /** @return Returns client */
  public Client getClient() {
    return client;
  }

  /**
   * @param oldTiles requires the tiles to exchange the tiles with the bag
   * @author vkaczmar a method to request the bag amount
   */
  public void exchangeTiles(Tile[] oldTiles) {
    connection.exchangeTiles(oldTiles);
  }

  /**
   * @return returns the boolean value of isAIActive
   * @author vkaczmar
   */
  public boolean getAIActive() {
    return isAIActive;
  }

  /**
   * Sets create game Controller
   *
   * @param createGameController Requires createGameController to be set
   */
  public void setCreateGameController(CreateGameController createGameController) {
    this.createGameController = createGameController;
  }

  /**
   * Set players to update view
   *
   * @param profiles Requires PlayerProfiles array
   */
  public void setCoPlayers(PlayerProfile[] profiles) {
    this.coPlayers = profiles;

    if (server != null) { // player is host
      createGameController.fillLobby(profiles);
    } else { // player is not host

    }
  }

  /**
   * Sets joinGameController
   *
   * @param joinController Requires join game Controller to be set
   */
  public void setJoinGameController(JoinGameController joinController) {
    System.out.println("JoinGameController set");
    this.joinGameController = joinController;
  }

  /**
   * Sets gameViewController
   *
   * @param gameController Requires gameViewController to be set
   */
  public void setGameViewController(GameViewController gameController) {
    this.gameViewController = gameController;
  }

  public void fillLobby(PlayerProfile[] profiles) {
    System.out.print("FillLobbyInhalt: ");
    for (PlayerProfile profile : profiles) {
      System.out.print(profile.getName() + ", ");
    }
    System.out.println();
    createGameController.fillLobby(profiles);
  }

  /** Load game view */
  public void loadGameView() {
    try {
      System.out.println("JoinGameController used");
      joinGameController.loadGameView();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** @return Returns if client is host or not */
  public boolean isHost() {
    return server != null && server.isRunning();
  }

  /** @return Returns playerProfiles as an Array */
  public PlayerProfile[] getCoPlayers() {
    return coPlayers;
  }

  /**
   * Sets coPlayerScores
   *
   * @param scores Requires scores array to be set
   */
  public void setCoPlayerScores(int[] scores) {
    this.coPlayerScores = scores;
  }

  /** @return Returns coPlayerScores as Array */
  public int[] getCoPlayerScores() {
    return coPlayerScores;
  }

  /**
   * Initiates sending of difficulty for AI Creation
   *
   * @param difficult Requires difficulty, true = hard, false = easy
   */
  public void addAIPlayer(boolean difficult) {
    connection.addAIPlayer(difficult);
  }
}
