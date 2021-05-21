package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class JoinGameController {
  /** @author vihofman for functionality */
  @FXML private TextField ipField;

  private Client client;

  /**
   * Sets client in JoinGameController
   *
   * @param client Requires client to be set
   */
  public void setModel(Client client) {
    this.client = client;
  }

  public void showErrorMessage() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/ipError.fxml"));
    Parent ipError = loader.load();
    Scene ipErrorScene = new Scene(ipError);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("ipError");
    window.setScene(ipErrorScene);
    window.setWidth(210);
    window.setHeight(180);
    window.show();
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double x = bounds.getMinX() + (bounds.getWidth() - window.getWidth()) * 0.5;
    double y = bounds.getMinY() + (bounds.getHeight() - window.getHeight()) * 0.7;
    window.setX(x);
    window.setY(y);
    window.show();
  }

  public void gameView(MouseEvent mouseEvent) {
    if (ipField.getText().matches("[0-9.]+")) {
      client.getNetClient().setIp(ipField.getText().trim());
      client.getNetClient().setJoinGameController(this);
      client.getNetClient().connect();

    } else {
      ipField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
      try {
        showErrorMessage();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void loadGameView() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameViewNew.fxml"));
    Parent gameView = loader.load();
    GameViewController controller = loader.getController();
    client.getNetClient().setGameViewController(controller);
    System.out.println("Game View Controller set");
    controller.setModel(client);

    Scene gameScene = new Scene(gameView);
    Stage window = client.getStage();
    window.setScene(gameScene);
    window.show();
  }

  /** Method to open the exit Screen in a new window
   * @throws IOException
   */
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

  /**
   * @author mnetzer Method to get back to the PlayScrabble Screen
   * @param mouseEvent to detect the current Stage
   * @throws IOException
   */
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
