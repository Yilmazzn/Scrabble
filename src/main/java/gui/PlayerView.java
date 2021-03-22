package gui;

import game.players.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class PlayerView {

    /**
     * @author yuzun
     * <p>
     * GUI, when player is loaded into the game and the game starts
     * Consists of:
     * TODO Menu
     * Board
     * Rack
     * TODO Chat
     * TODO Scoreboard
     */

    private Player player;


    @FXML
    private GridPane boardGrid;     // Board Grid
    @FXML
    private HBox exchangeRack;
    @FXML
    private Button passButton;
    @FXML
    private Button exchangeButton;
    @FXML
    private Button placeButton;
    @FXML
    private HBox rack;


    public PlayerView(Player player) {
        this.player = player;
    }

    @FXML
    private void pass() {

    }

    @FXML
    private void exchange() {

    }

    @FXML
    private void place() {

    }

}
