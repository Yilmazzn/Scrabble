package net.client;

import client.PlayerProfile;
import ft.XmlHandler;
import game.components.Board;
import game.components.Tile;
import javafx.application.Platform;
import net.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/** @author vkaczmar Handles all interactions from the server to the client */
public class ClientProtocol extends Thread {
  private final NetClient client;
  private final Socket clientSocket;
  private final ObjectOutputStream out;
  private final ObjectInputStream in;
  private boolean running = true;
  private final int id = -1;

  /**
   * Constructor for creating streams and connecting a client
   *
   * @param ip Requires the ip, the server runs on
   * @param netClient Requires username for current profile
   */
  public ClientProtocol(String ip, NetClient netClient) throws IOException {
    this.client = netClient;
    this.clientSocket = new Socket(ip, 12975);
    System.out.println(
        "Client with IP:" + ip + " connected, " + client.getPlayerProfile().getName());
    this.out = new ObjectOutputStream(clientSocket.getOutputStream());
    this.in = new ObjectInputStream(clientSocket.getInputStream());
    this.out.writeObject(new ConnectMessage(client.getPlayerProfile()));
    this.out.flush();
  }

  /** Method for writing a DisconnectMessage Object to the server */
  public void disconnect() {
    running = false;
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new DisconnectMessage(client.getClient().getSelectedProfile(), ""));
        clientSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets players readiness
   *
   * @param ready Requires value ready should be set to
   */
  public void setPlayerReady(boolean ready) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new PlayerReadyMessage(ready));
        this.out.flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Method for writing a StartGameMessage Object to the server */
  public void startGame() {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new StartGameMessage());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for writing a ChatMessage Object to the server
   *
   * @param chatMessage message to be sent
   */
  public void sendChatMessage(String chatMessage) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(
            new ChatMessage(chatMessage, client.getClient().getSelectedProfile().getName()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for writing a UpdateGameBoardMessage Object to the server
   *
   * @param board requires Board instance
   */
  public void updateGameBoard(Board board) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new UpdateGameBoardMessage(board));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** a method to submit the move of the client */
  public void submitMove() {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new SubmitMoveMessage());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for writing a UpdatePointsMessage Object to the server
   *
   * @param currentState
   * @param previousState
   */
  public void updatePoints(Board currentState, Board previousState) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new UpdatePointsMessage(currentState, previousState, id));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * A method for writing a SendPlayerMessage Object to server
   *
   * @param id Requires id to receive profile from
   */
  public void sendPlayerData(int id) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new SendPlayerDataMessage(id));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates and sends RequestBagAmountMessage instance to server
   *
   * @param oldTiles Requires tiles to be removed
   */
  public void exchangeTiles(Tile[] oldTiles) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new ExchangeTileMessage(oldTiles));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates and sends UpdateGameSettingsMessage instance to server
   *
   * @param tileScores Requires tileScores
   * @param tileDistributions Requires tileDistribution
   * @param dictionary Requires dictionary file
   */
  public void sendGameSettings(int[] tileScores, int[] tileDistributions, String dictionary) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(
            new UpdateGameSettingsMessage(tileScores, tileDistributions, dictionary));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates and sends AddAIMessage instance to server
   *
   * @param difficult Requires difficulty, true = hard, false = easy
   */
  public void addAIPlayer(boolean difficult) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new AddAIMessage(difficult));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * creates and sends KickplayerMessage instance to server
   *
   * @param index Requires the index of the player who should be kicked from server
   */
  public void kickPlayer(int index) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new KickPlayerMessage(index));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Creates and sends RequestDictionaryMessage to server */
  public void requestDictionary() {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new RequestDictionaryMessage());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(Message m) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(m);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Creates and sends RequestDistributionsMessage to server */
  public void requestDistributions() {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new RequestDistributionsMessage());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Creates and sends RequestValuesMessage to server */
  public void requestValues() {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new RequestValuesMessage());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates and sends PlaceTileMessage to server
   *
   * @param tile Requires Tile that is placed or removed on board
   * @param row Requires row of board
   * @param col Requires col of board
   */
  public void placeTile(Tile tile, int row, int col) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new PlaceTileMessage(tile, row, col));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendEndMessage(int type) {
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new EndGameMessage(type));
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
        System.out.println("Message received(Client-Side): " + m.getMessageType().toString());
        switch (m.getMessageType()) {
          case CONNECT:
            PlayerProfile[] lobbyProfiles = ((ConnectMessage) m).getProfiles();

            // sets list of coplayers and updates respective views
            Platform.runLater(
                () -> {
                  if (!client.isHost()) {
                    client.loadGameView(); // Load Game view only if connected
                  }

                  // set Data
                  client.setLobbyState(lobbyProfiles, new int[lobbyProfiles.length]);
                });
            setPlayerReady(false);
            break;
          case REFUSECONNECTION:
            Platform.runLater(
                () -> {
                  client.getClient().showPopUp(((RefuseConnectionMessage) m).getMessage());
                });
            running = false;
            break;
          case UPDATEGAMESETTINGS: // todo done eigentlich nur connection zu controllern herstellen
            // TODO netClient.getClient().updateGameSettings
            // TODO Dictionary erstmal als String, erst beim Spielstart wird auf dem Server das
            // richtige Dictionary erzeugt
            break;
          case STARTGAME:
            client.initializeGame(); // load Game view
            break;
          case CHATMESSAGE:
            // TODO add methode
            String username = ((ChatMessage) m).getUsername();
            String message = ((ChatMessage) m).getMsg();
            Platform.runLater(
                () -> {
                  client.updateChat(username, message);
                });
            break;
          case PLAYERREADY:
            PlayerReadyMessage prm = (PlayerReadyMessage) m;
            for (int i = 0; i < prm.getValues().length; i++) {
              System.out.println("Status " + (i + 1) + ": " + (prm.getValues()[i]));
            }
            Platform.runLater(
                () -> {
                  client.setReadies(prm.getValues());
                });
            break;
          case UPDATEGAMEBOARD:
            // TODO add method
            System.out.println("Update Board");
            break;
          case UPDATEPOINTS:
            // TODO updateView()
            // The message knows, which points have been changed
            System.out.println("Update Points View");
            // TODO nextPlayer()
            break;
          case SENDPLAYERDATA:
            // TODO show Profile
            System.out.println(((SendPlayerDataMessage) m).getProfile().getName());
            break;
          case GIVETILE:
            Platform.runLater(
                () -> {
                  client
                      .getClient()
                      .getLocalPlayer()
                      .addTilesToRack(((GiveTileMessage) m).getTile());
                });
            break;
          case TURN:
            Platform.runLater(
                () -> {
                  client.setTurns(((TurnMessage) m).getTurn(), ((TurnMessage) m).getTurns());
                  client.setBagSize(((TurnMessage) m).getBagSize());
                  client.setCoPlayerScores(((TurnMessage) m).getPoints());
                });
            break;
          case DISCONNECT:
            Platform.runLater(
                () -> {
                  try {
                    client
                        .getClient()
                        .showPopUp(
                            ((DisconnectMessage) m)
                                .getDisconnectMessage()); // show reason for forced disconnection
                    client.getClient().showMainMenu();
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                });
            break;
          case REQUESTDICTIONARY:
            String dictionary = ((RequestDictionaryMessage) m).getDictionary();
            client.updateGameSettings(null, null, dictionary);
            break;
          case REQUESTVALUES:
            int[] values = ((RequestValuesMessage) m).getValues();
            client.updateGameSettings(values, null, null);
            break;
          case REQUESTDISTRIBUTIONS:
            int[] distributions = ((RequestDistributionsMessage) m).getDistributions();
            client.updateGameSettings(null, distributions, null);
            break;
          case PLACETILE:
            PlaceTileMessage ptm = (PlaceTileMessage) m;
            Platform.runLater(
                () -> {
                  client.placeIncomingTile(ptm.getTile(), ptm.getRow(), ptm.getCol());
                });
            break;
          case ENDGAME:
            EndGameMessage egm = (EndGameMessage) m;
            String tmp;
            if (egm.getType() == 0) {
              tmp =
                  "The game ended, because the bag is empty and at least one player has no Tiles left in his rack.";
            } else if (egm.getType() == 1) {
              tmp = "The game ended, because at least one player clicked on end game";
            } else {
              tmp =
                  "The game ended, because at least one player used up more than 10 minutes of playtime.";
            }

            PlayerProfile tmp2 = client.getPlayerProfile();
            tmp2.setHighscore(Math.max(tmp2.getHighscore(), egm.getScore()));
            tmp2.setLastLogged(LocalDate.now());
            tmp2.setTotalScore(Math.max(tmp2.getHighscore() + egm.getScore(), Integer.MAX_VALUE));
            if (egm.getWinner()) {
              tmp2.setWins(tmp2.getWins() + 1);
            } else {
              tmp2.setLosses(tmp2.getLosses() + 1);
            }

            // TODO doesn't work problem might be, that two instances want to conenct to this
            // XmlHandler.saveXML(Arrays.asList(client.getCoPlayers()));

            Platform.runLater(
                () -> {
                  client.updateChat(null, tmp);
                  client.changeToResultView();
                });
            break;
          case ENDABLE:
            client.enableEndGameButton();
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
