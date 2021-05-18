package gui.controllers;

import client.Client;
import client.PlayerProfile;
import game.Scoreboard;
import game.components.*;
import game.players.LocalPlayer;
import gui.components.Bag;
import gui.components.Rack;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/** @author mnetzer Controller for the GameView */
public class GameViewController implements Initializable {

  @FXML private GridPane board;
  @FXML private GridPane tiles;
  @FXML private Label player1;
  @FXML private Label player2;
  @FXML private Label player3;
  @FXML private Label player4;
  @FXML private Label pointsPlayer1;
  @FXML private Label pointsPlayer2;
  @FXML private Label pointsPlayer3;
  @FXML private Label pointsPlayer4;
  @FXML private Label bag;

  private Client client;

  private LocalPlayer player;
  private Scoreboard scoreboard;

  private int draggedTileIndex = -1;


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Tooltip tip = new Tooltip();
    tip.setText("There are 0 tiles in the bag");
    bag.setTooltip(tip);
  }

  //TODO Get instance of Board/ PlayerProfiles/ Bag from NetClient

  /** Method to set the client and initialize the board */
  public void setModel(Client client) {
    this.client = client;
    this.player = client.initLocalPlayer(this);
    updateBoard();
    updateScoreboard();
    updateRack();
  }

  /** Method to initialize the board with instances of PlayerProfiles/ Board */
  public void updateBoard() {
    /** AnchorPane as graphical container for the tiles are created*/
    board.getChildren().removeAll();
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        AnchorPane anchorPane = createTile(player.getBoard().getField(j, i));
        board.add(anchorPane, i, j);
      }
    }
  }



  public void updateScoreboard(){
    Map<String, Integer> map = player.getScoreboard();
    Label[] playerLabels = {player1, player2, player3, player4};
    Label[] pointsLabels = {pointsPlayer1, pointsPlayer2, pointsPlayer3, pointsPlayer4};
    int idx = 0;
    for(Map.Entry<String, Integer> entry : map.entrySet()){
      playerLabels[idx].setText(entry.getKey());
      pointsLabels[idx].setText(Integer.toString(entry.getValue()));
      idx++;
    }
    for(int i = map.size(); i < 4; i++){
      playerLabels[i].setText("---");
      pointsLabels[i].setText("-");
    }
  }

  public void updateRack() {
    tiles.getChildren().clear();
    for (int i = 0; i < player.getRack().RACK_SIZE; i++) {
      if(player.getRack().isEmpty(i)){
        continue;
      }
      AnchorPane anchorPane = createBottomTile(player.getRack().getTile(i).getLetter(), player.getRack().getTile(i).getScore(), i);
      tiles.add(anchorPane, i, 0);
    }
  }

  public void setBagSize(int bagSize){
    Tooltip tip = bag.getTooltip();
    tip.setText("There are " + bagSize + " tiles in the game bag");
  }

  /** @author vihofman for chat */
  public void openChat() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameChat.fxml"));
    Parent gameChat = loader.load();
    Scene gameChatScene = new Scene(gameChat);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("gameChat");
    window.setScene(gameChatScene);
    window.setWidth(300);
    window.setHeight(500);
    window.show();
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double x = bounds.getMinX() + (bounds.getWidth() - window.getWidth()) * 0.78;
    double y = bounds.getMinY() + (bounds.getHeight() - window.getHeight()) * 0.45;
    window.setX(x);
    window.setY(y);
    window.show();
  }
  /** @author vihofman for statistics */
  public void openStatistics(PlayerProfile profile) throws IOException{
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/statistics.fxml"));
    Parent openStatistics = loader.load();
    StatisticsController controller = (StatisticsController) loader.getController();
    controller.setModel(profile);

    Scene openStatisticsScene = new Scene(openStatistics);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);

    window.setTitle(profile.getName());
    window.setScene(openStatisticsScene);
    window.setWidth(300);
    window.setHeight(500);
    window.show();
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double x = bounds.getMinX() + (bounds.getWidth() - window.getWidth()) * 0.2;
    double y = bounds.getMinY() + (bounds.getHeight() - window.getHeight()) * 0.45;
    window.setX(x);
    window.setY(y);
    window.show();
  }
  /** @author vihofman for statistic functionality*/
  public void playerOne() throws IOException{
    if(player.getProfiles().length >= 0){
      openStatistics(player.getProfiles()[0]);
    }
  }

  public void playerTwo() throws IOException{
    if(player.getProfiles().length >= 2){
      openStatistics(player.getProfiles()[1]);
    }
  }

  public void playerThree() throws IOException{
    if(player.getProfiles().length >= 4){
      openStatistics(player.getProfiles()[2]);
    }
  }

  public void playerFour() throws IOException{
    if(player.getProfiles().length >= 4){
      openStatistics(player.getProfiles()[3]);
    }
  }

  public void backToPlayerLobby(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
    Parent playerLobbyView = loader.load();
    PlayerLobbyController controller = loader.getController();
    controller.setModel(client);

    Scene profileControllerScene = new Scene(playerLobbyView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(profileControllerScene);
    window.show();
  }
  /** @author vihofman for opening settings */
  public void openSettings(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/settings.fxml"));
    Parent settings = loader.load();
    SettingsController controller = loader.getController();

    Scene profileControllerScene = new Scene(settings);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(profileControllerScene);
    window.show();
    //TODO go back to current game after opening settings
  }

  public void tiles() {
    System.out.println("tiles");
  }

  public void shuffle() {
    System.out.println("Shuffle");
    player.getRack().shuffleRack();
    updateRack();
  }

  public void submit() {
    System.out.println("Submit");
  }

  /** Method to create the Containers for the tiles on the Rack
   * includes graphical components and adds the Drag and Drop feature*/
  public AnchorPane createBottomTile(char letter, int value, int position) {
    AnchorPane pane = new AnchorPane();
    Label label = new Label();
    Label points = new Label();

    label.setText("" + letter);

    pane.setTopAnchor(label, 1.0);
    pane.setLeftAnchor(label, 1.0);
    pane.setRightAnchor(label, 1.0);
    pane.setBottomAnchor(label, 1.0);
    label.setAlignment(Pos.CENTER);
    label.getStylesheets().add("/stylesheets/labelstyle.css");
    String help = "rack" + Integer.toString(position + 1);
    if (player.tileSelected(position)){
      label.getStyleClass().add("tileBottomSelected");
    } else {
      label.getStyleClass().add("tileBottom");
    }

    points.setText(Integer.toString(value));
    pane.setTopAnchor(points, 1.0);
    pane.setLeftAnchor(points, 5.0);
    pane.setRightAnchor(points, 1.0);
    pane.setBottomAnchor(points, 1.0);
    points.setAlignment(Pos.BOTTOM_LEFT);
    points.getStylesheets().add("/stylesheets/labelstyle.css");
    points.getStyleClass().add("digit");

    pane.getChildren().add(label);
    pane.getChildren().add(points);

    pane.setOnDragDetected(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {
            String exchange = label.getText() + points.getText();
            Dragboard db = pane.startDragAndDrop(TransferMode.ANY);
            draggedTileIndex = position;
            ClipboardContent content = new ClipboardContent();
            content.putString(exchange);
            db.setContent(content);
            mouseEvent.consume();
          }
        });

    pane.setOnDragDone(
        new EventHandler<DragEvent>() {
          public void handle(DragEvent event) {
            /* the drag-and-drop gesture ended */
            if(event.isDropCompleted()){
              System.out.println("onDragDone");
              draggedTileIndex = -1;
            }

            event.consume();
          }
        });

    pane.setOnMouseClicked(
            new EventHandler<MouseEvent>() {
              public void handle(MouseEvent event) {
                System.out.println("ClickedOnTile");
                player.selectTile(position);
                updateBottomTile(letter, value, position);
                event.consume();
              }
            });

    return pane;
  }

  /** Method to create the Containers for the tiles on the Board
   * includes graphical components and adds the Drag and Drop feature*/
  public AnchorPane createTile(BoardField boardField) {
    AnchorPane pane = new AnchorPane();
    Label label = new Label();
    Label points = new Label();

    if (!boardField.isEmpty()) {
      label.setText(String.valueOf(boardField.getTile().getLetter()));
    } else {
      label.setText("");
    }
    pane.setTopAnchor(label, 1.0);
    pane.setLeftAnchor(label, 1.0);
    pane.setRightAnchor(label, 1.0);
    pane.setBottomAnchor(label, 1.0);
    label.setAlignment(Pos.CENTER);
    label.getStylesheets().add("/stylesheets/labelstyle.css");
    label.getStyleClass().add("tile");

    if (!boardField.isEmpty()) {
      points.setText(Integer.toString(boardField.getTile().getScore()));
    } else {
      points.setText("");
    }
    pane.setTopAnchor(points, 1.0);
    pane.setLeftAnchor(points, 5.0);
    pane.setRightAnchor(points, 1.0);
    pane.setBottomAnchor(points, 1.0);
    points.setAlignment(Pos.BOTTOM_LEFT);
    points.getStylesheets().add("/stylesheets/labelstyle.css");
    points.getStyleClass().add("digit");

    /** @author mnetzer verschiedene Felder erzeugen */
    if (!boardField.isEmpty()) {
      label.getStyleClass().add("tileWithLetter");
    } else {
      if (boardField.getType() == BoardField.FieldType.DWS) {
        label.getStyleClass().add("tileDWS");
      }
      if (boardField.getType() == BoardField.FieldType.TWS) {
        label.getStyleClass().add("tileTWS");
      }
      if (boardField.getType() == BoardField.FieldType.DLS) {
        label.getStyleClass().add("tileDLS");
      }
      if (boardField.getType() == BoardField.FieldType.TLS) {
        label.getStyleClass().add("tileTLS");
      }
      if (boardField.getType() == BoardField.FieldType.STAR) {
        label.getStyleClass().add("tileStar");
      }
    }



    pane.getChildren().add(label);
    pane.getChildren().add(points);

    pane.setOnDragOver(
        new EventHandler<DragEvent>() {
          public void handle(DragEvent event) {
            /* data is dragged over the target */
            System.out.println("onDragOver");

            /* accept it only if it is  not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != pane && event.getDragboard().hasString()) {
              /* allow for both copying and moving, whatever user chooses */
              event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
          }
        });

    pane.setOnDragDropped(
        new EventHandler<DragEvent>() {
          public void handle(DragEvent event) {
            /* data dropped */
            System.out.println("onDragDropped");
            /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
              updateTile(
                  db.getString().charAt(0),
                  Integer.valueOf(db.getString().substring(1)),
                  boardField.getRow(),
                  boardField.getColumn());
              // label.setText(db.getString());
              // label.getStyleClass().add("tileWithLetter");
              success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
          }
        });

    pane.setOnMouseClicked(
            new EventHandler<MouseEvent>() {
              public void handle(MouseEvent event) {
                System.out.println("Tile removed");
                if(!boardField.isEmpty()){
                  player.removePlacement(boardField.getRow(), boardField.getColumn());
                  updateBoard();
                  updateRack();
                }
              }
    });




    return pane;
  }

  /** Method to update the graphical container of a Tile on the board */
  public void updateTile(char letter, int points, int row, int col) {
    if(!player.getBoard().isEmpty(row, col)){

    }else{
      player.placeTile(draggedTileIndex, row, col);
      AnchorPane pane = createTile(player.getBoard().getField(row, col));
      board.add(pane, col, row);
    }
    updateBoard();
    updateRack();
  }

  public void updateBottomTile(char letter, int points, int col){
    AnchorPane pane = createBottomTile(letter, points, col);
    tiles.add(pane, col, 0);

  }



  public void updateBoard(char letter, int value, int row, int col) {}

  public void clickOnField(int row, int col) {}
}
