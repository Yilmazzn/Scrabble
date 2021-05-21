package game.players;

import game.Dictionary;
import game.components.Board;
import game.components.BoardField;
import game.components.Tile;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author nsiebler
 *     <p>Simple Ai Player Only implements think method
 */
public class EasyAiPlayer extends AiPlayer {

  public EasyAiPlayer() {
    super(DIFFICULTY.EASY);
  }

  /**
   * Main method which is triggered by the game instance All computations from start of round till
   * end need to be done in here!
   */
  @Override
  public void think(Board board, Dictionary dictionary) {
    // Use Player placeTile Method
    // What happens if no word can be set? exchange words
    // No Words found yet, build the first word
    // Build RegEx String to find it with the contain method

    String regEx = "";
    Iterator<Tile> it = getTilesFromPlayer().iterator();
    while (it.hasNext()) {
      regEx += it.next().getLetter();
    }

    String allWords[] = new String[10];

    // when ai is first to play
    if (getFoundWords().isEmpty()) {
      // Just simulate all the String words
      for (int i = 0; i < allWords.length; i++) {
        // for the first word there is no possible word longer than 7
        if (allWords[i].length() < 8) {
          if (contains(allWords[i], regEx)) {
            System.out.println(allWords[i]);
            for (int j = 0; j < allWords[i].length(); j++) {
              // Place the first found word
              // TODO place the right tile from your rack
              char c = allWords[i].charAt(j);
              int index = 0;
              for (int k = 0; k < allWords[i].length(); k++) {
                if (c == getTilesFromPlayer().get(k).getLetter()) {
                  index = k;
                }
              }
              placeTile(getTilesFromPlayer().get(index), 7, (7 + j));
            }
            submit();
          }
        }
      }
    } else {

      // check for word combination
      it = getTilesFromPlayer().iterator();
      while (it.hasNext()) {
        regEx += it.next().getLetter();
      }
    }

    for (int i = 0; i < allWords.length; i++) {
      // for the first word there is no possible word longer than 7
      if (allWords[i].length() < 8) {
        if (contains(allWords[i], regEx)) {
          String placement = checkForPlacement(board, allWords[i]);
          if (!placement.equals("")) {
            System.out.println(allWords[i]);
            String placePos[] = placement.split("\\|");
            String firstPos[] = placePos[0].split("\\.");
            String secondPos[] = placePos[1].split("\\.");
            int hPlacementIndex = Integer.parseInt(secondPos[1]) - Integer.parseInt(firstPos[1]);
            int cPlacementIndex = Integer.parseInt(secondPos[0]) - Integer.parseInt(firstPos[0]);
            for (int j = 0; j < allWords[i].length(); j++) {
              char c = allWords[i].charAt(j);
              int index = 0;
              for (int k = 0; k < allWords[i].length(); k++) {
                if (c == getTilesFromPlayer().get(k).getLetter()) {
                  index = k;
                }
              }

              if (placePos[2].equals("h")) {
                if (j != hPlacementIndex) {
                  placeTile(
                      getTilesFromPlayer().get(index),
                      Integer.parseInt(firstPos[0]),
                      Integer.parseInt(firstPos[1]) + j);
                }
              } else {
                if (j != cPlacementIndex) {
                  placeTile(
                      getTilesFromPlayer().get(index),
                      Integer.parseInt(firstPos[0] + j),
                      Integer.parseInt(firstPos[1]));
                }
              }
            }
          }
        }
      }
    }

    //  TODO check right away if the word is placeable do not calc every possible word
    // if no valid solution is found exchange random tiles
    // tiles to exchange
    int random = 1 + (int) (Math.random() * 7);
    if (game.getBagSize() < random) {
      random = game.getBagSize();
    }
    for (int i = 0; i < random; i++) {
      exchange(getTilesFromPlayer().get(i));
    }

    // Just finally submit everything and the rest is done
    submit();
  }

  /**
   * Information on how to place a tile start index of the word example 11.12 chars position which
   * already is set | | sep and if its place horizontal or vertical h or v example String
   * 11.12|11.14|v
   *
   * @param board to access the free spaces
   * @param word to see if the word is placeable
   * @return a String with the information on how to place a tile
   */
  public String checkForPlacement(Board board, String word) {
    char array[] = word.toCharArray();
    // will be added to the direction code
    boolean horizontal = false;

    for (int i = 0; i < getFoundWords().size(); i++) {
      // first check if the word contains the chars. than find the word position and if its
      // horizontal or vertical
      String foundWordsArray[] = (String[]) getFoundWords().toArray();
      if (!contains2(foundWordsArray[i], word).equals("")) {
        // get the information if the word has to be placed vertically or horizon
        String wordP = findWord(board, word);
        String s[] = wordP.split("\\|");
        String firstIndex[] = s[0].split("\\.");
        String secondIndex[] = s[1].split("\\.");
        if (firstIndex[0].equals(secondIndex[0])) {
          horizontal = true;
        }
        // get the index of the chars which are matching with the word
        String posChars[];

        if (horizontal) {
          posChars = contains2(foundWordsArray[i], word).split("\\|");
          for (int j = 0; j < posChars.length; j++) {
            // check if the word is placeable vertically
            // Board board, int row, int col, char c, String word, boolean horizontal
            String rowColPos[] = posChars[i].split("\\.");
            int col = Integer.parseInt(rowColPos[1]);

            int charIndex = col - Integer.parseInt(firstIndex[1]);

            if (wordIsPlaceable(
                board,
                Integer.parseInt(rowColPos[0]),
                Integer.parseInt(rowColPos[1]),
                word.charAt(charIndex),
                word,
                true)) {
              // return pos to set
              return firstIndex[0]
                  + "."
                  + firstIndex[1]
                  + "|"
                  + rowColPos[0]
                  + "."
                  + rowColPos[1]
                  + "v";
            }
          }
        } else {
          posChars = contains2(foundWordsArray[i], word).split("\\|");
          for (int j = 0; j < posChars.length; j++) {
            // check if the word is placeable vertically
            // Board board, int row, int col, char c, String word, boolean horizontal
            String rowColPos[] = posChars[i].split("\\.");
            int r = Integer.parseInt(rowColPos[0]);

            int charIndex = r - Integer.parseInt(firstIndex[0]);

            if (wordIsPlaceable(
                board,
                Integer.parseInt(rowColPos[0]),
                Integer.parseInt(rowColPos[1]),
                word.charAt(charIndex),
                word,
                false)) {
              // return pos to set
              return firstIndex[0]
                  + "."
                  + firstIndex[1]
                  + "|"
                  + rowColPos[0]
                  + "."
                  + rowColPos[1]
                  + "h";
            }
          }
        }
      }
    }
    return "";
  }

  /**
   * i wrote a contains method, to check wether a word in the hand of the player is playable, by
   * comparing every char. The reason for it, is that it is hard to do so with a RegEX
   */
  private boolean contains(String word, String regEx) {
    word.toUpperCase();
    char array[] = word.toCharArray();
    StringBuilder sb = new StringBuilder(regEx);
    int charInHand = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array.length; j++) {
        if (array[i] == sb.charAt(j)) {
          charInHand++;
          // delete the char at pos j
          sb.deleteCharAt(j);
        }
      }
    }

    if (charInHand < word.length()) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * the second contains method just checks wether a word has a char of a found word or not if yes,
   * we might can place the found word at the char yes means we send the place of the tile to the
   * checkforPlacement method, more tiles are marked through a seperator | Example 1.1|1.3
   */
  private String contains2(String word, String wordToPlace) {
    word.toUpperCase();
    char array[] = word.toCharArray();
    StringBuilder sb = new StringBuilder(wordToPlace);
    String positions = "";
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array.length; j++) {
        if (array[i] == sb.charAt(j)) {
          ++count;
          if (count > 1 && i != (array.length - 1)) {
            positions += Integer.toString(i) + "." + Integer.toString(j) + "|";
          } else if (i == (array.length - 1)) {
            positions += Integer.toString(i) + "." + Integer.toString(j);
          } else {
            positions += Integer.toString(i) + "." + Integer.toString(j);
          }
        }
      }
    }
    return positions;
  }

  /**
   * returns the position of the first and the last index of the word as a String sep by |. Example
   * 1.1|1.3
   *
   * @return
   */
  private String findWord(Board board, String word) {
    String pos = "";
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        if (board.getField(i, j).getTile().getLetter() == word.charAt(0)) {
          if (j == 14) {
            return Integer.toString(i) + ".14|" + Integer.toString(i + (word.length() - 1)) + ".14";
          } else if (i == 14) {
            return "14."
                + Integer.toString(j)
                + "|"
                + "14."
                + Integer.toString(j + (word.length() - 1));
          } else {
            // horizontal
            if (board.getField(i, (j + 1)).getTile().getLetter() == word.charAt(1)) {
              return Integer.toString(i)
                  + "."
                  + Integer.toString(j)
                  + "|"
                  + Integer.toString(i)
                  + "."
                  + Integer.toString(j + (word.length() - 1));
            } else {
              // vertical
              return Integer.toString(i)
                  + "."
                  + Integer.toString(j)
                  + Integer.toString(i + (word.length() - 1))
                  + "."
                  + Integer.toString(j);
            }
          }
        }
      }
    }
    return pos;
  }
  // last charIn is the col
  /**
   * checks if the word is placeable in the given position horizontal true means we have to place
   * the word verticaly word is the word that we want to place. However row and col are the place of
   * the char c of the already placed word
   */
  private boolean wordIsPlaceable(
      Board board, int row, int col, char c, String word, boolean horizontal) {
    int charIndex = 0;
    for (int i = 0; i < word.length(); i++) {
      if (c == word.charAt(i)) {
        charIndex = i;
      }
    }
    if (horizontal) {
      // check if vertical placement is possible
      // if word is not placeable
      if ((row + (word.length() - 1) - charIndex) > 14) {
        return false;
      }
      // check for word
      int firstRow = row - charIndex;
      int firstCol = col;
      for (int i = 0; i < word.length(); i++) {
        if (i != charIndex) {
          if (!board.getField(firstRow + i, col).isEmpty()) {
            return false;
          }
        }
      }

      if (checkLeftRightWord(board, firstRow, firstCol, word)) {
        return true;
      } else {
        return false;
      }
    } else {
      int firstRow = row;
      int firstCol = col - charIndex;
      for (int i = 0; i < word.length(); i++) {
        if (i != charIndex) {
          if (!board.getField(firstRow, col).isEmpty()) {
            return false;
          }
        }
      }
      for (int i = 0; i < word.length(); i++) {
        if (i != charIndex) {
          if (!board.getField(firstRow, firstCol + i).isEmpty()) {
            return false;
          }
        }
      }
      if (checkUpDownWord(board, firstRow, firstCol, word)) {
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * return if the word is placeable even with word combinations left and right
   *
   * @param board
   * @param row row and col are from the first index of the word
   * @param col
   * @param word
   * @return
   */
  private boolean checkLeftRightWord(Board board, int row, int col, String word) {
    for (int i = 0; i < word.length(); i++) {
      StringBuilder sb = new StringBuilder();
      // first append the char
      sb.append(board.getField(row + i, col).getTile().getLetter());
      // upper tile and down tile
      int lastIndex = row + (word.length() - 1);
      if (row > 0) {
        if (!board.getField(row - 1, col).isEmpty()) {
          return false;
        }
      }
      if (lastIndex < 14) {
        if (!board.getField(row + 1, col).isEmpty()) {
          return false;
        }
      }

      // left
      if (col > 0) {
        int left = 0;
        if (!board.getField(row + i, col - (left + 1)).isEmpty()) {
          for (int x = col; x >= 0; x--) {
            if (!board.getField(row + i, col - (left + 1)).isEmpty()) {
              sb.append(board.getField(row + i, col - (left + 1)).getTile().getLetter());
            }
            left++;
          }
          sb.reverse();
        }
      }
      // right
      if (col < 14) {
        int right = 0;
        if (!board.getField(row + i, col + (right + 1)).isEmpty()) {
          for (int x = col; x < 15; x++) {
            if (!board.getField(row + i, col + (right + 1)).isEmpty()) {
              sb.append(board.getField(row + i, col + (right + 1)).getTile().getLetter());
            }
            right++;
          }
        }
      }
      if (game.getDictionary().wordExists(sb.toString())) {
        return true;
      } else {
        return false;
      }
    }
    return true;
  }

  private boolean checkUpDownWord(Board board, int row, int col, String word) {
    for (int i = 0; i < word.length(); i++) {
      StringBuilder sb = new StringBuilder();
      // first append the char
      sb.append(board.getField(row, col + i).getTile().getLetter());
      // upper tile and down tile
      int rightIndex = col + (word.length() - 1);
      if (col > 0) {
        if (!board.getField(row, col - 1).isEmpty()) {
          return false;
        }
      }
      if (rightIndex < 14) {
        if (!board.getField(row, col + 1).isEmpty()) {
          return false;
        }
      }

      // up
      if (row > 0) {
        int up = 0;
        if (!board.getField(row - (up + 1), col + i).isEmpty()) {
          for (int x = row; x >= 0; x--) {
            if (!board.getField(row - (up + 1), col + i).isEmpty()) {
              sb.append(board.getField(row - (up + 1), col + i).getTile().getLetter());
            }
            up++;
          }
          sb.reverse();
        }
      }
      // down
      if (row < 14) {
        int down = 0;
        if (!board.getField(row + (down + 1), col + i).isEmpty()) {

          for (int x = row; x < 15; x++) {
            if (!board.getField(row + (down + 1), col + i).isEmpty()) {
              sb.append(board.getField(row + (down + 1), col + i).getTile().getLetter());
            }
            down++;
          }
        }
      }
      if (game.getDictionary().wordExists(sb.toString())) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }
}
