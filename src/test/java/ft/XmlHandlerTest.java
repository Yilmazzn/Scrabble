package ft;

import client.PlayerProfile;
import org.junit.jupiter.api.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yuzun
 * <p>
 * Test class of XmlHandler
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class XmlHandlerTest {

    private String sep = System.getProperty("file.separator");
    private String xmlPathTest = System.getProperty("user.dir") + sep + "src" + sep
            + "test" + sep + "resources" + sep + "xmlTest" + sep + "profilesTest.xml";
    private List<PlayerProfile> profiles;

    /**
     * Used to change XML_PATH in XmlHandler to test path location
     * https://stackoverflow.com/questions/30703149/mock-private-static-final-field-using-mockito-or-jmockit
     */
    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    @AfterAll
    static void tearDown() {

    }

    public static List<PlayerProfile> getMockProfiles() {
        ArrayList<PlayerProfile> profiles = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            String name = "Profile " + i + "";
            Random rand = new Random();
            int highscore = rand.nextInt(1000);
            long playtime = rand.nextLong();
            int wins = rand.nextInt(200);
            int losses = rand.nextInt(200);
            int totalScore = rand.nextInt(1000000);
            LocalDate create = LocalDate.now();
            LocalDate lastLogged = LocalDate.now();
            profiles.add(new PlayerProfile(name, highscore, wins, losses, totalScore, create, lastLogged));
        }

        return profiles;
    }

    @BeforeAll
    void setUp() throws Exception {
        Field pathField = XmlHandler.class.getDeclaredField("XML_PATH");
        setFinalStatic(pathField, xmlPathTest);     // mock static final parameter
        profiles = getMockProfiles();
    }

    @Test
    @Order(1)
    @DisplayName("Save profiles to XML")
    void saveXML() {
        XmlHandler.saveXML(profiles);
        Assertions.assertTrue(new File(xmlPathTest).exists());
    }

    @Test
    @Order(2)
    @DisplayName("Load profiles")
    void loadProfiles() {

        List<PlayerProfile> loadedProfiles = XmlHandler.loadProfiles();
        // size
        if (loadedProfiles == null) {
            Assertions.fail("No profiles loaded (list is null)");
        }

        if (loadedProfiles.size() != profiles.size()) {
            Assertions.fail("Wrong amount of profiles");
        }


        for (int i = 0; i < profiles.size(); i++) {
            System.out.println(profiles.get(i).getName());
            Assertions.assertEquals(profiles.get(i).getName(), loadedProfiles.get(i).getName());
            Assertions.assertEquals(profiles.get(i).getHighscore(), loadedProfiles.get(i).getHighscore());
            Assertions.assertEquals(profiles.get(i).getWins(), loadedProfiles.get(i).getWins());
            Assertions.assertEquals(profiles.get(i).getLosses(), loadedProfiles.get(i).getLosses());
            Assertions.assertEquals(profiles.get(i).getTotalScore(), loadedProfiles.get(i).getTotalScore());
            Assertions.assertEquals(profiles.get(i).getCreation(), loadedProfiles.get(i).getCreation());
            Assertions.assertEquals(profiles.get(i).getLastLogged(), loadedProfiles.get(i).getLastLogged());
        }


    }
}