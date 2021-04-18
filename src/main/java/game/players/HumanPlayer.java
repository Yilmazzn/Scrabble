package game.players;

import game.components.Board;
import game.components.Tile;

import java.util.Collection;

public class HumanPlayer extends Player {

  private final boolean host;
  // private ServerProtocol connection;

  public HumanPlayer(boolean host
      /* , ServerProtocol connection */
      ) {
    super(true);
    this.host = host;
  }

  @Override
  public void addTilesToRack(Collection<Tile> tiles) {
    super.addTilesToRack(tiles);
    // Connection send message TODO
  }

  @Override
  public void removeTilesFromRack(Collection<Tile> tiles) {
    super.removeTilesFromRack(tiles);
    // Connection send message TODO
  }

  @Override
  public void updateBoard(Board board) {}
}
