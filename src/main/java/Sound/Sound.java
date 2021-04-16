package Sound;

import org.jdom2.Document;
import org.jdom2.Element;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;



public class Sound {
    /* @author Nicolas Siebler
     * This class contains the sounds in the system. Each sound has a method and refers to a sound in a folder.
     * The sound are called by a Method. This class also contains a method to mute all sounds
     * The state of the mute variable is saved in a XML Document and read out of it in order to save
     * preferences
     * */

    //This variable changes it status based on the status of the mute Button
    public static boolean muted = false;

    private final static String sep = System.getProperty("file.separator");
    private final static String datadir = System.getProperty("user.dir") + sep + "src/main/resources/sounds/";
    /*This variable contains the soundPath. The regarding sound file name will be added to the path and than the sound
     * will be played
     * */
    public final static String soundPath = datadir;
    public static String playerCreated = "PlayerCreated";
    public static String rightWord = "RightWord";
    public static String tileSet = "TileSet";
    public static String titleMusic = "TitleMusic";


    // Mute function
    public static void mute() {
        muted = true;
        Document doc = SoundXML.getXMLDoc();
        Element root = doc.getRootElement();
        Element muted = root.getChild("Status").setText("true");
        SoundXML.saveXML();
    }

    // Unmute function
    public static void unmute() {
        muted = false;
        Document doc = SoundXML.getXMLDoc();
        Element root = doc.getRootElement();
        Element muted = root.getChild("Status").setText("false");
        SoundXML.saveXML();
    }


    /* All the Functions that play the sound are the same, but all the functions have a name
     * with the certain sound file as a variable, to access the certain sound file
     *
     * */

    // change all the function into one function and save the string names of the files
    // as class Variables TO DO
    public static void playMusic(String fileName) {
        // how do i check if the file is already their?
        if (new File(SoundXML.xmlSoundPath).exists()) {
            Document doc = SoundXML.getXMLDoc();
            Element root = doc.getRootElement();
            String muteStatus = root.getChild("Status").getText();
            muted = Boolean.valueOf(muteStatus);
        }
        if (!muted) {
            try {
                String path = datadir + fileName;
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
                // Get a sound clip resource.
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}




