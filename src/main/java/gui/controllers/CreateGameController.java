package gui.controllers;

import client.Client;
import client.PlayerProfile;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateGameController {
  /** @author vihofman for gameSettings and functionality */
  // setup for joined player
  @FXML private Label playerOne;

  @FXML private Label playerTwo;
  @FXML private Label playerThree;
  @FXML private Label playerFour;
  @FXML private Label readyTwo;
  @FXML private Label readyThree;
  @FXML private Label readyFour;
  @FXML private Button kickTwo;
  @FXML private Button kickThree;
  @FXML private Button kickFour;
  @FXML private Label host;
  @FXML private TextArea textArea;
  @FXML private ScrollPane scrollPane;
  @FXML private VBox chat;
  @FXML private Button startButton;
  @FXML private AnchorPane gameSettingsPane;
  @FXML private BorderPane createGamePane;

  @FXML private Label connectionDetails;

  private Client client;
  PlayerProfile[] profiles; // manage profiles with arrayList

  private int[] tileValues = {
    1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10
  }; // array storing values of letters
  private int[] tileDistributions = {
    9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 2
  }; // array storing distribution of letters
  private String dictionaryPath;

  /**
   * Sets client instance in CreateGameController, creates Server and connect first client
   *
   * @param client Requires client to be set
   */
  public void setModel(Client client) {
    this.client = client;
    profiles = new PlayerProfile[] {client.getSelectedProfile()};

    client.getNetClient().createServer();
    try {
      Thread.sleep(10);
    } catch (Exception e) {
    }

    client.getNetClient().connect();

    client.getNetClient().setCreateGameController(this);
    connectionDetails.setText(client.getNetClient().getIp());
    updateChat();
    gameSettingsPane.setVisible(false);
    gameSettingsPane.managedProperty().bind(gameSettingsPane.visibleProperty());
    createGamePane.managedProperty().bind(createGamePane.visibleProperty());
  }

  /**
   * Sets absolute path to dictionary
   *
   * @param path Requires path for dictionary
   */
  public void setDictionaryPath(String path) {
    dictionaryPath = path;
  }

  /** */
  public String getDictionaryPath() {
    return dictionaryPath;
  }

  /**
   * Sets tile values
   *
   * @param values Requires values to be set
   */
  public void setValues(int[] values) {
    for(int i = 0; i < values.length; i++){
      this.tileValues[i] = values[i];
    }
    System.out.println("setValues used");
  }

  /** @return Returns tile Values */
  public int[] getValues() {
    return tileValues;
  }

  /**
   * Sets tile distributions
   *
   * @param distributions Requires tile distributions
   */
  public void setDistributions(int[] distributions){
    for(int i=0;i<tileDistributions.length;i++){
      this.tileDistributions[i]=distributions[i];
    }
    System.out.println("setDistributions used");
  }

  /** @return Returns tile distributions */
  public int[] getDistributions() {
    return tileDistributions;
  }


  /**
   * Fills lobby with current client' profiles
   *
   * @param profiles Requires profiles to be set
   */
  public void fillLobby(PlayerProfile[] profiles) {
    this.profiles = profiles;
    Label[] areas = {playerOne, playerTwo, playerThree, playerFour};
    Label[] readyLabels = {readyTwo, readyThree, readyFour};
    Button[] kickButtons = {kickTwo, kickThree, kickFour};

    for (int i = 0; i < 4; i++) {
      areas[i].setText((i < profiles.length) ? profiles[i].getName() : "");
    }

    for (int i = 0; i < 3; i++) {
      readyLabels[i].setVisible(i < profiles.length - 1);
      kickButtons[i].setVisible(i < profiles.length - 1);
    }
  }

  /**
   * Method to get to the Settings
   *
   * @param mouseEvent to detect the current Stage
   */
  public void openGameSettings(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameSettings.fxml"));
    // Parent gameSettings = loader.load();
    gameSettingsPane.getChildren().add(loader.load());
    createGamePane.setVisible(false);
    gameSettingsPane.setVisible(true);
    gameSettingsController controller = loader.getController();

    controller.setModel(client, this);

    /*Scene gameSettingsScene = new Scene(gameSettings);
    Stage window = new Stage();
    window.setScene(gameSettingsScene);
    window.show();*/
  }

  /** Closes the GameSettings */
  public void closeSettings() {
    createGamePane.setVisible(true);
    gameSettingsPane.setVisible(false);
  }

  /** Method to open the Chat Screen */
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
    alert.showAndWait();

    if (alert.getResult() == ButtonType.CANCEL) {
      return;
    } else {
      client.getNetClient().addAIPlayer(alert.getResult() == ButtonType.NO);
    }
  }

  /** Creates a listener for the TextArea so Enter can be pressed */
  public void updateChat() {
    textArea.setOnKeyPressed(
        new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
              textArea.deletePreviousChar();
              sendMessage();
              keyEvent.consume();
            }
          }
        });
  }

  /** Creates a Box/Label when player sends a message Necessary to fill the ChatField */
  public void sendMessage() {
    HBox box = new HBox();
    box.setPrefHeight(Region.USE_COMPUTED_SIZE);
    box.setPrefWidth(Region.USE_COMPUTED_SIZE);
    box.setAlignment(Pos.BOTTOM_RIGHT);

    Text name = new Text(client.getSelectedProfile().getName());
    name.setFont(Font.font("Chalkboard", 14));
    name.setFill(Color.web("#170871"));

    Text message = new Text(textArea.getText());
    message.setFont(Font.font("Chalkboard", 14));
    message.setFill(Color.WHITE);

    TextFlow flowTemp = new TextFlow(name, new Text(System.lineSeparator()), message);

    // Label text = new Label("User \n" + textArea.getText());
    Label text = new Label(null, flowTemp);
    text.setWrapText(true);
    text.setPadding(new Insets(2, 10, 2, 2));
    text.getStylesheets().add("stylesheets/chatstyle.css");
    text.getStyleClass().add("textBubble");

    box.getChildren().add(text);
    chat.setAlignment(Pos.BOTTOM_RIGHT);
    chat.setSpacing(20);
    chat.getChildren().add(box);
    chat.heightProperty().addListener(observer -> scrollPane.setVvalue(1.0));
    client.getNetClient().sendChatMessage(textArea.getText());

    textArea.clear();
  }

  /**
   * Creates a Box/Label when player gets a message Necessary to fill the ChatField
   *
   * @param username,text to fill the Label with relevant data
   */
  public void getMessage(String username, String text) {
    HBox box = new HBox();
    box.setPrefHeight(Region.USE_COMPUTED_SIZE);
    box.setPrefWidth(Region.USE_COMPUTED_SIZE);
    box.setAlignment(Pos.BOTTOM_LEFT);

    Text name = new Text(username);
    name.setFont(Font.font("Chalkboard", 14));
    name.setFill(Color.BLACK);

    Text message = new Text(text);
    message.setFont(Font.font("Chalkboard", 14));
    message.setFill(Color.DARKGREY);

    TextFlow flowTemp = new TextFlow(name, new Text(System.lineSeparator()), message);

    Label label = new Label(null, flowTemp);
    label.setWrapText(true);
    label.setPadding(new Insets(2, 10, 2, 2));
    label.getStylesheets().add("stylesheets/chatstyle.css");
    label.getStyleClass().add("textBubbleFlipped");

    box.getChildren().add(label);
    chat.setSpacing(20);
    chat.getChildren().add(box);
    chat.heightProperty().addListener(observer -> scrollPane.setVvalue(1.0));
  }

  /** Creates a Box/Label to display system messages. Necessary to fill the ChatField */
  public void createSystemMessage(String message) {
    HBox box = new HBox();
    box.setPrefHeight(Region.USE_COMPUTED_SIZE);
    box.setPrefWidth(Region.USE_COMPUTED_SIZE);
    box.setAlignment(Pos.BOTTOM_CENTER);

    Text text = new Text(message);
    text.setFont(Font.font("Chalkboard", 14));
    text.setFill(Color.DARKGREY);
    TextFlow flowTemp = new TextFlow(text);
    flowTemp.setTextAlignment(TextAlignment.CENTER);

    Label label = new Label(null, flowTemp);
    label.setWrapText(true);
    label.setPrefWidth(chat.getPrefWidth() * 0.8);
    label.setPadding(new Insets(1, 2, 1, 2));
    label.getStylesheets().add("stylesheets/chatstyle.css");
    label.getStyleClass().add("textBubbleSystem");

    box.getChildren().add(label);
    chat.setSpacing(20);
    chat.getChildren().add(box);
    chat.heightProperty().addListener(observer -> scrollPane.setVvalue(1.0));
  }
  /** Methods for kicking a particular player */
  public void kickPlayerTwo() {
    client.getNetClient().kickPlayer(1);
  }

  public void kickPlayerThree() {
    client.getNetClient().kickPlayer(2);
    ;
  }

  public void kickPlayerFour() {
    client.getNetClient().kickPlayer(3);
    ;
  }

  /**
   * @author mnetzer Method to get back to the PlayScrabble Screen
   * @param mouseEvent to detect the current Stage
   */
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

  /** Method to open the exit Screen in a new window */
  public void exitGame() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/exitGame.fxml"));
    Parent exitGameView = loader.load();
    ExitGameController controller = loader.getController();
    controller.setModel(client);

    Scene exitGameScene = new Scene(exitGameView);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Exit Game");
    window.setScene(exitGameScene);
    window.setWidth(300);
    window.setHeight(200);
    window.showAndWait();
  }

  /**
   * Method to get to the GameView Screen
   *
   * @param mouseEvent to detect the current Stage
   */
  public void startGameView(MouseEvent mouseEvent) throws IOException {
    System.out.println("createGame");
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameView.fxml"));
    Parent gameView = loader.load();
    GameViewController controller = loader.getController();

    client.getNetClient().startGame();
    controller.setModel(client);
    int[] scores = new int[profiles.length];
    controller.updateScoreboard(profiles, scores);
    controller.loadChatFromHost(chat);

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
    startButton.setDisable(!enabled);
  }

  /** Set player's turns */
  public void updatePlayerReadies(boolean[] readies) {
    boolean allReady = true;
    for (int i = 1; i < readies.length; i++) {
      allReady = allReady && readies[i];
    }
    changeStartGameButton(allReady);

    Label[] readyLabels = {readyTwo, readyThree, readyFour};

    for (int i = 0; i < readies.length - 1; i++) {
      readyLabels[i].setStyle(
          "-fx-border-width: 2px;-fx-border-color: " + (readies[i + 1] ? "green" : "red"));
    }
  }
}
