package gui.controllers;

import client.Client;
import client.PlayerProfile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateGameController {
  /** @author vihofman for gameSettings and functionality */
  // setup for joined players
  @FXML private Label PlayerTwo;
  @FXML private Label PlayerThree;
  @FXML private Label PlayerFour;
  @FXML private Label ReadyTwo;
  @FXML private Label ReadyThree;
  @FXML private Label ReadyFour;

  private Label connectionDetails;

  private Client client;
  List<PlayerProfile> profiles = new ArrayList<>(); // manage profiles with arrayList

  private String[] values = {
    "1", "3", "3", "2", "1", "4", "2", "4", "1", "8", "5", "1", "3", "1", "1", "3", "10", "1", "1",
    "1", "1", "4", "4", "8", "4", "10"
  }; // array storing values of letters
  private String[] distributions = {
    "9", "2", "2", "4", "12", "2", "3", "2", "9", "1", "1", "4", "2", "6", "8", "2", "1", "6", "4",
    "6", "4", "2", "2", "1", "2", "1", "2"
  }; // array storing distribution of letters
  private String dictionaryPath;

  public void setModel(Client client) {
    this.client = client;

    client.getNetClient().createServer();
    client.getNetClient().connect();

    // TODO client.getNetClient().setGameLobbyController(this);
    // TODO
    // this.connectionDetails.setText(client.getNetClient().getServer().getConnectionDetails());
  }

  public void addPlayer(PlayerProfile profile) {
    profiles.add(profile);
  }

  public void removePlayer(PlayerProfile profile) {
    profiles.remove(profile);
  }

  public void updatePlayerList() {

    Label[] areas = {PlayerTwo, PlayerThree, PlayerFour};

    for (int i = 1; i < profiles.size(); i++) {
      areas[i].setText(profiles.get(i).getName());
    }
    for (int i = profiles.size(); i < 4; i++) {
      areas[i].setText("");
    }
  }

  public void setDictionaryPath(String path) {
    dictionaryPath = path;
  }

  public void setValues(String[] values) {
    this.values = values;
  }

  public String[] getValues() {
    return values;
  }

  public void setDistributions(String[] distributions) {
    this.distributions = distributions;
  }

  public String[] getDistributions() {
    return distributions;
  }

  public void openGameSettings(MouseEvent mouseEvent) throws IOException {

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameSettings.fxml"));
    Parent gameSettings = loader.load();
    gameSettingsController controller = loader.getController();

    controller.setModel(client, this);

    Scene gameSettingsScene = new Scene(gameSettings);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(gameSettingsScene);
    window.show();
  }
  public void openChat() throws IOException{
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameChat.fxml"));
    Parent gameChat = loader.load();
    Scene gameChatScene = new Scene(gameChat);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Chat");
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
  public void checkReadiness() throws IOException{
    //isReady();
      //isAgree();
      }
  public void addAiPlayer() throws IOException{ // add AI player to the GUI
    Label[] joinedPlayers = {PlayerTwo, PlayerThree, PlayerFour};
    for(int i = 0; i<joinedPlayers.length; i++){
      if (joinedPlayers[i].getText().equals("")) {
        joinedPlayers[i].setText("AI Player");
      }
    }
  }
  /** @author mnetzer Controller for more createGame methods */
  public void backToPlayScrabble(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playScrabbleView.fxml"));
    Parent playScrabbleView = loader.load();
    PlayScrabbleController controller = loader.getController();

    Scene playScrabbleScene = new Scene(playScrabbleView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(playScrabbleScene);
    window.show();
  }

  public void exitGame() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/exitGame.fxml"));
    Parent exitGameView = loader.load();
    // exitGameController controller = loader.getController();

    Scene exitGameScene = new Scene(exitGameView);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Exit Game");
    window.setScene(exitGameScene);
    window.setWidth(300);
    window.setHeight(200);
    window.showAndWait();
  }

  public void gameView(MouseEvent mouseEvent) throws IOException {
    System.out.println("createGame");
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameView.fxml"));
    Parent gameView = loader.load();
    GameViewController controller = loader.getController();
    controller.initBoard();

    Scene welcomeScene = new Scene(gameView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(welcomeScene);
    window.show();
  }
}
