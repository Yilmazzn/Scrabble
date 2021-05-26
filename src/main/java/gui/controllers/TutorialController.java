package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

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
  private int counter = 0;
  /**
   * This array consists of all the advice given by the tutorial. The content of the indices will be
   * printed out onto the instructions label.
   */
  private String[] steps = {
    "Place the tiles from your rack onto the board so they form a word."
        + "\n"
        + "You can place tiles either vertically or horizontally, always connected to previous tiles."
        + "\n"
        + "Based on the letter value you score points.",
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
        + "The game finishes as well, if all the tiles from the bag have been removed."
        + "\n"
  };

  private Client client;
  /**
   * Sets client in TutorialController
   *
   * @param client Requires client to be set
   */
  public void setModel(Client client) {

    this.client = client;
    showTutorialWelcome(true);
  }
  /**
   * method for showing the welcome screen
   * @param show
   */
  public void showTutorialWelcome(boolean show){
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

  public void shuffle() {}

  public void tiles() {}

  public void submit() {}

  public void start(MouseEvent mouseEvent) throws IOException {
    showTutorialWelcome(false);
  }
}
