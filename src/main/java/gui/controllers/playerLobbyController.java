package gui.controllers;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class playerLobbyController {

    @FXML
    private Label username;

    public void InitData(){
        username.setText("Enton123");
    }

    public void playScrabble(MouseEvent mouseEvent) {
        System.out.println("PlayScrabble");
    }

    public void manageProfiles(MouseEvent mouseEvent) throws IOException {
        System.out.println("ManageProfiles");

        /*Parent profileControllerView = FXMLLoader.load(getClass().getResource("playerProfileView.fxml"));
        Scene profileControllerScene = new Scene(profileControllerView);

        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(profileControllerScene);
        window.show();*/
    }

    public void settings(MouseEvent mouseEvent) {
        System.out.println("Settings");
    }

    public void backToLogin(MouseEvent mouseEvent) {
        System.out.println("BackToLogin");
    }

    public void exitGame(MouseEvent mouseEvent) {
        System.out.println("Exit");
    }


}
