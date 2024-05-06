package seng202.team9.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team9.io.Importable;
import seng202.team9.models.*;
import seng202.team9.models.stringenums.Obstacles;
import seng202.team9.models.stringenums.PeopleAndVehicles;
import seng202.team9.repository.CrashDAO;
import seng202.team9.repository.DatabaseManager;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Class to handle all actions for crashes. It acts as a controller, interacting with the gui (view) package and the models/repository packages.
 * @author ist46
 */
public class CrashManager {
    private static final Logger log = LogManager.getLogger(CrashManager.class);
    private final CrashDAO crashDAO;
    private static CrashManager instance = null;
    private static WindowState state;
    private static int lastBatch = 0;

    /** Maximum number of crashes to display on a page */
    public static final int TABLE_BATCH_SIZE = 20;
    /**
     * Maximum number of crashes to display on a map
     */
    public static final int MAP_BATCH_SIZE = 100000;
    /**
     * Constructor for CrashManager which instantiates a CrashDAO object which it uses to interact with the database
     */
    public CrashManager() {
        crashDAO = new CrashDAO();
        state = WindowState.getInstance();
    }

    /**
     * Singleton implementation. Returns current instance, creating it if necessary.
     * @return Singleton instance of CrashManager
     */
    public static CrashManager getInstance() {
        if (instance == null) {
            instance = new CrashManager();
        }
        return instance;
    }

    /**
     * Uses the CSVImporter to get the CSV file with a Stream and add it to a table in the database
     * @param importer  object to get the csv contents
     * @param filename the file to be read and uploaded
     * @param tableName the table to be uploaded into
     */
    public void addInitialCrashesToDatabase(Importable<Crash> importer, String filename, String tableName) {
        int currBatch = 0;
        List<Crash> crashes;
        do {
            crashes = importer.readInitialFileBatch(filename, currBatch);
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            if (databaseManager.getTableNames().contains(tableName)) {
                crashDAO.addItemsToExistingTable(tableName, crashes);
            } else {
                crashDAO.addItemsToNewTable(tableName, crashes);
            }
            currBatch++;
        } while (crashes.size() > 0);
    }

    /**
     * Uses the CSVImporter to get the CSV file and add it to a table in the database
     * @param importer  object to get the csv contents
     * @param filename the file to be read and uploaded
     * @param tableName the table to be uploaded into
     */
    public void addCrashesToDatabase(Importable<Crash> importer, File filename, String tableName) {
        int currBatch = 0;
        List<Crash> crashes;
        do {
            crashes = importer.readFileBatch(filename, currBatch);
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            if (databaseManager.getTableNames().contains(tableName)) {
                crashDAO.addItemsToExistingTable(tableName, crashes);
            } else {
                crashDAO.addItemsToNewTable(tableName, crashes);
            }
            currBatch++;
        } while (crashes.size() > 0);
    }


    /**
     * Adds a crash entry
     * @param crash crash to be added
     * @param tableName table to add the crash to
     * @throws SQLException an exception received from an invalid crash id
     */
    public void addCrash(Crash crash, String tableName) throws SQLException {
        crashDAO.addItemToExistingTable(tableName, crash);
    }

    /**
     * Deletes a crash entry
     * @param crash crash to be deleted
     * @param tableName the table to be deleted from
     */
    public void deleteCrash(Crash crash, String tableName) {
        crashDAO.delete((int)crash.getAttribute(AttributeType.OBJECTID), crash.isCustom, tableName);
    }



    /**
     * Gets a crash by its object ID
     * @param crashID ID of the crash object, with the isCustom flag this uniquely identifies the crash
     * @param isCustom Whether the crash is user-inputted or not, with the ID this uniquely identifies the crash
     * @param tableName the name of the table we're retrieving from
     * @return the requested Crash object
     */
    public Crash getCrashById(int crashID, boolean isCustom, String tableName) {
        return crashDAO.getOne(crashID, isCustom, tableName);
    }


    /**
     * Updates a given crash object attribute in a given table with a new value
     * @param crashID ID of the crash object
     * @param isCustom isCustom attribute of the crash object
     * @param attributeType string of the attribute type
     * @param newValue the value the attribute will be updated to
     * @param tableName name of the table containing the crash
     */
    public void updateCrash(int crashID, boolean isCustom, String attributeType, String newValue, String tableName) {
        crashDAO.update(crashID, isCustom, attributeType, newValue, tableName);
    }

    /**
     * Gets the counts of each attribute in the database
     *
     * @param attributes list of selected attributes
     * @param category the selected category
     *
     * @return A list of integers which represent the counts corresponding to each attribute
     */
    public List<Integer> getCounts(String category, List<String> attributes) {
        String queryTableName = DatabaseManager.getInstance().protectedTableNames.get("QUERY_TABLE");
        String tableStr = state.getCurrentQuery() == null ? state.getCurrentTable() : queryTableName;
        String sqlStatement = "SELECT count(*) FROM " + tableStr + " WHERE ";
        List<Integer> counts = new ArrayList<>();
        int count;
        switch (category) {
            case "Participants" -> {
                for (String attribute : attributes) {
                    PeopleAndVehicles participant = PeopleAndVehicles.fromString(attribute);
                    String sql = sqlStatement + participant + " > 0";
                    count = crashDAO.getCount(sql);
                    counts.add(count);
                }
            }
            case "Obstacles" -> {
                for (String attribute : attributes) {
                    Obstacles obstacle = Obstacles.fromString(attribute);
                    String sql = sqlStatement + obstacle + " > 0";
                    count = crashDAO.getCount(sql);
                    counts.add(count);
                }
            }
            case "Severity" -> {
                sqlStatement += "crashSeverity = '";
                for (String attribute : attributes) {
                    String sql = sqlStatement + attribute + "'";
                    count = crashDAO.getCount(sql);
                    counts.add(count);
                }
            }
            case "Light" -> {
                sqlStatement += "Light = '";
                for (String attribute : attributes) {
                    String sql = sqlStatement + attribute + "'";
                    count = crashDAO.getCount(sql);
                    counts.add(count);
                }
            }
            case "Holiday" -> {
                sqlStatement += "Holiday = '";
                for (String attribute : attributes) {
                    String sql = sqlStatement + attribute + "'";
                    count = crashDAO.getCount(sql);
                    counts.add(count);
                }
            }
            case "Region" -> {
                sqlStatement += "Region = '";
                for (String attribute : attributes) {
                    String sql;
                    if (attribute.equals("Hawke's Bay")) {
                        sql = sqlStatement + "Hawke''s Bay Region'";
                    } else {
                        sql = sqlStatement + attribute + " Region'";
                    }
                    count = crashDAO.getCount(sql);
                    counts.add(count);
                }
            }
            case "Traffic Control" -> {
                sqlStatement += "trafficControl = '";
                for (String attribute : attributes) {
                    String sql = sqlStatement + attribute + "'";
                    count = crashDAO.getCount(sql);
                    counts.add(count);
                }
            }
            case "Weather" -> {
                String sqlStatementA = sqlStatement + "weatherA = '";
                String sqlStatementB = sqlStatement + "weatherB = '";
                for (String attribute : attributes) {
                    String sql;
                    if (attribute.equals("Strong wind") || attribute.equals("Frost")) {
                        sql = sqlStatementB + attribute + "'";
                    } else {
                        sql = sqlStatementA + attribute + "'";
                    }
                    count = crashDAO.getCount(sql);
                    counts.add(count);
                }
            }
            case "Year" -> {
                sqlStatement += "crashYear = ";
                for (String attribute : attributes) {
                    String sql = sqlStatement + attribute;
                    count = crashDAO.getCount(sql);
                    counts.add(count);
                }
            }
        }
        return counts;
    }


    /**
     * Returns a 'batch' of crashes from the database with size BATCH_SIZE.
     * @param batchNo The number of the batch to get, zero-based so equivalent to (page number - 1)
     * @return An ArrayList of [BATCH_SIZE] Crash objects
     */
    public ArrayList<Crash> getCrashBatch (int batchNo) {
        String sql;
        String queryTableName = DatabaseManager.getInstance().protectedTableNames.get("QUERY_TABLE");
        String tableStr = state.getCurrentQuery() == null ? state.getCurrentTable() : queryTableName;
        if (lastBatch <= batchNo) {
            int startEntry = crashDAO.getStartOfLastRetrievedBatch() + TABLE_BATCH_SIZE * (batchNo - lastBatch);
            sql = """
                    SELECT *, ROWID
                    FROM %s
                    WHERE %s <= ROWID
                    ORDER BY ROWID
                    LIMIT %s
                    """.formatted(tableStr, startEntry, TABLE_BATCH_SIZE);
        } else {
            int startEntry = crashDAO.getStartOfLastRetrievedBatch() - (TABLE_BATCH_SIZE * (lastBatch - batchNo - 1) + 1);
            sql = """
                    SELECT *, ROWID
                    FROM (
                         SELECT *, ROWID
                         FROM %s
                         WHERE ROWID <= %s
                         ORDER BY ROWID DESC
                         LIMIT %s
                         )
                    ORDER BY ROWID ASC;
                    """.formatted(tableStr, startEntry, TABLE_BATCH_SIZE);
        }
        lastBatch = batchNo;
        return crashDAO.getCrashesFromSQL(sql);
    }

    /**
     * Retrieves a batch of MapKey objects from the database for mapping purposes.
     *
     * @param batchNo The batch number to retrieve.
     * @return An ArrayList of MapKey objects representing the map keys for the specified batch.
     */
    public ArrayList<MapKey> getMapKeyBatch(int batchNo) {
        String sql;
        String queryTableName = DatabaseManager.getInstance().protectedTableNames.get("QUERY_TABLE");
        String tableStr = state.getCurrentQuery() == null ? state.getCurrentTable() : queryTableName;
        int startEntry = batchNo * MAP_BATCH_SIZE;
        sql = """
                SELECT OBJECTID, LAT, LON, ISCUSTOM, ROWID
                FROM %s
                WHERE %s <= ROWID
                ORDER BY ROWID
                LIMIT %s
                """.formatted(tableStr, startEntry, MAP_BATCH_SIZE);
        log.info("Loading mapKeys %s -> %s".formatted(startEntry, startEntry+MAP_BATCH_SIZE));
        return crashDAO.getMapKeysFromSQL(sql);
    }

    /**
     * Gets the next available ID number where the crash isCustom
     * @param tableName Table to query
     * @return INT representing the next available custom ID
     */
    public int getNextCustomID(String tableName) {
        return crashDAO.getMaxCrashID(tableName, true) + 1;
    }

    /**
     * Gets the total number of 'batches' available from the current table.
     * @return Number of batches needed to fit the data for the specified table
     */
    public int getNumBatches() {
        String queryTableName = DatabaseManager.getInstance().protectedTableNames.get("QUERY_TABLE");
        String tableStr = state.getCurrentQuery() == null ? state.getCurrentTable() : queryTableName;
        String sql = """
                SELECT ROWID
                FROM %s
                ORDER BY ROWID DESC
                LIMIT 1
                """.formatted(tableStr);
        float numCrashes = crashDAO.getIntFromSQL(sql);
        return (int) Math.ceil(numCrashes / TABLE_BATCH_SIZE);
    }

    /**
     * Resets the protected query table to contain the result of the current query
     */
    public void setQueryTable() {
        String queryTableName = DatabaseManager.getInstance().protectedTableNames.get("QUERY_TABLE");
        DatabaseManager.getInstance().dropTable(queryTableName);
        String sql = """
                CREATE TABLE %s as
                SELECT * FROM %s
                WHERE %s
                """.formatted(queryTableName, state.getCurrentTable(), state.getCurrentQuery());
        DatabaseManager.getInstance().executeSQL(sql);
    }

    /**
     * Resets the last retrieved crash variable to provide a blank slate on each table open
     */
    public void refreshTable() {
        crashDAO.resetStartOfLastRetrievedBatch();
    }
}
