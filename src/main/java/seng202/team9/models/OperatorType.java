package seng202.team9.models;

/**
 * An enum class that has the different operator types that can be used to build a query
 *
 * @author ist46
 */
public enum OperatorType {
    /**
     * Represents the &gt; operator, indicating "greater than" in a nice format.
     */
    GREATER_THAN(">", "greater than"),

    /**
     * Represents the &lt; operator, indicating "less than" in a nice format.
     */
    LESS_THAN("<", "less than"),

    /**
     * Represents the = operator, indicating "equal to" in a nice format.
     */
    EQUAL_TO("=", "equal to"),

    /**
     * Represents the != operator, indicating "not equal to" in a nice format.
     */
    NOT_EQUAL_TO("!=", "not equal to");

    /**
     * The operator symbol in a nice, human-readable format.
     */
    public final String niceFormat;

    /**
     * A simple, human-readable form of the operator for display.
     */
    public final String simplePrint;

    /**
     * Constructor for the OperatorType enum, allowing the assignment of both niceFormat and simplePrint.
     *
     * @param niceFormat   The operator symbol in a nice, human-readable format.
     * @param simplePrint  A simple, human-readable form of the operator for display.
     */
    OperatorType(String niceFormat, String simplePrint) {
        this.niceFormat = niceFormat;
        this.simplePrint = simplePrint;
    }

    /**
     * Converts a string into an OperatorType enum value by matching the provided text with the simplePrint of each enum constant.
     *
     * @param text The string to convert into an OperatorType enum value.
     * @return The corresponding OperatorType enum value.
     * @throws IllegalArgumentException if no matching OperatorType enum value is found for the provided text.
     */
    public static OperatorType fromString(String text) {
        for (OperatorType item : OperatorType.values()) {
            if (item.simplePrint.equalsIgnoreCase(text)) {
                return item;
            }
        }
        throw new IllegalArgumentException("No matching OperatorType enum value for: " + text);
    }

}




