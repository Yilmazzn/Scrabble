package gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/** @author vihofman Controller for joinGame */
public class JoinGameController {

  public void backToPlayerLobby(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playerLobbyView"));
    Parent playerLobbyView = loader.load();
    PlayerLobbyController controller = loader.getController();
    controller.InitData();

    Scene profileControllerScene = new Scene(playerLobbyView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(profileControllerScene);
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
  public void startJoiningGame(MouseEvent mouseEvent) throws IOException{
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(this.getClass().getResource("/views/gameView.fxml"));
      Parent gameView = loader.load();
      GameViewController controller = loader.getController();

      Scene profileControllerScene = new Scene(gameView);
      Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
      window.setScene(profileControllerScene);
      window.show();
    }
}
