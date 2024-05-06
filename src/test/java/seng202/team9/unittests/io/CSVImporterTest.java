package seng202.team9.unittests.io;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import seng202.team9.io.CSVImporter;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;

/**
 * This class contains JUnit test cases for the CSVImporter class, which is responsible for reading crash data from CSV strings.
 *
 * @author jda178
 */
public class CSVImporterTest {
    /**
     * Test that valid data can be loaded successfully
     */
    @Test
    public void testReadValidLine() {
        String[] sampleString = new String[]{"1317619", "", "0", "0", "0", "0", "0", "West", "2021/2022", "KING STREET",
                "SUBSTATION LANE", "", "Non-Injury Crash", "No", "2022", "0", "North", "0", "0", "1", "Hill Road", "0",
                "", "0", "", "0", "Bright sun", "0", "0", "0", "2", "0", "0", "0", "0", "0", "", "0", "0",
                "Northland Region", "Nil", "2-way", "Sealed", "0", "0", "0", "0", "50", "0", "Off", "1", "0", "",
                "Whangarei District", "Nil", "0", "0", "0", "1", "0", "0", "Urban", "0", "0", "0", "Fine", "Null",
                "-35.5935471649473", "174.284470913299"};
        CSVImporter imp = new CSVImporter();
        Crash crash = imp.readCrashFromLine(sampleString);
        Assertions.assertNotNull(crash);
    }

    /**
     * Test that attempting to load invalid data fails
     */
    @Test
    public void testReadWrongFormatLine() {
        // Number of bicycles (should be int) now a String
        String[] sampleString = new String[]{"1317619", "", "INVALID", "0", "0", "0", "0", "West", "2021/2022", "KING STREET",
                "SUBSTATION LANE", "", "Non-Injury Crash", "No", "2022", "0", "North", "0", "0", "1", "Hill Road", "0",
                "", "0", "", "0", "Bright sun", "0", "0", "0", "2", "0", "0", "0", "0", "0", "", "0", "0",
                "Northland Region", "Nil", "2-way", "Sealed", "0", "0", "0", "0", "50", "0", "Off", "1", "0", "",
                "Whangarei District", "Nil", "0", "0", "0", "1", "0", "0", "Urban", "0", "0", "0", "Fine", "Null",
                "-35.5935471649473", "174.284470913299"};
        CSVImporter imp = new CSVImporter();
        Crash crash = imp.readCrashFromLine(sampleString);
        Assertions.assertNull(crash);
    }

    /**
     * Test that loading missing data doesn't error, but instead fills it with the default value for the type.
     */
    @Test
    public void testReadMissingDataLine() {
        String[] sampleString = new String[]{"1317619", "",
                "", // Number of bicycles (should be int) now a blank String
                "0", "0", "0", "0", "West", "2021/2022", "KING STREET",
                "SUBSTATION LANE", "", "Non-Injury Crash", "No", "2022", "0", "North", "0", "0", "1", "Hill Road", "0",
                "", "0", "", "0", "Bright sun", "0", "0", "0", "2", "0", "0", "0", "0", "0", "", "0", "0",
                "Northland Region", "Nil", "2-way", "Sealed", "0", "0", "0", "0", "50", "0", "Off", "1", "0", "",
                "Whangarei District", "Nil", "0", "0", "0", "1", "0", "0", "Urban", "0", "0", "0", "Fine", "Null",
                "-35.5935471649473", "174.284470913299"};
        CSVImporter imp = new CSVImporter();
        Crash crash = imp.readCrashFromLine(sampleString);
        Assertions.assertNotNull(crash);
        Assertions.assertEquals(Integer.class, crash.getAttribute(AttributeType.BICYCLE).getClass());
    }
}
