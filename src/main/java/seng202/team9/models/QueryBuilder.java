package seng202.team9.models;

import seng202.team9.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the queries based on the QueryCriterion selected by the user
 *
 * @author ist46
 */
public class QueryBuilder {

    /**
     * The current list of criteria
     */
    private List<QueryCriterion> criteria = new ArrayList<>();

    /**
     * Returns the current list of criteria
     * @return list of criteria
     */
    public List<QueryCriterion> getCriteria() {
        return criteria;
    }

    /**
     * Sets the list of criteria
     * @param criteria the list of criteria to set the criteria as.
     */
    public void setCriteria(List<QueryCriterion> criteria) {
        this.criteria = criteria;
    }

    /**
     * Adds the criterion to the current list of criteria
     * @param criterion the criterion to be added
     */
    public void addCriterion(QueryCriterion criterion) {
        criteria.add(criterion);
    }

    /**
     * Removes the criterion from the current list of criteria
     * @param criterion the criterion to be removed
     */
    public void removeCriterion(QueryCriterion criterion) { criteria.remove(criterion); }

    /**
     * Builds a Query based on the list of criterion
     * @return an instance of Query
     */
    public Query build() {
        return new Query(criteria);
    }

}
