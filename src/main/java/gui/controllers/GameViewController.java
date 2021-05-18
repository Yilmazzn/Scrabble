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
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import java.util.List;
import java.util.Map;

/** @author mnetzer Controller for the GameView */
public class GameViewController {

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

  private Client client;

  private LocalPlayer player;
  private Scoreboard scoreboard;

  private String playerName = "";


  //TODO Get instance of Board/ PlayerProfiles/ Bag from NetClient

  /** Method to set the client and initialize the board */
  public void setModel(Client client) {
    this.client = client;
    initBoard();
  }

  /** Method to initialize the board with instances of PlayerProfiles/ Board */
  public void updateBoard() {
    /** AnchorPane as graphical container for the tiles are created*/
    board.getChildren().removeAll();
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        AnchorPane anchorPane = createTile(playBoard.getField(j, i));
        board.add(anchorPane, i, j);

        if (i == 7 && j == 7) {
          StackPane stackPane = new StackPane();
          Image image = new Image(getClass().getResource("/pictures/Star.png").toExternalForm());
          ImageView view = new ImageView(image);
          view.setFitHeight(25.0);
          view.setFitWidth(25.0);

          stackPane.getChildren().add(view);
          board.add(stackPane, 7, 7);
        }
      }
    }
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
  public void openStatistics() throws IOException{
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/statistics.fxml"));
    Parent openStatistics = loader.load();
    Scene openStatisticsScene = new Scene(openStatistics);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle(player);
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
    playerName = "Player One";
    openStatistics();
  }

  public void playerTwo() throws IOException{
    playerName = "Player Two";
    openStatistics();
  }

  public void playerThree() throws IOException{
    playerName = "Player Three";
    openStatistics();
  }

  public void playerFour() throws IOException{
    playerName = "PLayer Four";
    openStatistics();
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
            System.out.println("onDragDone");
            label.setText("");
            points.setText("");
            event.consume();
          }
        });

    pane.setOnMouseClicked(
            new EventHandler<MouseEvent>() {
              public void handle(MouseEvent event) {
                System.out.println("ClickedOnTile");
                switch (position){
                  case 0:
                    if (rack1){
                    rack1 = false;
                    } else {
                      rack1 = true;
                    }
                    break;
                  case 1:
                    if (rack2){
                      rack2 = false;
                    } else {
                      rack2 = true;
                    }
                  break;
                  case 2:
                    if (rack3){
                      rack3 = false;
                    } else {
                      rack3 = true;
                    }
                    break;
                  case 3:
                    if (rack4){
                      rack4 = false;
                    } else {
                      rack4 = true;
                    }
                    break;
                  case 4:
                    if (rack5){
                      rack5 = false;
                    } else {
                      rack5 = true;
                    }
                    break;
                  case 5:
                    if (rack6){
                      rack6 = false;
                    } else {
                      rack6 = true;
                    }
                    break;
                  case 6:
                    if (rack7){
                      rack7 = false;
                    } else {
                      rack7 = true;
                    }
                    break;
                }
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
    if (!boardField.isEmpty()) {
      label.getStyleClass().add("tileWithLetter");
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
                  (Integer.valueOf(db.getString().charAt(1)) - 48),
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


    return pane;
  }

  /** Method to update the graphical container of a Tile on the board */
  public void updateTile(char letter, int points, int row, int col) {
    if(!player.getBoard().isEmpty(row, col)){
      updateBoard();
      updateRack();

    }else{
      player.getBoard().placeTile(new Tile(letter, points), row, col);
      AnchorPane pane = createTile(player.getBoard().getField(row, col));
      board.add(pane, col, row);
    }

  }

  public void updateBottomTile(char letter, int points, int col){
    AnchorPane pane = createBottomTile(letter, points, col);
    tiles.add(pane, col, 0);

  }

  public void updateRack() {}

  public void updateBoard(char letter, int value, int row, int col) {}

  public void clickOnField(int row, int col) {}
}
