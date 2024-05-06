package seng202.team9.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The crash object. Used to display and hold data necessary.
 * Contains getters for all columns in the Raw Data Viewer.
 * Contains getters for all categories in the CAS viewer.
 * Contains Setters for the editing of data in the raw data viewer (unimplemented_
 */
public class Crash {
    private final HashMap<AttributeType, Attribute> attributes = new HashMap<>();
    private final Participants participants;
    private final Location location;
    private final Condition condition;
    /**
    Boolean flag showing whether the data has come from a CSV (assumed to be reputable) or created by the user in-app.
     */
    public boolean isCustom;

    private final ArrayList<Attribute> crashDetailAttributes = new ArrayList<>();

    /**
     * Iterates over each attribute given to construct a crash object.
     * Retrieved by index, so if any data is out of order / missing,
     * it will only log a warning if a type conversion fails (in which
     * case that row of data will be ignored.) Assumes Crash is not custom,
     * so calls the full constructor with that in mind.
     *
     * @param data ArrayList of all CAS attributes, in the order / with the types given in AttributeType
     */
    public Crash(ArrayList<Attribute> data) {
        this(data, false);
    }

    /**
     * Iterates over each attribute given to construct a crash object.
     * Retrieved by index, so if any data is out of order / missing,
     * it will only log a warning if a type conversion fails (in which
     * case that row of data will be ignored.)
     *
     * @param data ArrayList of all CAS attributes, in the order / with the types given in AttributeType
     * @param isCustom boolean flag of whether the specified Crash is created by the user or not.
     */
    public Crash(ArrayList<Attribute> data, boolean isCustom) {
        this.isCustom = isCustom;
        AttributeType[] enumValues = AttributeType.values();
        for (int i = 0; i < data.size(); i++) {
            attributes.put(enumValues[i], data.get(i));
        }
        participants = new Participants(attributes);
        location = new Location(attributes);
        condition = new Condition(attributes);
        addCrashAttributes(attributes);
    }

    /**
     * Gets the value of any CAS data attribute.<br>
     * eg <code>getAttribute(AttributeType.SERIOUSINJURYCOUNT)</code>
     *
     * @param attrType AttributeType enum value specifying the header value being fetched
     * @return the value of the given attribute in this Crash
     */
    public Object getAttribute(AttributeType attrType) {
        return attributes.get(attrType).value;
    }

    /**
     * Sets the value of any CAS data attribute.
     *
     * @param attrType AttributeType enum value specifying the header value being set
     * @param value    A String value that the attribute is being set to.
     */
    public void setAttribute(AttributeType attrType, String value) {
        switch (attrType.classType) {
            case INT -> {
                attributes.get(attrType).value = Integer.parseInt(value);
            }
            case FLOAT -> {
                attributes.get(attrType).value = Float.parseFloat(value);
            }
            case STRING -> {
                attributes.get(attrType).value = value;
            }
            default -> {
                throw new IllegalArgumentException(String.format("ClassType of %s isn't Int, Float, or String. Got %s", attrType, attrType.classType));
            }
        }
    }

    /**
     * Return the participants' category.
     * Which stores all the vehicle, obstacles and others involved in the crash
     *
     * @return a Participants object
     */
    public Participants getParticipants() {
        return participants;
    }

    /**
     * Retrieves the Location category.
     * Which stores all the attributes relating to crash location.
     *
     * @return a Location category object
     */
    public Location getLocation(){ return location; }

    /**
     * Retrieves the Condition category
     * Which stores all attributes relating to the crash conditions
     *
     * @return a Condition object
     */
    public Condition getCondition() { return condition; }

    /**
     * Adds all the details of the crashes to a list of crash detail attributes
     *
     * @param attributes a HashMap that maps the attributeType to its corresponding attribute.
     */
    public void addCrashAttributes(HashMap<AttributeType, Attribute> attributes) {
        crashDetailAttributes.add(attributes.get(AttributeType.OBJECTID));
        crashDetailAttributes.add(attributes.get(AttributeType.CRASHFINANCIALYEAR));
        crashDetailAttributes.add(attributes.get(AttributeType.HOLIDAY));
        crashDetailAttributes.add(attributes.get(AttributeType.CRASHSEVERITY));
        crashDetailAttributes.add(attributes.get(AttributeType.DEBRIS));
        crashDetailAttributes.add(attributes.get(AttributeType.FATALCOUNT));
        crashDetailAttributes.add(attributes.get(AttributeType.MINORINJURYCOUNT));
        crashDetailAttributes.add(attributes.get(AttributeType.SERIOUSINJURYCOUNT));
        crashDetailAttributes.add(attributes.get(AttributeType.OBJECTTHROWNORDROPPED));
    }

    /**
     * Gets the list of crash detail attributes
     * @return crash detail attributes
     */
    public ArrayList<Attribute> getCrashDetailAttributes() {
        return crashDetailAttributes;
    }
}


