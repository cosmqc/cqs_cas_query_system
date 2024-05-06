package seng202.team9.unittests.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team9.models.Attribute;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;
import seng202.team9.repository.CrashDAO;
import seng202.team9.repository.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static seng202.team9.models.ClassType.FLOAT;
import static seng202.team9.models.ClassType.INT;

/**
 * Test file to validate the functions in CrashDAO;
 * mostly relating to database connection, adding, deleting and retrieving data
 *
 * @author Jake Dalton
 */
public class CrashDAOTest {

    private static final Logger log = LogManager.getLogger(CrashDAOTest.class);
    private static final String tableName = DatabaseManager.getInstance().protectedTableNames.get("TEST_TABLE");
    DatabaseManager databaseManager;

    /**
     * Ensures correct state of the database before each test by dropping and recreating test table.
     */
    @BeforeEach
    public void initDB() {
        databaseManager = DatabaseManager.getInstance();
        databaseManager.createNewTable(tableName);
        addSingle();
    }

    /**
     * Resets the state of the database after each test by dropping the test table.
     */
    @AfterEach
    public void resetDB() {
        databaseManager = DatabaseManager.getInstance();
        databaseManager.dropTable(tableName);
    }


    /**
     * Adds a single crash entry to the database with default attributes.
     * The entry is added to the "test" table with the string attribute "First."
     */
    public void addSingle() {
        databaseManager = DatabaseManager.getInstance();
        CrashDAO crashDAO = new CrashDAO();
        String stringAttribute = "First"; // "First" to indicate the entry added by initDB
        Crash crash;
        crash = generateSingleCrash(stringAttribute);
        crashDAO.addItemToExistingTable(tableName, crash);
    }

    /**
     * Generates a single Crash object with attributes based on the provided stringAttribute.
     *
     * @param stringAttribute The string attribute value for the generated Crash.
     * @return A single Crash object with attributes based on the provided stringAttribute.
     */
    public Crash generateSingleCrash(String stringAttribute) {
        ArrayList<Attribute> data = new ArrayList<>();
        for (int j=0; j < AttributeType.values().length; j++) {
            Attribute attribute;
            AttributeType header = AttributeType.values()[j];
            if (header.classType == INT) {
                if (j == 0) {
                    attribute = new Attribute(header, "99");
                } else {
                    attribute = new Attribute(header, "-100");
                }
            } else if (header.classType == FLOAT) {
                attribute = new Attribute(header, "-1.00");
            } else {
                attribute = new Attribute(header, stringAttribute);
            }
            data.add(attribute);
        }

        return new Crash(data);
    }

    /**
     * Generates a list of Crash objects with attributes based on the provided stringAttribute.
     *
     * @param numEntries The number of Crash objects to generate.
     * @return A list of Crash objects with attributes based on the provided stringAttribute.
     */
    public List<Crash> generateCrashes(int numEntries) {
        Crash crash;
        ArrayList<Crash> crashes = new ArrayList<>();
        for (int i=0; i < numEntries; i++) {
            crash = generateSingleCrash("Test");
            crashes.add(crash);
        }
        return crashes;
    }

    /**
     * Test we can actually connect to the database
     */
    @Test
    public void testDatabaseConnection() {
        try (Connection conn = databaseManager.connect()){
            Assertions.assertNotNull(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test adding a single crash entry to a specified,
     * exiting table (assumes the entry does not exit prior to addition)
     *
     */
    @Test
    public void testAddSingleExisting() {
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement()) {

            String expectedRoad = "First";
            ResultSet rs = stmt.executeQuery("SELECT CRASHLOCATIONONE FROM %s".formatted(tableName));
            while (rs.next()) {
                Assertions.assertEquals(expectedRoad, rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test adding a single crash entry to a specified,
     * new table (assumes the entry does not exit prior to addition)
     *
     */
    @Test
    public void testAddSingleNew() {
        CrashDAO crashDAO = new CrashDAO();
        String stringAttribute = "Second"; // "Second" to indicate the entry added by this test method
        Crash crash;
        databaseManager.dropTable(tableName);
        crash = generateSingleCrash(stringAttribute);
        crashDAO.addItemsToNewTable(tableName, crash);

        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement()) {

            String expectedRoad = "Second";
            ResultSet rs = stmt.executeQuery("SELECT CRASHLOCATIONONE FROM %s".formatted(tableName));
            while (rs.next()) {
                Assertions.assertEquals(expectedRoad, rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test adding a single crash entry to the database (assumes the entry ALREADY EXISTS prior to addition)
     */
    @Test
    public void testAddSingleDuplicate() {
        addSingle();
    }

    /**
     * Test deleting a crash entry from a specified table (assumes the entry already exists in the table)
     */
    @Test
    public void testDelete() {
        CrashDAO crashDAO = new CrashDAO();
        crashDAO.delete(99, false, tableName);

        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT EXISTS(SELECT * FROM %s WHERE ".formatted(tableName) +
                    "CRASHLOCATIONONE = 'First')");
            Assertions.assertFalse(rs.getBoolean(1));
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test deleting a non-existing entry in a given table
     */
    @Test
    public void testDeleteNonExistent() { // shouldn't return an error since it does not trigger an SQLException
        CrashDAO crashDAO = new CrashDAO();
        crashDAO.delete(-999976867, false, tableName);
    }

    /**
     * Test getting a single entry from the default table
     */
    @Test
    public void testGetOne() {
        CrashDAO crashDAO = new CrashDAO();
        int id = 99; // added previously by addSingle()
        Crash returned = crashDAO.getOne(id, false, tableName);
        Assertions.assertEquals(99, returned.getAttribute(AttributeType.OBJECTID));
    }

    /**
     * Test getting all entries from a table with a specified name (assuming the table exists)
     */
    @Test
    public void testGetAllByName() {
        int expectedNumCrashes;
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT COUNT(OBJECTID) FROM %s".formatted(tableName));
            expectedNumCrashes = rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        CrashDAO crashDAO = new CrashDAO();
        int actualNumCrashes = crashDAO.getAllByName(tableName).size();
        Assertions.assertEquals(expectedNumCrashes, actualNumCrashes);
    }

    /**
     * Test getting all entries from a non-existing table
     */
    @Test
    public void testGetAllByNameWrongTable() {
        CrashDAO crashDAO = new CrashDAO();
        String table = "random";
        boolean errored = false;
        try {
            List<Crash> crashes = crashDAO.getAllByName(table); // should return an error
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            errored = true;
        }
        Assertions.assertTrue(errored);
    }

    /**
     * Test adding crashes into an existing table
     */
    @Test
    public void testAddCrashesByTableName() {
        CrashDAO crashDAO = new CrashDAO();
        List<Crash> crashList;
        int numEntries = 3;

        crashList = generateCrashes(numEntries);
        crashDAO.addItemsToExistingTable(tableName, crashList);

        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT CRASHLOCATIONTWO FROM %s WHERE ".formatted(tableName) +
                    "CRASHLOCATIONONE = 'Test'");
            String expected = "Test";
            while (rs.next()) {
                Assertions.assertEquals(expected, rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test adding crash entries to a new table
     */
    @Test
    public void testAddCrashesToNewTableByName() {
        CrashDAO crashDAO = new CrashDAO();
        List<Crash> crashList;
        int numEntries = 3;

        crashList = generateCrashes(numEntries);

        databaseManager.dropTable(tableName);
        crashDAO.addItemsToNewTable(tableName, crashList);

        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format("SELECT CRASHLOCATIONONE FROM %s WHERE " +
                    "CRASHLOCATIONONE = 'Test'", tableName));
            String expected = "Test";
            while (rs.next()) {
                Assertions.assertEquals(expected, rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test adding crashes to an existing table with addItemsToNewTable
     */
    @Test
    public void testAddToNewTableWithExisting() {
        CrashDAO crashDAO = new CrashDAO();
        databaseManager.dropTable(tableName);

        String expectedMessage = "Table name '" + tableName + "' already exists in the database";
        String actualMessage;
        List<Crash> crashList;
        int numEntries = 3;

        crashList = generateCrashes(numEntries);
        crashDAO.addItemsToNewTable(tableName, crashList);

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> crashDAO.addItemsToNewTable(tableName, crashList));

        actualMessage = exception.getMessage();
        log.error(expectedMessage);
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test updating a given entry in the specified table (assuming the entry already exists)
     */
    @Test
    public void testUpdate() {
        CrashDAO crashDAO = new CrashDAO();
        int id = 99;
        String crashAttribute = "CRASHLOCATIONONE";
        String updateValue = "New Road";
        crashDAO.update(id, false, crashAttribute, updateValue, tableName);

        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format("SELECT CRASHLOCATIONONE FROM %s WHERE " +
                    "OBJECTID = %s", tableName, id));
            while (rs.next()) {
                Assertions.assertEquals(updateValue, rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test getting the max crash ID from a specified table
     */
    @Test
    public void testMaxCrashID() {
        CrashDAO crashDAO = new CrashDAO();
        int actualID;
        int expectedID = 99;
        actualID = crashDAO.getMaxCrashID(tableName);
        Assertions.assertEquals(expectedID, actualID);
    }

    /**
     * Test the getCount function
     * This test checks if the function returns the correct number of crashes (1) from test table.
     */
    @Test
    public void testGetCountValidQuery() {
        CrashDAO crashDAO = new CrashDAO();
        String validSQL = "SELECT COUNT(*) FROM %s".formatted(tableName);
        int count = crashDAO.getCount(validSQL);
        Assertions.assertEquals(1, count, "Expected count to be 1 for test table.");
    }

    /**
     * Test the getCount function with an empty table.
     * This test verifies if the function returns a count of 0 when querying an empty table.
     */
    @Test
    public void testGetCountEmptyTable() {
        CrashDAO crashDAO = new CrashDAO();
        databaseManager.dropTable(tableName);
        databaseManager.createNewTable(tableName);
        String sql = "SELECT COUNT(*) FROM %s".formatted(tableName);
        int count = crashDAO.getCount(sql);
        Assertions.assertEquals(0, count, "Expected count to be 0 for an empty table.");
    }
}
