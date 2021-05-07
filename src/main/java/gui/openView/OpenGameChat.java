package gui.openView;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** @author vihofman Class for opening certain views separately */
public class OpenGameChat extends Application {
    public static void main (String [] args){

        launch(args);
    }

    @Override

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Scrabble 13");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/views/gameChat.fxml"));
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}