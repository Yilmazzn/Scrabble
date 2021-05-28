package gui.controllers;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class DictionaryController {

  @FXML private ScrollPane scrollPane;
  @FXML private VBox vbox;
  @FXML Label page;
  // @FXML private TextArea textArea;
  private Client client;
  private String [] dictionaryText;
  private int currentPage = 1, pages;
  private int start = 0;
  private int end = 2000;

  public void setModel(Client client) {
    this.client = client;
    page.setText("1/1");
    vbox.heightProperty().addListener(observer -> scrollPane.setVvalue(0.0));
  }

  public void showDictionary(String text) {
    dictionaryText = text.split("\n");
    pages = dictionaryText.length / 2000 + 1;

    for (int i = start; i < Math.min(end, dictionaryText.length); i++) {
      Label label = new Label(dictionaryText[i]);
      label.setWrapText(true);
      vbox.getChildren().add(label);
    }
    scrollPane.setContent(vbox);
    scrollPane.setVvalue(0.0);
    page.setText(currentPage + "/" + pages);
  }

  public void previousPage() {
    currentPage--;
    start -= 2000;
    end -= 2000;
    if (start < 0) {
      start = (pages - 1) * 2000;
      end = dictionaryText.length;
      currentPage = pages;
    }
    vbox.getChildren().clear();
    vbox.getChildren().removeAll();
    for (int i = start; i < Math.min(end, dictionaryText.length); i++) {
      Label label = new Label(dictionaryText[i]);
      label.setWrapText(true);
      vbox.getChildren().add(label);
    }
    scrollPane.setContent(vbox);
    scrollPane.setVvalue(0.0);
    page.setText(currentPage + "/" + pages);
  }

  public void nextPage() {
    currentPage++;
    start += 2000;
    end += 2000;
    if (start > dictionaryText.length) {
      start = 0;
      end = 2000;
      currentPage = 1;
    }
    vbox.getChildren().clear();
    vbox.getChildren().removeAll();


    for (int i = start; i < Math.min(end, dictionaryText.length); i++) {
      Label label = new Label(dictionaryText[i]);
      label.setWrapText(true);
      vbox.getChildren().add(label);
    }
    scrollPane.setContent(vbox);
    scrollPane.setVvalue(0.0);
    page.setText(currentPage + "/" + pages);
  }
}
