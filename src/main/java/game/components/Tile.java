package game.components;

import java.io.Serializable;

/**
 * @author yuzun
 *     <p>Single playing piece
 */
public class Tile implements Serializable {

  private final int score; // score of this letter
  private final boolean joker; // true if this tile is a joker tile
  private char letter; // letter this tile represents

  /** Create tile by giving it a letter and the corresponding score */
  public Tile(char letter, int score) {
    this.letter = letter;
    this.score = score;
    this.joker = letter == ' ';
  }

  /** Returns the letter this tile represents */
  public char getLetter() {
    return letter;
  }

  /** Only used when joker tile was played and tile letter can be set */
  public void setLetter(char letter) {
    this.letter = Character.toUpperCase(letter);
  }

  /** Returns score of this tile */
  public int getScore() {
    return score;
  }

  /** Returns true if tile is a joker */
  public boolean isJoker() {
    return joker;
  }
}
