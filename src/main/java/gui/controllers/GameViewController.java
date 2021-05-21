package gui.controllers;

import client.Client;
import client.PlayerProfile;
import game.Scoreboard;
import game.components.BoardField;
import game.components.Tile;
import game.players.LocalPlayer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** @author mnetzer Controller for the GameView */
public class GameViewController implements Initializable {

  @FXML private GridPane board;
  @FXML private GridPane tiles;
  @FXML private Label player1;
  @FXML private Label player2;
  @FXML private Label player3;
  @FXML private Label player4;
  @FXML private Label pointsPlayer1;
  @FXML private Label pointsPlayer2;
  @FXML private Label pointsPlayer3;
  @FXML private Label pointsPlayer4;
  @FXML private Label bag;
  @FXML private Label turn1;
  @FXML private Label turn2;
  @FXML private Label turn3;
  @FXML private Label turn4;
  @FXML private Label time;
  @FXML private TextArea textArea;
  @FXML private ScrollPane scrollPane;
  @FXML private VBox chat;
  @FXML private Button distribution;
  @FXML private Button values;
  @FXML private Button dictionary;
  @FXML private CheckBox ready;
  @FXML private BorderPane agreements;

  private Client client;
  private LocalPlayer player;
  private int draggedTileIndex = -1;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Tooltip tip = new Tooltip();
    tip.setText("There are 0 tiles in the bag");
    tip.setShowDelay(Duration.seconds(0.1));
    bag.setTooltip(tip);
    agreements.managedProperty().bind(agreements.visibleProperty());
  }

  /**
   * Sets client instance in GameViewController, builds graphics of board, rack, chat and time
   *
   * @param client Requires client to be set
   */
  public void setModel(Client client) {
    this.client = client;
    this.player = client.initLocalPlayer(this);
    client.getNetClient().setGameViewController(this);
    updateBoard();
    updateRack();
    updateChat();
    updateTime(600000L);
    showAgreements(!client.getNetClient().isHost());  // show agreements if not host

    // TEST
    player.setTurn(true);
  }

  /**
   * Update the graphics of the board with instances of PlayerProfiles/Board. Called after each move
   */
  public void updateBoard() {
    /** AnchorPane as graphical container for the tiles are created */
    board.getChildren().removeAll();
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        AnchorPane anchorPane = createTile(player.getBoard().getField(j, i));
        board.add(anchorPane, i, j);
      }
    }
  }

  /** Updates Scoreboard, called from NetClient if changes are made */
  public void updateScoreboard(PlayerProfile[] profiles, int[] scores, int turnIdx) {
    Label[] playerLabels = {player1, player2, player3, player4};
    Label[] pointsLabels = {pointsPlayer1, pointsPlayer2, pointsPlayer3, pointsPlayer4};
    Label[] turnLabels = {turn1, turn2, turn3, turn4};

    System.out.println("Profiles received: " + profiles.length);
    System.out.println("Scores received: " + scores.length);
    for(int i = 0; i < profiles.length; i++){
      playerLabels[i].setText(profiles[i].getName());
      pointsLabels[i].setText(Integer.toString(scores[i]));
      turnLabels[i].setVisible(i == turnIdx);
    }
    for (int i = profiles.length; i < 4; i++) {
      playerLabels[i].setText("---");
      pointsLabels[i].setText("-");
      turnLabels[i].setVisible(false);
    }
  }

  /** Updates Rack, called after each move the LocalPlayer makes */
  public void updateRack() {
    tiles.getChildren().clear();
    for (int i = 0; i < player.getRack().RACK_SIZE; i++) {
      if (player.getRack().isEmpty(i)) {
        continue;
      }
      AnchorPane anchorPane =
          createBottomTile(
              player.getRack().getTile(i).getLetter(), player.getRack().getTile(i).getScore(), i);
      tiles.add(anchorPane, i, 0);
    }
  }

  /** Creates a listener for the TextArea so Enter can be pressed */
  public void updateChat(){
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
            }
    );
  }

  /** Update Remaining Time for the current move */
  public void updateTime(long milliseconds) {
    // TODO get time from Server
    String minutes = String.format("%02d", milliseconds / 1000 / 60);
    String seconds = String.format("%02d", (milliseconds / 1000) % 60);
    time.setText(minutes + ":" + seconds);
    if(milliseconds < 60000){   // less than one minute left
      time.setStyle("-fx-font-family: Chalkboard; -fx-text-fill: red; -fx-font-weight: bold");
    }
  }

  public void showAgreements(boolean show){
    agreements.setVisible(show);
  }

  /**
   * Sets bag size
   *
   * @param bagSize Requires size bag should have
   */
  public void setBagSize(int bagSize) {
    Tooltip tip = bag.getTooltip();
    tip.setText("There are " + bagSize + " tiles in the game bag");
  }

  /** @author vihofman for chat */
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

  /** @author vihofman for statistics */
  public void openStatistics(PlayerProfile profile) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/statistics.fxml"));
    Parent openStatistics = loader.load();
    StatisticsController controller = loader.getController();
    controller.setModel(profile);

    Scene openStatisticsScene = new Scene(openStatistics);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);

    window.setTitle(profile.getName());
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

  /** @author vihofman for statistic functionality */
  public void playerOne() throws IOException {
    openStatistics(player.getProfiles()[0]);
  }

  public void playerTwo() throws IOException {
    if (player.getProfiles().length >= 2) {
      openStatistics(player.getProfiles()[1]);
    }
  }

  public void playerThree() throws IOException {
    if (player.getProfiles().length >= 3) {
      openStatistics(player.getProfiles()[2]);
    }
  }

  public void playerFour() throws IOException {
    if (player.getProfiles().length >= 4) {
      openStatistics(player.getProfiles()[3]);
    }
  }

  /**
   * Returns to player Lobby and disconnects client
   *
   * @param mouseEvent Requires MouseEvent generated on Click
   * @throws IOException
   */
  public void backToPlayerLobby(MouseEvent mouseEvent) throws IOException {

    Alert alert =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            "Are you sure you want to quit a running game?\n\nYour Co-Players would be very disappointed...",
            ButtonType.YES,
            ButtonType.CANCEL);
    alert.setHeaderText(null);
    alert.showAndWait();

    if (alert.getResult() != ButtonType.YES) { //if not yes then leave method
      return;
    }
    client.getNetClient().disconnect();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
    Parent playerLobbyView = loader.load();
    PlayerLobbyController controller = loader.getController();
    controller.setModel(client);

    Scene profileControllerScene = new Scene(playerLobbyView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(profileControllerScene);
    window.show();
  }
  /** @author vihofman for opening settings */
  public void openSettings(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/settings.fxml"));
    Parent settings = loader.load();
    SettingsController controller = loader.getController();

    Scene profileControllerScene = new Scene(settings);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(profileControllerScene);
    window.show();
    // TODO go back to current game after opening settings
  }

  public void tiles() {
    System.out.println("tiles");
  }

  /** Shuffles the tiles on the rack */
  public void shuffle() {
    System.out.println("Shuffle");
    player.getRack().shuffleRack();
    updateRack();
  }

  public void submit() {
    System.out.println("Submit");
    // TEST
    player.setTurn(!player.isTurn());
  }

  /** Creates a Box/Label when player sends a message. Necessary to fill the ChatField */
  public void sendMessage() {
    // falls leer --> nix
    if (textArea.getText().equals("")) {
      return;
    }

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

  /** Creates a Box/Label when player gets a message. Necessary to fill the ChatField */
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

  public void createSystemMessage(){

  }

  /**
   * Method to create the Containers for the tiles on the Rack includes graphical components and
   * adds the Drag and Drop feature
   *
   * @param letter,value,position
   * @return AnchorPane
   */
  public AnchorPane createBottomTile(char letter, int value, int position) {
    AnchorPane pane = new AnchorPane();
    Label label = new Label();
    Label points = new Label();

    label.setText("" + letter);

    AnchorPane.setTopAnchor(label, 1.0);
    AnchorPane.setLeftAnchor(label, 1.0);
    AnchorPane.setRightAnchor(label, 1.0);
    AnchorPane.setBottomAnchor(label, 1.0);
    label.setAlignment(Pos.CENTER);
    label.getStylesheets().add("/stylesheets/labelstyle.css");
    String help = "rack" + (position + 1);
    if (player.tileSelected(position)) {
      label.getStyleClass().add("tileBottomSelected");
    } else {
      label.getStyleClass().add("tileBottom");
    }

    points.setText(Integer.toString(value));
    AnchorPane.setTopAnchor(points, 1.0);
    AnchorPane.setLeftAnchor(points, 5.0);
    AnchorPane.setRightAnchor(points, 1.0);
    AnchorPane.setBottomAnchor(points, 1.0);
    points.setAlignment(Pos.BOTTOM_LEFT);
    points.getStylesheets().add("/stylesheets/labelstyle.css");
    points.getStyleClass().add("digitRack");

    pane.getChildren().add(label);
    pane.getChildren().add(points);

    pane.setOnDragDetected(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {
            String exchange = label.getText() + points.getText();
            Dragboard db = pane.startDragAndDrop(TransferMode.ANY);
            draggedTileIndex = position;
            ClipboardContent content = new ClipboardContent();
            content.putString(exchange);
            db.setContent(content);
            mouseEvent.consume();
          }
        });

    pane.setOnDragDone(
        new EventHandler<DragEvent>() {
          public void handle(DragEvent event) {
            /* the drag-and-drop gesture ended */
            if (event.isDropCompleted()) {
              draggedTileIndex = -1;
            }

            event.consume();
          }
        });

    pane.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          public void handle(MouseEvent event) {
            player.selectTile(position);
            updateBottomTile(letter, value, position);
            event.consume();
          }
        });

    pane.setStyle("-fx-cursor: hand");

    return pane;
  }

  /**
   * Method to create the Containers for the tiles on the Board includes graphical components and
   * adds the Drag and Drop feature
   *
   * @param boardField
   * @return AnchorPane
   */
  public AnchorPane createTile(BoardField boardField) {
    AnchorPane pane = new AnchorPane();
    Label label = new Label();
    Label points = new Label();

    if (!boardField.isEmpty()) {
      label.setText(String.valueOf(boardField.getTile().getLetter()));
    } else {
      label.setText("");
    }
    AnchorPane.setTopAnchor(label, 1.0);
    AnchorPane.setLeftAnchor(label, 1.0);
    AnchorPane.setRightAnchor(label, 1.0);
    AnchorPane.setBottomAnchor(label, 1.0);
    label.setAlignment(Pos.CENTER);
    label.getStylesheets().add("/stylesheets/labelstyle.css");
    label.getStyleClass().add("tile");

    if (!boardField.isEmpty()) {
      points.setText(Integer.toString(boardField.getTile().getScore()));
    } else {
      points.setText("");
    }
    AnchorPane.setTopAnchor(points, 1.0);
    AnchorPane.setLeftAnchor(points, 5.0);
    AnchorPane.setRightAnchor(points, 1.0);
    AnchorPane.setBottomAnchor(points, 1.0);
    points.setAlignment(Pos.BOTTOM_LEFT);
    points.getStylesheets().add("/stylesheets/labelstyle.css");
    points.getStyleClass().add("digitTile");

    /** Assignment of different styles of the field to the labels*/
    if (!boardField.isEmpty()) {
      label.getStyleClass().add("tileWithLetter");
    } else {
      if (boardField.getType() == BoardField.FieldType.DWS) {
        label.getStyleClass().add("tileDWS");
      }
      if (boardField.getType() == BoardField.FieldType.TWS) {
        label.getStyleClass().add("tileTWS");
      }
      if (boardField.getType() == BoardField.FieldType.DLS) {
        label.getStyleClass().add("tileDLS");
      }
      if (boardField.getType() == BoardField.FieldType.TLS) {
        label.getStyleClass().add("tileTLS");
      }
      if (boardField.getType() == BoardField.FieldType.STAR) {
        label.getStyleClass().add("tileStar");
      }
    }

    pane.getChildren().add(label);
    pane.getChildren().add(points);

    pane.setOnDragOver(
        new EventHandler<DragEvent>() {
          public void handle(DragEvent event) {
            /* data is dragged over the target */

            /* accept it only if it is  not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != pane && event.getDragboard().hasString()) {
              /* allow for both copying and moving, whatever user chooses */
              event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
          }
        });

    pane.setOnDragDropped(
        new EventHandler<DragEvent>() {
          public void handle(DragEvent event) {
            /* data dropped */
            /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
              updateTile(
                  db.getString().charAt(0),
                  Integer.valueOf(db.getString().substring(1)),
                  boardField.getRow(),
                  boardField.getColumn());
              // label.setText(db.getString());
              // label.getStyleClass().add("tileWithLetter");
              success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
          }
        });

    pane.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          public void handle(MouseEvent event) {
            if (!boardField.isEmpty()) {
              player.removePlacement(boardField.getRow(), boardField.getColumn());
              updateBoard();
              updateRack();
            }
          }
        });

    // Color border yellow when entered
    pane.setOnDragEntered(
        event -> {
          pane.setBorder(
              new Border(
                  new BorderStroke(
                      Color.YELLOW,
                      BorderStrokeStyle.SOLID,
                      CornerRadii.EMPTY,
                      BorderWidths.DEFAULT)));
        });

    // Remove border
    pane.setOnDragExited(
        event -> {
          pane.setBorder(null);
        });

    return pane;
  }

  /** Method to update the graphical container of a Tile on the board */
  public void updateTile(char letter, int points, int row, int col) {
    if (!player.getBoard().isEmpty(row, col)) {

    } else {
      player.placeTile(draggedTileIndex, row, col);
      AnchorPane pane = createTile(player.getBoard().getField(row, col));
      board.add(pane, col, row);
    }
    updateBoard();
    updateRack();
  }

  /** Method to update the graphical container of a Tile on the rack */
  public void updateBottomTile(char letter, int points, int col) {
    AnchorPane pane = createBottomTile(letter, points, col);
    tiles.add(pane, col, 0);
  }

  /** Method to update the graphical containers of he board after a move */
  public void placeTile(Tile tile, int row, int col){
    System.out.println("Place Tile | GameViewController | " + row + ", " + col);
    player.getBoard().placeTile(tile, row, col);
    updateBoard();
  }


  public void clickOnField(int row, int col) {}

  public void showPopup(String message){
    client.showPopUp(message);
  }

  /** Requests tile distribution from server */
  @FXML
  public void showTileDistribution(){
    client.getNetClient().requestDistributions();
  }

  /** Requests tile distribution from server */
  @FXML
  public void showTileValues(){
    client.getNetClient().requestValues();
  }

  /** Requests tile distribution from server */
  @FXML
  public void showDictionary(){
    client.getNetClient().requestDictionary();
  }

  /** Triggered when checkBox 'I am ready' is selected/unselected */
  @FXML
  public void toggleReadyState(){
    client.getNetClient().setPlayerReady(ready.isSelected());
  }
}
