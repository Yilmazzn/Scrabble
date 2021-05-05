package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

/** @author vihofman Controller for the Game Chat */
public class gameChatController {
    @FXML
    private TextField inputMessage;
    @FXML
    private TextArea messageDisplay;
    @FXML
    private Button sendButton;

    public void closeChat(MouseEvent mouseEvent) { // close the chat
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.close();
    }
    class background extends Rectangle{ //idea for giving sent messages a background
        background(){
            setWidth(50);
            setHeight(20);
            setArcWidth(10);
            setArcHeight(10);
            setFill(Color.GREY);
            setStroke(Color.GREY);
        }
    }
    public void sendMessage(MouseEvent mouseEvent) throws IOException { //send a message by clicking the enter button
        String output = inputMessage.getText();
        messageDisplay.setText(messageDisplay.getText() + "\n" + output);
    }
}
