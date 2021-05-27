package gui.controllers;

import client.Client;
import ft.Sound;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/** @author mnetzer Controller for the WelcomeView */
public class WelcomeViewController {

  private Client client;

  /**
   * Sets client in JoinGameController
   *
   * @param client Requires client to be set
   */
  public void setModel(Client client){
    this.client = client;
  }

  /**
   * Start GameView without server
   * //TODO Help View or delete
   *
   * @param mouseEvent to detect the current Stage
   * @throws IOException
   */
  public void help(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    Sound.playMusic(Sound.tileSet);
    loader.setLocation(this.getClass().getResource("/views/gameView.fxml"));
    Parent root = loader.load();
    // Parent root = FXMLLoader.load(this.getClass().getResource("/views/playerLobbyView.fxml"));
    GameViewController controller = loader.getController();
    controller.setModel(client);
    Scene gameView = new Scene(root);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(gameView);
    window.show();
  }

  /**
   * Method to open the exit Screen in a new window
   *
   * @throws IOException
   */
  public void exitGame() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    Sound.playMusic(Sound.tileSet);
    loader.setLocation(this.getClass().getResource("/views/exitGame.fxml"));
    Parent exitGameView = loader.load();
    ExitGameController controller = loader.getController();
    controller.setModel(client);

    Scene exitGameScene = new Scene(exitGameView);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Exit Game");
    window.setScene(exitGameScene);
    window.setWidth(300);
    window.setHeight(200);
    window.showAndWait();
  }

  /**
   * Method to get to the playerLobby Screen
   *
   * @param mouseEvent to detect the current Stage
   * @throws IOException
   */
  public void continueToLobby(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    Sound.playMusic(Sound.tileSet);
    loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
    Parent lobbyView = loader.load();
    PlayerLobbyController controller = loader.getController();
    controller.setModel(client);

    Scene lobbyScene = new Scene(lobbyView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(lobbyScene);
    window.show();
  }
}
