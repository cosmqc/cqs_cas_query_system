package seng202.team9.cucumber.StepDefinitions;

import io.cucumber.java.AfterAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team9.business.CrashManager;
import seng202.team9.gui.EditDataModalController;
import seng202.team9.gui.MainController;
import seng202.team9.io.CSVImporter;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;
import seng202.team9.repository.DatabaseManager;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This class defines a set of Cucumber step definitions for testing the behavior of editing crash records.
 * It includes scenarios for updating crash records and validating the edit modal's behavior.
 */
public class CrashEditDefs {
    private static final Logger log = LogManager.getLogger(MainController.class);
    private static final String tableName = DatabaseManager.getInstance().protectedTableNames.get("TEST_TABLE");
    EditDataModalController editDataModalController;
    boolean result;
    CrashManager crashManager;

    /**
     * Initializes a test table with an existing crash record identified by the given ID.
     *
     * @param ID1 The ID of the existing crash record.
     */
    @Given("We have a table with an existing crash with an ID of {string}")
    public void createTableWithID(String ID1) {
        DatabaseManager.getInstance().createNewTable(tableName);
        CrashManager crashManager = CrashManager.getInstance();
        String[] crashString1 = new String[]{ID1, "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "0", "0", "0", "0", "0", "0", "", "", "", "",
                "", "", "0", "0", "0", "0", "0", "0", "", "0", "0", "0", "", "",
                "", ""};
        CSVImporter importer = new CSVImporter();
        Crash crash1 = importer.readCrashFromLine(crashString1);
        try {
            crashManager.addCrash(crash1, tableName);
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Attempts to edit a crash record in the table by changing its ID to the provided value.
     *
     * @param ID1 The new ID to assign to the crash record.
     */
    @When("We update a different crash in the table to {string}")
    public void editCrashID(String ID1) {
        editDataModalController = new EditDataModalController();
        editDataModalController.setTableName(tableName);
        result = editDataModalController.validateIDChange(ID1);
    }

    /**
     * Verifies that the edit modal rejects the change and the original crash record remains unchanged.
     *
     * @param ID1 The original ID of the crash record.
     */
    @Then("The edit modal rejects the change and the single entry {string} remains")
    public void assertCrashEquality(String ID1) {
        crashManager = CrashManager.getInstance();
        // Check result of the edit validation
        assertFalse(result); // ensuring the validator failed

        Crash crash = crashManager.getCrashById(Integer.parseInt(ID1), false, tableName);

        assertEquals(crash.getAttribute(AttributeType.OBJECTID), Integer.parseInt(ID1)); // Check the existing ID wasn't corrupted

    }

    /**
     * drops the testing table after the tests are ran
     */
    @AfterAll
    public static void dropTestingTable() {
        DatabaseManager.getInstance().dropTable(tableName); // Drop the table created for the test.
    }
}

