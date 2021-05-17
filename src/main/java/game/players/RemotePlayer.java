package game.players;

import client.PlayerProfile;
import game.Scoreboard;
import game.components.Board;
import game.components.Tile;
import net.server.ServerProtocol;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author yuzun
 *     <p>Remote player instance, which is controlled by ServerProtocol and sends messages back
 */
public class RemotePlayer extends Player {

  private final ServerProtocol connection;
  private boolean isReady;

  public RemotePlayer(PlayerProfile profile, ServerProtocol connection) {
    super(profile, true);
    this.connection = connection;
  }

  /**
   *
   * @param connection requires the serverprotocol
   */
  //todo maybe delete later if not used in controllers, actually used for test cases in clientTestClass
  public RemotePlayer(ServerProtocol connection) {
    super(true);
    this.connection = connection;
  }

  /** Sends board to set remote player's to the lastest game board state */
  public void updateBoard(Board board) {
    // TODO SEND BOARD MESSAGE
  }

  /** Sends scoreboard instance after every turn */
  public void sendScoreboard(Scoreboard scoreboard) {
    // TODO SEND SCOREBOARD
  }

  /**
   * Sets ready value for each player
   *
   * @param value Requires value to be set
   */
  public void setIsReady(boolean value) {
    System.out.println("remoteplayer class :"+this.getProfile().getName()+" value:"+value);
    isReady = value;
  }

  /** @return returns the player's readiness */
  public boolean getReady() {
    return this.isReady;
  }

  /**
   *
   * @return returns the serverprotocol connection
   */
  public ServerProtocol getConnection() {
    return connection;
  }

  /**
   *
   * @param profile Requires PlayerProfile
   */
  public void setPlayerProfile(PlayerProfile profile) {
    super.setPlayerProfile(profile);
  }

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
}
