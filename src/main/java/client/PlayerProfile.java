package client;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PlayerProfile implements Serializable {

  /**
   * @author nsiebler This class creates a new Player instance with all the values that are saved in the
   *     xml file later
   */
  private String name;

  private int highscore;
  private int wins;
  private int losses;
  private LocalDate creation;
  private int totalScore;
  /** This functionality will be implemented by this week */
  private LocalDate lastLogged;

  // Icon will be added, just have to clear which format will be excepted

  public PlayerProfile(
      String name,
      int highscore,
      int wins,
      int looses,
      int totalScore,
      LocalDate creation,
      LocalDate lastLogged) {
    super();
    this.name = name;
    this.highscore = highscore;
    this.wins = wins;
    this.losses = looses;
    this.totalScore = totalScore;
    this.creation = creation;
    this.lastLogged = lastLogged;
  }

  /**
   * the following methods will just contain all necessary getters and setters
   *
   * @return getValue
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getHighscore() {
    return highscore;
  }

  public void setHighscore(int highscore) {
    this.highscore = highscore;
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
}
