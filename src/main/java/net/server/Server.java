package net.server;

import client.PlayerProfile;
import game.components.Tile;
import net.message.ConnectMessage;
import net.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * a server class to setup the server
 *
 * @author:ygarip
 */
public class Server {
  private ServerSocket serverSocket;
  private boolean running;
  private static final int port = 12975;
  private String serverIp;
  private ArrayList<ServerProtocol> clients = new ArrayList<>();
  private ArrayList<String> clientNames = new ArrayList<>();
  private HashMap<String, Boolean> playersReady = new HashMap<>();
  private int clientID = 0;
  private int[] points = new int[5];
  private int clientCounter = 0;
  private PlayerProfile[] profiles = new PlayerProfile[5];
  private HashMap<String, Boolean> agreements = new HashMap<>();

  /** Constructor to create server, sets serverIP */
  public Server() {
    this.serverIp = "25.93.29.50";
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
        clients.add(clientThread);
        clientThread.start();
      }
    } catch (IOException e) {
      if (serverSocket != null && serverSocket.isClosed()) {
        System.out.println("Server stopped");
      } else {
        e.printStackTrace();
      }
    }
  }

  /**
   * a method to get the ip address of the server
   *
   * @return Returns serverIp
   */
  public String getIpAddress() {
    return this.serverIp;
  }

  /** a method for stopping the server immediately */
  public void stopServer() {
    running = false;
    if (!serverSocket.isClosed()) {
      try {
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(0);
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
    for (ServerProtocol i : clients) {
      try {
        i.sendToClient(m);
      } catch (IOException e) {
        index = clients.indexOf(i);
        clients.remove(index);
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
  public synchronized void removeClientName(String name) {
    this.clientNames.remove(name);
    this.playersReady.remove(name);
    this.agreements.remove(name);
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
    if (playersReady.get(s)) {
      playersReady.remove(s);
    }
    playersReady.put(s, b);
  }

  /**
   * sets ReadyState of client
   *
   * @param username requires username of client
   */
  public synchronized void setReady(String username) {
    playersReady.put(username, false);
  }

  /**
   * create a ConnectMessage to set the ID
   *
   * @param username requires the username of the client
   * @return returns the ConnectMessage to connect
   */
  public synchronized ConnectMessage setID(String username, PlayerProfile profile) {
    ConnectMessage cm = new ConnectMessage(username, profile);
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
  public synchronized void setAgree(String user) {
    agreements.put(user, false);
  }

  /**
   * Sets Agreement status, based on String value for key and boolean for true/false
   *
   * @param user Requires user as key
   * @param agree Requires Boolean as value
   */
  public synchronized void setPlayersAgree(String user, Boolean agree) {
    if (agreements.get(user)) {
      agreements.remove(user);
    }
    this.agreements.put(user, agree);
  }

  /**
   * Checks, if all players are ready and if so, sends message back to all
   *
   * @param m Requires message received in ServerProtocol
   */
  public synchronized void isReady(Message m) {
    boolean ready = true;
    for (String s : playersReady.keySet()) {
      ready = ready && playersReady.get(s);
    }
    if (ready) {
      sendToAll(m);
    }
  }

  /**
   * Checks, if all players agree with the dictionary and sends the message back to all players
   *
   * @param m requires the AgreeDictionary Message
   */
  public synchronized void isAgree(Message m) {
    boolean ready = true;
    for (String s : agreements.keySet()) {
      ready = ready && agreements.get(s);
    }
    if (ready) {
      sendToAll(m);
    }
  }
}
