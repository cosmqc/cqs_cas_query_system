package seng202.team9.unittests.models;

import org.junit.jupiter.api.Test;
import seng202.team9.models.AttributeType;
import seng202.team9.models.OperatorType;
import seng202.team9.models.QueryCriterion;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A JUnit test case for testing the functionality of the QueryCriterion class.
 * This class includes a test for the toString method, which generates a string representation of a query criterion.
 *
 * @author ist46
 */
public class QueryCriterionTest {

    /**
     * Test case for the toString method of the QueryCriterion class.
     */
    @Test
    public void toStringTest() {
        AttributeType attribute = AttributeType.OBJECTID;
        String value = "50";
        OperatorType operator = OperatorType.EQUAL_TO;
        String expectedString = "OBJECTID = 50";
        QueryCriterion queryCriterion = new QueryCriterion(attribute, value, operator);
        assertEquals(expectedString, queryCriterion.toString());
    }
}
