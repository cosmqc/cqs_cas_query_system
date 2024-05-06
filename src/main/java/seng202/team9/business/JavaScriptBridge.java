package seng202.team9.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;

/**
 * A bridge class between JavaScript and the Java application to interact with crash data.
 */
public class JavaScriptBridge {

    private static final Logger log = LogManager.getLogger(JavaScriptBridge.class);

    String tableName;
    CrashManager crashManager;

    /**
     * Constructs a new JavaScriptBridge with the selected table and a CrashManager instance.
     *
     * @param selectedTable      The name of the selected table.
     * @param controllerCrashManager A CrashManager instance for managing crash data.
     */
    public JavaScriptBridge(String selectedTable, CrashManager controllerCrashManager) {
        tableName = selectedTable;
        crashManager = controllerCrashManager;
    }

    /**
     * Changes the table to get the crash from when table is changed.
     * @param newTableName the table name to switch to
     */
    public void changeTableName(String newTableName) {
        this.tableName = newTableName;
    }

    /**
     * Gets crash information and encapsulates it into a JSONObject to pass back to JavaScript.
     *
     * @param crashID The crash ID for which information is requested.
     * @param isCustom The custom-data flag for the requested crash
     * @return A JSONObject containing crash information, including year, location, severity, people and vehicles involved, obstacles, speed, weather, and traffic control.
     */
    public JSONObject getInfo(int crashID, boolean isCustom) {
        Crash crash = crashManager.getCrashById(crashID, isCustom, tableName);
        JSONObject dataJSON = new JSONObject();
        dataJSON.put("year", crash.getAttribute(AttributeType.CRASHYEAR));
        dataJSON.put("location", crash.getLocation().getLocationString());
        dataJSON.put("severity", crash.getAttribute(AttributeType.CRASHSEVERITY).toString());
        dataJSON.put("peopleAndVehicles", crash.getParticipants().getPeopleVehiclesInvolved());
        dataJSON.put("objects", crash.getParticipants().getObstaclesString());
        dataJSON.put("speed", crash.getCondition().getSpeedString());
        dataJSON.put("weather", crash.getCondition().getWeatherString());
        dataJSON.put("traffic", crash.getAttribute(AttributeType.TRAFFICCONTROL).toString());
        return dataJSON;
    }
}
