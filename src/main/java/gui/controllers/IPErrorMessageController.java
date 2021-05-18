package gui.controllers;

import client.Client;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** @author vihofman Controller for the error Message for entering wrong IP at Joining game*/
public class IPErrorMessageController {
    private Client client;
    public void setModel(Client client){
        this.client = client;
    }
    public void closeErrorMessage(MouseEvent mouseEvent) { // close the error message
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.close();
    }
}
