package seng202.team9.models.stringenums;

/**
 * An enum that holds all the possible input values for light
 *
 * @author ist46
 */
public enum Light {

    /**
     * Bright sunlight condition.
     */
    BRIGHT_SUN("Bright sun"),

    /**
     * Overcast lighting condition.
     */
    OVERCAST("Overcast"),

    /**
     * Dark or nighttime lighting condition.
     */
    DARK("Dark"),

    /**
     * Twilight lighting condition.
     */
    TWILIGHT("Twilight");

    /**
     * The printable form of the lighting condition.
     */
    public String printForm;

    /**
     * Constructs a Light enum with the specified print form.
     *
     * @param printForm The printable form of the lighting condition.
     */
    Light(String printForm) {
        this.printForm = printForm;
    }
}
