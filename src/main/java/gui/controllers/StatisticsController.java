package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** @author vihofman Controller for the statistics Controller */
public class StatisticsController {
    // setup for all the statistics
    @FXML
    private TextArea name;
    @FXML
    private TextArea totalPoints;
    @FXML
    private TextArea currentPoints;
    @FXML
    private TextArea playtime;
    @FXML
    private TextArea playedGames;
    @FXML
    private TextArea wins;
    @FXML
    private TextArea losses;
    @FXML
    private TextArea scrabblerSince;

    //fill al the TextFields with Data from Player by clicking EVALUATE in gui
    String currentPlayer = gameResultsController.player;

  public void setText() {
    name.setText("currentPlayer.getName()");
    totalPoints.setText("currentPlayer.getTotalPoints()");
    currentPoints.setText("currentPlayer.getCurrentPoints()");
    playtime.setText("currentPLayer.getPlaytime()");
    playedGames.setText("currentPlayer.getPlayedGames()");
    wins.setText("currentPlayer.getWins()");
    losses.setText("currentPlayer.getLosses()");
    scrabblerSince.setText("currentPlayer.getScrabblerSince()");
  }

    public void closeStatistics(MouseEvent mouseEvent) { // close the statistics
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.close();
    }
}
