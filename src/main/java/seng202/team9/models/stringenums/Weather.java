package seng202.team9.models.stringenums;

/**
 * An enum that holds all the input values for WeatherA and WeatherB
 *
 * @author ist46
 */
public enum Weather {
    /** Absence of the other conditions */
    FINE("Fine"),

    /** Lightly raining */
    LIGHT_RAIN("Light rain"),

    /** Heavily raining */
    HEAVY_RAIN("Heavy rain"),

    /** Misty or foggy conditions */
    MIST_OR_FOG("Mist or Fog"),

    /** Snowing */
    SNOW("Snow"),

    /** Wind blowing strongly */
    STRONG_WIND("Strong wind"),

    /** Frosty */
    FROST("Frost");

    /** String containing the expected value for the Weather field */
    public String printForm;

    Weather(String printForm) {
        this.printForm = printForm;
    }
}
