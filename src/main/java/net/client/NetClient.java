package net.client;

import client.Client;
import client.PlayerProfile;
import ft.Sound;
import game.Dictionary;
import game.components.Board;
import game.components.Tile;
import gui.controllers.CreateGameController;
import gui.controllers.GameResultsController;
import gui.controllers.GameViewController;
import gui.controllers.JoinGameController;
import javafx.application.Platform;
import net.server.Server;

import java.io.IOException;

/**
 * a client class to create a client and connect with the server
 *
 * @author vkaczmar
 */
public class NetClient {
  private final Client client;
  private final int turnIdx = 0; // player whose turn it is
  private ClientProtocol connection;
  private String ipAdr;
  private Dictionary dictionary;
  private PlayerProfile profile;
  private boolean isAIActive;
  private Server server;
  private CreateGameController createGameController; // controls GUI
  private GameViewController gameViewController; // controls GUI Game
  private JoinGameController joinGameController;
  private GameResultsController gameResultsController;
  private PlayerProfile[] coPlayers; // players in lobby (with local player himself)
  private int[] coPlayerScores; // scores of players in lobby

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
    try {
      this.connection = new ClientProtocol(this.ipAdr, this);
      this.connection.start();
    } catch (IOException ioe) {
      client.showPopUp("Could not establish connection to a server on " + this.ipAdr);
    }
    System.out.println(
        "Netclient " + this.getPlayerProfile().getName() + " is connected |NetClient");
  }

  /** @return Returns ClientProtocol connected to NetClient */
  public ClientProtocol getConnection() {
    return connection;
  }

  /** @return Returns up of NetClient */
  public String getIp() {
    return ipAdr;
  }

  /**
   * method to set the ip
   *
   * @param ip requires the ip
   */
  public void setIp(String ip) {
    this.ipAdr = ip;
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
   * @param dictionary requires dictionary path
   */
  public void sendGameSettings(int[] tileScores, int[] tileDistributions, String dictionary) {
    connection.sendGameSettings(tileScores, tileDistributions, dictionary);
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
  public void startGame() {
    this.connection.startGame();
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

  /** @author ygarip checks if move is valid */
  public void submitMove() {
    this.connection.submitMove();
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

  /** @return Returns client */
  public Client getClient() {
    return client;
  }

  /**
   * Method for initiating ExchangeTileMessage
   *
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
  public void setLobbyState(PlayerProfile[] profiles, int[] scores) {
    this.coPlayers = profiles;
    this.coPlayerScores = scores;
    if (isHost() && !server.gameIsRunning()) { // player is host and game is not run
      createGameController.fillLobby(profiles);
    } else { // player is not host
      gameViewController.updateScoreboard(profiles, scores);
    }
  }

  public void updateChat(String user, String message) {
    if (user != null) { // not received from system
      if (isHost()
          && !server.gameIsRunning()
          && gameResultsController == null) { // user is host --> sees CreateGameScene
        createGameController.getMessage(user, message);
      } else if (gameResultsController != null) { // user is not host --> sees GameView
        gameResultsController.getMessage(user, message);
      } else {
        gameViewController.getMessage(user, message);
      }
    } else {
      if (isHost() && !server.gameIsRunning()) {
        createGameController.createSystemMessage(message);
      } else {
        if (gameViewController != null) {
          gameViewController.createSystemMessage(message);
        }
        if (gameResultsController != null) { // todo
          gameResultsController.createSystemMessage(message);
        }
      }
    }
  }

  public void updateGameSettings(int[] scores, int[] distributions, String dictionaryContent) {
    Platform.runLater(
        () -> {
          System.out.println("NetClient: in updateGameSettings");
          if (scores != null) {
            String content = "";
            for (int i = 0; i < scores.length; i++) {
              content += ((char) (i + 'A')) + "\t" + scores[i] + "\n";
            }
            client.showPopUp(content);
          }
          if (distributions != null) {
            String content = "";
            for (int i = 0; i < distributions.length - 1; i++) {
              content += ((char) (i + 'A')) + "\t" + distributions[i] + "\n";
            }
            content += "#\t" + distributions[26] + "\n";
            client.showPopUp(content);
          }
          if (dictionaryContent != null) {
            client.showPopUpDictionary(dictionaryContent);
          }
        });
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
    System.out.println("GameViewController used");
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

  /** @return Returns coPlayerScores as Array */
  public int[] getCoPlayerScores() {
    return coPlayerScores;
  }

  /**
   * Sets coPlayerScores
   *
   * @param scores Requires scores array to be set
   */
  public void setCoPlayerScores(int[] scores) {
    this.coPlayerScores = scores;
    gameViewController.updateScoreboard(coPlayers, scores);
  }

  /**
   * Initiates sending of difficulty for AI Creation
   *
   * @param difficult Requires difficulty, true = hard, false = easy
   */
  public void addAIPlayer(boolean difficult) {
    connection.addAIPlayer(difficult);
  }

  /**
   * Kicks player out of the game
   *
   * @param index Requires index, which player you want to kick
   */
  public void kickPlayer(int index) {
    connection.kickPlayer(index);
  }

  /** Initiates RequestValuesMessage */
  public void requestValues() {
    connection.requestValues();
  }

  /** */
  public void requestDistributions() {
    connection.requestDistributions();
  }

  public void requestDictionary() {
    connection.requestDictionary();
  }

  /** send Message to */
  public void placeTile(Tile tile, int row, int col) {
    connection.placeTile(tile, row, col);
  }

  /** Place tile on GUI */
  public void placeIncomingTile(Tile tile, int row, int col) {
    System.out.println("Place incoming Tile | NetClient");
    gameViewController.placeTile(tile, row, col);
  }

  /** Initializes game in GameView Controller */
  public void initializeGame() {
    if (!isHost()) {
      gameViewController.showAgreements(false);
    }
  }

  /** Triggered by incoming PLAYERREADYMESSAGE */
  public void setReadies(boolean[] readies) {
    if (isHost()) {
      createGameController.updatePlayerReadies(readies);
    }
  }

  /**
   * Triggered by incoming TURN MESSAGE
   *
   * @param turn true if it is this client's turn
   * @param turns array of turn state (to be able to show whose turn it is)
   */
  public void setTurns(boolean turn, boolean[] turns) {
    client.getLocalPlayer().setTurn(turn); // set turn of local player -> true
    gameViewController.updateTurns(turns); // show whose turn it is
  }

  /**
   * changes bag size to given int value
   *
   * @param bagSize Requires value to set to
   */
  public void setBagSize(int bagSize) {
    gameViewController.setBagSize(bagSize);
  }

  /**
   * Send End Message (type 0 bag empty, type 2 overtime exceeded)
   *
   * @param type Requires the EndGameMessage int type
   */
  public void sendEndMessage(int type) {
    connection.sendEndMessage(type);
  }

  public void enableEndGameButton() {
    gameViewController.showEndButton();
  }

  public void changeToResultView() {
    try {
      gameViewController.changeToResultView();
      gameResultsController.updateScoreboard(coPlayers, coPlayerScores);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setGameResultsController(GameResultsController gameResultsController) {
    this.gameResultsController = gameResultsController;
  }
}
