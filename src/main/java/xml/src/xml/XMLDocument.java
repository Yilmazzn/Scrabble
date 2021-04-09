package xml.src.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class XMLDocument {

	/* @author nsiebler
	 * This class creates the XML Document based on the NewPlayer Class
     * We want a method that just creates the Document here. The deletion and change of
	 * values is done on another Class. We just wanna make sure that we can create the
	 * doc that we created here
	*/
	
	public static Document xmlDoc;
	private final static String sep = System.getProperty("file.separator");
	private final static String datadir = System.getProperty("user.dir") +  sep + "src/main/resources/";
	public final static String xmlPath =  datadir + "player.xml";
	
	// This method create a new XML Document with all the important values
    // First initialization
	
	// Where is the xml doc i cant find it
	static void createNewPlayer(PlayerProfile nPlayer){
		Element rootEle = new Element("Players");  
		Element play = XMLHandler.playerToElement(nPlayer);
		xmlDoc = new Document(rootEle);		
		if (play != null) {
			xmlDoc.getRootElement().addContent(play);
		}
		
	}
	
	/**
	 * Method  saves the document given by the attribute
	 * xmlDoc in XML format to a file (defined in Parameters class)
	 * 
	 */
	 static void saveXML() {
		// Prepare XMLOutputter and it's format
		if (xmlDoc == null) {
			xmlDoc = getXMLDoc();
		}
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
		try {
			// Save XML
			xmlOutput.output(xmlDoc, new FileOutputStream(xmlPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Method which gives you the xml doc back

	 // Path is not perfect by now and needs to be redone
	public static Document getXMLDoc() {
		if (xmlDoc == null) {
			// Prepare SAXBuilder
			SAXBuilder builder = new SAXBuilder();
			try {
				// Make sure the XML file exists and return it
				File xmlFile = new File(xmlPath);
				if (xmlFile.exists() && xmlFile.canRead()){
					xmlDoc = builder.build(xmlFile);
				
				}
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		} 
		return xmlDoc;
	}

}
