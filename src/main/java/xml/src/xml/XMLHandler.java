package xml.src.xml;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.application.Platform;
import org.jdom2.Document;
import org.jdom2.Element;

public class XMLHandler {

    /* @author nsiebler
     * This is the class that contains the Methods to Handle the XML files
     * With that i mean create new files and update
     * files  that are already existing as well as read the values out of
     * the data. This is made by the playertoElement and elementToPlayer functions
     * Get Player Functionality is made by the ArrayList
     */

    static List<PlayerProfile> players = null;

    // return the size of the Document
    public static int size() {
        Document doc = XMLDocument.getXMLDoc();
        if (doc != null) {
            return doc.getRootElement().getChildren().size();
        } else {
            return 0;
        }
    }

    // return the player instance with the certain name
    public static PlayerProfile getPlayer(int i) {
        int count = 0;
        Document doc = XMLDocument.getXMLDoc();
        Element root = doc.getRootElement();

        List<Element> all = root.getChildren();
        Iterator<Element> it = all.iterator();

        while (it.hasNext()) {
            Element ele = it.next();
            count++;
            if (count == i) {
                return elementToPlayer(ele);
            }
        }
        return null;
        // Use a toString Method or a sys out inorder to see if this method is working
    }

    // when you add a match this method checks, wether the player name is already existend or not
    static boolean checkIfNameExisting(String name) {
        Document doc = XMLDocument.getXMLDoc();
        Element root = doc.getRootElement();

        List<Element> all = root.getChildren();
        Iterator<Element> it = all.iterator();

        while (it.hasNext()) {
            Element ele = it.next();
            String nameFromElement = ele.getChildText("Name");
            if (name.equals(nameFromElement)) {
                return false;
            }
        }
        return true;
    }

    // Add a new Player, a method that is used when a basic xml Document is already existing
    static void addNewPlayer(PlayerProfile player) {
        Document doc = XMLDocument.getXMLDoc();
        Element root = doc.getRootElement();

        // Iterate over all elements and check for duplicates
        for (Element ele : root.getChildren()) {
            String nameFromElement = ele.getChildText("Name");
            if (player.getName().equals(nameFromElement)) {
                // Print error
                String message = "This playername has already been added!";
                JOptionPane.showMessageDialog(new JFrame(), message, "WARNING", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // Add match to root
        root.addContent(playerToElement(player));
        // Print success
        String message = "New Player was added successfully!";
        JOptionPane.showMessageDialog(new JFrame(), message, "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method which delete Player Profiles
    static boolean deletePlayer(int i) {
        Document doc = XMLDocument.getXMLDoc();
        // here is an error
        Element root = doc.getRootElement();

        List<Element> all = root.getChildren();
        Iterator<Element> it = all.iterator();
        int k = -1;
        boolean found = false;
        while (it.hasNext()) {
            Element ele = it.next();
            k++;
            if (i == k) {
                found = true;
                doc.getRootElement().removeContent(ele);
                // Print success
                String message = "Deleted!";
                JOptionPane.showMessageDialog(new JFrame(), message, "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        if (!found) {
            String message = "Specified element was not found!";
            JOptionPane.showMessageDialog(new JFrame(), message, "WARNING", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return false;
    }


    // This Method creates a New Player Instance, which can be changed
    // based on new Values for example
    public static PlayerProfile elementToPlayer(Element player) {
        PlayerProfile nPlayer = null;
        try {

            // Local Date logged is 0/0/0
            // And String from creation needs to be changed into a date


            // Create the LocalDate Values from the String
            String[] datumStr = player.getChild("CreationDate").getText().split("/");
            LocalDate creation = LocalDate.of(Integer.parseInt(datumStr[2]), Integer.parseInt(datumStr[1]), Integer.parseInt(datumStr[0]));
            LocalDate lastLogged = creation;

            nPlayer = new PlayerProfile(player.getChildText("Name"),
                    Integer.parseInt(player.getChildText("Highscore")),
                    Integer.parseInt(player.getChildText("Playtime")),
                    Integer.parseInt(player.getChildText("PlayedGames")),
                    Integer.parseInt(player.getChildText("Wins")),
                    Integer.parseInt(player.getChildText("Losses")), 1.0,
                    creation, lastLogged);
            return nPlayer;
        } catch (Exception exp) {
            System.out.println(exp);
            System.exit(0);
        }

        return nPlayer;
    }

    // This Method creates an Element which can be added to the xml file
    public static Element playerToElement(PlayerProfile nPlayer) {
        // Root Element
        Element newPlayer = new Element("PlayerProfile");

        Element name = new Element("Name");
        name.addContent(nPlayer.getName());
        // For xml we treat the Integers as Strings to avoid using
        // Attributes and faciliate the work by treating everthing as Strings

        Element highscore = new Element("Highscore");
        highscore.addContent(Integer.toString(nPlayer.getHighscore()));

        Element playtime = new Element("Playtime");
        playtime.addContent(Integer.toString(nPlayer.getHighscore()));

        Element allPlayedGames = new Element("PlayedGames");
        allPlayedGames.addContent(Integer.toString(nPlayer.getPlayedGames()));

        Element wins = new Element("Wins");
        wins.addContent(Integer.toString(nPlayer.getWins()));

        Element losses = new Element("Losses");
        losses.addContent(Integer.toString(nPlayer.getLooses()));


        // last Value
        Element creationDate = new Element("CreationDate");
        creationDate.addContent(nPlayer.dateToString(nPlayer.getCreation()));

        Element lastLogged = new Element("LastLogged");
        lastLogged.addContent(nPlayer.dateToString(nPlayer.getCreation()));

        // add all the content, also the content that will follow
        newPlayer.addContent(name);
        newPlayer.addContent(highscore);
        newPlayer.addContent(playtime);
        newPlayer.addContent(allPlayedGames);
        newPlayer.addContent(wins);
        newPlayer.addContent(losses);
        newPlayer.addContent(creationDate);
        newPlayer.addContent(lastLogged);

        return newPlayer;
    }


    // read method

    // Worked Sav Method which generates a updatet XML-file
    // the normal save method still has to be their because this method saves the xml document itself

    static List read() {
        // Have to reset the List every time
        List<PlayerProfile> players = new ArrayList<PlayerProfile>();


        Document doc = XMLDocument.getXMLDoc();
        Element root = doc.getRootElement();

        List<Element> all = root.getChildren();
        Iterator<Element> it = all.iterator();

        while (it.hasNext()) {
            Element ele = it.next();
            players.add(elementToPlayer(ele));
        }

        return players;
    }

    // When all the PLayer instances got updatet, this method is called and creates the new updatet xml file
    static void updateXML(List<PlayerProfile> players) {
        XMLDocument.deleteXML();

        Iterator<PlayerProfile> it = players.iterator();
        int i = 0;
        while (it.hasNext()) {
            PlayerProfile a = it.next();
            if (i == 0) {
                XMLDocument.createNewPlayer(a);
            } else {
                XMLHandler.addNewPlayer(a);
            }
            i++;

        }
        XMLDocument.saveXML();
    }


}