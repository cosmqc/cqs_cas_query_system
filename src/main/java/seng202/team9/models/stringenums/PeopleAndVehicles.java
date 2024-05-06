package seng202.team9.models.stringenums;

/**
 * An enum that contains all the relevant attributes that are people and vehicles
 *
 * @author ist46
 */
public enum PeopleAndVehicles {

    /**
     * Represents a bicycle involved in the crash.
     */
    BICYCLE("Bicycle"),

    /**
     * Represents a bus involved in the crash.
     */
    BUS("Bus"),

    /**
     * Represents a car or station wagon involved in the crash.
     */
    CARSTATIONWAGON("Car or Station Wagon"),

    /**
     * Represents a moped involved in the crash.
     */
    MOPED("Moped"),

    /**
     * Represents a motorcycle involved in the crash.
     */
    MOTORCYCLE("Motorcycle"),

    /**
     * Represents a pedestrian involved in the crash.
     */
    PEDESTRIAN("Pedestrian"),

    /**
     * Represents a school bus involved in the crash.
     */
    SCHOOLBUS("School Bus"),

    /**
     * Represents a Sports Utility Vehicle (SUV) involved in the crash.
     */
    SUV("SUV"),

    /**
     * Represents a taxi involved in the crash.
     */
    TAXI("Taxi"),

    /**
     * Represents a train involved in the crash.
     */
    TRAIN("Train"),

    /**
     * Represents a truck involved in the crash.
     */
    TRUCK("Truck"),

    /**
     * Represents a van or ute involved in the crash.
     */
    VANORUTILITY("Van or Ute");

    /**
     * The human-readable representation of the people and vehicles.
     */
    public String printForm;

    /**
     * Constructor for the PeopleAndVehicles enum.
     *
     * @param printForm The human-readable representation of the people and vehicles.
     */
    PeopleAndVehicles(String printForm) {
        this.printForm = printForm;
    }

    /**
     * Converts a string representation to the corresponding PeopleAndVehicles enum value.
     *
     * @param text The string representation to convert.
     * @return The PeopleAndVehicles enum value that matches the input string.
     * @throws IllegalArgumentException if no matching enum value is found.
     */
    public static PeopleAndVehicles fromString(String text) {
        for (PeopleAndVehicles item : PeopleAndVehicles.values()) {
            if (item.printForm.equalsIgnoreCase(text)) {
                return item;
            }
        }
        throw new IllegalArgumentException("No matching PeopleAndVehicles enum value for: " + text);
    }


}
