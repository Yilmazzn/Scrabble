package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** @author vihofman Controller for the statistics  */
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

    private Client client;
    public void setModel(Client client){
        this.client = client;
    }

    String currentPlayer = gameResultsController.player;

  public void initData() {
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
