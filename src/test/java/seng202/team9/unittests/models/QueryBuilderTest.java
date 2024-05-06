package seng202.team9.unittests.models;

import org.junit.jupiter.api.Test;
import seng202.team9.models.AttributeType;
import seng202.team9.models.OperatorType;
import seng202.team9.models.QueryBuilder;
import seng202.team9.models.QueryCriterion;
import seng202.team9.repository.Query;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A set of JUnit test cases for testing the functionality of the QueryBuilder class.
 * This class includes tests for adding criteria and building a query.
 *
 * @author ist46
 */
public class QueryBuilderTest {

    private final QueryBuilder queryBuilder = new QueryBuilder();

    /**
     * Test case for adding a single criterion to the QueryBuilder.
     */
    @Test
    public void addOneCriterionTest() {
        QueryCriterion criterion = new QueryCriterion(AttributeType.OBJECTID, "50", OperatorType.EQUAL_TO);
        queryBuilder.addCriterion(criterion);
        assertEquals(criterion, queryBuilder.getCriteria().get(0));
    }

    /**
     * Test case for adding multiple criteria to the QueryBuilder.
     */
    @Test
    public void addMultipleCriterionTest() {
        QueryCriterion criterion1 = new QueryCriterion(AttributeType.OBJECTID, "50", OperatorType.EQUAL_TO);
        QueryCriterion criterion2 = new QueryCriterion(AttributeType.BICYCLE, "3", OperatorType.GREATER_THAN);
        QueryCriterion criterion3 = new QueryCriterion(AttributeType.CRASHSEVERITY, "Minor Crash", OperatorType.NOT_EQUAL_TO);
        queryBuilder.addCriterion(criterion1);
        queryBuilder.addCriterion(criterion2);
        queryBuilder.addCriterion(criterion3);
        List<QueryCriterion> criteria = new ArrayList<>();
        criteria.add(criterion1);
        criteria.add(criterion2);
        criteria.add(criterion3);
        assertEquals(criteria, queryBuilder.getCriteria());
    }

    /**
     * Test case for building a query using the added criteria.
     */
    @Test
    public void buildTest() {
        QueryCriterion criterion = new QueryCriterion(AttributeType.OBJECTID, "50", OperatorType.EQUAL_TO);
        queryBuilder.addCriterion(criterion);
        Query actualQuery = queryBuilder.build();
        List<QueryCriterion> criteria = new ArrayList<>();
        criteria.add(criterion);
        Query expectedQuery = new Query(criteria);
        assertEquals(expectedQuery.toString(), actualQuery.toString());
    }
}
