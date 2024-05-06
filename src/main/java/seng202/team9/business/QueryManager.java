package seng202.team9.business;

import seng202.team9.models.*;
import seng202.team9.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle all actions for queries. It acts as a controller, interacting with the gui (view) package and the models/repository packages.
 *
 * @author ist46
 */
public class QueryManager {

    /** Singleton instance of the QueryManager class. Gets set once and returned after that */
    public static QueryManager instance = null;

    private final QueryBuilder queryBuilder = new QueryBuilder();

    /**
     * Singleton implementation. Returns current instance, creating it if necessary.
     * @return Singleton instance of QueryManager
     */
    public static QueryManager getInstance() {
        if (instance == null) {
            instance = new QueryManager();
        }
        return instance;
    }

    /**
     * Adds the criterion to the current build
     *
     * @param attribute the selected attribute
     * @param value the text in the textfield
     * @param operator the selected operator
     */
    public void addCriterion(AttributeType attribute, String value, OperatorType operator) {
        QueryCriterion queryCriterion = new QueryCriterion(attribute, value, operator);
        queryBuilder.addCriterion(queryCriterion);
    }

    /**
     * Removes the criterion from the current build
     * @param criterion the QueryCriterion to be removed
     */
    public void removeCriterion(QueryCriterion criterion) {
        queryBuilder.removeCriterion(criterion);
    }

    /**
     * Calls the queryBuilder method to get the criteria
     *
     * @return A list of the query criteria
     */
    public List<QueryCriterion> getCriteria() {
        return queryBuilder.getCriteria();
    }

    /**
     * Calls the queryBuilder method to set the criteria
     *
     * @param criteria the list of criteria to set the criteria to.
     */
    public void setCriteria(List<QueryCriterion> criteria) {queryBuilder.setCriteria(criteria);}

    /**
     * Builds the query and executes it
     * @param tableName the table the query is to be executed on
     * @return an ArrayList of Crash objects resulting from the query
     */
    public ArrayList<Crash> executeQuery(String tableName) {
        Query query = queryBuilder.build();
        return (ArrayList<Crash>)query.generateSQL(tableName);
    }

    /**
     * @return string containing the query in SQL format
     */
    public Query getQuery() {
        return queryBuilder.build();
    }
}
