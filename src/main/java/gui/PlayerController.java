package gui;

import game.components.Board;
import game.components.BoardField;
import game.components.Tile;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

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

public class PlayerController {

    private static final String normalStyle = "-fx-background-color: green;-fx-border-color: black";
    private static final String normalHighlightedStyle = "-fx-background-color: green;-fx-border-color: yellow";
    @FXML
    private Button passButton;
    @FXML
    private Button exchangeButton;
    @FXML
    private Button placeButton;
    private static final String dwsStyle = "-fx-background-color: pink;-fx-border-color: black";
    private static final String dwsHighlightedStyle = "-fx-background-color: pink;-fx-border-color: yellow";
    private static final String twsStyle = "-fx-background-color: red;-fx-border-color: black";
    private static final String twsHighlightedStyle = "-fx-background-color: red;-fx-border-color: yellow";
    private static final String tlsStyle = "-fx-background-color: blue;-fx-border-color: black";
    private static final String tlsHighlightedStyle = "-fx-background-color: blue;-fx-border-color: yellow";
    private static final String dlsStyle = "-fx-background-color: lightblue;-fx-border-color: black";
    private static final String dlsHighlightedStyle = "-fx-background-color: lightblue;-fx-border-color: yellow";
    @FXML
    private final GridPane boardGui = new GridPane();
    private final StackPane[][] fields = new StackPane[15][15];
    private final LinkedList<Tile> rack = new LinkedList<>();
    @FXML
    private HBox exchangeRackGui;
    @FXML
    private HBox rackGui;
    private Tile tileDragged;
    private Board board;

    public PlayerController() {
        rack.add(new Tile('A', 2));
        rack.add(new Tile('H', 3));
        rack.add(new Tile('X', 10));
        rack.add(new Tile('L', 7));
        rack.add(new Tile('T', 5));
        rack.add(new Tile('E', 1));
        rack.add(new Tile('O', 3));
    }

    @FXML
    public void initialize() {
        board = new Board();
        initBoard();
        initRack();
    }

    private void initBoard() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                boardGui.add(getFieldGui(board.getField(i, j), i, j), i, j);
            }
        }
    }

    private StackPane getFieldGui(BoardField field, int x, int y) {
        StackPane fieldGui = new StackPane();
        switch (field.getType()) {
            case STAR:
                fieldGui.getChildren().add(new Label("X"));
            case DWS:
                fieldGui.setStyle(dwsStyle);
                break;
            case TWS:
                fieldGui.setStyle(twsStyle);
                break;
            case TLS:
                fieldGui.setStyle(tlsStyle);
                break;
            case DLS:
                fieldGui.setStyle(dlsStyle);
                break;
            case NORMAL:
                fieldGui.setStyle(normalStyle);
                break;
        }

        fieldGui.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                if (tileDragged != null) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        fieldGui.setOnDragEntered(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                /* show to the user that it is an actual gesture target */
                if (tileDragged != null) {
                    switch (field.getType()) {
                        case STAR:
                        case DWS:
                            fieldGui.setStyle(dwsHighlightedStyle);
                            break;
                        case TWS:
                            fieldGui.setStyle(twsHighlightedStyle);
                            break;
                        case TLS:
                            fieldGui.setStyle(tlsHighlightedStyle);
                            break;
                        case DLS:
                            fieldGui.setStyle(dlsHighlightedStyle);
                            break;
                        case NORMAL:
                            fieldGui.setStyle(normalHighlightedStyle);
                            break;
                    }
                }

                event.consume();
            }
        });

        fieldGui.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                if (tileDragged != null) {
                    switch (field.getType()) {
                        case STAR:
                        case DWS:
                            fieldGui.setStyle(dwsStyle);
                            break;
                        case TWS:
                            fieldGui.setStyle(twsStyle);
                            break;
                        case TLS:
                            fieldGui.setStyle(tlsStyle);
                            break;
                        case DLS:
                            fieldGui.setStyle(dlsStyle);
                            break;
                        case NORMAL:
                            fieldGui.setStyle(normalStyle);
                            break;
                    }
                }
                event.consume();
            }
        });

        fieldGui.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                boolean success = false;
                if (tileDragged != null) {
                    fieldGui.getChildren().add(getTileGui(tileDragged));
                    board.setTile(tileDragged, x, y);
                    success = true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });

        return fieldGui;
    }

    private void initRack() {
        for (Tile tile : rack) {
            rackGui.getChildren().add(getTileGui(tile));
        }
        StackPane test = new StackPane();
        test.setStyle("-fx-background-color: yellow");
        rackGui.getChildren().add(test);
    }

    private VBox getTileGui(Tile tile) {
        VBox tileGui = new VBox(1);
        tileGui.getChildren().addAll(new Label(Character.toString(tile.getLetter())), new Label(Integer.toString(tile.getScore())));
        tileGui.setStyle("-fx-border-color: black");
        tileGui.setPadding(new Insets(2, 2, 2, 2));
        tileGui.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start a drag-and-drop gesture*/
                /* allow any transfer mode */
                Dragboard db = tileGui.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(Character.toString(tile.getLetter()));
                tileDragged = tile;
                db.setContent(content);
                event.consume();
            }
        });
        tileGui.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag and drop gesture ended */
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    rackGui.getChildren().remove(tileGui);
                    rack.remove(tile);
                }
                event.consume();
            }
        });

        return tileGui;
    }

    @FXML
    public void pass() {
        System.out.println("PASS");
    }

    @FXML
    public void place() {
        System.out.println("PLACE");
    }

    @FXML
    public void exchange() {
        System.out.println("EXCHANGE");
    }

}
