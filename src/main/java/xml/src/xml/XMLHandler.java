package xml.src.xml;


import java.awt.List;
import java.util.Iterator;

import javax.lang.model.element.Element;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.Document;
import java.io.File;

public class XMLHandler {

	// @author nsiebler
	//This is the class that contains the Methods to Handle the XML files
	// With that i mean create new files and update
	// files  that are already existing as well as read the values out of
	// the data 
	
	
	
	// Maybe error occurs because intellij doesnt have the jar file
	// ***********
 // return the size of the Document
	public static int size(){
		Document doc = XMLDocument.getXMLDoc();
		if (doc != null){
			return doc.getRootElement().getChildren().size();
		} else {
			return 0;
		}
	}
	
	// return the player instance with the certain name
	public static NewPlayer getPlayer(int i){
		int count = 0;
		Document doc = XMLDocument.getXMLDoc();
		Element root = doc.getRootElement();

		List<Element> alles = root.getChildren();
		Iterator<Element> it = alles.iterator();

		while (it.hasNext()) {
			Element ele = it.next();
			if (count == i) {
				return PlayerToElement.elementToPlayer((org.jdom2.Element) ele);
			}
		}
		return null;
		// Use a toString Method or a sys out inorder to see if this method is working
	}
	
	// when you add a match this method checks, wether the player name is already existend or not
static boolean checkIfNameExisting (String name) {
	Document doc = XMLDocument.getXMLDoc();
	Element root = doc.getRootElement();

	List<Element> alles = root.getChildren();
	Iterator<Element> it = alles.iterator();

	while (it.hasNext()) {
		Element ele = it.next();
		String nameFromElement = ele.getChildText("NewPlayer");
		if (name.equals(nameFromElement)) {
			return false;		
		}
	}
	return true;
}
	// Add a new Player, a method that is used when a basic xml Document is already existing
static void addNewPlayer (NewPlayer player) {
	Document doc = BasicXMLHandler.getXMLDoc();
	Element root = doc.getRootElement();

	// Iterate over all elements and check for duplicates
	for (Element ele : root.getChildren()) {
		String nameFromElement = ele.getChildText("NewPlayer");
		if (player.getName().equals(nameFromElement)) {
			// Print error
			String message = "This playername has already been added!";
			JOptionPane.showMessageDialog(new JFrame(), message, "WARNING", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	// Add match to root
	root.addContent(XMLDocument.playerToElement(player));
	// Print success
	String message = "New Player was added successfully!";
	JOptionPane.showMessageDialog(new JFrame(), message, "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
}

// Method which delete Player Profiles

static boolean deletePlayer (int i) {
	Document doc = BasicXMLHandler.getXMLDoc();
	Element ele = getPlayer(i);
	if (ele != null){
		doc.getRootElement().removeContent(ele);
		// Print success
		String message = "Deleted!";
		JOptionPane.showMessageDialog(new JFrame(), message, "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
		return true;
	} else {
		String message = "Specified element was not found!";
		JOptionPane.showMessageDialog(new JFrame(), message, "WARNING", JOptionPane.ERROR_MESSAGE);
		return false;
	}
}
 public static void main(String[] args) {
		

	}

}