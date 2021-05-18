package net.client;

import client.Client;
import client.PlayerProfile;
import game.Dictionary;
import game.components.Board;
import game.components.Tile;
import gui.controllers.CreateGameController;
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
  private String ipAdr="192.168.0.9";
  private String username; // username from playersprofile
  private int points;
  private Dictionary dictionary;
  private PlayerProfile profile;
  private boolean isAIActive;
  private Client client;
  private Server server;

  private CreateGameController createGameController; // controls GUI

  /**
   * a constructor to create a client
   *
   * @param user a String representation of the username
   * @param profile profile object for representation of profile related statistics
   * @author ygarip
   */
  public NetClient(String user, PlayerProfile profile) {
    this.username = user;
    this.profile = profile;
    this.isAIActive = false;
  }

  /**
   * Constructor to create an AI-Client
   *
   * @param user requires the username of the AI
   * @param profile requires the profile of the AI
   * @param isAIActive requires the boolean value of isAIActive
   * @author vkaczmar
   */
  public NetClient(String user, PlayerProfile profile, boolean isAIActive) {
    this.username = user;
    this.profile = profile;
    this.isAIActive = isAIActive;
  }

  public NetClient(Client client) {
    this.client = client;
    /*
    try{
      this.ipAdr = Server.getLocalHostIp4Address();
    }catch(Exception e){
      e.printStackTrace();
    }
    */
  }

  /**
   * @author vkaczmar method call, when "Create Game" is clicked. Happens only once, only the host
   *     calls this method
   */
  // TODO change ClientTestClass to the class, that starts everything
  public static void createGame(ClientTestClass ctc) {
    ctc.startServer();
  }

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
  }

  /**
   * method to set the ip
   *
   * @param ip requires the ip
   */
  public void setIp(String ip) {
    this.ipAdr = ip;
  }

  public String getIp(){
    return ipAdr;
  }

  public Server getServer(){
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
  }catch(Exception e){
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
   * @param ready Requires boolean if player is ready
   * @param player Requires PlayerProfile to be set
   * @author ygarip a method to set the readiness of the player
   */
  public void setReadyState(boolean ready, PlayerProfile player) {
    this.connection.setReadyState(ready, player);
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

  public PlayerProfile testGetPlayerProfile() {
    return profile;
  }

  /** @author ygarip a method to get the tile from server */
  public void getTile() {
    connection.getTile();
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

  public void setCreateGameController(CreateGameController createGameController){
    this.createGameController = createGameController;
  }

  public void fillLobby(PlayerProfile[] profiles){
    createGameController.fillLobby(profiles);
  }
}
