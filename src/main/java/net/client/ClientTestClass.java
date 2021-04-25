package net.client;

import net.server.Server;

import java.io.IOException;

/**
 * a test class to test the server client
 *
 * @author ygarip
 */
public class ClientTestClass {
  private Server server;
  private Client client1;

  /** a constructor to create the client and server instances for testing */
  public ClientTestClass() {
    client1 = new Client("binsderclient");
    client1.createGame(this);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    client1.connect();
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
    Client client2 = new Client("Yaso");
    client2.connect();
    Thread.sleep(100);
    Client client3 = new Client("Nico");
    client3.connect();
    Thread.sleep(100);
    client1.startGame();
    Thread.sleep(100);
    client2.sendChatMessage("Hello World", client2.getUsername());
    Thread.sleep(100);
    System.out.println("client1 ready");
    client1.setReadyState(true, client1.getUsername());
    System.out.println("client2 ready");
    client2.setReadyState(true, client2.getUsername());
    System.out.println("client3 ready");
    client3.setReadyState(true, client3.getUsername());
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
