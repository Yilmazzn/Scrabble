package gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** @author mnetzer Controller for the exitGameView */
public class ExitGameController {

  public void exitGame(MouseEvent mouseEvent) {
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    System.out.println("You've successfully logged out!");
    window.close();
    Platform.exit();
    System.exit(0);
  }

  public void cancelExit(MouseEvent mouseEvent) {
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.close();
  }
}