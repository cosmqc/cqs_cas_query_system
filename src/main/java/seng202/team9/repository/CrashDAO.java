package seng202.team9.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team9.business.WindowState;
import seng202.team9.models.Attribute;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;
import seng202.team9.models.MapKey;

import java.sql.*;
import java.util.*;

/**
 * Implementation of Database Access Object that handles all crash interactions with the database
 *
 * @author ist46 jda178 jfl80 zwa100
 */
public class CrashDAO {
    private static final Logger log = LogManager.getLogger(CrashDAO.class);
    private final WindowState state = WindowState.getInstance();
    private final DatabaseManager databaseManager;
    private static int startOfLastRetrievedBatch = 0;


    /**
     * Creates a new CrashDAO object and gets a reference to the database singleton
     */
    public CrashDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Adds a list of Crash objects to an already existing table. More robust replacement for addBatch
     * @param tableName Name of the table the items are being added to
     * @param crashes List of Crash objects to be added
     */
    public void addItemsToExistingTable(String tableName, List<Crash> crashes) {
        String sqlStmt = "INSERT OR IGNORE INTO %s".formatted(tableName) +
                "(objectID, advisorySpeed, bicycle, bridge, bus, carStationWagon, cliffBank, " +
                "crashDirectionDescription, crashFinancialYear, crashLocationOne, CrashLocationTwo, crashRoadSideRoad, crashSeverity, crashSHDescription, crashYear, debris, " +
                "directionRoleDescription, ditch, fatalCount, fence, flatHill, guardRail, holiday, houseOrBuilding, intersection, kerb, light, minorInjuryCount, moped, motorcycle, " +
                "numberOfLanes, objectThrownOrDropped, otherObject, otherVehicleType, overBank, parkedVehicle, pedestrian, phoneBoxEtc, postOrPole, region, roadCharacter, " +
                "roadLane, roadSurface, roadworks, schoolBus, seriousInjuryCount, slipOrFlood, speedLimit, strayAnimal, streetLight, suv, taxi, temporarySpeedLimit, tlaName, " +
                "trafficControl, trafficIsland, trafficSign, train, tree, truck, unknownVehicleType, urban, vanOrUtility, vehicle, waterRiver, weatherA, weatherB, lat, lon, isCustom) " +
                "values (?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement addstmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            AttributeType[] enumValues = AttributeType.values();
            for (Crash crash : crashes) {
                for (int i = 0; i < enumValues.length; i++) {
                    switch (enumValues[i].classType) {
                        // enumValues uses 0-based indexing, while addstmt uses 1-based
                        case INT -> addstmt.setInt(i + 1, (int) crash.getAttribute(enumValues[i]));
                        case STRING -> addstmt.setString(i + 1, (String) crash.getAttribute(enumValues[i]));
                        case FLOAT -> addstmt.setFloat(i + 1, (float) crash.getAttribute(enumValues[i]));
                    }
                }
                addstmt.setBoolean(enumValues.length + 1, crash.isCustom);
                addstmt.addBatch();
            }
            addstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            log.error("Error in adding to table %s: ".formatted(tableName) + e.getMessage());
        }
    }

    /**
     * Overloaded ver of addItemsToExistingTable to allow for just adding a single crash
     * Adds a single Crash object to an already existing table.
     * @param tableName Name of the table the items are being added to
     * @param crash Crash object to be added
     */
    public void addItemToExistingTable(String tableName, Crash crash) {
        ArrayList<Crash> crashes = new ArrayList<>();
        crashes.add(crash);
        addItemsToExistingTable(tableName, crashes);
    }

    /**
     * Creates a new table with the provided name, then adds the provided list to the new table.
     * @param tableName Name of the table the items are being added to
     * @param crashes List of Crash objects to be added
     */
    public void addItemsToNewTable(String tableName, List<Crash> crashes) {
        List<String> tableNames = databaseManager.getTableNames();
        if (tableNames.contains(tableName)) {
            throw new IllegalArgumentException("Table name '%s' already exists in the database".formatted(tableName));
        } else {
            databaseManager.createNewTable(tableName);
            addItemsToExistingTable(tableName, crashes);
        }
    }

    /**
     * Overloaded ver of addItemsToNewTable to allow for just adding a single crash
     * Creates a new table with the provided name, then adds the provided list to the new table.
     * @param tableName Name of the table the items are being added to
     * @param crash Crash object to be added
     */
    public void addItemsToNewTable(String tableName, Crash crash) {
        ArrayList<Crash> crashes = new ArrayList<>();
        crashes.add(crash);
        addItemsToNewTable(tableName, crashes);
    }

    /**
     * Gets one crash from database.
     * @param objectID the id of the crash to get
     * @param isCustom the isCustom tag of the crash to get
     * @param tableName name of the table to retrieve from
     * @return Crash object
     */
    public Crash getOne(int objectID, boolean isCustom, String tableName) {
        String sql = """
                        SELECT *, ROWID
                        FROM %s
                        WHERE OBJECTID=%s
                        AND ISCUSTOM=%s""".formatted(tableName, objectID, isCustom);
        List<Crash> crashes = getCrashesFromSQL(sql);
        if (crashes.size() == 1) {
            return crashes.get(0);
        } else {
            log.error("Query '%s' returned more than one result.".formatted(sql));
        }
        return null;
    }
    /**
     * Get all crashes from specified table in database
     * @param tableName name of the table to retrieve from
     * @return a list of all crashes from a given table
     */
    public ArrayList<Crash> getAllByName(String tableName) {
        if (!databaseManager.getTableNames().contains(tableName)) {
            throw new IllegalArgumentException("Database does not contain table " + tableName);
        }
        String sql = "SELECT *, ROWID FROM " + tableName;
        return getCrashesFromSQL(sql);
    }

    /**
     * Helper func that runs a given SQL Query and returns the list of crashes resulting from it
     * @param sql Complete SQL string to be executed
     * @return list of crashes resulting from the SQL query
     */
    public ArrayList<Crash> getCrashesFromSQL(String sql) {
        boolean isFirstCrashOfBatch = true;
        ArrayList<Crash> crashes = new ArrayList<>();
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ArrayList<Attribute> data = new ArrayList<>();
                AttributeType[] enumValues = AttributeType.values();
                // Handle the normal CAS attributes
                for (int i = 0; i < enumValues.length; i++) {
                    data.add(new Attribute(enumValues[i], rs.getString(i + 1)));
                }
                // Handle isCustom flag
                boolean isCustom = rs.getBoolean(enumValues.length + 1);
                crashes.add(new Crash(data, isCustom));

                // Save the row number for fast pagination
                if (isFirstCrashOfBatch) {
                    startOfLastRetrievedBatch = rs.getInt("ROWID");
                    isFirstCrashOfBatch = false;
                }
            }
        } catch (SQLException e) {
            log.error("Error executing given SQL Script '%s'\n%s".formatted(sql, e.getMessage()));
            e.printStackTrace();
        }
        return crashes;
    }

    /**
     * Helper func that runs a given SQL Query and returns the list of MapKey objects resulting from it
     * @param sql Complete SQL string to be executed
     * @return list of crashes resulting from the SQL query
     */
    public ArrayList<MapKey> getMapKeysFromSQL(String sql) {
        ArrayList<MapKey> mapKeys = new ArrayList<>();
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("OBJECTID");
                float lat = rs.getFloat("LAT");
                float lon = rs.getFloat("LON");
                boolean isCustom = rs.getBoolean("ISCUSTOM");
                mapKeys.add(new MapKey(id, lat, lon, isCustom));
            }
        } catch (SQLException e) {
            log.error("Error executing given SQL Script '%s'\n%s".formatted(sql, e.getMessage()));
        }
        return mapKeys;
    }


    /**
     * Helper func that runs a given SQL Query and returns the integer resulting from it,
     * @param sql Complete SQL string to be executed
     * @return integer resulting from the SQL query
     */
    public int getIntFromSQL(String sql) {
        int result = 0;
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            result = rs.getInt(1);
        } catch (SQLException e) {
            log.error("Error executing SQL string '%s'\n%s".formatted(sql, e.getMessage()));
        }
        return result;
    }


    /**
     * Returns the maximum crash ID, without regard to whether it is custom or not
     * @param tableName the table to get the maximum from
     * @return the highest crash id
     */
    public int getMaxCrashID(String tableName) {
        String sql = """
                SELECT OBJECTID
                FROM %s t1
                ORDER BY OBJECTID
                DESC LIMIT 1""".formatted(tableName);
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
                return rs.getInt(1);
        } catch (SQLException e) {
            log.error("There was an error finding max crash id from the DB");
            log.error(e.getMessage());
        }
        return 0;
    }

    /**
     * Returns the maximum crash ID. isCustom restricts the results to either all custom or all non-custom
     * @param tableName the table to get the maximum from
     * @param isCustom boolean flag whether the returned data should be custom
     * @return the highest used crash id
     */
    public int getMaxCrashID(String tableName, boolean isCustom) {
        String sql = """
                SELECT OBJECTID
                FROM %s t1
                WHERE t1.isCustom == %s
                ORDER BY OBJECTID
                DESC LIMIT 1""".formatted(tableName, isCustom);
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1);
        } catch (SQLException e) {
            log.error("There was an error finding max crash id from the DB");
            log.error(e.getMessage());
        }
        return 0;
    }

    /**
     * Deletes a crash from a given table.
     * @param objectID takes the id of crash as input, with isCustom makes up primary key
     * @param isCustom boolean flag that specifies whether its user inputted, with ID makes up primary key
     * @param tableName name of the table to delete from
     */
    public void delete(int objectID, boolean isCustom, String tableName) {
        String sql = """
                        DELETE FROM %s
                        WHERE OBJECTID=%s
                        AND ISCUSTOM=%s
                        """.formatted(tableName, objectID, isCustom);
        databaseManager.executeSQL(sql);
    }

    /**
     * Updates a crash in the Database.
     * @param objectID id of the crash
     * @param isCustom isCustom attribute of the crash
     * @param crashAttribute crash Attribute to update
     * @param newValue value of the attribute to update to
     * @param tableName the name of the current table to update
     */
    public void update(int objectID, boolean isCustom, String crashAttribute, String newValue, String tableName) {
        String sql = """
                        UPDATE %s
                        SET %s="%s"
                        WHERE OBJECTID=%s
                        AND ISCUSTOM=%s
                        """.formatted(tableName, crashAttribute, newValue, objectID, isCustom);
        databaseManager.executeSQL(sql);
    }

    /**
     * Returns an integer specifying the count of crashes that matches the sql statement
     *
     * @param sqlStatement the statement to query the database
     * @return the count of crashes
     */
    public int getCount(String sqlStatement) {
        int count = 0;
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlStatement)) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Error executing given SQL Script " + sqlStatement);
        }
        return count;
    }


    /**
     * @return the table row number of the last Crash that was fetched
     */
    public int getStartOfLastRetrievedBatch() {
        return startOfLastRetrievedBatch;
    }

    /**
     * Resets the count of the last retrieved crash
     */
    public void resetStartOfLastRetrievedBatch() {
        startOfLastRetrievedBatch = 0;
    }
}



