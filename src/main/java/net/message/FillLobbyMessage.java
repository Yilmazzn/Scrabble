package net.message;

import client.PlayerProfile;
/** @author vihofman Message for adding player to lobby */
public class FillLobbyMessage extends Message{

    private PlayerProfile[] profiles;

    public FillLobbyMessage(PlayerProfile[] profile){
        super(MessageType.FILLLOBBYMESSAGE);
        this.profiles = profiles;
    }

    public PlayerProfile[] getProfiles(){
        return profiles;
    }
}
