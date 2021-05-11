package gui.controllers;

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
    char letterID; // represents the current letter chosen
    int valueID; // represents the current value of chosen letter
    int distributionID; // represents the current distribution of chosen letter
    String [] values = {"1","3","3","2","1","4","2","4","1","8","5","1","3","1","1","3","10","1","1","1","1","4","4","8","4","10"}; //array storing values of letters
    String [] distributions = {"9","2","2","4","12","2","3","2","9","1","1","4","2","6","8","2","1","6","4","6","4","2","2","1","2","1"}; //array storing distribution of letters

    public void init(){ //initializes fields in gui
        switch(letterID){
            case 'A':
                value.setText(values[0]);
                distribution.setText(distributions[0]);
            case 'B':
                value.setText(values[1]);
                distribution.setText(distributions[1]);
            case 'C':
                value.setText(values[2]);
                distribution.setText(distributions[2]);
            case 'D':
                value.setText(values[3]);
                distribution.setText(distributions[3]);
            case 'E':
                value.setText(values[4]);
                distribution.setText(distributions[4]);
            case 'F':
                value.setText(values[5]);
                distribution.setText(distributions[5]);
            case 'G':
                value.setText(values[6]);
                distribution.setText(distributions[6]);
            case 'H':
                value.setText(values[7]);
                distribution.setText(distributions[7]);
            case 'I':
                value.setText(values[8]);
                distribution.setText(distributions[8]);
            case 'J':
                value.setText(values[9]);
                distribution.setText(distributions[9]);
            case 'K':
                value.setText(values[10]);
                distribution.setText(distributions[10]);
            case 'L':
                value.setText(values[11]);
                distribution.setText(distributions[11]);
            case 'M':
                value.setText(values[12]);
                distribution.setText(distributions[12]);
            case 'N':
                value.setText(values[13]);
                distribution.setText(distributions[13]);
            case 'O':
                value.setText(values[14]);
                distribution.setText(distributions[14]);
            case 'P':
                value.setText(values[15]);
                distribution.setText(distributions[15]);
            case 'Q':
                value.setText(values[16]);
                distribution.setText(distributions[16]);
            case 'R':
                value.setText(values[17]);
                distribution.setText(distributions[17]);
            case 'S':
                value.setText(values[18]);
                distribution.setText(distributions[18]);
            case 'T':
                value.setText(values[19]);
                distribution.setText(distributions[19]);
            case 'U':
                value.setText(values[20]);
                distribution.setText(distributions[20]);
            case 'V':
                value.setText(values[21]);
                distribution.setText(distributions[21]);
            case 'W':
                value.setText(values[22]);
                distribution.setText(distributions[22]);
            case 'X':
                value.setText(values[23]);
                distribution.setText(distributions[23]);
            case 'Y':
                value.setText(values[24]);
                distribution.setText(distributions[24]);
            case 'Z':
                value.setText(values[25]);
                distribution.setText(distributions[25]);
        }
    }
  public void increaseLetter(){
        letterID = letter.getText().charAt(0);
        if (letterID <= 89) {
            letterID++;
            letter.setText(String.valueOf(letterID));
        }
        init();
  }
  public void decreaseLetter(){
        letterID = letter.getText().charAt(0);
        if (letterID >= 66) {
            letterID--;
            letter.setText(String.valueOf(letterID));
        }
        init();
  }
  public void increaseValue(){
        valueID = Integer.parseInt(value.getText());
        if(valueID >=0){
            valueID++;
            values[letterID - 65] = Integer.toString(valueID);
        }
        init();
  }
  public void decreaseValue(){
          valueID = Integer.parseInt(value.getText());
          if (valueID >= 0) {
            valueID--;
            values[letterID - 65] = Integer.toString(valueID);
          }
          init();
  }
  public void increaseDistribution(){
        distributionID = Integer.parseInt(distribution.getText());
        if(distributionID >= 0) {
            distributionID++;
            distributions[letterID - 65] = Integer.toString(distributionID);
        }
        init();
  }
  public void decreaseDistribution(){
      distributionID = Integer.parseInt(distribution.getText());
      if(distributionID >= 0) {
          distributionID--;
          distributions[letterID - 65] = Integer.toString(distributionID);
      }
      init();
  }
  public void setDictionary() {

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
