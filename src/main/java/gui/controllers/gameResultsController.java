package gui.controllers;

import client.Client;
import client.PlayerProfile;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.server.Server;

import java.io.IOException;
/** @author vihofman Controller for the Game Results */
public class gameResultsController {
  private Client client;
  public PlayerProfile player;
  public void setModel(Client client){
    this.client = client;
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

  public void backToPlayerLobby(MouseEvent mouseEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/playerLobbyView.fxml"));
    Parent playerLobbyView = loader.load();
    PlayerLobbyController controller = loader.getController();
    Scene playerLobbyScene = new Scene(playerLobbyView);
    Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
    window.setScene(playerLobbyScene);
    window.show();
  }

  public void openChat() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/gameChat.fxml"));
    Parent gameChat = loader.load();
    Scene gameChatScene = new Scene(gameChat);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("");
    window.setScene(gameChatScene);
    window.setWidth(300);
    window.setHeight(500);
    window.show();
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double x = bounds.getMinX() + (bounds.getWidth() - window.getWidth()) * 0.78;
    double y = bounds.getMinY() + (bounds.getHeight() - window.getHeight()) * 0.45;
    window.setX(x);
    window.setY(y);
    window.show();
  }
  public void openStatistics() throws IOException{
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(this.getClass().getResource("/views/statistics.fxml"));
    Parent openStatistics = loader.load();
    Scene openStatisticsScene = new Scene(openStatistics);
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("");
    window.setScene(openStatisticsScene);
    window.setWidth(300);
    window.setHeight(500);
    window.show();
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double x = bounds.getMinX() + (bounds.getWidth() - window.getWidth()) * 0.2;
    double y = bounds.getMinY() + (bounds.getHeight() - window.getHeight()) * 0.45;
    window.setX(x);
    window.setY(y);
    window.show();
  }

  public void playerOne() throws IOException{
    player = Server.getProfile(1); // TODO is this ID correct? because does not work at all
    StatisticsController.initData(player);
    openStatistics();
  }
  public void playerTwo() throws IOException{
     player = Server.getProfile(2);
     StatisticsController.initData(player);
     openStatistics();
  }

  public void playerThree() throws IOException{
    player = Server.getProfile(3);
    StatisticsController.initData(player);
    openStatistics();
  }

  public void playerFour() throws IOException{
    player = Server.getProfile(3);
    StatisticsController.initData(player);
    openStatistics();
  }
}