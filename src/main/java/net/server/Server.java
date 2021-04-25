package net.server;

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
  private int[] points = new int[] {-1, -1, -1, -1};

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
   * @param m message
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

  /** @return returns the serverSocket */
  // TODO maybe delete later if not needed
  public synchronized ServerSocket getServerSocket() {
    return this.serverSocket;
  }

  /** @return returns a hashmap containing pairs of String and Booleans */
  public synchronized HashMap<String, Boolean> getPlayersReady() {
    return this.playersReady;
  }

  /**
   * set the players ready state
   *
   * @param s requires the players username
   * @param b requires the boolean state of player's readiness
   */
  public synchronized void setPlayersReady(String s, Boolean b) {
    // System.out.println("here");
    if (playersReady.get(s)) {
      // System.out.println("here2");
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
  public synchronized ConnectMessage setID(String username) {
    ConnectMessage cm = new ConnectMessage(username);
    cm.setID(clientID++);
    return cm;
  }

  /*
  public synchronized void setPoints(int[] points) {
    for (int i = 0; i < points.length; i++) {
      if (this.points[i] == -1) {
        this.points[i] += (points[i] + 1);
      }
    }
  }*/
}
