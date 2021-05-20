package net.client;

import client.PlayerProfile;
import game.Dictionary;
import game.components.Board;
import game.components.Tile;
import javafx.application.Platform;
import net.message.*;


import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLOutput;

/** @author vkaczmar Handles all interactions from the server to the client */
public class ClientProtocol extends Thread {
  private NetClient client;
  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private boolean running = true;
  private int id = -1;

  /**
   * Constructor for creating streams and connecting a client
   *
   * @param ip Requires the ip, the server runs on
   * @param netClient Requires username for current profile
   */
  public ClientProtocol(String ip, NetClient netClient) {
    this.client = netClient;
    try {
      this.clientSocket = new Socket(ip, 12975);
      System.out.println(
          "Client with IP:" + ip + " connected, " + client.getPlayerProfile().getName());
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());
      this.out.writeObject(new ConnectMessage(client.getPlayerProfile()));
      this.out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Method for writing a DisconnectMessage Object to the server */
  public void disconnect() {
    running = false;
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new DisconnectMessage(client.getPlayerProfile()));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets players readiness
   *
   * @param ready Requires value ready should be set to
   */
  public void setPlayerReady(boolean ready) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new PlayerReadyMessage(ready));
        this.out.flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for writing a StartGameMessage Object to the server
   *
   * @param file Requires File, that includes Dictionary
   */
  public void startGame(File file) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new StartGameMessage(file));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for writing a ChatMessage Object to the server
   *
   * @param chatMessage message to be sent
   */
  public void sendChatMessage(String chatMessage) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new ChatMessage(chatMessage, client.getPlayerProfile()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for writing a UpdateGameBoardMessage Object to the server
   *
   * @param board requires Board instance
   */
  public void updateGameBoard(Board board) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new UpdateGameBoardMessage(board));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * a method to submit the move of the client
   *
   * @param username requires the players username
   * @param board the state of the current board
   */
  public void submitMove(String username, Board board) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new SubmitMoveMessage(username, board));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for writing a UpdatePointsMessage Object to the server
   *
   * @param currentState
   * @param previousState
   */
  public void updatePoints(Board currentState, Board previousState) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new UpdatePointsMessage(currentState, previousState, id));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * A method for writing a SendPlayerMessage Object to server
   *
   * @param id Requires id to receive profile from
   */
  public void sendPlayerData(int id) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new SendPlayerDataMessage(id));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * A method for creation and sending of GetTileMessage instance Receives One Tile in run method
   */
  public void getTile() {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new GetTileMessage());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates and sends RequestBagAmountMessage instance to server
   *
   * @param oldTiles Requires tiles to be removed
   */
  public void exchangeTiles(Tile[] oldTiles) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new ExchangeTileMessage(oldTiles));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates and sends UpdateGameSettingsMessage instance to server
   *
   * @param tileScores Requires tileScores
   * @param tileDistributions Requires tileDistribution
   * @param dictionary Requires dictionary file
   */
  public void updateGameSettings(int[] tileScores, int[] tileDistributions, String dictionary) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(
            new UpdateGameSettingsMessage(tileScores, tileDistributions, dictionary));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates and sends AddAIMessage instance to server
   *
   * @param difficult Requires difficulty, true = hard, false = easy
   */
  public void addAIPlayer(boolean difficult) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new AddAIMessage(difficult));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /**
   * Overwritten run method from Thread. Accepts and works through incoming messages from the server
   */
  public void run() {
    while (running) {
      try {
        Message m = (Message) in.readObject();
        System.out.println(m.getMessageType().toString());
        switch (m.getMessageType()) {
          case CONNECT:
            PlayerProfile[] lobbyProfiles = ((ConnectMessage) m).getProfiles();

            // sets list of coplayers and updates respective views
            Platform.runLater(
                () -> {
                  if (!client.isHost()) {
                    client.loadGameView(); // Load Game view only if connected
                  }

                  // set Data
                  client.setLobbyState(lobbyProfiles, new int[lobbyProfiles.length], 0);
                  // TODO setReadies
                });

            break;
          case REFUSECONNECTION:
            Platform.runLater(
                () -> {
                  client.getClient().showPopUp(((RefuseConnectionMessage) m).getMessage());
                });
            running = false;
            break;
          case UPDATEGAMESETTINGS: // todo done eigentlich nur connection zu controllern herstellen
            // TODO netClient.getClient().updateGameSettings
            // TODO Dictionary erstmal als String, erst beim Spielstart wird auf dem Server das
            // richtige Dictionary erzeugt
            break;
          case STARTGAME:
            // TODO add method

            System.out.println("Rufe FXML Wechsel auf");
            client.setDictionary(
                new Dictionary(((StartGameMessage) m).getFile().getAbsolutePath()));
            break;
          case CHATMESSAGE:
            // TODO add method
            PlayerProfile user = ((ChatMessage) m).getProfile();
            String message = ((ChatMessage) m).getMsg();
            Platform.runLater(
                    () -> {
                      client.updateChat(user, message);
                    });
            System.out.println(((ChatMessage) m).getProfile().getName() + ": " + ((ChatMessage) m).getMsg());
            break;
          case PLAYERREADY:
            PlayerReadyMessage prm = (PlayerReadyMessage) m;
            for (int i = 0; i < prm.getValues().length; i++) {
              System.out.println("Status " + (i + 1) + ": " + (prm.getValues()[i]));
            }
            client.changeStartGameButton(prm.getReady()); // all players are ready
            break;
          case UPDATEGAMEBOARD:
            // TODO add method
            System.out.println("Update Board");
            break;
          case SUBMITMOVE:
            // TODO if word valid, submit move, otherwise color wrong words red
            // TODO Button appears, which enables deletion of current layed tiled User can delete
            // all or just remove single tiles
            System.out.println("Submit Move");
            break;
          case UPDATEPOINTS:
            // TODO updateView()
            // The message knows, which points have been changed
            System.out.println("Update Points View");
            // TODO nextPlayer()
            break;
          case SENDPLAYERDATA:
            // TODO show Profile
            System.out.println(((SendPlayerDataMessage) m).getProfile().getName());
            break;
          case GETTILE:
            // TODO add tile to personal rack and display it
            System.out.println(((GetTileMessage) m).getTile().getLetter());
            break;
          case EXCHANGETILES:
            ExchangeTileMessage etm = (ExchangeTileMessage) m;
            if (etm.getError() == null) {
              // TODO remove oldTiles from Players rack
              // then add newTiles to Players rack
              // maybe chat message to other players, that rack is changed
              // call nextTurn()
              System.out.println("No Error");
            } else {
              System.out.println(etm.getError());
            }
            break;
          case DISCONNECT:
            Platform.runLater(
                () -> {
                  try {
                    client.getClient().showPopUp("Disconnected. Host closed the server");
                    client.getClient().showMainMenu();
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                });
            break;
          default:
            break;
        }
      } catch (IOException | ClassNotFoundException e) {
        break;
      }
    }
  }
}
