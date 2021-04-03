package xml.src.xml;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

/*  @author nsiebler
 * This class builds an Element out of a player instance or a
 *  player instance out of an ELement. I created an extra class for this, because 
 *  you just have a better overview about which class provides which functionality
 *   */


public class PlayerToElement {

	// This Method creates an Element which can be added to the xml file
	public static Element playerToElement(NewPlayer nPlayer) {
	// Root Element
		Element newPlayer = new Element("NewPlayer");
	
		Element name = new Element("Name");
		name.addContent(nPlayer.getName());
		// For xml we treat the Integers as Strings to avoid using 
		// Attributes and faciliate the work by treating everthing as Strings
		
		Element highscore = new Element("Highscore");
		highscore.addContent(Integer.toString(nPlayer.getHighscore()));
		
		Element playtime = new Element("Playtime");
		playtime.addContent(Double.toString(nPlayer.getHighscore()));
		
		Element allPlayedGames = new Element("PlayedGames");
		allPlayedGames.addContent(Integer.toString(nPlayer.getPlayedGames()));
		
		Element wins = new Element("Wins");
		wins.addContent(Integer.toString(nPlayer.getWins()));
		
		Element losses = new Element("Looses");
		losses.addContent(Integer.toString(nPlayer.getLooses()));
		
		
		// last Value
		Element creationDate = new Element("CreationDate");
		creationDate.addContent(nPlayer.dateToString(nPlayer.getCreation()));
		
		// add all the content, also the content that will follow
	newPlayer.addContent(name);
	newPlayer.addContent(highscore);
	newPlayer.addContent(playtime);
	newPlayer.addContent(allPlayedGames);
	newPlayer.addContent(wins);
	newPlayer.addContent(losses);
	newPlayer.addContent(creationDate);
		return newPlayer;
	}
	
	
	// This Method creates a New Player Instance, which can be changed 
	// based on new Values for example
	
	public static NewPlayer elementToPlayer(Element player) {	
	NewPlayer nPlayer = null;
	try {
		// average score is just value 1
		// Local Date logged is 0/0/0
		// And String from creation needs to be changed into a date
		
		
		// Create the LocalDate Values from the String
		String[] datumsStr = player.getChild("Datum").getText().split("/");
		LocalDate creation = LocalDate.parse(player.getChildText("CreationDate"), 
				DateTimeFormatter.ofPattern("d/MM/yyyy"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		LocalDate lastLogged = LocalDate.of(0, 0, 0);
	
		nPlayer = new NewPlayer(player.getChildText("NewPlayer"), 
				Integer.parseInt(player.getChildText("Highscore")),
				Integer.parseInt(player.getChildText("Playtime")), 
				Integer.parseInt(player.getChildText("PlayedGames")),
				Integer.parseInt(player.getChildText("Wins")), 
						Integer.parseInt(player.getChildText("Looses")),1.0,
						creation,lastLogged);
		} catch (Exception exp) {
			System.out.println(exp);
			System.exit(0);
		}
	
		return nPlayer;
	}
	
	
}
