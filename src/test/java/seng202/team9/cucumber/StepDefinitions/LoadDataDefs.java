package seng202.team9.cucumber.StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import seng202.team9.io.CSVImporter;
import seng202.team9.business.CrashManager;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;
import seng202.team9.repository.DatabaseManager;
import seng202.team9.repository.CrashDAO;

import java.io.File;
import java.util.List;

/**
 * Step definitions for loading data into a table and verifying the loaded data.
 */
public class LoadDataDefs {
    File loadFile;
    private static final String tableName = DatabaseManager.getInstance().protectedTableNames.get("TEST_TABLE");
    /**
     * Initializes an empty table for testing.
     */
    @Given("An empty table exists")
    public void anEmptyTableExists() {
        DatabaseManager.getInstance().createNewTable(tableName);
    }

    /**
     * Marks a file as existing for the test.
     *
     * @param string The name of the file to be used in the test.
     */
    @Given("The file {string} exists")
    public void theFileExists(String string) {
        loadFile = new File("src/test/resources/test_files/" + string);
        Assertions.assertTrue(loadFile.exists() && !loadFile.isDirectory());
    }

    /**
     * Uploads the specified file into the table.
     */
    @When("The file is uploaded")
    public void theFileIsUploaded() {
        CrashManager crashManager = CrashManager.getInstance();
        crashManager.addCrashesToDatabase(new CSVImporter(), loadFile, tableName);
    }

    /**
     * Verifies the number of values in the table after uploading the file.
     *
     * @param nValues The expected number of values in the table.
     */
    @Then("The table contains {int} files")
    public void theTableContainsNValuesValues(int nValues) {
        CrashDAO crashDAO = new CrashDAO();
        Assertions.assertEquals(nValues, crashDAO.getAllByName(tableName).size());
    }

    /**
     * Verifies that the uploaded crash is custom and valid.
     */
    @Then("The crash is custom and valid")
    public void theCrashIsCustomAndValid() {
        CrashDAO crashDAO = new CrashDAO();
        List<Crash> crashes = crashDAO.getAllByName(tableName);
        Assertions.assertEquals(1, crashes.size());
        Assertions.assertTrue(crashes.get(0).isCustom);
        Assertions.assertEquals(80, crashes.get(0).getAttribute(AttributeType.OBJECTID));
    }
}
