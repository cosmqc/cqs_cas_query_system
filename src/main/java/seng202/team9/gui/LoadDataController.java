package seng202.team9.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team9.business.CrashManager;
import seng202.team9.io.CSVImporter;
import seng202.team9.repository.CrashDAO;
import seng202.team9.repository.DatabaseManager;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;


/**
 * Controller class for the load data modal
 */
public class LoadDataController {
    private static final Logger log = LogManager.getLogger(CrashDAO.class);

    /**
     * Hold table name from LoadData GUI
     */
    @FXML
    private TextField tableNameField;

    /**
     * Displays table names
     */
    @FXML
    private ListView<String> existingTableNames;

    /**
     * Group toggle for radio buttons
     */
    @FXML
    private ToggleGroup tableSelectionToggleGroup;

    /**
     * Exisitng table radio button
     */
    @FXML
    private RadioButton existingTableButton;

    /**
     * New table radio button
     */
    @FXML
    private RadioButton newTableButton;

    /**
     * Current Radio Button
     */
    @FXML
    private Toggle currentToggle;

    /**
     * The upload file button
     */
    @FXML
    private Button uploadFile;

    /**
     * The current stage
     */
    private Stage stage;
    private MainController mainController;

    /**
     * Initialises the load data gui
     * @param stage the current stage
     * @param mainController the mainController
     **/
    public void init(Stage stage, MainController mainController) {
        this.stage = stage;
        this.mainController = mainController;
        Image appIcon = new Image("icons/appIcon.png");
        stage.getIcons().add(appIcon);
    }

    /**
     * Local storage of selected file
     */
    private File selectedFile;

    /**
     * The name of the table
     */
    private String tableName;

    /**
     * Opens a filechooser window and allows a user to select a single and only *.csv file to import
     * and stores the File in selectedFile
     */
    @FXML
    void getCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        try {
            fileChooser.setInitialDirectory(new File(MainController.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI()).getParentFile());
        } catch (URISyntaxException e) {
            log.warn(e.getMessage());
        }


        // Store selected file locally for later
        selectedFile = fileChooser.showOpenDialog(stage);
        if(!(selectedFile == null)) {
            // Update/Display selected file name to user
            uploadFile.setText(selectedFile.getName());
        } else {
            uploadFile.setText("Upload CSV File");
        }
    }

    /**
     * Function that controls the GUI and response to user choices
     */
    public void initialize() {
        // Initialize empty ListView
        existingTableNames.setItems(FXCollections.observableArrayList());

        // Add listener to ToggleGroup
        tableSelectionToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            //Update From class level variable
            currentToggle = newValue;

            // If existingTableButton selected
            if (newValue == existingTableButton) {
                // Clear the text field
                tableNameField.clear();

                // Fetch table names in database
                DatabaseManager databaseManager = DatabaseManager.getInstance();
                List<String> tableNames = databaseManager.getTableNames();

                // Update the ListView
                existingTableNames.setItems(FXCollections.observableArrayList(tableNames));

                // Make the TextField non-editable
                tableNameField.setEditable(false);

                // Listener for ListView selection changes to update text field
                existingTableNames.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        tableNameField.setText(newSelection);
                    }
                });
            }

            // If newTableButton selected
            if (newValue == newTableButton ) {

                // Clear the text field
                tableNameField.clear();

                // Clear the ListView
                existingTableNames.getItems().clear();

                // Make the TextField non-editable
                tableNameField.setEditable(true);
            }
        });
    }

    /**
     * Method that processes the users uploaded data and
     * checks if file has been selected and a table name entered
     * Then puts the data in new table or in existing table
     */
    @FXML
    private void loadRawDataTableView() {

        // Check if a file has been selected
        if (selectedFile == null) {
            // Show error message for file selection
            Alert fileAlert = new Alert(AlertType.ERROR);
            fileAlert.setTitle("Error Dialog");
            fileAlert.setHeaderText("File Selection Error");
            fileAlert.setContentText("Please select a valid CSV file.");
            fileAlert.showAndWait();
            return;
        }

        if(currentToggle == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Button Selection Error");
            alert.setContentText("Please choose between new and existing table");
            alert.showAndWait();
            return;
        }

        // Passed input checks


        // Create crash manager
        CrashManager crashManager = CrashManager.getInstance();

        // Read user selected CSV file and add to table from specified name
        if (currentToggle == existingTableButton) {

            if (tableNameField.getText().trim().isEmpty()) {
                // Show error message
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("No table selected");
                alert.setContentText("Please select a table.");
                alert.showAndWait();
                return;
            }
            tableName = tableNameField.getText();
            // Add data to database table
            crashManager.addCrashesToDatabase(new CSVImporter(), selectedFile, tableName);
        }

        // Read user selected CSV file and add to new table from specified name
        if (currentToggle == newTableButton) {

            // Check if tableNameField is empty or contains only spaces
            if (!validateTableName(tableNameField.getText())) {
                // Show error message
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Table Name Error");
                alert.setContentText("Please enter a valid table name.");
                alert.showAndWait();
                return;
            }

            // Get table name
            tableName = tableNameField.getText();
            // Fetch table names in database
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            List<String> tableNames = databaseManager.getTableNames();

            // Check if the table name already exists
            if (tableNames.contains(tableName)) {
                // Show error message
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Table Name Already Exists");
                alert.setHeaderText("The table name you entered is already in use.");
                alert.setContentText("Please choose a different table name.");
                alert.showAndWait();
                // Dont close load data
                return;
            }

            if (tableName.trim().contains((" "))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Table Name");
                alert.setHeaderText("The table name must not contain any spaces");
                alert.setContentText("Please choose a different table name.");
                alert.showAndWait();
                // Dont close load data
                return;
            }

            // Add data to database table
            crashManager.addCrashesToDatabase(new CSVImporter(), selectedFile, tableName);

        }

        // Check for CSV importer errors
        if (checkCSVImporterErrors()) {

            // Throw error message

            // Fetch table names in database
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            List<String> tableNames = databaseManager.getTableNames();

            // Revert table created if new table was selected
            if (currentToggle == newTableButton & tableNames.contains(tableName)) {
                databaseManager.dropTable(tableName);
            }

            // Error found, Keep load data GUI open
            return;

        } else {
            if (currentToggle == existingTableButton) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Added CSV to " + tableName);
                alert.setHeaderText(null);
                alert.setContentText("The csv has been successfully added to " + tableName + ". You can select " + tableName + " from the drop down table menu");
                alert.showAndWait();
            } else if (currentToggle == newTableButton) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Created new table: " + tableName);
                alert.setHeaderText(null);
                alert.setContentText("The csv has been added to the new table: " + tableName + ". You can select " + tableName + " from the drop down table menu");
                alert.showAndWait();
            }

        }

        // Passed all checks, data successfully added to database
        // Re-init the drop-down menu in the main controller
        mainController.updateTableNames();
        mainController.updateTable(tableName);
        close();
    }

    /**
     * Ensures the user-inputted table name is valid
     * @param tableName the table name to check
     * @return boolean flag representing whether the given table name is valid
     */
    private boolean validateTableName(String tableName) {
        tableName = tableName.trim().toLowerCase();
        return !tableName.isEmpty() &&
                !DatabaseManager.getInstance().getProtectedTableNames().contains(tableName) &&
                !tableName.startsWith("sqlite_");
    }

    /**
     * Function to return true or false if there were CSVImporter Errors
     */
    private boolean checkCSVImporterErrors() {
            // Check for CSV importer errors
            if ("ERROR".equals(CSVImporter.getLastLogLevel())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("CSV Validation Error");
                alert.setHeaderText("An error occurred while validating the CSV file.");
                alert.setContentText("Please make sure the CSV file is formatted correctly.");
                alert.showAndWait();

                // Rest the LastLogLevel
                CSVImporter.setLastLogLevel(null);

                return true;
            }

            return false;
        }

    /**
     * Closes the load data modal
     */
    public void close() {
        stage.close();
    }





}
