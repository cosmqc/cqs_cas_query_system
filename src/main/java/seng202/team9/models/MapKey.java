package seng202.team9.models;

/**
 * A class representing a map key that includes an ID, latitude (lat), and longitude (lon).
 */
public class MapKey {
    private final int id;     // The unique identifier associated with this map key.
    private final float lat;  // The latitude coordinate of the map key.
    private final float lon;  // The longitude coordinate of the map key.
    private final boolean isCustom;

    /**
     * Constructs a MapKey with the specified ID, latitude, and longitude.
     *
     * @param id  The identifier of the map key - together with isCustom makes it unique.
     * @param lat The latitude coordinate of the map key.
     * @param lon The longitude coordinate of the map key.
     * @param isCustom The custom-flag of the map key - together with the ID makes it unique
     */
    public MapKey(int id, float lat, float lon, boolean isCustom) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.isCustom = isCustom;
    }

    /**
     * Retrieves the unique identifier (ID) of this map key.
     *
     * @return The ID of the map key.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the latitude coordinate of this map key.
     *
     * @return The latitude of the map key.
     */
    public float getLat() {
        return lat;
    }

    /**
     * Retrieves the longitude coordinate of this map key.
     *
     * @return The longitude of the map key.
     */
    public float getLon() {
        return lon;
    }

    /**
     * Retrieves whether this map key is from user-inputted data or a CSV.
     *
     * @return the isCustom attribute of the related crash.
     */
    public boolean getIsCustom() { return isCustom; }
}
