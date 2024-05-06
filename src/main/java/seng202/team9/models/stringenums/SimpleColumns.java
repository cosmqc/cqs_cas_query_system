package seng202.team9.models.stringenums;

/**
 * Holds all the column names for the simple data viewer.
 */
public enum SimpleColumns {
    /** Column containing the year in which a crash occurred. */
    YEAR("Year"),

    /** Column containing information relating to the crash severity */
    SEVERITY("Severity"),

    /** Column containing information relating to the crash location */
    LOCATION("Location"),

    /** Column containing information relating to the people and vehicles involved */
    PEOPLE_AND_VEHICLES("People and Vehicles Involved"),

    /** Column containing information relating to the objects involved, usually hit */
    OBJECTS_INVOLVED("Objects Involved"),

    /** Column containing information relating to the various speed restrictions in the crash */
    SPEED_CONDITIONS("Speed Conditions"),

    /** Column containing information relating to the weather conditions */
    WEATHER("Weather"),

    /** Column containing information relating to the traffic control in place at the time */
    TRAFFIC_CONTROL("Traffic Control");

    /** Display name for the column. */
    private final String displayName;

    /**
     * Constructor for the SimpleColumns enum.
     *
     * @param displayName The name to be displayed for this column.
     */
    SimpleColumns(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the column.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }
}
