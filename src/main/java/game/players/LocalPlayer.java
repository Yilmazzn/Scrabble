package game.players;

import client.Client;
import client.PlayerProfile;
import game.OvertimeWatch;
import game.components.Board;
import game.components.BoardField;
import game.components.Tile;
import gui.components.Rack;
import gui.controllers.GameViewController;
import javafx.scene.control.ChoiceDialog;

import java.util.ArrayList;
import java.util.Iterator;
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
  private int bagSize;

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

  /** @return returns true if it is turn of localplayer instance */
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

  /**
   * Toggles isSelected state of one tile of rack
   *
   * @param position Requires position in rack
   */
  public void selectTile(int position) {
    rack.getField(position).setSelected(!rack.isSelected(position));
  }

  public void setBagSize(int bagSize) {
    this.bagSize = bagSize;
  }

  public void exchange() {

    if (!turn) {
      controller.createSystemMessage("Wait for your turn!");
      return;
    }

    // Put placements on board back to rack!
    for (Iterator<BoardField> iterator = placements.iterator(); iterator.hasNext(); ) {
      BoardField field = iterator.next();
      rack.add(field.getTile());
      client.getNetClient().placeTile(null, field.getRow(), field.getColumn());
      iterator.remove();
    }

    ArrayList<Tile> tmp = new ArrayList<>();
    int counter = 0;
    for (int i = 0; i < 7; i++) {
      if (rack.getTile(i) != null && rack.getField(i).isSelected()) {
        counter++;
      }
    }
    if (counter > bagSize) {
      controller.createSystemMessage(
          "You selected "
              + counter
              + (counter == 1 ? " Tile" : " Tiles")
              + ", but only "
              + bagSize
              + (bagSize == 1 ? " Tile is" : " Tiles are")
              + " still in the bag. Please reduce number of selected Tiles");
      return;
    }
    if (bagSize == 0) {
      controller.createSystemMessage("There are no Tiles remaining in the bag.");
      return;
    }
    for (int i = 0; i < 7; i++) {
      if (rack.getTile(i) != null && rack.getField(i).isSelected()) {
        tmp.add(rack.getTile(i));
        rack.remove(i);
      }
    }
    Tile[] tiles = new Tile[tmp.size()];
    tiles = tmp.toArray(tiles);
    client.getNetClient().exchangeTiles(tiles);
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
      controller.createSystemMessage("Wait for your turn!");
      return;
    }
    if (board.isEmpty(row, col)) {
      if (rack.getTile(position).isJoker()) { // Joker -> Change letter
        // BUild CHoice DIalog
        List<String> choices = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
          choices.add(Character.toString(c));
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>("A", choices);
        dialog.setTitle("Joker Tile");
        dialog.setHeaderText(null);
        dialog.setContentText("Choose your letter:");
        String choice = client.showDialog(dialog);
        if (choice == null) { // Cancel --> do nothing
          return;
        }
        rack.getTile(position).setLetter(choice.charAt(0));
      }
      rack.getField(position).setSelected(false);
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
      if (board.getTile(row, col).isJoker()) { // if it was a joker --> back to being a joker
        board.getTile(row, col).setLetter('#');
      }
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

    if (bagSize == 0 && rack.isEmpty()) { //player has empty Rack and no tiles remaining in bag
      client.getNetClient().sendEndMessage(0);
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
