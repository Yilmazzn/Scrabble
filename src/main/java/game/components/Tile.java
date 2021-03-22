package game.components;

public class Tile {

    /**
     * @author yuzun
     *
     * Single playing piece
     */

    private char letter;            // letter of this tile
    private int score;              // score of this letter
    private boolean joker;          // true if this tile is a joker tile

    /**
     * Tile get initialized with the rank of the letter in the alphabet, and the corresponding score
     *
     * @param letterNum letter with nth position in alphabet (starting to count at 0)
     * @param score     score of nth letter
     */
    public Tile(int letterNum, int score) {
        letter = (char) (letterNum + 65);
        this.score = score;

        joker = letterNum == 26;
        if (joker) {
            letter = ' ';
        }
    }

    public char getLetter() {
        return letter;
    }

    /**
     * Only used when joker tile was played and tile letter can be set
     */
    public void setLetter(char letter) {
        this.letter = Character.toUpperCase(letter);
    }

    public int getScore() {
        return score;
    }

    public boolean isJoker() {
        return joker;
    }
}
