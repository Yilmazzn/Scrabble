package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/** @author vihofman Controller for the pregame Agreement Controller */
public class PregameAgreementController {
    @FXML private CheckBox dictionaryAgree;
    @FXML private CheckBox dictionaryDisagree;
    @FXML private CheckBox ready;
    @FXML private TextField dictionary;
    private Client client;
    public void setModel(Client client){
        this.client = client;
    }
    //functionality for agreement and disagreement
    public void setDictionaryAgreement(){ // agree with dictionary
        dictionaryDisagree.setSelected(false);
    }
    public void setDictionaryDisagreement(){ // disagree with dictionary
        dictionaryAgree.setSelected(false);
    }
    // set the dictionary from host
    public void initData(){ // set dictionary from Host
        dictionary.setText("Host.getDictionary()");
    }
    //Getter methods for Agreement and Ready Status
    public boolean getDictionaryAgreement(){
        return dictionaryAgree.isSelected();
    }
    public boolean getReadyStatus(){
        return ready.isSelected();
    }
}
