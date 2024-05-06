package seng202.team9.business;

import seng202.team9.gui.ViewController;
import seng202.team9.repository.DatabaseManager;
import seng202.team9.repository.Query;

/**
 * Singleton class responsible for storing the variables that remain consistent across the whole application.
 * @author Jake Dalton
 */
public class WindowState {

    private static WindowState instance;

    private String currentTable;
    private Query currentQuery;
    private ViewController currentController;

    /**
     * Initialize the WindowState to its defaults.
     * In this case, the table should start as the program's default table with no query applied.
     */
    public WindowState() {
        currentTable = DatabaseManager.getInstance().protectedTableNames.get("DEFAULT_TABLE");
        currentQuery = null;
    }

    /**
     * Singleton method to get current Instance if exists otherwise create it
     *
     * @return the single instance of WindowState
     */
    public static WindowState getInstance() {
        if (instance == null) {
            instance = new WindowState();
        }
        return instance;
    }

    /**
     * @return the current table
     */
    public String getCurrentTable() {
        return currentTable;
    }

    /**
     * @return the current Query
     */
    public Query getCurrentQuery() {
        return currentQuery;
    }

    /**
     * @return the current ViewController
     */
    public ViewController getCurrentController() {
        return currentController;
    }

    /**
     * Sets the value of the current table, but doesn't switch to it.
     * @param tableName name of the table to set.
     */
    public void setCurrentTable(String tableName) {
        currentTable = tableName;
    }

    /**
     * Sets the value of the current query, but doesn't apply it.
     * New query will be applied on a set
     * @param query Query to set
     */
    public void setCurrentQuery(Query query) {
        currentQuery = query;
    }

    /**
     * Sets the value of the current controller, but doesn't switch to it.
     * @param controller ViewController to set
     */
    public void setCurrentController(ViewController controller) {
        currentController = controller;
    }
}
