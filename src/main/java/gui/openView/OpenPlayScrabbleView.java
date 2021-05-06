package gui.openView;

import gui.controllers.PlayScrabbleController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** @author mnetzer Class for opening certain views separately */
public class OpenPlayScrabbleView extends Application {
  public static void main(String[] args) {

    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    primaryStage.setTitle("Scrabble 13");
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playScrabbleView.fxml"));
    Parent root = loader.load();
    // Parent root = FXMLLoader.load(this.getClass().getResource("/views/playerLobbyView.fxml"));
    PlayScrabbleController controller = loader.getController();

    primaryStage.setScene(new Scene(root));
    primaryStage.setMinHeight(700);
    primaryStage.setMinWidth(1000);
    primaryStage.show();
  }
}
