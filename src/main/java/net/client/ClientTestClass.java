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
    Client.createGame(this);
    try {
      joinClients();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /** Method to start the server from somewhere else e.g. from client.Client.createGame(this) */
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
        new PlayerProfile("profilname4", 0, 0, 0, 0, LocalDate.now(), LocalDate.now());
    PlayerProfile profile5 =
        new PlayerProfile("profilname5", 0, 0, 0, 0, LocalDate.now(), LocalDate.now());
    Client client1 = new Client("binsderclient", profile1);
    client1.connect();
    Thread.sleep(100);
    Client client2 = new Client("Yaso", profile2);
    client2.connect();
    Thread.sleep(100);
    Client client3 = new Client("Nico", profile3);
    client3.connect();
    Thread.sleep(100);
    client1.startGame(
        new File(
            "C:\\Users\\Valentin\\Downloads\\Collins Scrabble Words (2019) with definitions.txt"));
    Thread.sleep(2000);
    client2.sendChatMessage("Hello World", client2.getUsername());
    Thread.sleep(100);
    System.out.println("client1 ready");
    client1.setReadyState(true, client1.getUsername());
    Thread.sleep(100);
    System.out.println("client2 ready");
    client2.setReadyState(true, client2.getUsername());
    Thread.sleep(100);
    System.out.println("client3 ready");
    client3.setReadyState(true, client3.getUsername());
    Thread.sleep(100);
    System.out.println(client1.wordExists("AARRGHH"));
    System.out.println(client2.wordExists("SPATES"));
    System.out.println(client3.wordExists("SPATES"));
    client1.sendPlayerData(2);
    Thread.sleep(100);
    client2.sendPlayerData(0);
    Thread.sleep(100);
    client3.sendPlayerData(1);
    Thread.sleep(100);
    client1.getTile();
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
    client2.exchangeTiles(oldTiles);
    Client client4 = new Client("yilmaz", profile4);
    client4.connect();
    Thread.sleep(100);
    Client client5 = new Client("tooMuch", profile5);
    client5.connect();
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
