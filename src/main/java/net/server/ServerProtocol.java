package net.server;

import client.PlayerProfile;
import game.components.Tile;
import game.players.*;
import net.message.*;
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
  private final Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private final Server server;
  private String clientName;
  private boolean running = true;

  private RemotePlayer player; // Controlled player

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
        System.out.println("Message received(Server-Side): " + m.getMessageType().toString());
        switch (m.getMessageType()) {
          case CONNECT:
            PlayerProfile profile = ((ConnectMessage) m).getProfile();
            ConnectMessage cm = (ConnectMessage) m;
            player.setPlayerProfile(profile);
            System.out.println("Server added: " + profile.getName());

            server.sendToAll(new ChatMessage(profile.getName() + " joined our round!" + (profile.getName().split(" ")[0].equals("Bot") ? "*Beep-Boop*" : ""), null)); // send system Message to all

            // send new lobby list to all
            cm.setProfiles(server.getPlayerProfilesArray());
            server.sendToAll(cm);
            break;
          case DISCONNECT:
            profile = ((DisconnectMessage) m).getProfile();
            server.removePlayer(player); // Remove Player from server
            System.out.println("Server removed: " + profile.getName());
            if (player.isHost()) {
              server.sendToAll(
                  new DisconnectMessage(
                      null,
                      "The server is closed.\n\nThe Host sadly doesn't want to play any longer"));
              server.stopServer();
            } else { // take player out of player list and send new ConnectMessage with all player

              server.sendToAll(new ChatMessage(profile.getName() + " left", null)); // send system Message to all

              // profiles connected
              cm = new ConnectMessage(null);
              cm.setProfiles(server.getPlayerProfilesArray());
              server.sendToAll(cm);
            }
            disconnect();
            break;
          case CHATMESSAGE: // Send chat message to all other connections
            server.sendToOthers(this, m);
            break;
          case UPDATEGAMESETTINGS:
            UpdateGameSettingsMessage ugsm = (UpdateGameSettingsMessage) m;
            int[] tileScores = ugsm.getTileScores();
            int[] tileDistributions = ugsm.getTileDistributions();
            String dictionary = ugsm.getDictionary();
            server.updateGameSettings(tileScores, tileDistributions, dictionary);
            break;
          case PLAYERREADY:
            PlayerReadyMessage prm = (PlayerReadyMessage) m;
            player.setIsReady(prm.getReady());
            Player[] player = server.getPlayers();
            boolean[] playerReady = new boolean[player.length];
            boolean ready = true;
            for (int i = 0; i < playerReady.length; i++) {
              playerReady[i] = ((RemotePlayer) player[i]).getReady();
              ready = ready && playerReady[i];
            }
            prm.setValues(playerReady);
            prm.setReady(ready);
            server.sendToAll(prm);
            break;
          case STARTGAME:
            server.createDictionary(((StartGameMessage) m).getFile());
            server.startGame();
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
          case ADDAI:
            // Check if server has space for a player
            if (server.getPlayers().length >= 4) {
              break;
            }
            AddAIMessage ai = (AddAIMessage) m;
            AiPlayer aiPlayer;
            if (ai.getDifficulty()) {
              aiPlayer = new HardAiPlayer();
            } else {
              aiPlayer = new EasyAiPlayer();
            }

            server.addPlayer(aiPlayer);
            ConnectMessage cm1 = new ConnectMessage(null);
            cm1.setProfiles(server.getPlayerProfilesArray());
            server.sendToAll(cm1);
            break;
          case KICKPLAYER:
            System.out.println(
                "Host kicked: "
                    + server
                        .getPlayers()[((KickPlayerMessage) m).getIndex()]
                        .getProfile()
                        .getName());

            DisconnectMessage dm =
                new DisconnectMessage(
                    null,
                    "You were kicked out of the lobby!\n\nThe host doesn't like you anymore!");

            // Send message to kicked player
            RemotePlayer rp1 =
                (RemotePlayer) server.getPlayers()[((KickPlayerMessage) m).getIndex()];
            rp1.getConnection().sendToClient(dm);

            // remove from server
            server.removePlayer(
                    server
                            .getPlayers()[((KickPlayerMessage) m).getIndex()]); // Remove Player from server

            // Send system message
            server.sendToAll(
                new ChatMessage(rp1.getProfile().getName() + " was kicked by the host", null));

            // update lobby profiles
            cm = new ConnectMessage(null);
            cm.setProfiles(server.getPlayerProfilesArray());
            server.sendToAll(cm);
            break;
          case REQUESTVALUES:
            RequestValuesMessage rvm = (RequestValuesMessage) m;
            rvm.setValues(server.getTileScores());
            sendToClient(rvm);
            break;
          case REQUESTDISTRIBUTIONS:
            RequestDistributionsMessage rdm = (RequestDistributionsMessage) m;
            rdm.setDistributions(server.getTileDistributions());
            sendToClient(rdm);
            break;
          case REQUESTDICTIONARY:
            RequestDictionaryMessage rdm2 = (RequestDictionaryMessage) m;
            rdm2.setDictionary(server.getDictionaryString());
            sendToClient(rdm2);
            break;
          case PLACETILE:
            // TODO Game logic
            server.sendToOthers(this, m);
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

  /**
   * Sets remote player in ServerProtocol
   *
   * @param player Requires player to be set
   */
  public void setPlayer(RemotePlayer player) {
    this.player = player;
  }
}
