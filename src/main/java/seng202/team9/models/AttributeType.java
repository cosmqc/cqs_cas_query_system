package seng202.team9.models;



/**
 * Enum storing all the CAS data headers and what data type they should be.
 * We have the option to add custom default values to certain attributes.
 * e.g. AdvisorySpeed should likely be empty, not 0 like the other ints.
 *
 * @author Jake Dalton
 */
public enum AttributeType {
    /**
     * Unique identifier for an object.
     */
    OBJECTID("ID", ClassType.INT),

    /**
     * Advisory speed related to a location.
     */
    ADVISORYSPEED("Advisory Speed", ClassType.INT),

    /**
     * Number of bicycles.
     */
    BICYCLE("Bicycles", ClassType.INT),

    /**
     * Number of bridges.
     */
    BRIDGE("Bridges", ClassType.INT),

    /**
     * Number of buses.
     */
    BUS("Buses", ClassType.INT),

    /**
     * Number of car or station wagon vehicles.
     */
    CARSTATIONWAGON("Car/Station Wagon", ClassType.INT),

    /**
     * Number of cliff banks.
     */
    CLIFFBANK("Cliff Banks", ClassType.INT),

    /**
     * Description of crash direction.
     */
    CRASHDIRECTIONDESCRIPTION("Crash Direction Description", ClassType.STRING),

    /**
     * Financial year associated with a crash.
     */
    CRASHFINANCIALYEAR("Financial Year", ClassType.STRING),

    /**
     * Location one of a crash.
     */
    CRASHLOCATIONONE("Location One", ClassType.STRING),

    /**
     * Location two of a crash.
     */
    CRASHLOCATIONTWO("Location Two", ClassType.STRING),
    /**
     * Number of vehicles on the side road involved in a crash.
     */
    CRASHROADSIDEROAD("Side Road", ClassType.INT),

    /**
     * Severity level of a crash.
     */
    CRASHSEVERITY("Severity", ClassType.STRING),

    /**
     * Description of a State Highway (SH) where a crash occurred.
     */
    CRASHSHDESCRIPTION("SH Description", ClassType.STRING),

    /**
     * Year of a crash event.
     */
    CRASHYEAR("Year", ClassType.INT),

    /**
     * Number of debris involved in a crash.
     */
    DEBRIS("Debris", ClassType.INT),

    /**
     * Description of a direction's role in a crash.
     */
    DIRECTIONROLEDESCRIPTION("Direction Role Description", ClassType.STRING),

    /**
     * Number of ditches involved in a crash.
     */
    DITCH("Ditches", ClassType.INT),

    /**
     * Number of fatalities in a crash.
     */
    FATALCOUNT("Fatality Count", ClassType.INT),

    /**
     * Number of fences involved in a crash.
     */
    FENCE("Fences", ClassType.INT),

    /**
     * Description of whether a location is flat or hilly.
     */
    FLATHILL("Flat/Hill", ClassType.STRING),

    /**
     * Number of guard rails involved in a crash.
     */
    GUARDRAIL("Guard Rails", ClassType.INT),

    /**
     * Description of a holiday associated with a crash.
     */
    HOLIDAY("Holiday", ClassType.STRING),

    /**
     * Number of houses or buildings involved in a crash.
     */
    HOUSEORBUILDING("Houses/Buildings", ClassType.INT),
    /**
     * Description of an intersection where a crash occurred.
     */
    INTERSECTION("Intersection", ClassType.STRING),

    /**
     * Number of kerbs involved in a crash.
     */
    KERB("Kerbs", ClassType.INT),

    /**
     * Light level at the crash location.
     */
    LIGHT("Light Level", ClassType.STRING),

    /**
     * Number of minor injuries in a crash.
     */
    MINORINJURYCOUNT("Minor Injury Count", ClassType.INT),

    /**
     * Number of mopeds involved in a crash.
     */
    MOPED("Mopeds", ClassType.INT),

    /**
     * Number of motorcycles involved in a crash.
     */
    MOTORCYCLE("Motorcycles", ClassType.INT),

    /**
     * Number of lanes at the crash location.
     */
    NUMBEROFLANES("Number of lanes", ClassType.INT),

    /**
     * Number of objects thrown or dropped during a crash.
     */
    OBJECTTHROWNORDROPPED("Objects Thrown/Dropped", ClassType.INT),

    /**
     * Number of other objects involved in a crash.
     */
    OTHEROBJECT("Other Objects", ClassType.INT),

    /**
     * Number of other types of vehicles involved in a crash.
     */
    OTHERVEHICLETYPE("Other Vehicles", ClassType.INT),

    /**
     * Number of embankment interactions involved in a crash.
     */
    OVERBANK("Embankment interactions", ClassType.INT),

    /**
     * Number of parked vehicles involved in a crash.
     */
    PARKEDVEHICLE("Parked Vehicles", ClassType.INT),

    /**
     * Number of pedestrians involved in a crash.
     */
    PEDESTRIAN("Pedestrians", ClassType.INT),

    /**
     * Number of public furniture objects involved in a crash.
     */
    PHONEBOXETC("Public Furniture", ClassType.INT),

    /**
     * Number of posts or poles involved in a crash.
     */
    POSTORPOLE("Post/Pole", ClassType.INT),

    /**
     * Description of a region where a crash occurred.
     */
    REGION("Region", ClassType.STRING),
    /**
     * Description of the road character.
     */
    ROADCHARACTER("Road Character", ClassType.STRING),

    /**
     * Configuration of road lanes.
     */
    ROADLANE("Lane Configuration", ClassType.STRING),

    /**
     * Description of the road surface.
     */
    ROADSURFACE("Road Surface", ClassType.STRING),

    /**
     * Number of roadworks.
     */
    ROADWORKS("Roadworks", ClassType.INT),

    /**
     * Number of school buses involved in a crash.
     */
    SCHOOLBUS("School Buses", ClassType.INT),

    /**
     * Number of serious injuries in a crash.
     */
    SERIOUSINJURYCOUNT("Serious Injury Count", ClassType.INT),

    /**
     * Number of slips or floods involved in a crash.
     */
    SLIPORFLOOD("Slips/Floods", ClassType.INT),

    /**
     * Speed limit on the road.
     */
    SPEEDLIMIT("Speed Limit", ClassType.INT),

    /**
     * Number of stray animals involved in a crash.
     */
    STRAYANIMAL("Stray Animals", ClassType.INT),

    /**
     * Description of streetlights at the crash location.
     */
    STREETLIGHT("Street Lights", ClassType.STRING),

    /**
     * Number of SUVs involved in a crash.
     */
    SUV("SUVs", ClassType.INT),

    /**
     * Number of taxis involved in a crash.
     */
    TAXI("Taxis", ClassType.INT),

    /**
     * Temporary speed limit at the crash location.
     */
    TEMPSPEEDLIMIT("Temporary Speed Limit", ClassType.INT),

    /**
     * Name of the TLA (Territorial Local Authority).
     */
    TLANAME("TLA Name", ClassType.STRING),

    /**
     * Description of traffic control at the crash location.
     */
    TRAFFICCONTROL("Traffic Control", ClassType.STRING),

    /**
     * Number of traffic islands involved in a crash.
     */
    TRAFFICISLAND("Traffic Islands", ClassType.INT),

    /**
     * Number of traffic signs involved in a crash.
     */
    TRAFFICSIGN("Traffic Signs", ClassType.INT),

    /**
     * Number of trains involved in a crash.
     */
    TRAIN("Trains", ClassType.INT),

    /**
     * Number of trees involved in a crash.
     */
    TREE("Trees", ClassType.INT),

    /**
     * Number of trucks involved in a crash.
     */
    TRUCK("Trucks", ClassType.INT),

    /**
     * Number of unknown vehicle types involved in a crash.
     */
    UNKNOWNVEHICLETYPE("Unknown Vehicle Types", ClassType.INT),

    /**
     * Description of an urban area.
     */
    URBAN("Urban", ClassType.STRING),

    /**
     * Number of van or utility vehicles involved in a crash.
     */
    VANORUTILITY("Van/Utility Vehicles", ClassType.INT),

    /**
     * Number of vehicles involved in a crash.
     */
    VEHICLE("Vehicles", ClassType.INT),

    /**
     * Number of bodies of water involved in a crash.
     */
    WATERRIVER("Bodies of water", ClassType.INT),

    /**
     * Description of weather conditions (Weather Type A).
     */
    WEATHERA("Weather A", ClassType.STRING),

    /**
     * Description of weather conditions (Weather Type B).
     */
    WEATHERB("Weather B", ClassType.STRING),

    /**
     * Latitude information.
     */
    LAT("Latitude", ClassType.FLOAT),

    /**
     * Longitude information.
     */
    LON("Longitude", ClassType.FLOAT);

    /**
     * A more readable format of the attributeTypes
     */
    public final String prettyPrint;
    /**
     * The data type of the attributeType
     */
    public final ClassType classType;


    AttributeType(String prettyPrint, ClassType classType) {
        this.prettyPrint = prettyPrint;
        this.classType = classType;
    }

    /**
     * Converts the prettyPrint form back to the original AttributeType
     * @param text the prettyPrint form
     * @return the AttributeType associated with the prettyPrint form.
     */
    public static AttributeType fromString(String text) {
        for (AttributeType item : AttributeType.values()) {
            if (item.prettyPrint.equalsIgnoreCase(text)) {
                return item;
            }
        }
        throw new IllegalArgumentException("No matching AttributeType enum value for: " + text);
    }
}

