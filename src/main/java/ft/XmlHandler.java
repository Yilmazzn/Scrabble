package ft;

import client.PlayerProfile;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlHandler {

  /**
   * @author nsiebler This is the class that contains the Methods to Handle the XML files With that
   *     i mean create new files and update files that are already existing as well as read the
   *     values out of the data. This is made by the playertoElement and elementToPlayer functions
   *     Get Player Functionality is made by the ArrayList
   *     <p>This class creates the XML Document based on the NewPlayer Class We want a method that
   *     just creates the Document here. The deletion and change of values is done on another Class.
   *     We just wanna make sure that we can create the doc that we created here
   */
  private static final String sep = System.getProperty("file.separator");

  public static final String XML_PATH = System.getProperty("user.dir") + sep + "res" + sep + "profiles.xml";

  public static Document xmlDoc;

  private static void initDocument() {
    if (xmlDoc == null) {
      // Prepare SAXBuilder
      SAXBuilder builder = new SAXBuilder();
      try {
        // Make sure the XML file exists
        File resDir = new File(System.getProperty("user.dir") + sep + "res");
        if(!resDir.exists()){
          if(!resDir.mkdir()){  // Create directory
            System.out.println("Directory 'res' could not be created.");
          }
        }
        File xmlFile = new File(XML_PATH);
        if(!xmlFile.exists()){
          if(!xmlFile.createNewFile()){  // Create new file
            System.out.println("File 'profiles.xml' could not be created.");
          }else{
            FileWriter writer = new FileWriter(xmlFile);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n<PlayerProfiles></PlayerProfiles>"); // Write new XML
            writer.flush();
            writer.close();
          }
        }
        xmlDoc = builder.build(xmlFile);
      } catch (JDOMException | IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Method saves the document given by the attribute xmlDoc in XML format to a file (defined in
   * Parameters class)
   */
  public static void saveXML(List<PlayerProfile> profiles) {
    // Prepare XMLOutputter and it's format
    if (xmlDoc == null) {
      initDocument();
    }
    // delete
    xmlDoc.getRootElement().removeContent();
    // write profiles
    for (PlayerProfile profile : profiles) {
      xmlDoc.getRootElement().addContent(playerToElement(profile));
    }
    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
    try {
      // Save XML
      xmlOutput.output(xmlDoc, new FileOutputStream(XML_PATH));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This Method creates a New Player Instance, which can be changed based on new Values for example
   */
  public static PlayerProfile elementToPlayer(Element player) {
    PlayerProfile nPlayer = null;
    try {

      // Local Date logged is 0/0/0
      // And String from creation needs to be changed into a date
      // Create the LocalDate Values from the String
      String[] datumStr = player.getChild("creationDate").getText().split("\\.");
      LocalDate creation =
          LocalDate.of(
              Integer.parseInt(datumStr[2]),
              Integer.parseInt(datumStr[1]),
              Integer.parseInt(datumStr[0]));

      String[] lastLoggedStr = player.getChild("lastLogged").getText().split("\\.");
      LocalDate lastLogged =
          LocalDate.of(
              Integer.parseInt(lastLoggedStr[2]),
              Integer.parseInt(lastLoggedStr[1]),
              Integer.parseInt(lastLoggedStr[0]));

      nPlayer =
          new PlayerProfile(
              player.getChildText("name"),
              Integer.parseInt(player.getChildText("highscore")),
              Integer.parseInt(player.getChildText("wins")),
              Integer.parseInt(player.getChildText("losses")),
              Integer.parseInt(player.getChildText("totalScore")),
              creation,
              lastLogged);
      return nPlayer;
    } catch (Exception exp) {
      System.out.println(exp);
      System.exit(0);
    }

    return nPlayer;
  }

  /**
   * This Method creates an Element which can be added to the xml file
   *
   * @param nPlayer
   * @return Element that can be added to the xml
   */
  public static Element playerToElement(PlayerProfile nPlayer) {
    // Root Element
    Element newPlayer = new Element("profile");

    Element name = new Element("name");
    name.addContent(nPlayer.getName());
    // For xml we treat the Integers as Strings to avoid using
    // Attributes and faciliate the work by treating everthing as Strings

    Element highscore = new Element("highscore");
    highscore.addContent(Integer.toString(nPlayer.getHighscore()));

    Element wins = new Element("wins");
    wins.addContent(Integer.toString(nPlayer.getWins()));

    Element losses = new Element("losses");
    losses.addContent(Integer.toString(nPlayer.getLosses()));

    Element totalScore = new Element("totalScore");
    totalScore.addContent(Integer.toString(nPlayer.getTotalScore()));

    // last Value
    Element creationDate = new Element("creationDate");
    creationDate.addContent(nPlayer.getCreation());

    Element lastLogged = new Element("lastLogged");
    lastLogged.addContent(nPlayer.getCreation());

    // add all the content, also the content that will follow
    newPlayer.addContent(name);
    newPlayer.addContent(highscore);
    newPlayer.addContent(wins);
    newPlayer.addContent(losses);
    newPlayer.addContent(totalScore);
    newPlayer.addContent(creationDate);
    newPlayer.addContent(lastLogged);

    return newPlayer;
  }

  /**
   * Worked Sav Method which generates a updated XML-file the normal save method still has to be
   * their because this method saves the xml document itself
   */
  public static List<PlayerProfile> loadProfiles() {
    // Have to reset the List every time
    List<PlayerProfile> players = new ArrayList<PlayerProfile>();

    if (xmlDoc == null) {
      initDocument();
    }
    Element root = xmlDoc.getRootElement();

    List<Element> all = root.getChildren();
    Iterator<Element> it = all.iterator();

    while (it.hasNext()) {
      Element ele = it.next();
      players.add(elementToPlayer(ele));
    }

    return players;
  }

  /** Deletes the old Document when the Player Instances are changed and added to a new xml file */
  static void deleteXML() {
    try {
      Files.delete(Path.of(XML_PATH));
    } catch (NoSuchFileException x) {
      System.err.format("%s: no such" + " file or directory%n", XML_PATH);
    } catch (IOException x) {
      System.err.println(x);
    }
  }
}
