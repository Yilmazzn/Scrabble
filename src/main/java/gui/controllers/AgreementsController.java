package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
/** @author vihofman Controller for the pregame Agreement Controller */
public class AgreementsController {
  @FXML private CheckBox dictionaryAgree;
  @FXML private CheckBox dictionaryDisagree;
  @FXML private CheckBox ready;
  @FXML private Label dictionary;
  public static String player = ""; // TODO functionality getting the players, somehow deleted?
  private Client client;

  public void setModel(Client client) {
    this.client = client;
  }
  // functionality for agreement and disagreement
  public void setDictionaryAgreement() { // agree with dictionary
    dictionaryDisagree.setSelected(false);
  }

  public void setDictionaryDisagreement() { // disagree with dictionary
    dictionaryAgree.setSelected(false);
  }
  // Getter methods for Agreement and Ready Status
  public boolean getDictionaryAgreement() {
    return dictionaryAgree.isSelected();
  }

  public boolean getReadyStatus() {
    return ready.isSelected();
  }

  public void openChat() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameChat.fxml"));
    Parent gameChat = loader.load();
    Scene gameChatScene = new Scene(gameChat);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("gameChat");
    window.setScene(gameChatScene);
    window.setWidth(300);
    window.setHeight(500);
    window.show();
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double x = bounds.getMinX() + (bounds.getWidth() - window.getWidth()) * 0.78;
    double y = bounds.getMinY() + (bounds.getHeight() - window.getHeight()) * 0.45;
    window.setX(x);
    window.setY(y);
    window.show();
  }

  public void openStatistics() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/statistics.fxml"));
    Parent openStatistics = loader.load();
    Scene openStatisticsScene = new Scene(openStatistics);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle(player);
    window.setScene(openStatisticsScene);
    window.setWidth(300);
    window.setHeight(500);
    window.show();
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double x = bounds.getMinX() + (bounds.getWidth() - window.getWidth()) * 0.2;
    double y = bounds.getMinY() + (bounds.getHeight() - window.getHeight()) * 0.45;
    window.setX(x);
    window.setY(y);
    window.show();
  }

  public void playerOne() throws IOException {
    player = "Player One"; // here we need names of players
    openStatistics();
  }

  public void playerTwo() throws IOException {
    player = "Player Two";
    openStatistics();
  }

  public void playerThree() throws IOException {
    player = "Player Three";
    openStatistics();
  }

  public void playerFour() throws IOException {
    player = "PLayer Four";
    openStatistics();
  }

  public void openSettings(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/settings.fxml"));
    Parent settings = loader.load();
    SettingsController controller = loader.getController();
    controller.setModel(client);

    Scene settingsControllerScene = new Scene(settings);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(settingsControllerScene);
    window.show();
    // TODO go back to current game after opening settings
  }

  public void backToPlayerLobby(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
    Parent playerLobbyView = loader.load();
    PlayerLobbyController controller = loader.getController();
    controller.setModel(client);

    Scene playerLobbyControllerScene = new Scene(playerLobbyView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(playerLobbyControllerScene);
    window.show();
  }

  public void startGame() throws IOException {
    if (true) { // TODO check if host has clicked create Game
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(this.getClass().getResource("/views/gameViewNew.fxml"));
      Parent playerLobbyView = loader.load();
      GameViewController controller = loader.getController();
      controller.setModel(client);

      Scene gameViewControllerScene = new Scene(playerLobbyView);
      Stage window = (Stage) dictionary.getScene().getWindow();
      window.setScene(gameViewControllerScene);
      window.show();
    }
  }
}
