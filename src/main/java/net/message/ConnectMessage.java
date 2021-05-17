package net.message;

import client.PlayerProfile;
import game.players.Player;

/**
 * An message that handles the player's connect Request to the server
 *
 * @author ygarip
 */
public class ConnectMessage extends Message {
  private String username;
  private int id = 0;
  private PlayerProfile profile;
  private PlayerProfile[] profiles;

  /**
   * an constructor to create a ConnectMessage with the username of the player which sends a connect
   * Request to the server
   *
   * @param username requires the username of the player who wants to connect
   * @param profile requires PlayerProfile
   */
  // todo probably delete later if not used
  public ConnectMessage(String username, PlayerProfile profile) {
    super(MessageType.CONNECT);
    this.username = username;
    this.profile = profile;
  }

  /**
   * a constructor to create a ConnectMessage with a playerprofile
   *
   * @param profile Requires a PlayerProfile
   */
  public ConnectMessage(PlayerProfile profile) {
    super(MessageType.CONNECT);
    this.profile = profile;
  }

  /**
   * a method to get the username of the player who wants to connect
   *
   * @return returns the username of the player who wants to connects
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * sets id of ConnectMessage object
   *
   * @param id requires id
   */
  public void setID(int id) {
    this.id = id;
  }

  /** @return returns the id of the ConnectMessage object */
  public int getID() {
    return id;
  }

  /** @return returns the playerprofile */
  public PlayerProfile getProfile() {
    return profile;
  }

  /**
   * sets profile array in message
   *
   * @param profiles requires profiles array to be set
   */
  public void setProfiles(PlayerProfile[] profiles) {
    this.profiles = profiles;
  }

  /** @return returns to the connected player all connected playerprofiles */
  public PlayerProfile[] getProfiles() {
    return this.profiles;
  }
}
