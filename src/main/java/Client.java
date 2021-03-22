import game.players.HumanPlayer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {

    private HumanPlayer player;


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


        //Game game = new Game(players, defaultLD, defaultLS);

        player.setTurn(true);


        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/boardField.fxml"));
        //loader.setController(field);
        //stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}
