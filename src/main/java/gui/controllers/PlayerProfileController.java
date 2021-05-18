package gui.controllers;

import client.Client;
import client.PlayerProfile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** @author mnetzer Controller for the playerProfileView */
public class PlayerProfileController {

  @FXML private Label playerNo;
  @FXML private Label playerName;
  @FXML private Label playerTotalPoints;
  @FXML private Label playerCurrentHighscore;
  @FXML private Label playerPlayedGames;
  @FXML private Label playerWins;
  @FXML private Label playerLosses;
  @FXML private Label playerScrabblerSince;

  private Client client;
  private List<PlayerProfile> profiles;
  private int selectedIdx = 0;

  public void setModel(Client client) {
    this.client = client;
    this.profiles = client.getPlayerProfiles();
    if (profiles.size() > 0 && client.getSelectedProfile() != null) {
      selectedIdx = profiles.indexOf(client.getSelectedProfile());
      showPlayer();
    }
  }

  public void showPlayer() {
    playerNo.setText(String.valueOf(selectedIdx + 1));
    playerName.setText(profiles.get(selectedIdx).getName());
    playerTotalPoints.setText(String.valueOf(profiles.get(selectedIdx).getTotalScore()));
    playerCurrentHighscore.setText(String.valueOf(profiles.get(selectedIdx).getHighscore()));
    playerPlayedGames.setText(profiles.get(selectedIdx).getPlayedGames() + " Game(s)");
    playerWins.setText(String.valueOf(profiles.get(selectedIdx).getWins()));
    playerLosses.setText(String.valueOf(profiles.get(selectedIdx).getLosses()));
    playerScrabblerSince.setText(profiles.get(selectedIdx).getCreation());
  }

  public void backToLogin(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
    Parent playerLobbyView = loader.load();
    PlayerLobbyController controller = loader.getController();
    controller.setModel(client);

    // Parent profileControllerView =
    // FXMLLoader.load(getClass().getResource("/views/playerProfileView.fxml"));

    Scene profileControllerScene = new Scene(playerLobbyView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(profileControllerScene);
    window.show();
  }

  public void editProfile() {
    TextInputDialog td = new TextInputDialog();
    td.setTitle("Edit Profile");
    td.setHeaderText("Enter new name of profile");
    td.setContentText("Name: ");
    Optional<String> result = td.showAndWait();
    result.ifPresent(
        name -> {
          profiles.get(selectedIdx).setName(name);
          showPlayer();
          client.savePlayerProfiles();
        });
  }

  public void exitGame() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/exitGame.fxml"));
    Parent exitGameView = loader.load();
    // exitGameController controller = loader.getController();

    Scene exitGameScene = new Scene(exitGameView);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Exit Game");
    window.setScene(exitGameScene);
    window.setWidth(300);
    window.setHeight(200);
    window.showAndWait();
  }

  public void previousPlayer(MouseEvent mouseEvent) {
    selectedIdx = Math.abs((selectedIdx - 1) % profiles.size());
    client.setSelectedProfile(profiles.get(selectedIdx));
    showPlayer();
  }

  public void nextPlayer(MouseEvent mouseEvent) {
    selectedIdx = (selectedIdx + 1) % profiles.size();
    client.setSelectedProfile(profiles.get(selectedIdx));
    showPlayer();
  }

  public void createNewProfile() {
    System.out.println("CreateNewProfile");

    TextInputDialog td = new TextInputDialog();
    td.setTitle("Create New Profile");
    td.setHeaderText("Enter name of new profile");
    td.setContentText("Name: ");
    Optional<String> result = td.showAndWait();
    result.ifPresent(
        name -> {
          profiles.add(new PlayerProfile(name, 0, 0, 0, 0, LocalDate.now(), LocalDate.now()));
          selectedIdx = profiles.size() - 1;
          showPlayer();
          client.setSelectedProfile(profiles.get(selectedIdx));
          client.savePlayerProfiles();
        });
  }

  public void deleteProfile(MouseEvent mouseEvent) {
    if (profiles.size() <= 1) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Minimum Profile");
      alert.setHeaderText(null);
      alert.setContentText("You cannot delete your last profile!");
      alert.showAndWait();

    } else {
      profiles.remove(profiles.get(selectedIdx));
      selectedIdx = Math.abs((selectedIdx - 1) % profiles.size());
      showPlayer();
      client.setSelectedProfile(profiles.get(selectedIdx));
      client.savePlayerProfiles();
    }
  }

}
