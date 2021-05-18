package gui.controllers;

import client.Client;
import client.PlayerProfile;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/** @author vihofman Controller for the statistics  */
public class StatisticsController {
    // setup for all the statistics
    @FXML
    private Text name;
    @FXML
    private Text totalPoints;
    @FXML
    private Text currentPoints;
    @FXML
    private Text playedGames;
    @FXML
    private Text wins;
    @FXML
    private Text losses;
    @FXML
    private Text scrabblerSince;
    //TODO Label in FXML

    private PlayerProfile profile;

    public void setModel(PlayerProfile profile){
        this.profile = profile;
        initData(profile);
    }


  public void initData(PlayerProfile player) { //TODO does not work
    name.setText(player.getName());
    totalPoints.setText(Integer.toString(player.getTotalScore()));
    currentPoints.setText(Integer.toString(player.getTotalScore()));
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
