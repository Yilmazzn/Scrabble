package net.message;

import java.io.File;

/**
 * a message to start the game
 *
 * @author ygarip
 */
public class StartGameMessage extends Message {
  private File file;

  /** a constructor to create a StartGame Message */
  public StartGameMessage(File file) {
    super(MessageType.STARTGAME);
    this.file = file;
  }

  /** @return returns the File */
  public File getFile() {
    return this.file;
  }
}
