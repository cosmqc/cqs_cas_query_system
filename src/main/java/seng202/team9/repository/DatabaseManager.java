package seng202.team9.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team9.business.CrashManager;
import seng202.team9.io.CSVImporter;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

/**
 * Singleton class responsible for interaction with SQLite database
 * @author Morgan English
 */
public class DatabaseManager {
    private static DatabaseManager instance = null;
    private static final Logger log = LogManager.getLogger(DatabaseManager.class);
    private final String url;

    /**
     * Map of program-used table names, serves as a place to change the table names if necessary
     * and used to stop users from overwriting them.
     * IMPORTANT: this changes all occurrences in the code; but the table name in the .sql files needs to
     * be changed and the database reset before the next run.
     */
    public final Map<String, String> protectedTableNames = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("DEFAULT_TABLE", "crashdata"),
            new AbstractMap.SimpleEntry<>("QUERY_TABLE", "cqs_query"),
            new AbstractMap.SimpleEntry<>("TEST_TABLE", "cqs_test")
    );

    /**
     * Private constructor for singleton purposes
     * Creates database if it does not already exist in specified location
     * @param urlIn the url
     */
    public DatabaseManager(String urlIn) {
        if (urlIn == null || urlIn.isEmpty()) {
            this.url = getDatabasePath();
        } else {
            this.url = urlIn;
        }
        if (!checkDatabaseExists(url)) {
            createDatabaseFile(url);
            resetDB();
        }
    }

    /**
     * Singleton method to get current Instance if exists otherwise create it
     *
     * @return the single instance DatabaseSingleton
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            // The following line can be used to reach a db file within the jar, however this will not be modifiable
            instance = new DatabaseManager("jdbc:sqlite:crashes.db");
        }
        return instance;
    }

    /**
     * Connect to the database
     *
     * @return database connection
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            log.error(e);
        }
        return conn;
    }

    /**
     * Resets the database to a completely blank slate, without re-initialising the default database
     */
    public void dropAllUserTables() {
        for (String table : getTableNames()) {
            dropTable(table);
        }
    }
    /**
     * Resets current tables if it does not exist using the sql script included in resources
     */
    public void resetDB() {
        dropAllUserTables();
        try {
            InputStream in = getClass().getResourceAsStream("/sql/initialise_database.sql");
            executeSQLScript(in);
        } catch (NullPointerException e) {
            log.error("Error loading database initialisation file", e);
        }

        String defaultTableName = protectedTableNames.get("DEFAULT_TABLE");
        DatabaseManager.getInstance().createNewTable(defaultTableName);
        CrashManager.getInstance().addInitialCrashesToDatabase(new CSVImporter(),
                "/CSV/crash_data_10k.csv", defaultTableName);
    }


    /**
     * Gets path to the database relative to the jar file
     *
     * @return jdbc encoded url location of database
     */
    private String getDatabasePath() {
        String path = DatabaseManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File jarDir = new File(path);
        return "jdbc:sqlite:" + jarDir.getParentFile() + "/crashes.db";
    }

    /**
     * Check that a database exists in the expected location
     *
     * @param url expected location to check for database
     * @return True if database exists else false
     */
    private boolean checkDatabaseExists(String url) {
        File f = new File(url.substring(12));
        return f.exists();
    }

    /**
     * Creates a database file at the location specified by the url
     *
     * @param url url to creat database at
     */
    private void createDatabaseFile(String url) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                String metaDriverLog = String.format("A new database has been created. The driver name is %s", meta.getDriverName());
                log.info(metaDriverLog);
            }
        } catch (SQLException e) {
            log.error(String.format("Error creating new database file url:%s", url));
            log.error(e);
        }
    }

    /**
     * Reads and executes all statements within the sql file provided
     * Note that each statement must be separated by '--SPLIT' this is not a desired limitation but allows for a much
     * wider range of statement types.
     *
     * @param sqlFile input stream of file containing sql statements for execution (separated by --SPLIT)
     */
    private void executeSQLScript(InputStream sqlFile) {
        String s;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(sqlFile))) {
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            String[] individualStatements = sb.toString().split("--SPLIT");
            try (Connection conn = this.connect();
                 Statement statement = conn.createStatement()) {
                for (String singleStatement : individualStatements) {
                    statement.executeUpdate(singleStatement);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("Error could not find specified database initialisation file", e);
        } catch (IOException e) {
            log.error("Error working with database initialisation file", e);
        } catch (SQLException e) {
            log.error("Error executing sql statements in database initialisation file", e);
        }
    }

    /**
     * Executes a given sql string. No return argument so best used for specific CREATEs etc
     *
     * @param sql the sql string to be executed
     */
    public void executeSQL(String sql) {
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("Error executing SQL command '%s': %s".formatted(sql, e.getMessage()));
        }
    }

    /**
     * Gets the names of all non-protected tables in database and returns them as a list
     *
     * @return List of table name Strings, excluding protected tables.
     */
    public List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");

            while (rs.next()) {
                String tableName = rs.getString("name");
                String queryTableName = protectedTableNames.get("QUERY_TABLE");
                if (!tableName.startsWith("sqlite_") && !tableName.equals(queryTableName)) {
                    tableNames.add(tableName);
                }
            }
        } catch (SQLException e) {
            log.error("Error in fetching table names");
            log.error(e.getMessage());
        }
        return tableNames;
    }

    /**
     * Creates a new table in the current database with the provided name.
     * If a table with the same name already exists in the database, the command is ignored.
     *
     * @param tableName String containing the name of the table to be created.
     */
    public void createNewTable(String tableName) {
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS %s".formatted(tableName));
            stmt.execute("CREATE TABLE IF NOT EXISTS %s (".formatted(tableName) +
                    """
                               OBJECTID INTEGER,
                               advisorySpeed INTEGER,
                               bicycle INTEGER,
                               bridge INTEGER,
                               bus INTEGER,
                               carStationWagon INTEGER,
                               cliffBank INTEGER,
                               crashDirectionDescription TEXT,
                               crashFinancialYear VARCHAR(10),
                               crashLocationOne TEXT,
                               crashLocationTwo TEXT,
                               crashRoadSideRoad INTEGER,
                               crashSeverity TEXT,
                               crashSHDescription VARCHAR(3),
                               crashYear INTEGER,
                               debris INTEGER,
                               directionRoleDescription VARCHAR(5),
                               ditch INTEGER,
                               fatalCount INTEGER,
                               fence INTEGER,
                               flatHill VARCHAR (10),
                               guardRail INTEGER,
                               holiday TEXT,
                               houseOrBuilding INTEGER,
                               intersection TEXT,
                               kerb INTEGER,
                            light TEXT,
                            minorInjuryCount INTEGER,
                            moped INTEGER,
                            motorcycle INTEGER,
                            numberOfLanes INTEGER,
                            objectThrownOrDropped INTEGER,
                            otherObject INTEGER,
                            otherVehicleType INTEGER,
                            overBank INTEGER,
                            parkedVehicle INTEGER,
                            pedestrian INTEGER,
                            phoneBoxEtc INTEGER,
                            postOrPole INTEGER,
                            region TEXT,
                            roadCharacter TEXT,
                            roadLane VARCHAR(10),
                            roadSurface VARCHAR(8),
                            roadworks INTEGER,
                            schoolBus INTEGER,
                            seriousInjuryCount INTEGER,
                            slipOrFlood INTEGER,
                            speedLimit INTEGER,
                            strayAnimal INTEGER,
                            streetLight VARCHAR(10),
                            suv INTEGER,
                            taxi INTEGER,
                            temporarySpeedLimit INTEGER,
                            tlaName TEXT,
                            trafficControl TEXT,
                            trafficIsland INTEGER,
                            trafficSign INTEGER,
                            train INTEGER,
                            tree INTEGER,
                            truck INTEGER,
                            unknownVehicleType INTEGER,
                            urban VARCHAR(10),
                            vanOrUtility INTEGER,
                            vehicle INTEGER,
                            waterRiver INTEGER,
                            weatherA VARCHAR(15),
                            weatherB VARCHAR(15),
                               lat REAL,
                               lon REAL,
                               isCustom INTEGER,
                               UNIQUE (OBJECTID, isCustom)
            );""");
        } catch (SQLException e) {
            log.error("Error in creating new table '%s': ".formatted(tableName) + e.getMessage());
        }
    }

    /**
     * Drops a table from the current database with the provided name.
     * If a table with the same name does not exist in the database, the command is ignored.
     *
     * @param tableName String containing the name of the table to be dropped.
     */
    public void dropTable(String tableName) {
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS %s;".formatted(tableName));
        } catch (SQLException e) {
            log.error("Error in dropping table '%s': ".formatted(tableName) + e.getMessage());
        }
    }

    /**
     * @return a list of table names that the user cannot overwrite.
     * This includes sqlite names, and program-used names.
     */
    public List<String> getProtectedTableNames() {
        List<String> programUsed = new ArrayList<>(protectedTableNames.values());
        programUsed.add("table"); // Reserved by SQLite
        return programUsed;
    }
}
