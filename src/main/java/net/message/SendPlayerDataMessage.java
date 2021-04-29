package net.message;

import client.PlayerProfile;

/** @author vkaczmar a message class to send the data of the player */
public class SendPlayerDataMessage extends Message {
  private PlayerProfile profile;
  private int id;

  /**
   * a constructor to create a SendPlayerDataMessage
   *
   * @param id requires id
   */
  public SendPlayerDataMessage(int id) {
    super(MessageType.SENDPLAYERDATA);
    this.id = id;
  }

  /** @return Returns profile from Message */
  public PlayerProfile getProfile() {
    return profile;
  }

  /**
   * Method for setting the playerProfile inside the message
   *
   * @param profile requires the playerProfile
   */
  public void setProfile(PlayerProfile profile) {
    this.profile = profile;
  }

  /** @return returns the id of the player message */
  public int getID() {
    return id;
  }
}
