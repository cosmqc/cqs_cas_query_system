package seng202.team9.models;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines and stores all attributes of a crash relevant to the conditions
 */
public class Condition {
    private final Attribute roadSurface;
    private final Attribute roadWorks;
    private final Attribute weatherA;
    private final Attribute weatherB;
    private final Attribute roadLane;
    private final Attribute intersection; // At no point is this correctly entered in the whole dataset.
    private final Attribute light;
    private final Attribute numLanes;
    private final Attribute roadCharacter;
    private final Attribute advisorySpeed;
    private final Attribute tempSpeedLimit;
    private final Attribute speedLimit;
    private final Attribute streetLight;
    private final Attribute trafficControl;

    /**
     * Constructor of a Condition object.
     *
     * @param attributes all the attributes in a crash
     */
    public Condition(HashMap<AttributeType, Attribute> attributes) {
        roadSurface = attributes.get(AttributeType.ROADSURFACE);
        roadWorks = attributes.get(AttributeType.ROADWORKS);
        weatherA = attributes.get(AttributeType.WEATHERA);
        weatherB = attributes.get(AttributeType.WEATHERB);
        roadLane = attributes.get(AttributeType.ROADLANE);
        intersection = attributes.get(AttributeType.INTERSECTION);
        light = attributes.get(AttributeType.LIGHT);
        numLanes = attributes.get(AttributeType.NUMBEROFLANES);
        roadCharacter = attributes.get(AttributeType.ROADCHARACTER);
        advisorySpeed = attributes.get(AttributeType.ADVISORYSPEED);
        tempSpeedLimit = attributes.get(AttributeType.TEMPSPEEDLIMIT);
        speedLimit = attributes.get(AttributeType.SPEEDLIMIT);
        streetLight = attributes.get(AttributeType.STREETLIGHT);
        trafficControl = attributes.get(AttributeType.TRAFFICCONTROL);
    }

    /**
     * Combines Speed limit, advised speed, temp speed all into a single string.
     * Contains checking to ensure the values are not null.
     * If values were null they are not displayed in the String.
     *
     * @return A formatted string displaying all relevant speed conditions
     */
    public String getSpeedString() {
        String finalStr = "";
        if ((int) speedLimit.value > 0) {
            finalStr += speedLimit.prettyPrintString + ": " + speedLimit.value;
        }
        if ((int)advisorySpeed.value > 0 && (int) advisorySpeed.value != (int) speedLimit.value ) {
            // The advisory speed is not needed if it is the same as the limit.
            finalStr += "\n" + advisorySpeed.prettyPrintString + ": " + advisorySpeed.value;
        }
        if ((int)tempSpeedLimit.value > 0 && (int) tempSpeedLimit.value != (int) speedLimit.value && (int) tempSpeedLimit.value != (int) advisorySpeed.value)  {
            // The temp speed limit should not be displayed again if is same as advised or actual speed limit.
            finalStr += "\n" + tempSpeedLimit.prettyPrintString + ": " + tempSpeedLimit.value;
        }

        // If all the values are null - unlikely but highly possible - should be stated that the speed conditions are unknown.
        if (finalStr.length() == 0) {
            finalStr += "Speed Conditions\nUnknown.";
        }
        return finalStr;
    }

    /**
     * Combines weatherA and weatherB into a single string.
     * Contains checking to ensure the values are not null.
     * If values were null they are not displayed in the String.
     *
     * @return A formatted string displaying all relevant weather conditions
     */
    public String getWeatherString(){
        String finalStr ="";
        if (((String) weatherA.value).length() > 0) {
            finalStr += weatherA.value.toString();
        }
        if (((String) weatherB.value).length() > 0 && finalStr.length() > 0) {
            finalStr += " & " +weatherB.value;
        } else {
            finalStr += weatherB.value.toString();

        }
        if (finalStr.length() == 0) {
            finalStr += "Unknown";
        }
        return finalStr;
    }

    /**
     * Gets all the attributes in the condition class
     *
     * @return a list of attributes that represent conditions
     */
    public ArrayList<Attribute> getConditionAttributes() {
        ArrayList<Attribute> conditionAttributes = new ArrayList<>();
        conditionAttributes.add(roadSurface);
        conditionAttributes.add(roadWorks);
        conditionAttributes.add(weatherA);
        conditionAttributes.add(weatherB);
        conditionAttributes.add(roadLane);
        conditionAttributes.add(intersection);
        conditionAttributes.add(light);
        conditionAttributes.add(numLanes);
        conditionAttributes.add(roadCharacter);
        conditionAttributes.add(advisorySpeed);
        conditionAttributes.add(tempSpeedLimit);
        conditionAttributes.add(speedLimit);
        conditionAttributes.add(streetLight);
        conditionAttributes.add(trafficControl);
        return conditionAttributes;
    }
}
