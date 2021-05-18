package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.server.Server;

import java.io.IOException;

public class JoinGameController {
  /** @author vihofman for functionality */
  @FXML private TextField ipField;

  private Client client;

  public void setModel(Client client) {
    this.client = client;
  }

  public void checkEnterIP() throws IOException { // checking if entered IP is correct
    if (!ipField.getText().equals(Server.getIpAddress())) {
      showErrorMessage();
    } else {

    }
  }

  public void showErrorMessage() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/IPError.fxml"));
    Parent errorMessage = loader.load();

    Scene exitGameScene = new Scene(errorMessage);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Exit Game");
    window.setScene(exitGameScene);
    window.setWidth(200);
    window.setHeight(200);
    window.showAndWait();
  }

  /** @author mnetzer Controller for the joinGameView */
  public void gameView(MouseEvent mouseEvent) throws IOException {
    if (ipField.getText().matches("[0-9.]+")) {
      client.getNetClient().setIp(ipField.getText().trim());
      client.getNetClient().connect();
    } else {
      ipField.setText("ERROR, no valid format");
      return;
      // TODO set Color of textField red frame
    }
    System.out.println("createGame");
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameView.fxml"));
    Parent gameView = loader.load();
    GameViewController controller = loader.getController();
    controller.setModel(client);

    Scene welcomeScene = new Scene(gameView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(welcomeScene);
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

  public void backToPlayScrabble(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playScrabbleView.fxml"));
    Parent playScrabbleView = loader.load();
    PlayScrabbleController controller = loader.getController();
    controller.setModel(client);

    Scene playScrabbleScene = new Scene(playScrabbleView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(playScrabbleScene);
    window.show();
  }
}
