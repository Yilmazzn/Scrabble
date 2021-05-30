package client;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

/**
 * This class creates a new Player instance with all the values that are saved in the xml file
 * later.
 *
 * @author nsiebler
 */
public class PlayerProfile implements Serializable {

  private String name;

  private int highScore;
  private int wins;
  private int losses;
  private LocalDate creation;
  private int totalScore;
  private LocalDate lastLogged;

  // default image
  private transient Image image =
      new Image(getClass().getResourceAsStream("/pictures/ProfileIcon.png"));

  // Icon will be added, just have to clear which format will be excepted

  /**
   * Constructor to initialize a player.
   *
   * @param name name of the player
   * @param highscore score of the player
   * @param wins wins of a player
   * @param looses looses of a player
   * @param totalScore score of a player
   * @param creation date when a player is created
   * @param lastLogged date when a player was lastly created
   * @param image profile picture of the image
   */
  public PlayerProfile(
      String name,
      int highscore,
      int wins,
      int looses,
      int totalScore,
      LocalDate creation,
      LocalDate lastLogged,
      Image image) {
    super();
    this.name = name;
    this.highScore = highscore;
    this.wins = wins;
    this.losses = looses;
    this.totalScore = totalScore;
    this.creation = creation;
    this.lastLogged = lastLogged;
    if (image != null) {
      this.image = image;
    }
  }

  public PlayerProfile(
      String name,
      int highScore,
      int wins,
      int looses,
      int totalScore,
      LocalDate creation,
      LocalDate lastLogged) {
    super();
    this.name = name;
    this.highScore = highScore;
    this.wins = wins;
    this.losses = looses;
    this.totalScore = totalScore;
    this.creation = creation;
    this.lastLogged = lastLogged;
  }

  /**
   * The following methods will just contain all necessary getters and setters.
   *
   * @return getValue
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getHighScore() {
    return highScore;
  }

  public void setHighScore(int highScore) {
    this.highScore = highScore;
  }

  public int getPlayedGames() {
    return this.wins + this.losses;
  }

  public int getWins() {
    return wins;
  }

  public void setWins(int wins) {
    this.wins = wins;
  }

  public int getLosses() {
    return losses;
  }

  public void setLosses(int losses) {
    this.losses = losses;
  }

  public String getCreation() {
    return creation.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  public void setCreation(LocalDate creation) {
    this.creation = creation;
  }

  public String getLastLogged() {
    return lastLogged.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  public void setLastLogged(LocalDate lastLogged) {
    this.lastLogged = lastLogged;
  }

  public int getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(int totalScore) {
    this.totalScore = totalScore;
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    image = SwingFXUtils.toFXImage(ImageIO.read(s), null);
  }

  private void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", s);
  }
}
