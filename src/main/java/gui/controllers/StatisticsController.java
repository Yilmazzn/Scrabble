package gui.controllers;

import client.Client;
import client.PlayerProfile;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/** @author vihofman Controller for the statistics  */
public class StatisticsController {
    // setup for all the statistics
    @FXML
    private static TextArea name;
    @FXML
    private static TextArea totalPoints;
    @FXML
    private static TextArea currentPoints;
    @FXML
    private static TextArea playtime;
    @FXML
    private static TextArea playedGames;
    @FXML
    private static TextArea wins;
    @FXML
    private static TextArea losses;
    @FXML
    private static TextArea scrabblerSince;

    private Client client;
    public void setModel(Client client){
        this.client = client;
    }


  public static void initData(PlayerProfile player) throws IOException { //TODO does not work
    name.setText(player.getName());
    totalPoints.setText(Integer.toString(player.getTotalScore()));
    currentPoints.setText(Integer.toString(player.getTotalScore()));
    playtime.setText("currentPlayer.getPlaytime()"); // TODO
    playedGames.setText(Integer.toString(player.getPlayedGames()));
    wins.setText(Integer.toString(player.getLosses()));
    losses.setText(Integer.toString(player.getLosses()));
    scrabblerSince.setText(player.getCreation());
  }

    public void closeStatistics(MouseEvent mouseEvent) { // close the statistics
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.close();
    }
}
