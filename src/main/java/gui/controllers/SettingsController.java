package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
/** @author vihofman Controller for the Settings */
public class SettingsController {
  //setup for settings
  @FXML
  private CheckBox soundOn;
  @FXML
  private CheckBox soundOff;
  @FXML
  private CheckBox animationsOn;
  @FXML
  private CheckBox animationsOff;
  private Client client;
  public void setModel(Client client){
    this.client = client;
  }
  //settings logic with setter methods
  public void enableSound(){
    soundOff.setSelected(false);
  }
  public void disableSound(){
    soundOn.setSelected(false);
  }
  public void enableAnimations(){
    animationsOff.setSelected(false);
  }
  public void disableAnimations(){
    animationsOn.setSelected(false);
  }

 //Getter methods for settings
  public boolean getSound(){
    return soundOn.isSelected();
  }
  public boolean getAnimations() {
    return animationsOn.isSelected();
  }

  public void exitGame() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/exitGame.fxml"));
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
    loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
    Parent playerLobbyView = loader.load();
    PlayerLobbyController controller = loader.getController();
    //controller.InitData();

    Scene playerLobbyScene = new Scene(playerLobbyView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(playerLobbyScene);
    window.show();
  }

}
