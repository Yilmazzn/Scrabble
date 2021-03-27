package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    private Object client;          // TODO Client


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Scrabble 13");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/playerView.fxml"));
        PlayerController playerController = new PlayerController();

        playerController = loader.getController();
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();





        /*
        int[] defaultLDArr = new int[]{
                9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        int[] defaultLSArr = new int[]{
                1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};

        HashMap<Character, Integer> defaultLD = new HashMap<>();
        for(int i = 0; i < defaultLDArr.length-1; i++){
            defaultLD.put((char)(i+65), defaultLDArr[i]);
        }
        defaultLD.put(' ', 2);

        HashMap<Character, Integer> defaultLS = new HashMap<>();
        for(int i = 0; i < defaultLSArr.length-1; i++){
            defaultLS.put((char)(i+65), defaultLSArr[i]);
        }
        defaultLD.put(' ', 0);

        player = new HostPlayer(this);
        player.setTurn(true);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        Game game = new Game(players, defaultLD, defaultLS);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/playerView.fxml"));
        loader.setController(player.getGui());
        stage.setScene(new Scene(loader.load()));
        stage.show();

        */

    }
}
