package seng202.team9.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * All entries of the crash data which relate to the crash location
 * Ensures easy modularisation for mapping and querying.
 */
public class Location {

    private final Attribute crashLocationOne;
    private final Attribute crashLocationTwo;
    private final Attribute region;
    private final Attribute tlaName;
    private final Attribute lat;
    private final Attribute lon;
    private final Attribute crashDirectionDescription;
    private final Attribute directionRoleDescription;
    private final Attribute crashRoadSideRoad;
    private final Attribute urban;

    /**
     * Stores all attributes relating to the location of a crash
     * @param attributes the hashmap of all attributes in a crash
     */
    public Location(HashMap<AttributeType, Attribute> attributes) {
        crashLocationOne = attributes.get(AttributeType.CRASHLOCATIONONE);
        crashLocationTwo = attributes.get(AttributeType.CRASHLOCATIONTWO);
        region = attributes.get(AttributeType.REGION);
        tlaName = attributes.get(AttributeType.TLANAME);
        lat = attributes.get(AttributeType.LAT);
        lon = attributes.get(AttributeType.LON);
        crashDirectionDescription = attributes.get(AttributeType.CRASHDIRECTIONDESCRIPTION);
        directionRoleDescription = attributes.get(AttributeType.DIRECTIONROLEDESCRIPTION);
        crashRoadSideRoad = attributes.get(AttributeType.CRASHROADSIDEROAD);
        urban = attributes.get(AttributeType.URBAN);
    }

    /**
     * Creates a string summarising all attributes of the crash location for the simple CAS viewer.
     *
     * @return a string detailing crash location(s) and region.
     */
    public String getLocationString() {
        String finalStr = "";
        if (crashLocationOne.value.toString().length() > 0) {
            finalStr += crashLocationOne.value.toString();
        }
        if (crashLocationTwo.value.toString().length() > 0) {
            finalStr += String.format(", %s", crashLocationTwo.value.toString());
        }
        if (tlaName.value.toString().length() > 0) {
            finalStr += String.format(" (%s)", tlaName.value.toString());
        }

        if (finalStr.length() == 0) {
            finalStr += "Unknown";
        }

        return finalStr;
    }


    /**
     * Gets all the attributes related to this Location.
     * @return an array list of the Attribute's related to this Location
     */
    public ArrayList<Attribute> getLocationAttributes() {
        ArrayList<Attribute> locationAttributes = new ArrayList<>();
        locationAttributes.add(crashLocationOne);
        locationAttributes.add(crashLocationTwo);
        locationAttributes.add(region);
        locationAttributes.add(tlaName);
        locationAttributes.add(lat);
        locationAttributes.add(lon);
        locationAttributes.add(crashDirectionDescription);
        locationAttributes.add(directionRoleDescription);
        locationAttributes.add(crashRoadSideRoad);
        locationAttributes.add(urban);

        return locationAttributes;
    }
}
