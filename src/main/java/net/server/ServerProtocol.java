package net.server;

import client.PlayerProfile;
import game.components.Tile;
import game.players.RemotePlayer;
import net.message.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
  private RemotePlayer rp;

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

  /** the run method of ServerProtocol to handle the incoming messages of the server */
  public void run() {
    try {

      while (running) {
        Message m = (Message) in.readObject();
        String username;
        switch (m.getMessageType()) {
          case CONNECT:
            /* server.setReady(username);
            server.setAgree(username);
            */
            // Todo
            PlayerProfile profile = ((ConnectMessage) m).getProfile();
            ConnectMessage cm = server.setID(profile);
            rp = (RemotePlayer) server.getPlayerOfID(cm.getID());
            rp.setPlayerProfile(profile);
            server.setPlayerProfiles(cm.getID(), profile);
            System.out.println("Server added: " + profile.getName());
            int index = Math.min(4, server.getNumberOfClients());
            PlayerProfile[] temp = new PlayerProfile[index];
            for (int i = 0; i < index; i++) {
              temp[i] = server.getProfile(i);
            }
            cm.setProfiles(temp);
            server.sendToAll(cm);
            break;
          case DISCONNECT:
            profile = ((DisconnectMessage) m).getProfile();
            server.removePlayer(rp);
            System.out.println("Server removed: " + profile.getName());
            running = false;
            disconnect();
            break;
          case CHATMESSAGE:
            server.sendToAll(m);
            break;
          case UPDATEGAMESETTINGS:
            UpdateGameSettingsMessage ugsm = (UpdateGameSettingsMessage) m;
            int[] tileScores = ugsm.getTileScores();
            int[] tileDistributions = ugsm.getTileDistributions();
            File dictionary = ugsm.getDictionary();
            server.updateGameSettings(tileScores, tileDistributions, dictionary);
            server.sendToAll(m);
            break;
          case PLAYERREADY:
            PlayerReadyMessage prm = (PlayerReadyMessage) m;
            rp.setIsReady(prm.getReady());
            prm.setPlayers(server.getPlayers());
            System.out.println(server.getPlayers().length);
            server.sendToAll(prm);
            break;
          case STARTGAME:
            server.sendToAll(m);
            // Checks, if all player are ready, then sends message to all clients
            //  prm = (PlayerReadyMessage) m;
            // TODO   server.setPlayersReady(prm.getUsername(), prm.getReady());

            break;
          case UPDATEGAMEBOARD:
            server.sendToAll(m);
            break;
          case SUBMITMOVE:
            // TODO add checkValid method
            // Board.check()
            // Message m contains username and valid and board attributes
            sendToClient(m);
            break;
          case UPDATEPOINTS:
            // TODO add pointUpdating method
            // Game.evaluateScore()
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

// TODO Requirements
/*
We have:
- Player Profiles [1 - 4]
- Client id
- All Players agreed to dictionary
- Chat message
- Disconnected
- Exchange Tiles, send old Tiles, get same amount of new tiles
- Get Tile from bag to personal rack
- All players ready
- Dictionary
- Move submitted
- Updated Board
- Updated Points
- Server (?)
- is AI active?

We need:
- Access to CreateGameController, to set Players
- Dictionary agreement boolean value
- add message to chat
- toggle readiness of player
- update board view
- color words red, if submit is wrong
- update points
- access to game, to trigger nextRound()
- access to player to read and write to and from personal rack
- change fxml after dictionary agreement
- game checkValid from server
- game evaluateScore from server
 */
