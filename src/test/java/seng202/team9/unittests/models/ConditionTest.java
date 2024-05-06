package seng202.team9.unittests.models;
import org.junit.jupiter.api.Test;
import seng202.team9.io.CSVImporter;
import seng202.team9.models.Condition;
import seng202.team9.models.Crash;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the string methods of the condition category class.
 */
class ConditionTest {

    /**
     * Generates a manual crash instance.
     * Crash which sets speed limit to 100 ,Advisory speed to 60
     * Temp speed to 20.
     * This instance also has weatherA set to "Fine" and weatherB set to "Light Rain"
     * Creation process previously tested in CSVImporterTest.
     */
    public Crash generateCrashOne() {
        String[] sampleString = new String[]{"1317619", "60", "0", "0", "1", "0", "0", "West", "2021/2022", "KING STREET",
                "SUBSTATION LANE", "", "Non-Injury Crash", "No", "2022", "0", "North", "0", "0", "", "Hill Road", "0",
                "", "0", "", "0", "Bright sun", "0", "2", "0", "2", "0", "0", "0", "0", "0", "", "0", "2",
                "Northland Region", "Nil", "2-way", "Sealed", "0", "0", "0", "0", "100", "0", "Off", "0", "0", "20",
                "Whangarei District", "Nil", "0", "1", "0", "0", "0", "0", "Urban", "0", "0", "0", "Fine", "Light Rain",
                "-35.5935471649473", "174.284470913299"};
        CSVImporter importer = new CSVImporter();
        return importer.readCrashFromLine(sampleString);
    }

    /**
     * Generates a manual crash instance.
     * This instance also has weatherB set to "" and weatherB set to "Light Rain"
     * Creation process previously tested in CSVImporterTest.
     */
    public Crash generateCrashTwo() {
        String[] sampleString = new String[]{"1317619", "60", "0", "0", "1", "0", "0", "West", "2021/2022", "KING STREET",
                "SUBSTATION LANE", "", "Non-Injury Crash", "No", "2022", "0", "North", "0", "0", "", "Hill Road", "0",
                "", "0", "", "0", "Bright sun", "0", "2", "0", "2", "0", "0", "0", "0", "0", "", "0", "2",
                "Northland Region", "Nil", "2-way", "Sealed", "0", "0", "0", "0", "100", "0", "Off", "0", "0", "20",
                "Whangarei District", "Nil", "0", "1", "0", "0", "0", "0", "Urban", "0", "0", "0", "", "Light Rain",
                "-35.5935471649473", "174.284470913299"};
        CSVImporter importer = new CSVImporter();
        return importer.readCrashFromLine(sampleString);
    }


    /**
     * Generates an empty manual crash instance.
     * To check for the default/empty situation.
     */
    public Crash generateCrashEmpty() {
        String[] sampleString = new String[]{"99", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "0", "0", "0", "0", "0", "0", "", "", "", "",
                "", "", "0", "0", "0", "0", "0", "0", "", "0", "0", "0", "", "",
                "", ""};
        CSVImporter importer = new CSVImporter();
        return importer.readCrashFromLine(sampleString);
    }

    /**
     * Tests the speedString method of a crash entries condition class.
     * Has test case which all three entries are not null.
     * A fully empty crash entry with nothing relating to speed.
     */
    @Test
    void getSpeedString() {
        Crash crashToTest = generateCrashOne(); // Crash with all 3 set.
        Condition testCondition = crashToTest.getCondition();
        String expected = "Speed Limit: 100\nAdvisory Speed: 60\nTemporary Speed Limit: 20";
        assertEquals(expected, testCondition.getSpeedString());

        crashToTest = generateCrashEmpty(); // empty crash
        testCondition = crashToTest.getCondition();
        assertEquals("Speed Conditions\nUnknown.", testCondition.getSpeedString());
    }


    /**
     * Tests the weatherString method of a crash entries condition class.
     * Has test case which both attributes are entered, and where none are entered.
     * A fully empty crash entry with nothing relating to speed.
     */
    @Test
    void getWeatherString() {
        Crash crashToTest = generateCrashOne(); // Crash with weather set to fine, and light rains
        Condition testCondition = crashToTest.getCondition();
        String expected = "Fine & Light Rain";
        assertEquals(expected, testCondition.getWeatherString());

        crashToTest = generateCrashTwo(); // empty crash
        testCondition = crashToTest.getCondition();
        assertEquals("Light Rain", testCondition.getWeatherString());

        crashToTest = generateCrashEmpty(); // empty crash
        testCondition = crashToTest.getCondition();
        assertEquals("Unknown", testCondition.getWeatherString());
    }
}