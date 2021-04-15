import client.PlayerProfile;
import gui.controllers.playerLobbyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedList;

/**
 * @author yuzun
 * <p>
 * Main class
 * launches JavaFX window and manages navigation between scenes
 * holds application-scope data
 */

public class Client extends Application {

    private LinkedList<PlayerProfile> playerProfiles;       // List of player profiles

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @author mnetzer
     * open window with first fxml
     */
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
