package ft;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.*;
import java.io.File;

public class Sound {
  /**
   * @author nsiebler This class contains the sounds in the system. Each sound has a method and
   *     refers to a sound in a folder. The sound are called by a Method. This class also contains a
   *     method to mute all sounds The state of the mute variable is saved in a XML Document and
   *     read out of it in order to save preferences
   */
  private static final String sep = System.getProperty("file.separator");

  private static final String datadir =
      System.getProperty("user.dir")
          + sep
          + "src"
          + sep
          + "main"
          + sep
          + "resources"
          + sep
          + "sounds"
          + sep;

  /**
   * This variable contains the soundPath. The regarding sound file name will be added to the path
   * and than the sound will be played
   */
  public static final String soundPath = datadir;

  public static String playerCreated = "PlayerCreated.mp3";
  public static String rightWord = "RightWord.mp3";
  public static String tileSet = "TileSet.mp3";
  public static String titleMusic = "TitleMusic.mp3";
  public static boolean muteStatus = false;
  static MediaPlayer mediaPlayer;

  public static void main(String[] args) {
    playMusic(titleMusic);
  }

  // Mute function
  public static void mute() {
    muteStatus = true;
    if (mediaPlayer != null && mediaPlayer.isAutoPlay()) {
      mediaPlayer.stop();
      mediaPlayer.dispose();
    }
  }

  // Unmute function
  public static void unmute() {
    muteStatus = false;
    if (mediaPlayer != null) {
      mediaPlayer.play();
    }
  }

  /**
   * All the Functions that play the sound are the same, but all the functions have a name with the
   * certain sound file as a variable, to access the certain sound file
   */

  // change all the function into one function and save the string names of the files
  // as class Variables TO DO
  public static void playMusic(String fileName) {
    if (!muteStatus) {
      try {
        // is needed for initialization
        final JFXPanel fxPanel = new JFXPanel();
        String path = datadir + fileName;
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        Thread.sleep(50);
        if (fileName.equals(titleMusic)) {
          mediaPlayer.setAutoPlay(true);
          mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } else {
          mediaPlayer.setAutoPlay(true);
          // make a thread to dispose the mediaplayer afterwards
          try {
            Thread.sleep((long) mediaPlayer.getTotalDuration().toMillis());
            mediaPlayer.dispose();
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void changeTitleMusicStatus() {
    if (mediaPlayer != null && mediaPlayer.isAutoPlay()) {
      mediaPlayer.stop();
    } else if (mediaPlayer != null) {
      mediaPlayer.dispose();
      playMusic(titleMusic);
    }
  }
}
