package seng202.team9.models.stringenums;


/**
 * An enum that contains all the relevant attributes that are obstacles
 *
 * @author ist46
 */
public enum Obstacles {

    /**
     * Represents a "Cliff or bank" obstacle.
     */
    CLIFFBANK("Cliff or bank"),

    /**
     * Represents a "Body of water" obstacle.
     */
    WATERRIVER("Body of water"),

    /**
     * Represents a "Debris" obstacle.
     */
    DEBRIS("Debris"),

    /**
     * Represents a "Bridge" obstacle.
     */
    BRIDGE("Bridge"),

    /**
     * Represents a "Ditch" obstacle.
     */
    DITCH("Ditch"),

    /**
     * Represents a "Fence" obstacle.
     */
    FENCE("Fence"),

    /**
     * Represents a "Guard rail" obstacle.
     */
    GUARDRAIL("Guard rail"),

    /**
     * Represents a "House or building" obstacle.
     */
    HOUSEORBUILDING("House or building"),

    /**
     * Represents a "Kerb" obstacle.
     */
    KERB("Kerb"),

    /**
     * Represents an "Embankment interaction" obstacle.
     */
    OVERBANK("Embankment interaction"),

    /**
     * Represents a "Parked vehicle" obstacle.
     */
    PARKEDVEHICLE("Parked vehicle"),

    /**
     * Represents "Public furniture" as an obstacle.
     */
    PHONEBOXETC("Public furniture"),

    /**
     * Represents a "Post or pole" obstacle.
     */
    POSTORPOLE("Post or pole"),

    /**
     * Represents "Road cones and signs" as obstacles.
     */
    ROADWORKS("Roadcones and signs"),

    /**
     * Represents "Slips or floods" as obstacles.
     */
    SLIPORFLOOD("Slips or floods"),

    /**
     * Represents a "Stray animal" obstacle.
     */
    STRAYANIMAL("Stray animal"),

    /**
     * Represents a "Traffic island" obstacle.
     */
    TRAFFICISLAND("Traffic island"),

    /**
     * Represents "Traffic signage" as obstacles.
     */
    TRAFFICSIGN("Traffic signage"),

    /**
     * Represents a "Tree" obstacle.
     */
    TREE("Tree");

    /**
     * The human-readable form of the obstacle.
     */
    public String printForm;

    /**
     * Constructor for the Obstacles enum, allowing the assignment of a human-readable printForm to each constant.
     *
     * @param printForm The human-readable form of the obstacle.
     */
    Obstacles(String printForm) {
        this.printForm = printForm;
    }

    /**
     * Converts a string into an Obstacles enum value by matching the provided text with the printForm of each enum constant.
     *
     * @param text The string to convert into an Obstacles enum value.
     * @return The corresponding Obstacles enum value.
     * @throws IllegalArgumentException if no matching Obstacles enum value is found for the provided text.
     */
    public static Obstacles fromString(String text) {
        for (Obstacles obstacle : Obstacles.values()) {
            if (obstacle.printForm.equalsIgnoreCase(text)) {
                return obstacle;
            }
        }
        throw new IllegalArgumentException("No matching Obstacles enum value for: " + text);
    }
}
