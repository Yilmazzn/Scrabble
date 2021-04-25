package net.client;

import game.components.Board;

import java.io.IOException;

/**
 * a client class to create a client and connect with the server
 *
 * @author vkaczmar
 */
public class Client {
  private ClientProtocol connection;
  private final String ipAdr = "25.93.29.50";
  private String username; // username from playersprofile

  /**
   * a constructor to create a client
   *
   * @param user a String representation of the username
   * @author ygarip
   */
  // TODO Probably remove later
  public Client(String user) {
    this.username = user;
  }

  /**
   * @author vkaczmar method call, when "Create Game" is clicked. Happens only once, only the host
   *     calls this method
   */
  // TODO change ClientTestClass to the class, that starts everything
  public void createGame(ClientTestClass ctc) {
    ctc.startServer();
  }

  /**
   * a method to connect the client to the server
   *
   * @author ygarip
   */
  public void connect() {
    this.connection = new ClientProtocol(this.ipAdr, username);
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
  public void startGame() {
    this.connection.startGame();
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
   * @param username requires the player's username
   */
  public void updatePoints(Board currentState, Board previousState, String username) {
    this.connection.updatePoints(currentState, previousState, username);
  }

  /**
   * @author ygarip
   * @param points requires the points of the player client
   */
  /*
  public void sendStatistics(int points) {
    this.connection.sendStatistics(points);
  } */

  /**
   * @author vkaczmar a method to get the username of client
   * @return returns the username of the client
   */
  public String getUsername() {
    return this.username;
  }
}
