import ft.TxtFileHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {
  public static void main(String[] args) {
    launch(args);
    //TxtFileHandler tfh =
        new TxtFileHandler(
            "C:\\Users\\Valentin\\Downloads\\Collins Scrabble Words (2019) with definitions.txt");
  }

  @Override
  public void start(Stage stage) throws Exception {

    stage.setTitle("Scrabble 13");
    Parent root = FXMLLoader.load(this.getClass().getResource("/sample.fxml"));
    stage.setScene(new Scene(root, 350, 200));
    stage.show();
  }
}
