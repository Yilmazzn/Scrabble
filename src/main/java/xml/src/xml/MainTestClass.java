package xml.src.xml;

import java.time.LocalDate;

public class MainTestClass {
// Just a test class to see if we can create the xml file
	public static void main(String[] args) {
		// all the values here will be read out of the other game classes later on
		LocalDate date = LocalDate.now();
		NewPlayer n = new NewPlayer("Nicooo",100,1,0,0,0,0.0,date,date);
		XMLDocument.createNewPlayer(n);
		XMLDocument.saveXML();
		
	}

}
