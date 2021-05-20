package game.players;

import client.Client;
import client.PlayerProfile;
import game.OvertimeWatch;
import game.components.Board;
import game.components.BoardField;
import game.components.Tile;
import gui.components.Rack;
import gui.components.RackField;
import gui.controllers.GameViewController;

import java.time.LocalDate;
import java.util.*;

/**
 * Class for representing player on client side
 *
 * @author yuzun
 */
public class LocalPlayer {

  private Client client;
  private GameViewController controller;

  private PlayerProfile profile;
  private Board board = new Board();
  private Rack rack = new Rack();
  private PlayerProfile[] profiles;

  private OvertimeWatch overtimeWatch;

  private List<BoardField> placements = new LinkedList<>();

  private boolean turn = true;

  /**
   * Sets basic attributes of class, for testing it creates 5 tiles in personal rack
   *
   * @param client Requires Client
   * @param controller Requires GameViewController to interact with GUI
   */
  public LocalPlayer(Client client, GameViewController controller) {
    this.client = client;
    this.controller = controller;
    overtimeWatch = new OvertimeWatch(controller);
    this.profile = client.getSelectedProfile();
    rack.add(new Tile('H', 1));
    rack.add(new Tile('E', 2));
    rack.add(new Tile('L', 1));
    rack.add(new Tile('L', 1));
    rack.add(new Tile('O', 4));
  }

  /**
   * Adds tiles to personal Rack
   *
   * @param tiles Requires collection of any kind to add elements from it to personal rack
   */
  public void addTilesToRack(Collection<Tile> tiles) {
    tiles.forEach(
        tile -> {
          rack.add(tile);
        });
    controller.updateRack();
  }


  /** @return Returns personal rack */
  public Rack getRack() {
    return rack;
  }

  /** @return Returns personal score */
 /* public int getScore() {
    return score;
  }
*/
  /**
   * TODO Ehrlich gesagt keine Ahnung was diese Methode macht
   *
   * @param position Requires postion in rack
   */
  public void selectTile(int position) {
    rack.getField(position).setSelected(!rack.isSelected(position));
  }

  /**
   * @param position Requires position in rack
   * @return Returns if a specific tile is selected
   */
  public boolean tileSelected(int position) {
    return rack.getField(position).isSelected();
  }

  /** @return Returns Game Board */
  public Board getBoard() {
    return board;
  }

  /** @return Returns playerProfiles as an Array */
  public PlayerProfile[] getProfiles() {
    return profiles;
  }

  /**
   * Place Tile on board
   *
   * @param position Requires which tile from rack you want to set
   * @param row Requires which row you placed the tile in
   * @param col Requires which column you placed the tile in
   */
  public void placeTile(int position, int row, int col) {
    if (!turn) {
      return;
    }
    if (board.isEmpty(row, col)) {
      placements.add(board.getField(row, col));
      board.placeTile(rack.getField(position).getTile(), row, col);
      rack.remove(position);
    }
  }

  /**
   * Removes tiles from board
   *
   * @param row Requires row, where to remove tile from board from
   * @param col Requires column, where to remove tile from board from
   */
  public void removePlacement(int row, int col) {
    if (placements.contains(board.getField(row, col))) {
      addTilesToRack(Arrays.asList(board.getTile(row, col)));
      board.getField(row, col).setTile(null);
      placements.remove(board.getField(row, col));
    }
  }

  /**
   * Sets turn of own player
   *
   * @param turn Requires new value of turn
   */
  public void setTurn(boolean turn) {
    placements.clear();
    this.turn = turn;

    if(turn){   // if set to true --> start countdown
      overtimeWatch.start();
    }else{      // if set to false --> stop countdown
      overtimeWatch.stopCountdown();
    }
  }

  /** @return Returns, if the LocalPlayer has its own turn */
  public boolean isTurn() {
    return turn;
  }

  public OvertimeWatch getOvertime(){
    return this.overtimeWatch;
  }

  public void sendMessage(String message){
    client.getNetClient().sendChatMessage(message);
  }
  public PlayerProfile getProfile(){
    return this.profile;
  }
}
