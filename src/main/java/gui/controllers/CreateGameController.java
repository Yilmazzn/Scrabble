package gui.controllers;

import client.Client;
import client.PlayerProfile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
  // setup for joined player
  @FXML private Label PlayerOne;

  @FXML private Label PlayerTwo;
  @FXML private Label PlayerThree;
  @FXML private Label PlayerFour;
  @FXML private Label ReadyTwo;
  @FXML private Label ReadyThree;
  @FXML private Label ReadyFour;
  @FXML private Label Host;

  @FXML private Label connectionDetails;

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

  /**
   * Sets client instance in CreateGameController, creates Server and connect first client
   *
   * @param client Requires client to be set
   */
  public void setModel(Client client) {
    this.client = client;

    client.getNetClient().createServer();
    try {
      Thread.sleep(10);
    } catch (Exception e) {
    }

    client.getNetClient().connect();

    client.getNetClient().setCreateGameController(this);
    connectionDetails.setText(client.getNetClient().getIp());
  }

  /**
   * Adds player to profiles list
   *
   * @param profile Requires PlayerProfile to be added
   */
  public void addPlayer(PlayerProfile profile) {
    profiles.add(profile);
  }

  /**
   * Removes player from profiles list
   *
   * @param profile Requires PlayerProfile to be removed
   */
  public void removePlayer(PlayerProfile profile) {
    profiles.remove(profile);
  }

  /** Updates player list, if client connects or disconnects */
  public void updatePlayerList() {

    Label[] areas = {PlayerOne, PlayerTwo, PlayerThree, PlayerFour};

    for (int i = 1; i < profiles.size(); i++) {
      areas[i].setText(profiles.get(i).getName());
    }
    for (int i = profiles.size(); i < 4; i++) {
      areas[i].setText("");
    }
  }

  /**
   * Sets absolute path to dictionary
   *
   * @param path Requires path for dictionary
   */
  public void setDictionaryPath(String path) {
    dictionaryPath = path;
  }

  /**
   * Sets tile values
   *
   * @param values Requires values to be set
   */
  public void setValues(String[] values) {
    this.values = values;
  }

  /** @return Returns tile Values */
  public String[] getValues() {
    return values;
  }

  /**
   * Sets tile distributions
   *
   * @param distributions Requires tile distributions
   */
  public void setDistributions(String[] distributions) {
    this.distributions = distributions;
  }

  /** @return Returns tile distributions */
  public String[] getDistributions() {
    return distributions;
  }

  /**
   * Fills lobby with current client' profiles
   *
   * @param profiles Requires profiles to be set
   */
  public void fillLobby(PlayerProfile[] profiles) {
    Label[] areas = {PlayerOne, PlayerTwo, PlayerThree, PlayerFour};

    for (int i = 0; i < 4; i++) {
      areas[i].setText((i < profiles.length) ? profiles[i].getName() : "");
    }
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

  public void openChat() throws IOException {
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

  public void checkReadiness() throws IOException {
    // if(profile.getName().isReady() && profile.getName().isAgree()){
    ReadyTwo.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
    // else
    ReadyTwo.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
    // TODO get connection from players with isReady and isAgree
    ReadyThree.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
    ReadyFour.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
  }

  /**
   * Shows Popup, for AI difficulty, then adds AI Player
   *
   * @throws IOException
   */
  public void addAiPlayer() throws IOException { // add AI player to the GUI
    Alert alert =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            "Add AI\n\nYes = Simple\tNo = Hard",
            ButtonType.YES,
            ButtonType.NO,
            ButtonType.CANCEL);
    alert.setTitle("Add AI");
    alert.setHeaderText(null);

    if (alert.getResult() == ButtonType.CANCEL) {
      return;
    } else {
      client.getNetClient().addAIPlayer(alert.getResult() == ButtonType.NO);
    }
  }

  /** @author mnetzer Controller for more createGame methods */
  public void backToPlayScrabble(MouseEvent mouseEvent) throws IOException {
    // disonnect player from server (server shuts down because client was host)
    client.getNetClient().disconnect();

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playScrabbleView.fxml"));
    Parent playScrabbleView = loader.load();
    PlayScrabbleController controller = loader.getController();
    controller.setModel(client);

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
    controller.setModel(client);

    Scene welcomeScene = new Scene(gameView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(welcomeScene);
    window.show();
  }

  /**
   * Sets the state of the start game button to a specific value, if enabled, it can be pressed
   *
   * @param enabled Requires the boolean value for enabled state of start game button
   */
  public void changeStartGameButton(boolean enabled) {
    if (enabled) {
      // TODO enable start game Button
    } else {
      // TODO disable start game Button
    }
  }
}
