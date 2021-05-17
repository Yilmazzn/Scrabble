package net.server;

import client.PlayerProfile;
import game.components.Tile;
import net.message.ConnectMessage;
import net.message.Message;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;


/**
 * a server class to setup the server
 *
 * @author:ygarip
 */
public class Server {
  private ServerSocket serverSocket;
  private boolean running;
  private static final int port = 12975;
  private static String serverIp;
  private ArrayList<ServerProtocol> clients = new ArrayList<>();
  private ArrayList<String> clientNames = new ArrayList<>();
  private HashMap<String, Boolean> playersReady = new HashMap<>();
  private int clientID = 0;
  private int[] points = new int[5];
  private int clientCounter = 0;
  private static PlayerProfile[] profiles = new PlayerProfile[5];

  /** Constructor to create server, sets serverIP */
  public Server() {
    try {
      this.serverIp = getLocalHostIp4Address();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  // https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
  // TODO but method name differs
  /**
   * @author from stackoverflow
   * @return IP
   * @throws UnknownHostException
   */
  public String getLocalHostIp4Address() throws UnknownHostException {

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
      System.out.println("Server started");
      while (running) {
        Socket clientSocket = serverSocket.accept();
        ServerProtocol clientThread = new ServerProtocol(clientSocket, this);
        players.add(new RemotePlayer(clientThread));
        clientThread.start(); // TODO maybe players.get().getConnection().start()
      }
    } catch (IOException e) {
      if (serverSocket != null && serverSocket.isClosed()) {
        System.out.println("Server stopped");
      } else {
        e.printStackTrace();
      }
    }
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void removePlayer(Player player) {
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
  public void stopServer() {
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
      try {
        ((RemotePlayer) player).getConnection().sendToClient(m);
      } catch (IOException e) {
        index = players.indexOf(player);
        players.remove(index);
      }
    }
  }

  /**
   * adds a new clientname to the list of clientnames
   *
   * @param name the name of the client
   */
  public synchronized void addClientName(String name) {
    this.clientNames.add(name);
  }

  /**
   * removes a client name from the list of clientnames
   *
   * @param name requires name to be removed from the arraylist
   */
  public synchronized void removeClientName(Player name) {
    this.clientNames.remove(name);
    // TODO this.playersReady.remove(name);
    // TODO this.mapPlayersReady.remove(name);
  }

  /**
   * removes a client from the List of clients
   *
   * @param toRemove the protocol which should be removed
   */
  public synchronized void removeClient(ServerProtocol toRemove) {
    this.clients.remove(toRemove);
  }

  /** @return returns the number of clients in the list */
  public synchronized int getNumberOfClients() {
    return this.clients.size();
  }

  /**
   * set the players ready state
   *
   * @param s requires the players username
   * @param b requires the boolean state of player's readiness
   */
  public synchronized void setPlayersReady(String s, Boolean b) {
    // TODO if (playersReady.get(s)) {
    // TODO     playersReady.remove(s);
    // TODO }
    // TODO playersReady.put(s, b);
  }

  /**
   * sets ReadyState of client
   *
   * @param username requires username of client
   */
  public synchronized void setReady(String username) {
    // TODO playersReady.put(username, false);
  }

  /**
   * create a ConnectMessage to set the ID
   *
   * @param profile requires the playerprofile
   * @return returns the ConnectMessage to connect
   */
  public synchronized ConnectMessage setID(PlayerProfile profile) {
    ConnectMessage cm = new ConnectMessage(profile);
    cm.setID(clientID++);
    return cm;
  }

  /**
   * a method to set the playerprofile
   *
   * @param id requires the id of the clientplayer
   * @param profile requires the playerprofile of the clientplayer
   */
  public synchronized void setPlayerProfiles(int id, PlayerProfile profile) {
    profiles[id] = profile;
  }

  /**
   * a method go get the playerProfile
   *
   * @param id requires the id of the clientplayer
   * @return returns the PlayerProfile
   */
  public synchronized PlayerProfile getProfile(int id) {
    return profiles[id];
  }

  public synchronized HashMap<String, Boolean> getPlayersReady() {
    // TODO playersReady.put("Test", true);
    // TODO return playersReady;
    return null;
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
   * Sets Agreement to false, on specified user
   *
   * @param user Requires user, which wants to change HashMap
   */
  public synchronized void setAgree(Player user) {
    // TODO mapPlayersReady.put(user, false);
  }

  /**
   * Sets Agreement status, based on String value for key and boolean for true/false
   *
   * @param user Requires user as key
   * @param agree Requires Boolean as value
   */
  public synchronized void setPlayersAgree(Player user, Boolean agree) {
    // TODO if (mapPlayersReady.get(user)) {
    // TODO    mapPlayersReady.remove(user);
    // TODO }
    // TODO this.mapPlayersReady.put(user, agree);

  }


  public synchronized void updateGameSettings(int[] tileScores, int[] tileDistributions, File dictionary) {
    this.tileScores = tileScores;
    this.tileDistributions = tileDistributions;
    this.dictionary = new Dictionary(dictionary.getAbsolutePath());
  }
}
