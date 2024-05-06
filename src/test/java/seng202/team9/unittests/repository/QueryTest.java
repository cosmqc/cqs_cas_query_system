package seng202.team9.unittests.repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team9.business.CrashManager;
import seng202.team9.io.CSVImporter;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;
import seng202.team9.models.OperatorType;
import seng202.team9.models.QueryCriterion;
import seng202.team9.repository.DatabaseManager;
import seng202.team9.repository.Query;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A JUnit test case for testing the functionality of the Query class, which is used to generate SQL queries based on criteria.
 * This class includes a test case for generating SQL queries with specific criteria and checking the resulting crash data.
 *
 * @author ist46
 */
public class QueryTest {

    private final DatabaseManager databaseManager = DatabaseManager.getInstance();

    /**
     * Before running the tests, add crashes to the database by reading a CSV file.
     */
    @BeforeAll
    public static void addCrashesToDatabase() {
        CrashManager crashManager = CrashManager.getInstance();
        crashManager.addCrashesToDatabase(new CSVImporter(), new File("src/main/resources/CSV/test.csv"), "test");
    }

    /**
     * After each test, reset the state of the database by dropping the test table.
     */
    @AfterEach
    public void resetDB() {
        databaseManager.dropTable("test");
    }

    /**
     * Test case for generating SQL queries with specific criteria and checking the resulting crash data.
     */
    @Test
    public void generateSQLTest() {
        List<QueryCriterion> criteria = new ArrayList<>();
        QueryCriterion criterion1 = new QueryCriterion(AttributeType.OBJECTID, "4", OperatorType.LESS_THAN);
        QueryCriterion criterion2 = new QueryCriterion(AttributeType.BICYCLE, "0", OperatorType.GREATER_THAN);
        QueryCriterion criterion3 = new QueryCriterion(AttributeType.CRASHSEVERITY, "'Minor Crash'", OperatorType.NOT_EQUAL_TO);
        criteria.add(criterion1);
        criteria.add(criterion2);
        criteria.add(criterion3);
        Query query = new Query(criteria);
        List<Crash> crashes = query.generateSQL("test");
        for(Crash crash: crashes) {
            assertTrue((Integer)crash.getAttribute(AttributeType.OBJECTID) < 4);
            assertTrue((Integer)crash.getAttribute(AttributeType.BICYCLE) > 0);
            assertNotSame("Minor Crash", crash.getAttribute(AttributeType.CRASHSEVERITY));
        }
    }
}
