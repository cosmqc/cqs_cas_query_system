package seng202.team9.unittests.models;
import org.junit.jupiter.api.*;
import seng202.team9.io.CSVImporter;
import seng202.team9.models.Attribute;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;
import seng202.team9.models.Participants;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Participants category
 * Particularly relating to the decision of which entities are involved.
 */
class ParticipantsTest {
    Participants testParticipants;


    /**
     * Generates a manual crash instance.
     * Has a Bus and two Mopeds' involved in a crash with a traffic sign and two post/poles
     * Creation process previously tested in CSVImporterTest.
     * Must be used to ensure all crashes are filled with attributes.
     */
    public Crash generateCrashOne() {
        String[] sampleString = new String[]{"1317619", "", "0", "0", "1", "0", "0", "West", "2021/2022", "KING STREET",
                "SUBSTATION LANE", "", "Non-Injury Crash", "No", "2022", "0", "North", "0", "0", "", "Hill Road", "0",
                "", "0", "", "0", "Bright sun", "0", "2", "0", "2", "0", "0", "0", "0", "0", "", "0", "2",
                "Northland Region", "Nil", "2-way", "Sealed", "0", "0", "0", "0", "0", "0", "Off", "0", "0", "",
                "Whangarei District", "Nil", "0", "1", "0", "0", "0", "0", "Urban", "0", "0", "0", "Fine", "Null",
                "-35.5935471649473", "174.284470913299"};
        CSVImporter importer = new CSVImporter();
        return importer.readCrashFromLine(sampleString);
    }

    /**
     * Generates a manual crash instance. This crash involves a cliff, two fences, pedestrian, 2 bicycles and SUV
     * Creation process previously tested in CSVImporterTest.
     * Must be used to ensure all crashes are filled with attributes.
     */
    public Crash generateCrashTwo() {
        String[] sampleString = new String[]{"1317619", "", "2", "0", "0", "0", "1", "West", "2021/2022", "KING STREET",
                "SUBSTATION LANE", "", "Non-Injury Crash", "No", "2022", "0", "North", "0", "0", "2", "Hill Road", "0",
                "", "0", "", "0", "Bright sun", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "0",
                "Northland Region", "Nil", "2-way", "Sealed", "0", "0", "0", "0", "0", "0", "Off", "1", "0", "",
                "Whangarei District", "Nil", "0", "0", "0", "0", "0", "0", "Urban", "0", "0", "0", "Fine", "Null",
                "-35.5935471649473", "174.284470913299"};
        CSVImporter importer = new CSVImporter();
        return importer.readCrashFromLine(sampleString);
    }


    /**
     * Generates a manual crash instance. This crash involves a cliff, two fences, 2 pedestrian, 1 bicycles and 2 * SUV
     * Creation process previously tested in CSVImporterTest.
     * Must be used to ensure all crashes are filled with attributes.
     */
    public Crash generateCrashThree() {
        String[] sampleString = new String[]{"1317619", "", "1", "0", "0", "0", "2", "West", "2021/2022", "KING STREET",
                "SUBSTATION LANE", "", "Non-Injury Crash", "No", "2022", "0", "North", "0", "0", "2", "Hill Road", "0",
                "", "0", "", "0", "Bright sun", "0", "0", "0", "0", "0", "0", "0", "0", "0", "2", "0", "0",
                "Northland Region", "Nil", "2-way", "Sealed", "0", "0", "0", "0", "0", "0", "Off", "1", "0", "",
                "Whangarei District", "Nil", "0", "0", "0", "0", "0", "0", "Urban", "0", "0", "0", "Fine", "Null",
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
     * Testing to see if the obstacles involved are found an assigned correctly.
     * Checks to see the correct obstacles are inserted.
     * THat their values - i.e. how many involved are preserved correctly.
     */
    @Test
    public void testGetObstacles() {
        Crash crashToTest = generateCrashOne();
        testParticipants = crashToTest.getParticipants();
        ArrayList<Attribute> obstacles = testParticipants.getObstacles();

        assertEquals(2, obstacles.size()); // ensuring the correct number of obstacles
        assertEquals("Post/Pole", obstacles.get(0).prettyPrintString);
        assertEquals("Traffic Signs", obstacles.get(1).prettyPrintString);
        assertEquals(2, obstacles.get(0).value);
        assertEquals(1, obstacles.get(1).value);
    }

    /**
     * Testing to see if the vehicles involved are found an assigned correctly.
     * Checks to see the correct vehicles are inserted.
     * THat their values - i.e. how many involved are preserved correctly.
     */
    @Test
    public void testGetVehicles() {
        Crash crashToTest = generateCrashOne();
        testParticipants = crashToTest.getParticipants();
        ArrayList<Attribute> vehicles = testParticipants.getVehicles();

        assertEquals(2, vehicles.size()); // ensuring the correct number of obstacles
        assertEquals("Buses", vehicles.get(0).prettyPrintString);
        assertEquals("Mopeds", vehicles.get(1).prettyPrintString);
        assertEquals(1, vehicles.get(0).value);
        assertEquals(2, vehicles.get(1).value);
    }

    /**
     * Tests the String method returning how many people and vehicles involved
     * Involves all aspects of the methods, multiple vehicles. single vehicles and no vehicles or ped.
     */
    @Test
    public void testGetPeopleVehiclesInvolved() {
        Crash crashToTest = generateCrashTwo(); // crash with two bicycles a pedestrian and an SUV
        testParticipants = crashToTest.getParticipants();

        String expected = String.format("%s, 2 * %s, %s", AttributeType.SUV.prettyPrint, AttributeType.BICYCLE.prettyPrint, AttributeType.PEDESTRIAN.prettyPrint);
        assertEquals(expected, testParticipants.getPeopleVehiclesInvolved());

        crashToTest = generateCrashThree(); // crash with two pedestrians a bicycle and an SUV
        testParticipants = crashToTest.getParticipants();
        expected = String.format("%s, %s, 2 * %s", AttributeType.SUV.prettyPrint, AttributeType.BICYCLE.prettyPrint, AttributeType.PEDESTRIAN.prettyPrint);
        assertEquals(expected, testParticipants.getPeopleVehiclesInvolved());


        crashToTest = generateCrashEmpty();
        testParticipants = crashToTest.getParticipants();
        assertEquals("None Listed.", testParticipants.getPeopleVehiclesInvolved());
    }

    /**
     * Tests the String method returning how many obstacles involved in the crash
     * Involves all aspects of the method, multiple obstacles. single obstacles and no obstacles.
     */
    @Test
    public void testGetObstaclesInvolved() {
        Crash crashToTest = generateCrashTwo(); // crash with single pedestrian and SUV
        testParticipants = crashToTest.getParticipants();

        String expected = String.format("%s, 2 * %s", AttributeType.CLIFFBANK.prettyPrint, AttributeType.FENCE.prettyPrint);
        assertEquals(expected, testParticipants.getObstaclesString());

        crashToTest = generateCrashEmpty();
        testParticipants = crashToTest.getParticipants();
        assertEquals("None Listed.", testParticipants.getObstaclesString());
    }

}


