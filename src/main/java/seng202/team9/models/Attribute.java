package seng202.team9.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstraction class for all CAS data, made up of Integer, String, and Float values.
 * Provides one class for JavaFX to interact with to remove a massive amount of repeated code
 * Could look at using generics if we have time?
 *
 * @author Jake Dalton
 */
public class Attribute {
    private static final Logger log = LogManager.getLogger(Attribute.class);

    /**
     * Header value of the attribute, stored as an AttributeType enum
     */
    final AttributeType type;
    /**
     * The nicely formatted string
     */
    public String prettyPrintString;
    /**
     * Value of the attribute, Object class, so it can be Integer, String, or Float
     */
    public Object value;

    /**
     * Constructs a new Attribute object. Value is assumed to be a string from the CSVReader import,
     * so string -> (string/int/float) conversions and error checking are done here.
     *
     * @param type  Which CAS header the data is from, encoded as an AttributeType enum value.
     * @param value A string containing the raw value taken from the CAS data.
     */
    public Attribute(AttributeType type, String value) throws NumberFormatException {
        this.type = type;
        this.prettyPrintString = type.prettyPrint;

        switch (this.type.classType) {
            case INT -> {
                if (!value.isEmpty() && !value.equals("-1")) {
                    this.value = Integer.parseInt(value);
                } else {
                    this.value = 0; // Default val for ints
                }
            }
            case FLOAT -> {
                if (!value.isEmpty()) {
                    this.value = Float.parseFloat(value);
                } else {
                    this.value = 0f; // Default val for floats - prob not accurate as only floats are coordinates
                }
            }
            case STRING -> {
                if (!value.equalsIgnoreCase("null")) {
                    this.value = value;
                } else {
                    this.value = ""; // Default val for strings
                }
            }
            default ->
                    log.warn(String.format("ClassType of %s isn't Int, Float, or String. Got %s", this.type, this.type.classType));
        }
    }

    /**
     * Gets the type of attribute
     * @return the type of the attribute
     */
    public AttributeType getType() {
        return this.type;
    }
}
