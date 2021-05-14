package net.client;

import client.PlayerProfile;
import game.Dictionary;
import game.components.Board;
import game.components.Tile;

import java.io.File;
import java.io.IOException;

/**
 * a client class to create a client and connect with the server
 *
 * @author vkaczmar
 */
public class NetClient {
  private ClientProtocol connection;
  private final String ipAdr = "25.93.29.50";
  private String username; // username from playersprofile
  private int points;
  private Dictionary dictionary;
  private PlayerProfile profile;
  private boolean isAIActive;

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

  /**
   * @author vkaczmar method call, when "Create Game" is clicked. Happens only once, only the host
   *     calls this method
   */
  // TODO change ClientTestClass to the class, that starts everything
  public static void createGame(ClientTestClass ctc) {
    ctc.startServer();
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
   * a method to disconnect the client from the server
   *
   * @throws IOException exception occurs if disconnect didn't work
   * @author ygarip
   */
  public void disconnect() throws IOException {
    connection.disconnect();
  }

  /**
   * a method to start the game
   *
   * @author ygarip
   */
  public void startGame(File file) {
    this.connection.startGame(file);
  }

  /** @author vkaczmar sends text message to all other clients */
  public void sendChatMessage(String chatMessage, String username) {
    this.connection.sendChatMessage(chatMessage, username);
  }

  /**
   * @author ygarip a method to set the readiness of the player
   * @param ready requires boolean if player is ready
   * @param username requires the players username
   */
  public void setReadyState(boolean ready, String username) {
    this.connection.setReadyState(ready, username);
  }

  /**
   * @author vkaczmar call method, when a new tile is placed
   * @param board represents the game board
   */
  public void updateGameBoard(Board board) {
    this.connection.updateGameBoard(board);
  }

  /**
   * @author ygarip checks if move is valid
   * @param username username who wants to check
   */
  public void submitMove(String username, Board board) {
    this.connection.submitMove(username, board);
  }

  /**
   * @author vkaczmar a method to update the points of the players
   * @param currentState requires the current state of the board
   * @param previousState requires the state from teh beginning of the turn
   */
  public void updatePoints(Board currentState, Board previousState) {
    this.connection.updatePoints(currentState, previousState);
  }

  /**
   * @author vkaczmar a method to get the username of client
   * @return returns the username of the client
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * @author vkaczmar
   * @return returns points
   */
  public int getPoints() {
    return points;
  }

  /**
   * @author ygarip a method to set the wished dictionary
   * @param dictionary requires the dictionary to be set
   */
  public void setDictionary(Dictionary dictionary) {
    this.dictionary = dictionary;
  }

  /**
   * @author vkaczmar a method to look up a word in dictionary and send back if it exists or not
   * @param word requires the word to be tested
   * @return returns true if word exists otherwise false
   */
  public boolean wordExists(String word) {
    return dictionary.wordExists(word);
  }

  /** @author ygarip a method to send the playerdata to the server */
  public void sendPlayerData(int id) {
    connection.sendPlayerData(id);
  }

  /**
   * @author vkaczmar a method to return the player profile
   * @return returns the players profile
   */
  public PlayerProfile getPlayerProfile() {
    return profile;
  }

  /** @author ygarip a method to get the tile from server */
  public void getTile() {
    connection.getTile();
  }

  /**
   * @author vkaczmar a method to request the bag amount
   * @param oldTiles requires the tiles to exchange the tiles with the bag
   */
  public void exchangeTiles(Tile[] oldTiles) {
    connection.exchangeTiles(oldTiles);
  }

  /**
   * a method to agree on the dictionary
   *
   * @author ygarip
   * @param agree requires the boolean value of agree
   * @param username requires the username
   */
  public void agreeOnDictionary(boolean agree, String username) {
    connection.agreeOnDictionary(agree, username);
  }

  /**
   * @author vkaczmar
   * @return returns the boolean value of isAIActive
   */
  public boolean getAIActive() {
    return isAIActive;
  }
}
