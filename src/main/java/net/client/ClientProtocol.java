package net.client;

import net.message.Connect;
import net.message.Disconnect;
import net.message.Message;
import net.message.StartGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/** @author vkaczmar Handles all interactions from the server to the client */
public class ClientProtocol extends Thread {
  private String client;
  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private boolean running = true;

  /**
   * Constructor for creating streams and connecting a client
   *
   * @param ip Requires the ip, the server runs on
   * @param client Requires username for current profile
   */
  public ClientProtocol(String ip, String client) {
    this.client = client;
    try {
      this.clientSocket = new Socket(ip, 12975);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());
      this.out.writeObject(new Connect(client));
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
  Test
  public void toServer(Message m) throws IOException {
    this.out.writeObject(m);
    out.flush();
    out.reset();
  }*/

  /** Method for writing a Disconnect Object to the server */
  public void disconnect() {
    running = false;
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new Disconnect(client));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Method for writing a StartGame Object to the server */
  public void startGame() {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new StartGame());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Overwritten run method from Thread. Accepts and works through incoming messages from the server
   */
  public void run() {
    while (running) {
      try {
        Message m = (Message) in.readObject();
        switch (m.getMessageType()) {
          case CONNECT:
            break;
          case DISCONNECT:
            break;
          case STARTGAME:
            System.out.println("Rufe FXML Wechsel auf");
            break;
          default:
            break;
        }
      } catch (IOException | ClassNotFoundException e) {
        break;
      }
    }
  }
}
