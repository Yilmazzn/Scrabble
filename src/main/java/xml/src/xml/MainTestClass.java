package xml.src.xml;

import org.jdom2.Document;
import org.jdom2.Element;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MainTestClass {
// Just a test class to see if we can create the xml file
	public static void main(String[] args) {
		// all the values here will be read out of the other game classes later on
/*
		LocalDate date = LocalDate.now();
		for(int i = 0; i <= 5; i++) {
			PlayerProfile n = new PlayerProfile("Player " + Integer.toString(i+10), 100, 10, 0, 0, 0, 1.0, date, date);
			if(i == 0){
				XMLDocument.createNewPlayer(n);
			}
			XMLHandler.addNewPlayer(n);
		}
        XMLDocument.saveXML();

		List<PlayerProfile> play = XMLHandler.read();
		Iterator<PlayerProfile> it = play.iterator();

		while(it.hasNext()){
			PlayerProfile a = it.next();
			System.out.println(a);
		}
		XMLDocument.saveXML();
	}

*/

	}

}
