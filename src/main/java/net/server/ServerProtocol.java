package net.server;

import client.PlayerProfile;
import game.components.Tile;
import net.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

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
   * @param m the Message of a MessageType which should be send to the client.Client
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
            PlayerProfile profile = ((ConnectMessage) m).getProfile();
            server.setReady(username);
            this.clientName = username;
            server.addClientName(username);
            ConnectMessage cm = server.setID(username, profile);
            server.setPlayerProfiles(cm.getID(), profile);
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
            for (String s : playersReady.keySet()) {
              ready = ready && playersReady.get(s);
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
          case SENDPLAYERDATA:
            SendPlayerDataMessage spdm = (SendPlayerDataMessage) m;
            spdm.setProfile(server.getProfile(spdm.getID()));
            sendToClient(spdm);
            break;
          case GETTILE:
            ((GetTileMessage) m).setTile(server.getTile());
            sendToClient(m);
            break;
          case EXCHANGETILES:
            ExchangeTileMessage etm = (ExchangeTileMessage) m;
            Tile[] newTiles = new Tile[etm.getOldTiles().length];
            if (server.getAmountOverValue(etm.getOldTiles().length)) {
              for (int i = 0; i < etm.getOldTiles().length; i++) {
                newTiles[i] = server.getTile();
              }
              etm.setNewTiles(newTiles);
              server.addTiles(etm.getOldTiles());
            } else {
              etm.setError("Requested tile amount exceeds the amount of tiles in bag!");
            }
            sendToClient(etm);
            break;
          default:
            break;
        }
      }
    } catch (IOException e) {
      running = false;
      if (socket.isClosed()) {
        System.out.println("Socket was closed. client.Client:" + clientName);
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
