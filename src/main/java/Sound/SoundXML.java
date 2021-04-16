package Sound;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* @author Nicolas Siebler
 * This class handles the xml file which solely contains the state of the variable muted
 */
public class SoundXML {
    public static Document soundXML;
    private final static String sep = System.getProperty("file.separator");
    private final static String datadir = System.getProperty("user.dir") + sep + "src/main/resources/";
    public final static String xmlSoundPath = datadir + "muteStatus.xml";


    // save the XML
    static void saveXML() {
        // Prepare XMLOutputter and it's format
        if (soundXML == null) {
            soundXML = getXMLDoc();
        }
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
        try {
            // Save XML
            xmlOutput.output(soundXML, new FileOutputStream(xmlSoundPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // create the sound xml
    static void createNewSoundFile() {
        Element rootEle = new Element("MuteStatus");
        Element status = new Element("Status");
        status.addContent(String.valueOf(Sound.muted));
        soundXML = new Document(rootEle);
        if (status != null) {
            soundXML.getRootElement().addContent(status);
        }
    }

    // get the XML from Ressources
    public static Document getXMLDoc() {
        if (soundXML == null) {
            // Prepare SAXBuilder
            SAXBuilder builder = new SAXBuilder();
            try {
                // Make sure the XML file exists and return it
                File xmlFile = new File(xmlSoundPath);
                if (xmlFile.exists() && xmlFile.canRead()) {
                    soundXML = builder.build(xmlFile);

                }
            } catch (JDOMException | IOException e) {
                e.printStackTrace();
            }
        }
        return soundXML;
    }
}
