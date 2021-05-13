package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateGameController {
  /** @author vihofman for gameSettings and functionality */
  //setup for joined players
  @FXML
  private TextArea PlayerTwo;
  @FXML
  private TextArea PlayerThree;
  @FXML
  private TextArea PlayerFour;
  private Client client;

  boolean joined2 = false;// booleans for joined Players
  boolean joined3 = false;
  boolean joined4 = false;

  public void setModel(Client client){
    this.client = client;
  }
  public void setPlayerTwo(){// fill the names of joined players once they have connected to the creating game
    PlayerTwo.setText("playerTwo.getName()");
    joined2 = true;
  }
  public void setPlayerThree(){
    PlayerThree.setText("playerThree.getName()");
    joined3 = true;
  }
  public void setPlayerFour(){
    PlayerFour.setText("playerFour.getName()");
    joined4 = true;
  }
  //delete the players from game creation if they leave
  public void deletePlayerTwo(){
    PlayerTwo.setText("");
    joined2 = false;
  }
  public void deletePlayerThree(){
    PlayerThree.setText("");
    joined2 = false;
  }
  public void deletePlayerFour(){
    PlayerFour.setText("");
    joined2 = false;
  }
  //getter methods for joined players
  public String getPlayerTwo(){
    if(joined2){
      return "playerTwo.getName()";
    }
    else return null;
  }
  public String getPlayerThree(){
    if(joined3){
      return "playerThree.getName()";
    }
    else return null;
  }
  public String getPlayerFour(){
    if(joined4){
      return "playerFour.getName()";
    }
    else return null;
  }
  public void gameSettings(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameSettings.fxml"));
    Parent gameSettings = loader.load();
    gameSettingsController controller = loader.getController();

    Scene gameSettingsScene = new Scene(gameSettings);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(gameSettingsScene);
    window.show();
  }
  /** @author mnetzer Controller for more createGame methods*/
  public void backToPlayScrabble(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playScrabbleView.fxml"));
    Parent playScrabbleView = loader.load();
    PlayScrabbleController controller = loader.getController();

    Scene playScrabbleScene = new Scene(playScrabbleView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(playScrabbleScene);
    window.show();
  }

  public void exitGame() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/exitGame.fxml"));
    Parent exitGameView = loader.load();
    // exitGameController controller = loader.getController();

    Scene exitGameScene = new Scene(exitGameView);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Exit Game");
    window.setScene(exitGameScene);
    window.setWidth(300);
    window.setHeight(200);
    window.showAndWait();
  }

  public void gameView(MouseEvent mouseEvent) throws IOException {
    System.out.println("createGame");
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameView.fxml"));
    Parent gameView = loader.load();
    GameViewController controller = loader.getController();
    controller.initBoard();

    Scene welcomeScene = new Scene(gameView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(welcomeScene);
    window.show();
  }
}
