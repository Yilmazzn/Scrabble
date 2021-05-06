package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/** @author vihofman Controller for the Game Settings */

public class gameSettingsController {

    @FXML
    private TextField valueA;

  public void valueIncreaseA(ActionEvent ae) {
    Node source = (Node) ae.getSource();
    Integer colIndex = GridPane.getColumnIndex(source);
    Integer rowIndex = GridPane.getRowIndex(source);

    // List<TextField> tfList = new ArrayList<TextField>();
    // for(Node node : anchorPane.getChildren()){

  }

    public void valueDecreaseA(ActionEvent ae){

    }
    public void distributionIncreaseA(ActionEvent ae){

    }
    public void distributionDecreaseA(ActionEvent ae){

    }
    public void exitGame() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/views/exitGame.fxml"));
        Parent exitGameView = loader.load();
        Scene exitGameScene = new Scene(exitGameView);
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Exit Game");
        window.setScene(exitGameScene);
        window.setWidth(300);
        window.setHeight(200);
        window.showAndWait();
    }

    public void backToCreateGame(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/views/createGame.fxml"));
        Parent createGame = loader.load();
        CreateGameController controller = loader.getController();
        Scene playerLobbyScene = new Scene(createGame);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(playerLobbyScene);
        window.show();
    }

}
