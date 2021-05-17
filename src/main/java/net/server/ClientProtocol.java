package net.server;

import client.PlayerProfile;
import game.Dictionary;
import game.components.Board;
import game.components.Tile;
import game.players.Player;
import game.players.RemotePlayer;
import net.client.NetClient;
import net.message.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
   * @param client Requires username for current profile
   */
  public ClientProtocol(String ip, NetClient client) {
    this.client = client;
    try {
      this.clientSocket = new Socket(ip, 12975);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());
      // TODO this.out.writeObject(new ConnectMessage(client.getPlayerProfile()));
      this.out.writeObject(new ConnectMessage(client.testGetPlayerProfile()));
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Method for writing a DisconnectMessage Object to the server */
  public void disconnect() {
    running = false;
    try {
      if (!clientSocket.isClosed()) {
        // TODO this.out.writeObject(new DisconnectMessage(client.getPlayerProfile()));
        this.out.writeObject(new DisconnectMessage(client.testGetPlayerProfile()));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Method for writing a StartGameMessage Object to the server */
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
   * @param username user, which sends the message
   */
  public void sendChatMessage(String chatMessage, String username) {
    try {
      if (!clientSocket.isClosed()) {
        // todo this.out.writeObject(new ChatMessage(chatMessage, client.getPlayerProfile()));
        this.out.writeObject(new ChatMessage(chatMessage, client.testGetPlayerProfile()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for writing a PlayReadyMessage Object to the server
   *
   * @param ready true, if player is ready
   * @param player Requires player to be included in message
   */
  public void setReadyState(boolean ready, PlayerProfile player) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new PlayerReadyMessage(ready, player));
        this.out.flush();
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

  /** @param id Requires id to receive profile from */
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
   * @param tileScores Requires tileScores
   * @param tileDistributions Requires tileDistribution
   * @param dictionary Requires dictionary file
   */
  public void updateGameSettings(int[] tileScores, int[] tileDistributions, File dictionary) {
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
   * Overwritten run method from Thread. Accepts and works through incoming messages from the server
   */
  public void run() {
    while (running) {
      try {
        Message m = (Message) in.readObject();
        System.out.println(m.getMessageType());
        switch (m.getMessageType()) {
          case CONNECT:
            if (this.id == -1) {
              this.id = ((ConnectMessage) m).getID();
              if (this.id >= 4) {
                System.out.println("Lobby is full");
                disconnect();
              }
            }
            // TODO update Lobby View
            // System.out.println("New player joined the lobby");
            break;
          case STARTGAME:
            // TODO add method
            System.out.println("Rufe FXML Wechsel auf");
            client.setDictionary(
                new Dictionary(((StartGameMessage) m).getFile().getAbsolutePath()));
            break;
          case CHATMESSAGE:
            // TODO add method
            System.out.println(
                ((ChatMessage) m).getProfile().getName() + ": " + ((ChatMessage) m).getMsg());
            break;
          case PLAYERREADY:
            // TODO add method
            System.out.println("Host should now be able to start the game");
            /*
            if (host) {
              do something, like enable the startgame button
            }
             */
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
          default:
            break;
        }
      } catch (IOException | ClassNotFoundException e) {
        break;
      }
    }
  }
}
