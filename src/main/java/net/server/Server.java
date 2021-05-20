package net.server;

import client.Client;
import client.PlayerProfile;
import game.Dictionary;
import game.Game;
import game.components.Tile;
import game.players.Player;
import game.players.RemotePlayer;
import net.client.NetClient;
import net.message.ConnectMessage;
import net.message.Message;
import net.message.RefuseConnectionMessage;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.rmi.Remote;
import java.util.*;

/**
 * a server class to setup the server
 *
 * @author ygarip
 */
public class Server extends Thread {
  private ServerSocket serverSocket;
  private boolean running;
  private static final int port = 12975;
  private static String serverIp;
  private List<Player> players = new LinkedList<>();
  private Game game;

  int[] tileScores;
  int[] tileDistributions;
  String dictionaryString;
  Dictionary dictionary;

  /** Constructor to create server, sets serverIP */
  public Server() {
    try {
      this.serverIp = getLocalHostIp4Address();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  /** a method to run the server */
  public void run() {
    this.listen();
  }

  /**
   * a method to return the player at index i
   *
   * @param i Requires an integer
   * @return returns the player at index i
   */
  public synchronized Player getPlayerOfID(int i) {
    return this.players.get(i);
  }

  /** @return returns the playerList in an array */
  public synchronized Player[] getPlayers() {
    // TODO Vorher in for each mit Doppelpunkt Elemente rausgezogen und in Array gepackt, immer
    // null, mit player.get(i) funktioniert es irgendwie, weiß jemand warum?
    return players.toArray(new Player[0]);
  }

  /** Starts game from server */
  public void startGame() {
    // Convert TileScores and TileDistributions to HashMap
    HashMap<Character, Integer> mapTileDistribution = new HashMap<>();
    HashMap<Character, Integer> mapScores = new HashMap<>();
    for (int i = 0; i < tileDistributions.length; i++) {
      mapTileDistribution.put(((char) ('A' + i)), tileDistributions[i]);
      mapScores.put(((char) ('A' + i)), tileScores[i]);
    }
    // TODO Might change type of players in game or in server , but it could work like this
    game = new Game((ArrayList<Player>) players, mapTileDistribution, mapScores, dictionary);
  }

  /**
   *
   * @return returns if the game is running
   */
  public boolean gameIsRunning(){
    return game != null;
  }

  public void stopGame(){
    // TODO send stats back
    game = null;
  }

  /**
   * Creates new dictionary from absolute path
   *
   * @param file Requires File that includes Path to dictionary.txt
   */
  public void createDictionary(File file) {
    dictionary = new Dictionary(file.getAbsolutePath());
  }

  // TODO test if method from StackOverflow works as well, should use it then, so we could cite the
  // right code
  /**
   * @return Returns ip from localhost
   * @throws UnknownHostException
   * @author from
   *     https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
   */
  public static String getLocalHostIp4Address() throws UnknownHostException {

    try {

      InetAddress candidateAddress = null;

      for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
          ifaces.hasMoreElements(); ) {

        NetworkInterface iface = ifaces.nextElement();

        for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses();
            inetAddrs.hasMoreElements(); ) {

          InetAddress inetAddr = inetAddrs.nextElement();

          if (!inetAddr.isLoopbackAddress() && inetAddr instanceof Inet4Address) {

            if (inetAddr.isSiteLocalAddress()) {

              return inetAddr.getHostAddress();

            } else if (candidateAddress == null) {

              candidateAddress = inetAddr;
            }
          }
        }
      }

      if (candidateAddress != null) {

        return candidateAddress.getHostAddress();
      }

      InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();

      if (jdkSuppliedAddress == null) {

        throw new UnknownHostException(
            "The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
      }

      return jdkSuppliedAddress.getHostAddress();

    } catch (Exception e) {

      UnknownHostException unknownHostException =
          new UnknownHostException("Failed to determine LAN address: " + e);

      unknownHostException.initCause(e);

      throw unknownHostException;
    }
  }

  /**
   * starts the serverSocket and listens for incoming clients which want to connect to the server
   */
  public void listen() {
    running = true;
    try {
      serverSocket = new ServerSocket(Server.port);
      System.out.println("Server running on " + serverIp + ":" + port);
      while (running) {
        Socket clientSocket = serverSocket.accept();
        ServerProtocol clientThread = new ServerProtocol(clientSocket, this);

        // if full send ConnectionRefusedMessage back
        if (players.size() >= 4) {
          clientThread.sendToClient(new RefuseConnectionMessage("Lobby is full"));
        } else if (game != null) {
          clientThread.sendToClient(
              new RefuseConnectionMessage(
                  "Connection Refused. Game is already running.\n\nSorry, your friends started the game without you :("));
        } else {
          RemotePlayer newPlayer = new RemotePlayer(clientThread, players.size() == 0);
          clientThread.setPlayer(newPlayer);
          players.add(newPlayer);

          clientThread.start();
        }
      }
    } catch (IOException e) {
      if (serverSocket != null && serverSocket.isClosed()) {
        System.out.println("Server stopped | " + e.getMessage());
      } else {
        e.printStackTrace();
      }
    }
    System.out.println("Server: Server stopped");
  }

  /**
   * method to add a player to the server's playerList
   *
   * @param player Requires the player
   */
  public synchronized void addPlayer(Player player) {
    players.add(player);
  }

  /**
   * method to remove a player from the server's playerList
   *
   * @param player Requires the player
   */
  public synchronized void removePlayer(Player player) {
    players.remove(player);
  }

  /**
   * a method to get the ip address of the server
   *
   * @return Returns serverIp
   */
  public static String getIpAddress() {
    return serverIp;
  }

  /** a method for stopping the server immediately */
  public synchronized void stopServer() {
    running = false;
    if (!serverSocket.isClosed()) {
      try {
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * a method for sending a message to all clients
   *
   * @param m requires the message
   */
  public synchronized void sendToAll(Message m) {
    int index = 0;
    for (Player player : players) {
      if (!player.isHuman()) {
        continue;
      }
      try {
        ((RemotePlayer) player).getConnection().sendToClient(m);
      } catch (IOException e) {   // remove player and send new lobby list to all
        players.remove(player);
        ConnectMessage cm = new ConnectMessage(null);
        cm.setProfiles(getPlayerProfilesArray());
        sendToAll(cm);
      }
    }
  }

  /**
   * a method for sending a message to all client but not the given Player (given)
   *
   * @param m requires the message
   */
  public synchronized void sendToOthers(ServerProtocol protocol, Message m){
    players.forEach(
        player -> {
          if (player.isHuman() && ((RemotePlayer) player).getConnection() != protocol) { // if not protocol who initiated the call
            try {
              ((RemotePlayer) player).getConnection().sendToClient(m);
              System.out.println("ChatMessage sent to " + player.getProfile().getName());
            } catch (IOException e) { // remove player and send lobby list to all
              players.remove(player);
              ConnectMessage cm = new ConnectMessage(null);
              cm.setProfiles(getPlayerProfilesArray());
              sendToAll(cm);
            }
          }
        });
  }

  /**
   * a method go get the playerProfile
   *
   * @param id requires the id of the clientplayer
   * @return returns the PlayerProfile
   */
  public synchronized PlayerProfile getProfile(int id) {
    return players.get(id).getProfile(); // profiles[id];
  }

  // TODO remove one tile from bag,increment bag
  // send this one back to the server protocol

  /** @return returns a Tile from the bag */
  public synchronized Tile getTile() {
    return new Tile('A', 2);
  }

  /**
   * @param value requires the value of the requested tiles
   * @return Returns true, if value is bigger than bag amount
   */
  public synchronized boolean getAmountOverValue(int value) {
    // TODO if bag amount >= value then
    if ((value >= 0) && (value <= 7)) {
      return true;
    }
    return false;
  }

  /**
   * a method to add the old tiles to the bag
   *
   * @param oldTiles requires the oldTiles array of tiles
   */
  public synchronized void addTiles(Tile[] oldTiles) {
    // TODO add the old tiles to the bag
  }

  /**
   * a method to update the GameSettings of the server
   *
   * @param tileScores Requires the tileScores array
   * @param tileDistributions Requires the tileDistribution array
   * @param dictionary Requires the dictionaryString's txt file
   */
  public synchronized void updateGameSettings(
      int[] tileScores, int[] tileDistributions, String dictionary) {
    this.tileScores = tileScores;
    this.tileDistributions = tileDistributions;
    this.dictionaryString = dictionary;
  }

  /** @return Returns if server is still running */
  public boolean isRunning() {
    return running;
  }

  /** @return Returns array with all playerProfiles */
  public PlayerProfile[] getPlayerProfilesArray() {
    int index = Math.min(4, getPlayers().length);
    PlayerProfile[] temp = new PlayerProfile[index];
    for (int i = 0; i < index; i++) {
      temp[i] = getProfile(i);
    }
    return temp;
  }
}
