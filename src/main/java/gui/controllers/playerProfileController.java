package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class playerProfileController {

    @FXML
    private Label username;

    public void InitData(){
        username.setText("Enton123");
    }

    public void playScrabble(MouseEvent mouseEvent) {
        System.out.println("PlayScrabble");
    }

    public void manageProfiles(MouseEvent mouseEvent){
        System.out.println("ManageProfiles");
    }

    public void settings(MouseEvent mouseEvent) {
        System.out.println("Settings");
    }

    public void backToLogin(MouseEvent mouseEvent){
        System.out.println("BackToLogin");
    }

    public void exitGame(MouseEvent mouseEvent){
        System.out.println("Exit");
    }
}
