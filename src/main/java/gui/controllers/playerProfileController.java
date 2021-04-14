package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author mnetzer
 * Controller for the playerProfileView
 */

public class playerProfileController {

    @FXML private Label playerNo;
    @FXML private Label playerName;
    @FXML private Label playerTotalPoints;
    @FXML private Label playerCurrentHighscore;
    @FXML private Label playerPlaytime;
    @FXML private Label playerPlayedGames;
    @FXML private Label playerWins;
    @FXML private Label playerLosses;
    @FXML private Label playerScrabblerSince;


    public void InitData(){
        playerNo.setText("1");
        playerName.setText("Enton123");
        playerTotalPoints.setText("350 Points");
        playerCurrentHighscore.setText("350 Points");
        playerPlaytime.setText("0h 20min 10sec");
        playerPlayedGames.setText("1 Game");
        playerWins.setText("1 Win");
        playerLosses.setText("0 Losses");
        playerScrabblerSince.setText("01.04.2021");
    }

    public void backToLogin(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
        Parent playerLobbyView = loader.load();
        playerLobbyController controller = loader.getController();
        controller.InitData();

        //Parent profileControllerView = FXMLLoader.load(getClass().getResource("/views/playerProfileView.fxml"));

        Scene profileControllerScene = new Scene(playerLobbyView);
        Stage window = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        window.setScene(profileControllerScene);
        window.show();
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
    }

    //Placeholder
    public void previousPlayer(MouseEvent mouseEvent) {
        System.out.println("PreviousPlayer");
    }

    //Placeholder
    public void nextPlayer(MouseEvent mouseEvent) {
        System.out.println("NextPlayer");
    }

    //Placeholder
    public void createNewProfile(MouseEvent mouseEvent){
        System.out.println("CreateNewProfile");
    }

}
