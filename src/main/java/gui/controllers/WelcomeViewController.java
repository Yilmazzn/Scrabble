package gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeViewController {

    public void help(MouseEvent mouseEvent) throws IOException {
        System.out.println("Help");
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

    public void continueToLobby(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
        Parent lobbyView = loader.load();
        PlayerLobbyController controller = loader.getController();
        controller.InitData();

        //Parent profileControllerView = FXMLLoader.load(getClass().getResource("/views/playerProfileView.fxml"));

        Scene lobbyScene = new Scene(lobbyView);
        Stage window = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        window.setScene(lobbyScene);
        window.show();
    }

}
