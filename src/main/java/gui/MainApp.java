package gui;

import game.Game;
import game.players.HumanPlayer;
import game.players.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MainApp extends Application {

    private HumanPlayer player;
    private Object client;          // TODO Client


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Scrabble 13");

        int[] defaultLD = new int[]{
                9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 2};
        int[] defaultLS = new int[]{
                1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10, 0};

        player = new HumanPlayer(null);
        player.setTurn(true);
        ArrayList<Player> players = new ArrayList<>();
        Game game = new Game(players, defaultLD, defaultLS);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/playerView.fxml"));
        //stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}
