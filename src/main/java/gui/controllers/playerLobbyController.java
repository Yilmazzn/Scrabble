package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;

/**
 * @author mnetzer
 * Controller for the playerLobbyView
 */

public class playerLobbyController {

    @FXML private Label username;

    public void InitData(){
        username.setText("Enton123");
    }

    public void playScrabble(MouseEvent mouseEvent) {
        System.out.println("PlayScrabble");
    }

    public void settings(MouseEvent mouseEvent) {
        System.out.println("Settings");
    }

    public void backToLogin(MouseEvent mouseEvent) {
        System.out.println("BackToLogin");
    }

    public void exitGame() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/views/exitGame.fxml"));
        Parent exitGameView = loader.load();
        //exitGameController controller = loader.getController();

        Scene exitGameScene = new Scene(exitGameView);
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Exit Game");
        window.setScene(exitGameScene);
        window.setWidth(300);
        window.setHeight(200);
        window.showAndWait();

       /*Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setWidth(800.0);
        alert.setWidth(1000.0);
        alert.setResizable(true);
        alert.onShownProperty().addListener(e -> {
            Platform.runLater(() -> alert.setResizable(false));
        });
        alert.setTitle("Exit Game");
        alert.setHeaderText("You're about to close the game!");
        alert.setContentText("Are you sure?");

        if(alert.showAndWait().get() == ButtonType.OK){
            Stage window = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            System.out.println("You've successfully logged out!");
            window.close();
        }*/

    }

    public void changeToProfileView(MouseEvent mouseEvent) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/views/playerProfileView.fxml"));
        Parent profileControllerView = loader.load();
        playerProfileController controller = loader.getController();
        controller.InitData();

        //Parent profileControllerView = FXMLLoader.load(getClass().getResource("/views/playerProfileView.fxml"));

        Scene profileControllerScene = new Scene(profileControllerView);
        Stage window = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        window.setScene(profileControllerScene);
        window.show();
    }


}
