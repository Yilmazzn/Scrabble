package net.server;

import net.message.ConnectMessage;
import net.message.DisconnectMessage;
import net.message.Message;
import net.message.PlayerReadyMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A ServerProtocol class to handle serverside messages
 *
 * @author ygarip
 */
public class ServerProtocol extends Thread {
  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private Server server;
  private String clientName;
  private boolean running = true;

  /**
   * constructor for creating a new Serverprotocol and connecting the server to the clients and the
   * input and output streams
   *
   * @param client the client which is connected to the server
   * @param server the server which is providing the game
   */
  public ServerProtocol(Socket client, Server server) {
    this.socket = client;
    this.server = server;
    try {
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** a method to disconnect the client from the server */
  public void disconnect() {
    running = false;
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * a method to send a Message to the client
   *
   * @param m the Message of a MessageType which should be send to the Client
   * @throws IOException exception by sending the message to the client
   */
  public void sendToClient(Message m) throws IOException {
    this.out.writeObject(m);
    out.flush();
  }

  /** the run method of serverprotocol to handle the incoming messages of the server */
  public void run() {
    try {

      while (running) {
        Message m = (Message) in.readObject();
        String username;
        switch (m.getMessageType()) {
          case CONNECT:
            username = ((ConnectMessage) m).getUsername();
            server.setReady(username);
            this.clientName = username;
            server.addClientName(username);
            ConnectMessage cm = server.setID(username);
            System.out.println("Server added: " + username);
            server.sendToAll(cm);
            break;
          case DISCONNECT:
            username = ((DisconnectMessage) m).getUsername();
            server.removeClientName(username);
            server.removeClient(this);
            System.out.println("Server removed: " + username);
            running = false;
            disconnect();
            break;
          case CHATMESSAGE:
            server.sendToAll(m);
            break;
          case STARTGAME:
            server.sendToAll(m);
            break;
          case PLAYERREADY:
            // Checks, if all player are ready, then sends message to all clients
            HashMap<String, Boolean> playersReady = server.getPlayersReady();
            boolean ready = true;
            PlayerReadyMessage prm = (PlayerReadyMessage) m;
            server.setPlayersReady(prm.getUsername(), prm.getReady());
            Iterator<String> player = playersReady.keySet().iterator();
            while (player.hasNext()) {
              ready = ready && playersReady.get(player.next());
            }
            if (ready) {
              server.sendToAll(m);
            }
            break;
          case UPDATEGAMEBOARD:
            server.sendToAll(m);
            break;
          case SUBMITMOVE:
            // TODO add checkValid method
            // Message m contains username and valid and board attributes
            sendToClient(m);
            break;
          case UPDATEPOINTS:
            // TODO add pointUpdating method
            server.sendToAll(m);
            break;
          case SENDSTATISTICS:
            server.sendToAll(m);
            break;
          case DISPLAYSTATISTICS:
            break;
          default:
            break;
        }
      }
    } catch (IOException e) {
      running = false;
      if (socket.isClosed()) {
        System.out.println("Socket was closed. Client:" + clientName);
      } else {
        try {
          socket.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    } catch (ClassNotFoundException e2) {
      System.out.println(e2.getMessage());
      e2.printStackTrace();
    }
  }
}
