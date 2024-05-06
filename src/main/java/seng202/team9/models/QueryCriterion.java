package seng202.team9.models;


/**
 * The criterion selected by the user to perform a query
 *
 * @author ist46
 */
public class QueryCriterion {
    private final AttributeType attribute;
    private final String value;
    private final OperatorType operator;


    /**
     * Returns a string that is in the format required for the WHERE clause for an SQL statement
     *
     * @return a string in sql format
     */
    @Override
    public String toString() {
        return attribute + " " + operator.niceFormat + " " + value;
    }


    /**
     * Constructor for the QueryCriterion, represents a comparison between an Attribute and a user-entered value
     * @param attribute the attribute that is getting compared
     * @param value the value the attribute is getting compared to
     * @param operator an OperatorType object that determines how the attribute and value are compared
     */
    public QueryCriterion(AttributeType attribute, String value, OperatorType operator) {
        this.attribute = attribute;
        this.value = value;
        this.operator = operator;

    }
}
