import client.PlayerProfile;
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

  @Override
  public void start(Stage stage) throws Exception {

    stage.setTitle("Scrabble 13");
    Parent root = FXMLLoader.load(this.getClass().getResource("/sample.fxml"));
    stage.setScene(new Scene(root, 350, 200));
    stage.show();
  }
}
