package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/** @author vihofman Controller for the Game Settings */
public class gameSettingsController {

  @FXML private TextField letter;
  @FXML private TextField value;
  @FXML private TextField distribution;
  @FXML private Label dictionary;
  @FXML private TextField joker;

  private Client client;
  private CreateGameController createController;
  private char letterID; // represents the current letter chosen
  private int valueID; // represents the current value of chosen letter
  private int distributionID; // represents the current distribution of chosen letter
  private int jokerID; // represents the amount of jokers in the game
  private String dictionaryID; // current dictionary

  private int[] values; // array storing values of letters
  private int[] distributions; // array storing distribution of letters

  public void setModel(Client client, CreateGameController createController) {
    this.client = client;
    this.createController = createController;

    values = new int [createController.getValues().length];
    for(int i = 0; i < createController.getValues().length; i++){
        values[i] = createController.getValues()[i];
    }

    distributions = new int [createController.getDistributions().length];
    for(int i = 0; i < createController.getDistributions().length; i++){
      distributions[i] = createController.getDistributions()[i];
    }
    initData();
    joker.setText(Integer.toString(distributions[26]));
  }

  public void initData() { // initializes fields in gui
    for (int i = 65; i <= 90; i++) {
      char test = (char) i;
      if (String.valueOf(test).equals(letter.getText())) {
        value.setText(Integer.toString(this.values[i - 65]));
        distribution.setText(Integer.toString(this.distributions[i - 65]));
        break;
      }
    }
  }
  /** Applies the Settings and closes the GameSettingView through the CreateGameController */
  public void apply() {
      System.out.println("APPLY");
    createController.closeSettings();
    createController.setValues(values);
    createController.setDistributions(distributions);
    client
        .getNetClient()
        .sendGameSettings(values, distributions, createController.getDictionaryPath());
  }

  /** Closes the GameSettings Screen through the CreateGameController */
  public void cancel() {
      System.out.println("Cancel_Button");
      createController.closeSettings();
  }

  /** opens a FileChooser to choose the fxml File which is supposed to be the dictionary */
  /** if File is not a txt File or nothing has been selected, then show Error Message */
  public void selectDictionary(MouseEvent mouseEvent) throws IOException {
    FileChooser fc = new FileChooser();
    File selectedFile = fc.showOpenDialog(null);
    if ((selectedFile != null) && selectedFile.getName().matches("/*.*.txt")) {
      dictionary.setText(selectedFile.getPath());
      dictionaryID = dictionary.getText();
      System.out.println("Dictionary has been uploaded successfully!");
    } else {
      openDictionaryError();
    }
  }

  public int getValue(char a) { // getter method for value of letter
    int valueLetter = Character.getNumericValue(a);
    if ((valueLetter >= 97) && (valueLetter <= 122)) { // no caps
      return values[valueLetter - 97];
    }
    if ((valueLetter >= 65) && (valueLetter <= 90)) { // caps
      return values[valueLetter - 65];
    } else return 0;
  }

  public int getDistribution(char a) { // getter method for distribution of letter
    int distributionLetter = Character.getNumericValue(a);
    if ((distributionLetter >= 97) && (distributionLetter <= 122)) { // no caps
      return values[distributionLetter - 97];
    }
    if ((distributionLetter >= 65) && (distributionLetter <= 90)) { // caps
      return values[distributionLetter - 65];
    } else return 0;
  }

  public void decreaseJoker() {
    int jokers = distributions[26];
    jokers--;
    joker.setText(Integer.toString(jokers));
    distributions[26] = jokers;
  }

  public void increaseJoker() {
    int jokers = distributions[26];
    jokers++;
    joker.setText(Integer.toString(jokers));
    distributions[26] = jokers;
  }

  public void increaseLetter() { // to swap to next letter
    letterID = letter.getText().charAt(0);
    if (letterID <= 89) {
      letterID++;
      letter.setText(String.valueOf(letterID));
      initData();
    }
  }

  public void decreaseLetter() { // to swap to letter before
    letterID = letter.getText().charAt(0);
    if (letterID >= 66) {
      letterID--;
      letter.setText(String.valueOf(letterID));
    }
    initData();
  }

  public void increaseValue() { // to increase selected letter above
    letterID = letter.getText().charAt(0);
    valueID = Integer.parseInt(value.getText());
    if (valueID > 0) {
      valueID++;
      this.values[letterID - 65] = valueID;
      initData();
    }
  }

  public void decreaseValue() { // to decrease selected letter above
    letterID = letter.getText().charAt(0);
    valueID = Integer.parseInt(value.getText());
    if (valueID > 0) {
      valueID--;
      this.values[letterID - 65] = valueID;
    }
    initData();
  }

  public void increaseDistribution() { // to increase distribution of selected letter above
    letterID = letter.getText().charAt(0);
    distributionID = Integer.parseInt(distribution.getText());
    if (distributionID >= 0) {
      distributionID++;
      this.distributions[letterID - 65] = distributionID;
    }
    initData();
  }

  public void decreaseDistribution() { // to decrease distribution of selected letter above
    letterID = letter.getText().charAt(0);
    distributionID = Integer.parseInt(distribution.getText());
    if (distributionID > 0) {
      distributionID--;
      this.distributions[letterID - 65] = distributionID;
    }
    initData();
  }

  public void openDictionaryError() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/dictionaryError.fxml"));
    Parent errorMessage = loader.load();

    Scene exitGameScene = new Scene(errorMessage);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Exit Game");
    window.setScene(exitGameScene);
    window.setWidth(200);
    window.setHeight(200);
    window.showAndWait();
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

  public void backToCreateGame(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/createGame.fxml"));
    Parent createGame = loader.load();
    CreateGameController controller = loader.getController();

    controller.setModel(client);
    controller.setDictionaryPath(dictionaryID);
    controller.setValues(values);
    controller.setDistributions(distributions);

    Scene playerLobbyScene = new Scene(createGame);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(playerLobbyScene);
    window.show();
  }
}
