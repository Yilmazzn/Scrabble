package gui.controllers;

import game.components.Board;
import game.components.BoardField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/** @author mnetzer Controller for the GameView */
public class GameViewController2 {

  @FXML private Label buchstabe1;
  @FXML private GridPane board;

  public void initBoard(){
    buchstabe1.setText("A");

    Board initBoard = new Board();


    for(int i=0; i<15; i++){
      for(int j=0; j<15; j++){
        AnchorPane anchorPane = createTile(i,j,initBoard.getField(j,i).getType());
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

  public AnchorPane createTile(int col, int row, BoardField.FieldType type){
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
    pane.getChildren().add(label);

    return pane;
  }

  public void clickOnField(int row, int col){

  }
}
