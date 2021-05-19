package game.players;

import client.PlayerProfile;
import game.Scoreboard;
import game.components.Board;
import game.components.Tile;
import net.server.ServerProtocol;

import java.util.Collection;

/**
 * @author yuzun
 *     <p>Remote player instance, which is controlled by ServerProtocol and sends messages back
 */
public class RemotePlayer extends Player {

  private final ServerProtocol connection;
  private boolean isReady;
  private boolean host;
  private int id;

  /**
   * Creates new RemotePlayer instance
   *
   * @param connection requires the serverProtocol
   * @param host Requires boolean, if Player is host
   */
  public RemotePlayer(ServerProtocol connection, boolean host) {
    super(true);
    this.host = host;
    this.connection = connection;
    this.host = host;
  }

  /**
   * Sends board to set remote player's to the latest game board state
   *
   * @param board Requires board to send to server
   */
  public void updateBoard(Board board) {
    // TODO SEND BOARD MESSAGE
  }

  /**
   * Sends scoreboard instance after every turn
   *
   * @param scoreboard Requires scoreboard to send to server
   */
  public void sendScoreboard(Scoreboard scoreboard) {
    // TODO SEND SCOREBOARD
  }

  /**
   * Sets ready value for each player
   *
   * @param value Requires value to be set
   */
  public void setIsReady(boolean value) {
    System.out.println("remoteplayer class :" + this.getProfile().getName() + " value:" + value);
    isReady = value;
  }

  /** @return returns the player's readiness */
  public boolean getReady() {
    return this.isReady;
  }

  /** @return returns the serverprotocol connection */
  public ServerProtocol getConnection() {
    return connection;
  }

  /** @param profile Requires PlayerProfile */
  public void setPlayerProfile(PlayerProfile profile) {
    super.setPlayerProfile(profile);
  }

  /**
   * Sets turn for RemotePlayer
   *
   * @param turn Requires if it is players turn or not
   */
  @Override
  public void setTurn(boolean turn) {
    super.setTurn(turn);
    // TODO SEND MESSAGES
  }

  /** Quit from game. Player is sent all relevant information */
  @Override
  public void quit() {
    // TODO SEND QUIT MESSAGE
  }

  /** Add tile to player's rack */
  @Override
  public void addTilesToRack(Collection<Tile> tiles) {
    tiles.forEach(
        tile -> {
          // TODO SEND MESSAGE
        });
  }

  /** @return Returns if this player is host of the game */
  public boolean isHost() {
    return host;
  }
}
