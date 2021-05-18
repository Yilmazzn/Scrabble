package client;

import ft.XmlHandler;
import gui.controllers.WelcomeViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.client.NetClient;

import java.util.List;

/**
 * @author yuzun
 *     <p>Main class launches JavaFX window and manages navigation between scenes holds
 *     application-scope data
 */
public class Client extends Application {

  private List<PlayerProfile> playerProfiles; // List of player profiles
  private PlayerProfile selectedProfile;
  private NetClient netClient;

  public static void main(String[] args) {
    launch(args);
  }

  public List<PlayerProfile> getPlayerProfiles() {
    return playerProfiles;
  }

  public void savePlayerProfiles() {
    XmlHandler.saveXML(playerProfiles);
  }

  public void setSelectedProfile(PlayerProfile profile) {
    this.selectedProfile = profile;
  }

  public PlayerProfile getSelectedProfile() {
    return selectedProfile;
  }

  public NetClient getNetClient() {
    if(netClient == null){
      netClient = new NetClient(this);
    }
    return netClient;
  }

  /** @author mnetzer open window with first fxml: start game */
  @Override
  public void start(Stage primaryStage) throws Exception {
    // load profiles
    this.playerProfiles = XmlHandler.loadProfiles();
    if (playerProfiles.size() > 0) {
      selectedProfile = playerProfiles.get(0);
    }


    primaryStage.setTitle("Scrabble 13");
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/welcomeView.fxml"));
    Parent root = loader.load();
    // Parent root = FXMLLoader.load(this.getClass().getResource("/views/playerLobbyView.fxml"));
    WelcomeViewController controller = loader.getController();
    controller.setModel(this);

    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      public void handle(WindowEvent event){
        Platform.exit();
        if(netClient != null){
          netClient.disconnect();
        }
      }
    });

    primaryStage.setScene(new Scene(root));
    primaryStage.setMinHeight(700);
    primaryStage.setMinWidth(1000);
    primaryStage.show();
  }
}
