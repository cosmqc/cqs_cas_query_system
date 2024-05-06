package seng202.team9.cucumber.StepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.AfterAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team9.business.CrashManager;
import seng202.team9.business.WindowState;
import seng202.team9.io.CSVImporter;
import seng202.team9.repository.DatabaseManager;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Step definitions for Cucumber scenarios related to obtaining counts of attributes in the Crash data.
 *
 * @author ist46
 */
public class GetCountsStepDefinitions {

    private String category;

    private List<String> attributes;

    private static String table;

    private final CrashManager crashManager = CrashManager.getInstance();

    private List<Integer> counts;

    /**
     * Specifies the category for attribute counts.
     *
     * @param string The category name.
     */
    @Given("the category is {string}")
    public void theCategoryIs(String string) {
        this.category = string;
    }

    /**
     * Specifies a list of attributes for which counts will be obtained.
     *
     * @param attributes A DataTable containing attribute names.
     */
    @And("the list of attributes is:")
    public void theListOfAttributesIs(DataTable attributes) {
        this.attributes = attributes.asList(String.class);
    }

    /**
     * Ensures that the specified database table is prepared for attribute counting.
     *
     * @param table The name of the database table.
     */
    @And("the database contains the table {string}")
    public void databaseContainsTable(String table) {
        this.table = table;
        WindowState.getInstance().setCurrentTable(table);
        crashManager.addCrashesToDatabase(new CSVImporter(), new File("src/test/resources/test_files/countsTest.csv"), table);
    }

    /**
     * Initiates the process of obtaining counts of attributes in the specified category.
     */
    @When("I get the counts of each attribute")
    public void getCountsOfEachAttribute() {
        counts = crashManager.getCounts(category, attributes);
    }

    /**
     * Verifies that the obtained attribute counts match the expected values.
     *
     * @param expectedCountsData A DataTable containing the expected counts.
     */
    @Then("the respective counts should be:")
    public void respectiveCountsShouldBe(DataTable expectedCountsData) {
        List<Integer> expectedCounts = expectedCountsData.asList(Integer.class);
        for(int i = 0; i < 4; i++) {
            assertEquals(expectedCounts.get(i), counts.get(i));
        }
    }

    /**
     * Drops the testing table after the test is finished
     */
    @AfterAll
    public static void dropGetCountsTable() {
        DatabaseManager.getInstance().dropTable(table);
    }
}
