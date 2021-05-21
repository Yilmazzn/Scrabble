package gui.controllers;

import client.Client;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** @author mnetzer Controller for the exitGameView */
public class ExitGameController {
  private Client client;

  /**
   * Sets client instance in ExitGameController
   *
   * @param client Requires client to be set
   */
  public void setModel(Client client) {
    this.client = client;
  }

  /** Closes all windows and ends the game */
  public void exitGame(MouseEvent mouseEvent) {
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    System.out.println("You've successfully logged out!");
    window.close();
    client.getNetClient().disconnect();
    Platform.exit();
    System.exit(0);
  }

  /** Application will be continued only the exit window is closed */
  public void cancelExit(MouseEvent mouseEvent) {
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.close();
  }
}
