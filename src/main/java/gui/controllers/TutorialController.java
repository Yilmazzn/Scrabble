package gui.controllers;

import client.Client;
import client.PlayerProfile;
import game.Dictionary;
import game.components.Board;
import game.components.BoardException;
import game.components.BoardField;
import game.components.Tile;
import game.players.LocalPlayer;
import gui.components.Rack;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** @author vihofman Controller for the tutorial controller */
public class TutorialController {
  /** instructions shows the tutorial tips */
  @FXML private Label instructions;
  /** stepOverview shows the progress of tutorial */
  @FXML private Label stepOverview;
  /** tutorialWelcome is welcoming screen */
  @FXML private VBox tutorialWelcome;
  /** counter indicates the current step of the tutorial */
  @FXML private GridPane board;

  @FXML private Label player1;
  @FXML private Label player2;
  @FXML private Label pointsPlayer1;
  @FXML private Label pointsPlayer2;
  @FXML private ImageView image1;
  @FXML private ImageView image2;
  @FXML private GridPane tiles;

  private Client client;
  private int counter = 0;
  private int draggedTileIndex = -1;
  private Rack rack;
  private List<BoardField> placements = new ArrayList<>();
  private Dictionary dictionary = new Dictionary();
  private Board gameBoard;

  private boolean stageOneUnlocked = true,
      stageTwoUnlocked,
      stageThreeUnlocked,
      stageFourUnlocked,
      stageFiveUnlocked;

  private final Image defaultImage =
      new Image(getClass().getResourceAsStream("/pictures/ProfileIcon.png"));

  /**
   * This array consists of all the advice given by the tutorial. The content of the indices will be
   * printed out onto the instructions label.
   */
  private String[] steps = {
    "Place the tiles from your rack onto the board so they form a word."
        + "\n"
        + "You can place tiles either vertically or horizontally, always joining the cluster of tiles already placed."
        + "\n"
        + "When a correct word has been placed, you score points based on the letter value."
        + "\n"
        + "Additionally, the board colors increase your score."
        + "\n"
        + "Light Blue: doubles letter value"
        + "\n"
        + "Dark Blue: triples letter value"
        + "\n"
        + "Pink: doubles the word value"
        + "\n"
        + "Red: triples the word value"
        + "\n"
        + "Words covering the center are doubled in value.",
    "If a blank tile (joker) is played the player has to define for what letter the blank tile stands for and it remains "
        + "\n"
        + "to stand for this letter throughout the game. The blank tile scores zero points.",
    "Start by placing your first word over the star icon in the middle."
        + "\n"
        + "To make a move, click and drag & drop the tile from your rack onto the board."
        + "\n"
        + "To undo a placement, simply click onto the tile you want to put back to your rack.",
    "If you need some creativity boost, click the Shuffle-Button to rearrange the order of tiles in your rack.",
    "Once you have placed your word, click submit."
        + "\n"
        + "Within the chat you will see if your word was valid."
        + "\n"
        + "If it was not, please reconsider your move by clicking onto the tiles you want to remove."
        + "\n"
        + "If you want to exchange your tiles randomly with the once left in the bag, click the Exchange-Button and your turn ends."
        + "\n"
        + "If you can not find a valid word, remove the tiles you have placed within this round and click the Submit-Button to pass your turn."
        + "\n"
        + "Now wait for the Bot's turn.",
    "The green circle next to the Players on the left hand side indicates whose turn it is currently."
        + "\n"
        + "In the chat you can see what the other players have done during their turn."
        + "\n"
        + "The timer in the top left corner indicates the amount of time left for you to finish you turn."
        + "\n"
        + "Every turn can last up to 10 minutes, if exceeded the game ends."
        + "\n"
        + "The game finishes as well, if all the tiles from a players rack and the bag have been removed or "
        + "\n"
        + "within 6 rounds nothing has been placed and one of the players decides to leave."
        + "\n"
  };

  /**
   * Sets client in TutorialController
   *
   * @param client Requires client to be set
   */
  public void setModel(Client client) {
    this.client = client;
    gameBoard = new Board();
    rack = new Rack();
    showTutorialWelcome(true);
  }

  /** Learns how to place tiles */
  public void loadStageOne() {
    // Give player tiles "HELLO"
    rack.add(new Tile('H', 2));
    rack.add(new Tile('L', 3));
    rack.add(new Tile('E', 1));
    rack.add(new Tile('L', 3));
    rack.add(new Tile('O', 4));
    // Prompt him to place HELLO
    instructions.setText(steps[0]);

    updateBoard();
    updateRack();
  }

  public void loadStageTwo() {
    if (!stageTwoUnlocked) {
      // popup error
      return;
    }
    counter++;
    updateScoreboard(1, 10); // 1 für player 2 für bot

    for (int i = 0; i < rack.RACK_SIZE; i++) {
      rack.remove(i);
    }
    rack.add(new Tile('R', 2));
    rack.add(new Tile('F', 3));
    rack.add(new Tile('#', 1));
    rack.add(new Tile('I', 3));
    rack.add(new Tile('E', 4));
    rack.add(new Tile('D', 2));
    rack.add(new Tile('N', 3));
    updateRack();

    gameBoard.placeTile(new Tile('D', 1), 1, 1);
    updateBoard();

    instructions.setText(steps[1]);
  }

  public void loadStageThree() {}

  /**
   * Update the graphics of the board with instances of PlayerProfiles/Board. Called after each move
   */
  public void updateBoard() {
    /** AnchorPane as graphical container for the tiles are created */
    board.getChildren().removeAll();
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        AnchorPane anchorPane = createTile(gameBoard.getField(j, i));
        board.add(anchorPane, i, j);
      }
    }
  }

  /** Updates Scoreboard, called from NetClient if changes are made */
  public void updateScoreboard(int idx, int score) {
    if (idx == 1) {
      pointsPlayer1.setText(Integer.toString(score));
    } else {
      pointsPlayer2.setText(Integer.toString(score));
    }
  }

  /** Updates Rack, called after each move the LocalPlayer makes */
  public void updateRack() {
    tiles.getChildren().clear();
    for (int i = 0; i < Rack.RACK_SIZE; i++) {
      if (rack.isEmpty(i)) {
        continue;
      }
      AnchorPane anchorPane =
          createBottomTile(rack.getTile(i).getLetter(), rack.getTile(i).getScore(), i);
      tiles.add(anchorPane, i, 0);
    }
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

    // Tooltip if Joker
    if (letter == '#') {
      Tooltip tip = new Tooltip("This is a joker tile");
      tip.setShowDelay(Duration.millis(1500));
      tip.setShowDuration(Duration.millis(3000));
    }

    AnchorPane.setTopAnchor(label, 1.0);
    AnchorPane.setLeftAnchor(label, 1.0);
    AnchorPane.setRightAnchor(label, 1.0);
    AnchorPane.setBottomAnchor(label, 1.0);
    label.setAlignment(Pos.CENTER);
    label.getStylesheets().add("/stylesheets/labelstyle.css");
    String help = "rack" + (position + 1);
    if (rack.getField(position).isSelected()) {
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
            rack.getField(position).setSelected(!rack.isSelected(position));
            updateBottomTile(letter, value, position);
            event.consume();
            System.out.println("here in mouseclick");
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

    /** Assignment of different styles of the field to the labels */
    if (!boardField.isEmpty()) {
      label.getStyleClass().add("tileWithLetter");
      if (placements.contains(
          boardField)) { // tile is in placements of player this turn --> change border color
        label.setBorder(
            new Border(
                new BorderStroke(
                    Color.YELLOW,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT)));
      }
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
              int row = boardField.getRow();
              int col = boardField.getColumn();
              if (placements.contains(gameBoard.getField(row, col))) {
                if (gameBoard
                    .getTile(row, col)
                    .isJoker()) { // if it was a joker --> back to being a joker
                  gameBoard.getTile(row, col).setLetter('#');
                }
                addTilesToRack(gameBoard.getTile(row, col));
                gameBoard.getField(row, col).setTile(null);
                placements.remove(gameBoard.getField(row, col));
              }
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

  public void addTilesToRack(Tile tile) {
    rack.add(tile);
    updateRack();
  }

  /** Method to update the graphical container of a Tile on the board */
  public void updateTile(char letter, int points, int row, int col) {
    if (!gameBoard.isEmpty(row, col)) {

    } else {
      placeTile(draggedTileIndex, row, col);
      AnchorPane pane = createTile(gameBoard.getField(row, col));
      board.add(pane, col, row);
    }
    updateBoard();
    updateRack();
  }

  /**
   * Place Tile on board when player himself
   *
   * @param position Requires which tile from rack you want to set
   * @param row Requires which row you placed the tile in
   * @param col Requires which column you placed the tile in
   */
  public void placeTile(int position, int row, int col) {
    if (gameBoard.isEmpty(row, col)) {
      if (rack.getTile(position).isJoker()) { // Joker -> Change letter
        // BUild CHoice DIalog
        List<String> choices = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
          choices.add(Character.toString(c));
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>("A", choices);
        dialog.setTitle("Joker Tile");
        dialog.setHeaderText(null);
        dialog.setContentText("Choose your letter:");
        String choice = client.showDialog(dialog);
        if (choice == null) { // Cancel --> do nothing
          return;
        }
        rack.getTile(position).setLetter(choice.charAt(0));
      }
      rack.getField(position).setSelected(false);
      placements.add(gameBoard.getField(row, col));
      gameBoard.placeTile(rack.getField(position).getTile(), row, col);
      rack.remove(position);
    }
  }

  /** Method to update the graphical container of a Tile on the rack */
  public void updateBottomTile(char letter, int points, int col) {
    AnchorPane pane = createBottomTile(letter, points, col);
    tiles.add(pane, col, 0);
  }

  /** Method to update the graphical containers of he board after a move */
  public void placeTile(Tile tile, int row, int col) {
    System.out.println("Place Tile | GameViewController | " + row + ", " + col);
    gameBoard.placeTile(tile, row, col);
    updateBoard();
  }

  /**
   * method for showing the welcome screen
   *
   * @param show
   */
  public void showTutorialWelcome(boolean show) {
    tutorialWelcome.setVisible(show);
  }

  /** method for initializing the starting interface */
  public void initialize() {

    instructions.setText(steps[0]);
    tutorialWelcome.managedProperty().bind(tutorialWelcome.visibleProperty());
  }

  /**
   * method for getting to the next tutorial step
   *
   * @param mouseEvent
   * @throws IOException
   */
  public void nextStep(MouseEvent mouseEvent) throws IOException {
    if (counter < 4) {
      counter++;
      instructions.setText(steps[counter]);
      stepOverview.setText(counter + 1 + "/5");
    }
  }

  /**
   * method for getting to the previous tutorial step
   *
   * @param mouseEvent
   * @throws IOException
   */
  public void previousStep(MouseEvent mouseEvent) throws IOException {
    if (counter > 0) {
      counter--;
      instructions.setText(steps[counter]);
      stepOverview.setText(counter + 1 + "/5");
    }
  }

  /**
   * Method for getting back to the play Scrabble Screen after finishing the tutorial
   *
   * @param mouseEvent
   * @throws IOException
   */
  public void backToPlayScrabble(MouseEvent mouseEvent) throws IOException {
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

  public void playerOne() {}

  public void bot() {}

  /** Shuffles the tiles on the rack */
  public void shuffle() {
    rack.shuffleRack();
    updateRack();
  }

  public void tiles() {}

  public void submit() {
    if (counter == 0) { // Stage 1
      try {
        gameBoard.check(placements, dictionary, true);
        if (gameBoard.getFoundWords().size() == 0) {
          client.showPopUp("Try to place a word first!");
        } else if (gameBoard.getFoundWords().get(0).equals("HELLO")) {
          stageTwoUnlocked = true;
          loadStageTwo();
        } else {
          client.showPopUp("Please try again...");
        }
      } catch (BoardException be) {
        client.showPopUp(be.getMessage());
      }
    }
    if (counter == 1) { // Stage 2
      System.out.println("Hier");
    }
  }
  // TODO end tutorial button
  public void start(MouseEvent mouseEvent) throws IOException {
    showTutorialWelcome(false);
    loadStageOne();
  }
}
