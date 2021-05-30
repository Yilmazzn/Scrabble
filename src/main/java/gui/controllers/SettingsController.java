package gui.controllers;

import client.Client;
import ft.Sound;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** @author vihofman Controller for the Settings */
public class SettingsController implements Initializable {

  @FXML private CheckBox soundOn;
  @FXML private CheckBox soundOff;
  private Client client;

  /**
   * Sets client in SettingsController
   *
   * @param client Requires client to be set
   */
  public void setModel(Client client) {
    this.client = client;
  }
  // settings logic with setter methods

  /** Enable the gameSound within the game */
  public void enableSound() {
    Sound.unMute();
    soundOff.setSelected(false);
    Sound.playMusic(Sound.tileSet);
  }

  /** Disable the gameSound within the game */
  public void disableSound() {
    soundOn.setSelected(false);
    Sound.mute();
  }

  // Getter methods for settings
  public boolean getSound() {
    return soundOn.isSelected();
  }

  public void exitGame() throws IOException {

    FXMLLoader loader = new FXMLLoader();
    Sound.playMusic(Sound.tileSet);
    loader.setLocation(this.getClass().getResource("/views/exitGame.fxml"));
    Sound.playMusic(Sound.tileSet);
    Parent exitGameView = loader.load();
    Scene exitGameScene = new Scene(exitGameView);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Exit Game");
    window.setScene(exitGameScene);
    window.setWidth(300);
    window.setHeight(200);
    window.showAndWait();
  }

  public void backToPlayerLobby(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    Sound.playMusic(Sound.tileSet);
    loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
    Parent playerLobbyView = loader.load();
    PlayerLobbyController controller = loader.getController();
    controller.setModel(client);

    Scene playerLobbyScene = new Scene(playerLobbyView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(playerLobbyScene);
    window.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    soundOn.setSelected(!Sound.muteStatus);
    soundOff.setSelected(Sound.muteStatus);
  }
}
