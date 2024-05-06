package seng202.team9.models;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Category relating to all participating entities in a crash
 * i.e. Vehicles, Obstacles and pedestrians involved in the crash
 */
public class Participants {

    private final ArrayList<Attribute> vehicles = new ArrayList<>();

    private final Attribute pedestrian;

    private final ArrayList<Attribute> obstacles = new ArrayList<>();

    private final ArrayList<Attribute> participantsAttributes = new ArrayList<>();

    /**
     * Constructor of a Participants object.
     * Uses the helper functions to store only vehicles and obstacles involved in the crash
     * @param attributes all the attributes in a crash
     */
    public Participants(HashMap<AttributeType, Attribute> attributes) {
        pedestrian = attributes.get(AttributeType.PEDESTRIAN);
        addObstaclesInvolved(attributes);
        addVehiclesInvolved(attributes);
        addParticipantsAttribute(attributes);
    }

    /**
     * Takes a crash and finds which vehicles, if any were involved
     *
     * @param attributes takes the HashMap of all crash attributes and types
     */
    private void addVehiclesInvolved(HashMap<AttributeType, Attribute> attributes) {
        checkVehicleInvolvement(attributes.get(AttributeType.VEHICLE));
        checkVehicleInvolvement(attributes.get(AttributeType.TRUCK));
        checkVehicleInvolvement(attributes.get(AttributeType.TAXI));
        checkVehicleInvolvement(attributes.get(AttributeType.BUS));
        checkVehicleInvolvement(attributes.get(AttributeType.SCHOOLBUS));
        checkVehicleInvolvement(attributes.get(AttributeType.CARSTATIONWAGON));
        checkVehicleInvolvement(attributes.get(AttributeType.SUV));
        checkVehicleInvolvement(attributes.get(AttributeType.MOPED));
        checkVehicleInvolvement(attributes.get(AttributeType.MOTORCYCLE));
        checkVehicleInvolvement(attributes.get(AttributeType.VANORUTILITY));
        checkVehicleInvolvement(attributes.get(AttributeType.UNKNOWNVEHICLETYPE));
        checkVehicleInvolvement(attributes.get(AttributeType.TRAIN));
        checkVehicleInvolvement(attributes.get(AttributeType.OTHERVEHICLETYPE));
        checkVehicleInvolvement(attributes.get(AttributeType.BICYCLE));

    }

    /**
     * Takes a crash and finds which obstacles, if any were involved
     *
     * @param attributes takes the HashMap of all crash attributes and types
     */
    private void addObstaclesInvolved(HashMap<AttributeType, Attribute> attributes) {
        checkObstacleInvolvement(attributes.get(AttributeType.CLIFFBANK));
        checkObstacleInvolvement(attributes.get(AttributeType.WATERRIVER));
        checkObstacleInvolvement(attributes.get(AttributeType.PARKEDVEHICLE));
        checkObstacleInvolvement(attributes.get(AttributeType.FENCE));
        checkObstacleInvolvement(attributes.get(AttributeType.BRIDGE));
        checkObstacleInvolvement(attributes.get(AttributeType.DITCH));
        checkObstacleInvolvement(attributes.get(AttributeType.GUARDRAIL));
        checkObstacleInvolvement(attributes.get(AttributeType.HOUSEORBUILDING));
        checkObstacleInvolvement(attributes.get(AttributeType.KERB));
        checkObstacleInvolvement(attributes.get(AttributeType.OVERBANK));
        checkObstacleInvolvement(attributes.get(AttributeType.PHONEBOXETC));
        checkObstacleInvolvement(attributes.get(AttributeType.POSTORPOLE));
        checkObstacleInvolvement(attributes.get(AttributeType.TRAFFICISLAND));
        checkObstacleInvolvement(attributes.get(AttributeType.TRAFFICSIGN));
        checkObstacleInvolvement(attributes.get(AttributeType.STRAYANIMAL));
        checkObstacleInvolvement(attributes.get(AttributeType.TREE));
        checkObstacleInvolvement(attributes.get(AttributeType.SLIPORFLOOD));
        checkObstacleInvolvement(attributes.get(AttributeType.OTHEROBJECT));
    }


    /**
     * Checks to see if the vehicle type was involved in the crash.
     * If it is involved once or more, the attribute will be added to vehicles list.
     * @param vehicleAttribute an Attribute relating to a vehicle
     */
    public void checkVehicleInvolvement(Attribute vehicleAttribute) {
        int occurrences = (int)vehicleAttribute.value; // All will be ints - or would have been caught in attribute.
        if (occurrences > 0) {
            vehicles.add(vehicleAttribute);
        }
    }

    /**
     * Checks to see if the obstacle was involved in the crash.
     * If it is involved once or more, the attribute will be added to obstacles list.
     *
     * @param obstacleAttribute an attribute relating to an obstacle
     */
    public void checkObstacleInvolvement(Attribute obstacleAttribute) {
        int occurrences = (int) obstacleAttribute.value; //All will be ints - or would have been caught in attribute.
        if (occurrences > 0) {
            obstacles.add(obstacleAttribute);
        }
    }

    /**
     * Provides a string with a multiplier attached to an attribute
     * @param attribute The attribute which is being multiplied.
     * @param currLen The current length of the string. To see if a comma needs to be added at the start of the string.
     * @return A nicely formatted string to be appended to the existing string.
     */
    private String multiplier(Attribute attribute, int currLen) {
        if (currLen > 0) {
            return String.format(", %d * %s", (int) attribute.value, attribute.prettyPrintString);
        } else {
            return String.format("%d * %s", (int) attribute.value, attribute.prettyPrintString);
        }
    }

    /**
     * Set of logic to show which people and vehicles were involved in crash.
     * To be shown in CAS viewer.
     *
     * @return String of [number *] vehicles and pedestrians involved.
     */
    public String getPeopleVehiclesInvolved() {
        String finalStr = "";

        if (vehicles.size() > 0) {
            for (Attribute vehicle: vehicles) {
                if ((int) vehicle.value > 1) {
                    finalStr += multiplier(vehicle, finalStr.length());
                } else {
                    if (finalStr.length() == 0) {
                        finalStr += vehicle.prettyPrintString;

                    } else {
                        finalStr += String.format(", %s",vehicle.prettyPrintString);
                    }
                }
            }
            if ((int)pedestrian.value > 1 ) {
                finalStr += String.format(", %d * %s",(int)pedestrian.value, pedestrian.prettyPrintString);
            } else if ((int)pedestrian.value == 1) {    // We only want to add pedestrian if it is not 0
                finalStr += String.format(", %s",pedestrian.prettyPrintString);
            }
        } else {    // No vehicles involved
            if ((int) pedestrian.value > 1) {
                finalStr += String.format("%d * ", (int) pedestrian.value);
            } else if ((int)pedestrian.value == 1) {
                finalStr += pedestrian.prettyPrintString;
            }
        }

        if (finalStr.length() == 0) {     // No Vehicles nor pedestrians involved
            finalStr += "None Listed.";  // There will be a descriptive col header
        }
        return finalStr;
    }


    /**
     * Set of logic to show which obstacle(s) were involved in crash.
     * To be shown in CAS viewer.
     *
     * @return String of [number *] obstacles and pedestrians involved.
     */
    public String getObstaclesString() {
        String finalStr = "";
        if (obstacles.size() > 0) {
            for (Attribute obstacle : obstacles) {
                if ((int) obstacle.value > 1) {
                    finalStr += multiplier(obstacle, finalStr.length());
                } else {
                    if (finalStr.length() == 0) {
                        finalStr += obstacle.prettyPrintString;
                    } else {
                        finalStr += String.format(", %s",obstacle.prettyPrintString);
                    }
                }
            }
        } else {
            finalStr = "None Listed.";
        }
        return finalStr;
    }

    /**
     * Retrieves the vehicle types involved in the crash
     * Will often just be a single vehicle
     *
     * @return an array list of Attributes
     */
    public ArrayList<Attribute> getVehicles() {
        return vehicles;
    }


    /**
     * Retrieves the Attribute of pedestrians involved in this crash.
     *
     * @return An integer representing the count of pedestrians involved.
     */
    public Attribute getPedestrian() {
        return pedestrian;
    }

    /**
     * Retrieves the obstacles involved in the crash
     * Will often just be a single vehicle
     *
     * @return an array list of Attributes
     */
    public ArrayList<Attribute> getObstacles() {
        return obstacles;
    }


    /**
     * Adds participant-related attributes to the list of participant attributes.
     *
     * @param attributes A HashMap containing participant-related attributes to add.
     */
    public void addParticipantsAttribute(HashMap<AttributeType, Attribute> attributes) {
        participantsAttributes.add(attributes.get(AttributeType.VEHICLE));
        participantsAttributes.add(attributes.get(AttributeType.TRUCK));
        participantsAttributes.add(attributes.get(AttributeType.TAXI));
        participantsAttributes.add(attributes.get(AttributeType.BUS));
        participantsAttributes.add(attributes.get(AttributeType.SCHOOLBUS));
        participantsAttributes.add(attributes.get(AttributeType.CARSTATIONWAGON));
        participantsAttributes.add(attributes.get(AttributeType.SUV));
        participantsAttributes.add(attributes.get(AttributeType.MOPED));
        participantsAttributes.add(attributes.get(AttributeType.MOTORCYCLE));
        participantsAttributes.add(attributes.get(AttributeType.VANORUTILITY));
        participantsAttributes.add(attributes.get(AttributeType.UNKNOWNVEHICLETYPE));
        participantsAttributes.add(attributes.get(AttributeType.TRAIN));
        participantsAttributes.add(attributes.get(AttributeType.OTHERVEHICLETYPE));
        participantsAttributes.add(attributes.get(AttributeType.BICYCLE));
        participantsAttributes.add(pedestrian);
        participantsAttributes.add(attributes.get(AttributeType.CLIFFBANK));
        participantsAttributes.add(attributes.get(AttributeType.WATERRIVER));
        participantsAttributes.add(attributes.get(AttributeType.PARKEDVEHICLE));
        participantsAttributes.add(attributes.get(AttributeType.FENCE));
        participantsAttributes.add(attributes.get(AttributeType.BRIDGE));
        participantsAttributes.add(attributes.get(AttributeType.DITCH));
        participantsAttributes.add(attributes.get(AttributeType.GUARDRAIL));
        participantsAttributes.add(attributes.get(AttributeType.HOUSEORBUILDING));
        participantsAttributes.add(attributes.get(AttributeType.KERB));
        participantsAttributes.add(attributes.get(AttributeType.OVERBANK));
        participantsAttributes.add(attributes.get(AttributeType.PHONEBOXETC));
        participantsAttributes.add(attributes.get(AttributeType.POSTORPOLE));
        participantsAttributes.add(attributes.get(AttributeType.TRAFFICISLAND));
        participantsAttributes.add(attributes.get(AttributeType.TRAFFICSIGN));
        participantsAttributes.add(attributes.get(AttributeType.STRAYANIMAL));
        participantsAttributes.add(attributes.get(AttributeType.TREE));
        participantsAttributes.add(attributes.get(AttributeType.SLIPORFLOOD));
        participantsAttributes.add(attributes.get(AttributeType.OTHEROBJECT));

    }
    /**
     * Retrieves the list of participant-related attributes.
     *
     * @return An ArrayList of participant-related attributes.
     */
    public ArrayList<Attribute> getParticipantsAttributes() {
        return participantsAttributes;
    }

}
