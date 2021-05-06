package gui.openView;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/** @author vihofman View for the Settings */
public class OpenSettings extends Application {
    public static void main (String [] args){

        launch(args);
    }

    @Override

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Scrabble 13");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/views/settings.fxml"));
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
