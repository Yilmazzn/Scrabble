package net.client;

import client.PlayerProfile;
import game.components.Tile;
import net.server.Server;

import java.io.File;

import java.io.IOException;
import java.time.LocalDate;

/**
 * a test class to test the server client
 *
 * @author ygarip
 */
public class ClientTestClass {
  private Server server;

  /** a constructor to create the client and server instances for testing */
  public ClientTestClass() {
    NetClient.createGame(this);
    try {
      joinClients();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /** Method to start the server from somewhere else e.g. from Client.createGame(this) */
  public void startServer() {
    server = new Server();
    new ServerListenThread().start();
  }

  /**
   * a method to join all clients to the server
   *
   * @throws IOException an exception thrown if a Input/Output error occurs
   * @throws InterruptedException an exception if a thread is interrupted
   */
  public void joinClients() throws IOException, InterruptedException {
    Thread.sleep(100);
    PlayerProfile profile1 =
        new PlayerProfile("profilname1", 0, 0, 0, 0, LocalDate.now(), LocalDate.now());
    PlayerProfile profile2 =
        new PlayerProfile("profilname2", 0, 0, 0, 0, LocalDate.now(), LocalDate.now());
    PlayerProfile profile3 =
        new PlayerProfile("profilname3", 0, 0, 0, 0, LocalDate.now(), LocalDate.now());
    PlayerProfile profile4 =
        new PlayerProfile("AI4", 0, 0, 0, 0, LocalDate.now(), LocalDate.now());
    PlayerProfile profile5 =
        new PlayerProfile("profilname5", 0, 0, 0, 0, LocalDate.now(), LocalDate.now());
    NetClient netClient1 = new NetClient("binsderclient", profile1);
    netClient1.connect();
    Thread.sleep(100);
    NetClient netClient2 = new NetClient("Yaso", profile2);
    netClient2.connect();
    Thread.sleep(100);
    NetClient netClient3 = new NetClient("Nico", profile3);
    netClient3.connect();
    Thread.sleep(100);
    netClient1.startGame(
        new File(
            "C:\\Users\\Valentin\\Downloads\\Collins Scrabble Words (2019) with definitions.txt"));
    Thread.sleep(2000);
    netClient2.sendChatMessage("Hello World", netClient2.getUsername());
    Thread.sleep(100);
    System.out.println("client1 ready");
    netClient1.setReadyState(true, netClient1.getUsername());
    Thread.sleep(100);
    System.out.println("client2 ready");
    netClient2.setReadyState(true, netClient2.getUsername());
    Thread.sleep(100);
    System.out.println("client3 ready");
    netClient3.setReadyState(true, netClient3.getUsername());
    Thread.sleep(100);
    System.out.println(netClient1.wordExists("AARRGHH"));
    System.out.println(netClient2.wordExists("SPATES"));
    System.out.println(netClient3.wordExists("SPATES"));
    netClient1.sendPlayerData(2);
    Thread.sleep(100);
    netClient2.sendPlayerData(0);
    Thread.sleep(100);
    netClient3.sendPlayerData(1);
    Thread.sleep(100);
    netClient1.getTile();
    Tile[] oldTiles =
        new Tile[] {
          new Tile('A', 2),
          new Tile('B', 3),
          new Tile('C', 4),
          new Tile('D', 5),
          new Tile('A', 2),
          new Tile('B', 3),
          new Tile('C', 4),
          new Tile('D', 5)
        };
    netClient2.exchangeTiles(oldTiles);
    NetClient netClient4 = new NetClient("ai", profile4, true);
    netClient4.connect();
    Thread.sleep(100);
    NetClient netClient5 = new NetClient("tooMuch", profile5);
    netClient5.connect();
    Thread.sleep(100);
    netClient1.agreeOnDictionary(true, netClient1.getUsername());
    Thread.sleep(100);
    netClient2.agreeOnDictionary(true, netClient2.getUsername());
    Thread.sleep(100);
    netClient3.agreeOnDictionary(true, netClient3.getUsername());
    Thread.sleep(100);
    netClient4.agreeOnDictionary(true, netClient4.getUsername());
  }

  /** a inner thread class to start the server */
  class ServerListenThread extends Thread {
    public void run() {
      server.listen();
    }
  }

  /**
   * Main method to start the network tests
   *
   * @param args args get passed, when you start the program
   */
  public static void main(String[] args) {
    new ClientTestClass();
  }
}
