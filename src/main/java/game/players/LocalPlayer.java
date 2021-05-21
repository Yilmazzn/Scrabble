package game.players;

import client.Client;
import client.PlayerProfile;
import game.OvertimeWatch;
import game.components.Board;
import game.components.BoardField;
import game.components.Tile;
import gui.components.Rack;
import gui.controllers.GameViewController;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for representing player on client side
 *
 * @author yuzun
 */
public class LocalPlayer {

  private final Client client;
  private final GameViewController controller;

  private final PlayerProfile profile;
  private final Board board = new Board();
  private final Rack rack = new Rack();

  private PlayerProfile[] profiles;

  private OvertimeWatch overtimeWatch;

  private final List<BoardField> placements = new LinkedList<>();

  private boolean turn = true;
  private int overtime = 600000;

  /**
   * Sets basic attributes of class, for testing it creates 5 tiles in personal rack
   *
   * @param client Requires Client
   * @param controller Requires GameViewController to interact with GUI
   */
  public LocalPlayer(Client client, GameViewController controller) {
    this.client = client;
    this.controller = controller;
    this.profile = client.getSelectedProfile();
  }

  public boolean isTurn() {
    return turn;
  }

  /**
   * Adds tiles to personal Rack and updates GUI
   *
   * @param tile Tile to add to rack (GUI)
   */
  public void addTilesToRack(Tile tile) {
    rack.add(tile);
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

  /**
   * Place Tile on board when player himself
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
      client.getNetClient().placeTile(rack.getField(position).getTile(), row, col);
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
    if (!turn) {
      return;
    }
    if (placements.contains(board.getField(row, col))) {
      addTilesToRack(board.getTile(row, col));
      client.getNetClient().placeTile(null, row, col);
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

    if (overtimeWatch != null) {
      overtime = overtimeWatch.stopCountdown();
    }

    if (turn) { // if set to true --> start countdown
      overtimeWatch = new OvertimeWatch(controller, overtime);
      overtimeWatch.start();
    }
  }

  public void sendMessage(String message) {
    client.getNetClient().sendChatMessage(message);
  }

  public void setProfiles(PlayerProfile[] profiles) {
    this.profiles = profiles;
  }

  /** @return Returns playerProfiles as an Array */
  public PlayerProfile[] getProfiles() {
    return profiles;
  }

  public PlayerProfile getProfile() {
    return this.profile;
  }

  /** Submits turn */
  public void submit() {
    if (turn) {
      client.getNetClient().submitMove();
    }
  }

  /** Returns placements of this player this turn */
  public List<BoardField> getPlacements() {
    return placements;
  }
}
