package gui;

import gui.controllers.playerLobbyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Scrabble 13");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
        Parent root = loader.load();
        //Parent root = FXMLLoader.load(this.getClass().getResource("/views/playerLobbyView.fxml"));
        playerLobbyController controller = loader.getController();
        controller.InitData();

        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(900);
        primaryStage.show();
    }
}
