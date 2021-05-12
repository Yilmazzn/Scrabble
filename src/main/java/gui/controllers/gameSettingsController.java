package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/** @author vihofman Controller for the Game Settings */

public class gameSettingsController {

    @FXML
    private TextField letter;
    @FXML
    private TextField value;
    @FXML
    private TextField distribution;
    @FXML
    private TextField dictionary;

    private Client client;
    char letterID; // represents the current letter chosen
    int valueID; // represents the current value of chosen letter
    int distributionID; // represents the current distribution of chosen letter
    String dictionaryID; // current dictionary
    String [] values = {"1","3","3","2","1","4","2","4","1","8","5","1","3","1","1","3","10","1","1","1","1","4","4","8","4","10"}; //array storing values of letters
    String [] distributions = {"9","2","2","4","12","2","3","2","9","1","1","4","2","6","8","2","1","6","4","6","4","2","2","1","2","1"}; //array storing distribution of letters

    public void setModel(Client client){
        this.client = client;
    }

  public void initData() { // initializes fields in gui
    for (int i = 65; i <= 90; i++) {
      char test = (char) i;
      if (String.valueOf(test).equals(letter.getText())) {
        value.setText(values[i-65]);
        distribution.setText(distributions[i-65]);
        break;
      }
    }
  }
  public void increaseLetter(){
        letterID = letter.getText().charAt(0);
        if (letterID <= 89) {
            letterID++;
            letter.setText(String.valueOf(letterID));
            initData();
        }
  }
  public void decreaseLetter(){
        letterID = letter.getText().charAt(0);
        if (letterID >= 66) {
            letterID--;
            letter.setText(String.valueOf(letterID));
        }
        initData();
  }
  public void increaseValue(){
        valueID = Integer.parseInt(value.getText());
        if(valueID >=0){
            valueID++;
            values[letterID - 65] = Integer.toString(valueID);
            initData();
        }

  }
  public void decreaseValue(){
          valueID = Integer.parseInt(value.getText());
          if (valueID > 0) {
            valueID--;
            values[letterID - 65] = Integer.toString(valueID);
          }
          initData();
  }
  public void increaseDistribution(){
        distributionID = Integer.parseInt(distribution.getText());
        if(distributionID >= 0) {
            distributionID++;
            distributions[letterID - 65] = Integer.toString(distributionID);
        }
        initData();
  }
  public void decreaseDistribution(){
      distributionID = Integer.parseInt(distribution.getText());
      if(distributionID > 0) {
          distributionID--;
          distributions[letterID - 65] = Integer.toString(distributionID);
      }
      initData();
  }
  public void setDictionary() {
    if(dictionary.getText() != "/.*.txt"){
        dictionaryID = dictionary.getText();
        System.out.println("Dictionary has been uploaded successfully!");

    }
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
        Scene playerLobbyScene = new Scene(createGame);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(playerLobbyScene);
        window.show();
    }

}
