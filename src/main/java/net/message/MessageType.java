package net.message;

/**
 * an enum class for the message types that need to be handled
 *
 * @author ygarip not completed yet
 */
public enum MessageType {
  DISCONNECT, // DONE
  STARTGAME, // TODO send TextFile? What about root in Dictionary? Other GameSettings?
  PLAYERREADY, // DONE
  CONNECT, // DONE
  CHATMESSAGE, // DONE
  UPDATEGAMEBOARD, // DONE
  SUBMITMOVE, // DONE
  UPDATEPOINTS, // DONE
  REMOVEPLAYER, // in playerlobby
  KICKPLAYER, // ingame kick players
  GOTOMENU, // TODO go to menu and get back into the game?
  SENDSTATISTICS,
  DISPLAYSTATISTICS,
  WRONGMOVE,
  HOSTSELECTION,
}
