package gui.controllers;

import game.components.Board;
import game.components.BoardField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import javafx.stage.Stage;

import java.io.IOException;

/** @author mnetzer Controller for the GameView */
public class GameViewController2 {

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

  public void initBoard(){

    player1.setText("Spieler 1");
    player2.setText("Spieler 2");
    player3.setText("Spieler 3");
    player4.setText("Spieler 4");
    pointsPlayer1.setText("1");
    pointsPlayer2.setText("2");
    pointsPlayer3.setText("3");
    pointsPlayer4.setText("4");


    Board initBoard = new Board();

    for(int i=0; i<15; i++){
      for(int j=0; j<15; j++){
        AnchorPane anchorPane = createTile(initBoard.getField(j,i).getType());
        board.add(anchorPane, i, j);

        if (i==7 && j==7){
          StackPane stackPane = new StackPane();
          Image image = new Image(getClass().getResource("/pictures/Star.png").toExternalForm());
          ImageView view = new ImageView(image);
          view.setFitHeight(25.0);
          view.setFitWidth(25.0);

          stackPane.getChildren().add(view);
          board.add(stackPane, 7,7);
        }
      }
    }

    for(int i=0; i<7; i++){
      AnchorPane bottomPane = createBottomTile();
      tiles.add(bottomPane, i,0);
    }


  }

  public void backToPlayerLobby(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
    Parent playerLobbyView = loader.load();
    PlayerLobbyController controller = loader.getController();
    controller.InitData();

    Scene profileControllerScene = new Scene(playerLobbyView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(profileControllerScene);
    window.show();
  }

  public void settings() throws IOException {
    System.out.println("Settings");
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

  public void chat() {
    System.out.println("Chat");
  }

  public void playerOne() {
    System.out.println("Player1");
  }

  public void playerTwo() {
    System.out.println("Player2");
  }

  public void playerThree() {
    System.out.println("Player3");
  }

  public void playerFour() {
    System.out.println("Player4");
  }

  public AnchorPane createBottomTile(){
    AnchorPane pane = new AnchorPane();
    Label label = new Label();

    int randomInt = (int) ((Math.random() * (90-65)) + 65);
    char randomChar = (char) randomInt;
    label.setText("" + randomChar);

    pane.setTopAnchor(label, 1.0);
    pane.setLeftAnchor(label, 1.0);
    pane.setRightAnchor(label, 1.0);
    pane.setBottomAnchor(label, 1.0);
    label.setAlignment(Pos.CENTER);
    label.getStylesheets().add("/stylesheets/labelstyle.css");
    label.getStyleClass().add("tileBottom");

    label.setOnDragDetected(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        Dragboard db = label.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(label.getText());
        db.setContent(content);
        mouseEvent.consume();
      }
    });

    label.setOnDragDone(new EventHandler <DragEvent>() {
      public void handle(DragEvent event) {
        /* the drag-and-drop gesture ended */
        System.out.println("onDragDone");
        label.setText("");
        event.consume();
      }
    });

    pane.getChildren().add(label);

    return pane;
  }

  public AnchorPane createTile(BoardField.FieldType type){
    AnchorPane pane = new AnchorPane();
    Label label = new Label();
    label.setText("");
    pane.setTopAnchor(label, 1.0);
    pane.setLeftAnchor(label, 1.0);
    pane.setRightAnchor(label, 1.0);
    pane.setBottomAnchor(label, 1.0);
    label.setAlignment(Pos.CENTER);
    label.getStylesheets().add("/stylesheets/labelstyle.css");

    label.getStyleClass().add("tile");

    /** @author mnetzer
     * verschiedene Felder erzeugen */
    if (type == BoardField.FieldType.DWS){
      label.getStyleClass().add("tileDWS");
    }
    if (type == BoardField.FieldType.TWS){
      label.getStyleClass().add("tileTWS");
    }
    if (type == BoardField.FieldType.DLS){
      label.getStyleClass().add("tileDLS");
    }
    if (type == BoardField.FieldType.TLS){
      label.getStyleClass().add("tileTLS");
    }
    if (type == BoardField.FieldType.STAR){
      label.getStyleClass().add("tileStar");
    }

    label.setOnDragOver(new EventHandler <DragEvent>() {
      public void handle(DragEvent event) {
        /* data is dragged over the target */
        System.out.println("onDragOver");

        /* accept it only if it is  not dragged from the same node
         * and if it has a string data */
        if (event.getGestureSource() != label &&
                event.getDragboard().hasString()) {
          /* allow for both copying and moving, whatever user chooses */
          event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
      }
    });


    label.setOnDragDropped(new EventHandler <DragEvent>() {
      public void handle(DragEvent event) {
        /* data dropped */
        System.out.println("onDragDropped");
        /* if there is a string data on dragboard, read it and use it */
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
          label.setText(db.getString());
          label.getStyleClass().add("tileWithLetter");
          success = true;
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        event.setDropCompleted(success);

        event.consume();
      }
    });


    pane.getChildren().add(label);

    return pane;
  }

  public void clickOnField(int row, int col){

  }
}
