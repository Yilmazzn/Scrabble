package net.client;

import game.components.Board;
import net.message.*;
import org.jdom2.output.support.SAXOutputProcessor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/** @author vkaczmar Handles all interactions from the server to the client */
public class ClientProtocol extends Thread {
  private String client;
  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private boolean running = true;
  private int id;

  /**
   * Constructor for creating streams and connecting a client
   *
   * @param ip Requires the ip, the server runs on
   * @param client Requires username for current profile
   */
  public ClientProtocol(String ip, String client) {
    this.client = client;
    try {
      this.clientSocket = new Socket(ip, 12975);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());
      this.out.writeObject(new ConnectMessage(client));
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
        this.out.writeObject(new DisconnectMessage(client));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Method for writing a StartGameMessage Object to the server */
  public void startGame() {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new StartGameMessage());
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
        this.out.writeObject(new ChatMessage(chatMessage, username));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for writing a PlayReadyMessage Object to the server
   *
   * @param ready true, if player is ready
   * @param username user, which wants to set his ready state
   */
  public void setReadyState(boolean ready, String username) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new PlayerReadyMessage(ready, username));
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
   * @param username
   */
  public void updatePoints(Board currentState, Board previousState, String username) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new UpdatePointsMessage(currentState, previousState, username));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
  public void sendStatistics(int points) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new SendStatisticsMessage(points, this.id));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void sendStatistics(int[] points) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new DisplayStatisticsMessage(points));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }*/

  /**
   * Overwritten run method from Thread. Accepts and works through incoming messages from the server
   */
  public void run() {
    while (running) {
      try {
        Message m = (Message) in.readObject();
        switch (m.getMessageType()) {
          case CONNECT:
            this.id = ((ConnectMessage) m).getID();
            // TODO update Lobby View
            // System.out.println("New player joined the lobby");
            break;
          case DISCONNECT:
            break;
          case STARTGAME:
            // TODO add method
            System.out.println("Rufe FXML Wechsel auf");
            break;
          case CHATMESSAGE:
            // TODO add method
            System.out.println(((ChatMessage) m).getUsername() + ": " + ((ChatMessage) m).getMsg());
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
            // TODO if word valid, submit move, otherwise set board back to the beginning of the
            // turn
            System.out.println("Submit Move");
            break;
          case UPDATEPOINTS:
            // TODO updateView()
            System.out.println("Update Points View");
            // TODO nextPlayer()
            break;
            /* case SENDSTATISTICS:
            int[] array = new int[4];
            array[((SendStatisticsMessage)m).getId()] = ((SendStatisticsMessage)m).getPoints();
            sendStatistics(array);
            break;*/
          default:
            break;
        }
      } catch (IOException | ClassNotFoundException e) {
        break;
      }
    }
  }
}
