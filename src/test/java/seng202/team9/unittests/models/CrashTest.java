package seng202.team9.unittests.models;

import org.junit.jupiter.api.Test;
import seng202.team9.io.CSVImporter;
import seng202.team9.models.Attribute;
import seng202.team9.models.Crash;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * This class contains JUnit test cases for the Crash class and its associated attributes.
 */
public class CrashTest {


    /**
     * Creates a sample Crash object for testing.
     *
     * @return A sample Crash object with predefined attributes.
     */
    public Crash createCrash() {
        String[] sampleString = new String[]{"123456", "90", "0", "0", "1", "0", "0", "West", "2021/2022", "KING STREET",
                "SUBSTATION LANE", "", "Non-Injury Crash", "No", "2022", "0", "North", "0", "135", "", "Hill Road", "0",
                "", "0", "", "0", "Bright sun", "325", "2", "0", "2", "333", "0", "0", "0", "0", "", "0", "2",
                "Northland Region", "Nil", "2-way", "Sealed", "111", "0", "222", "0", "100", "0", "Off", "0", "0", "80",
                "Whangarei District", "Nil", "0", "1", "0", "0", "0", "0", "Urban", "0", "0", "0", "Fine", "Windy",
                "-35.5935471649473", "174.284470913299"};

        CSVImporter importer = new CSVImporter();
        return importer.readCrashFromLine(sampleString);
    }

    /**
     * Tests the retrieval of Crash detail attributes from a Crash object.
     */
    @Test
    public void testGetCrashAttributes() {
        Crash testCrash = createCrash();
        ArrayList<Attribute> testCrashAttributes  = testCrash.getCrashDetailAttributes();
        assertEquals(123456, testCrashAttributes.get(0).value);
        assertEquals("2021/2022", testCrashAttributes.get(1).value);
        assertEquals("", testCrashAttributes.get(2).value);
        assertEquals("Non-Injury Crash", testCrashAttributes.get(3).value);
        assertEquals(0, testCrashAttributes.get(4).value);
        assertEquals(135, testCrashAttributes.get(5).value);
        assertEquals(325, testCrashAttributes.get(6).value);
        assertEquals(222, testCrashAttributes.get(7).value);
        assertEquals(333, testCrashAttributes.get(8).value);
    }

    /**
     * Tests the retrieval of Condition attributes from a Crash object.
     */
    @Test
    public void testGetConditionAttributes() {
        Crash testCrash = createCrash();
        ArrayList<Attribute> testCrashAttributes  = testCrash.getCondition().getConditionAttributes();
        assertEquals("Sealed", testCrashAttributes.get(0).value);
        assertEquals(111, testCrashAttributes.get(1).value);
        assertEquals("Fine", testCrashAttributes.get(2).value);
        assertEquals("Windy", testCrashAttributes.get(3).value);
        assertEquals("2-way", testCrashAttributes.get(4).value);
        assertEquals("", testCrashAttributes.get(5).value);
        assertEquals("Bright sun", testCrashAttributes.get(6).value);
        assertEquals(2, testCrashAttributes.get(7).value);
        assertEquals("Nil", testCrashAttributes.get(8).value);
        assertEquals(90, testCrashAttributes.get(9).value);
        assertEquals(80, testCrashAttributes.get(10).value);
        assertEquals(100, testCrashAttributes.get(11).value);
        assertEquals("Off", testCrashAttributes.get(12).value);
        assertEquals("Nil", testCrashAttributes.get(13).value);
    }
}

