package seng202.team9.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team9.business.CrashManager;
import seng202.team9.business.QueryManager;
import seng202.team9.business.WindowState;
import seng202.team9.models.ModalType;
import seng202.team9.repository.DatabaseManager;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static seng202.team9.io.CSVImporter.setLastLogLevel;

/**
 * Controller for the main.fxml window
 * @author seng202 teaching team
 */
public class MainController {

    private static final Logger log = LogManager.getLogger(MainController.class);
    private static WindowState state;
    private Stage stage;
    @FXML
    private BorderPane mainWindow;
    @FXML
    private ComboBox<String> existingTablesDropdown;

    /**
     * Holds current modal
     */
    private Stage currentModal = null;
    /**
     * Holds the current modal type
     */
    public ModalType currentModalType = ModalType.NONE;

    /**
     * Menu containing delete method
     */
    private MenuItem deleteItem;

    /**
     * Initialize the window
     *
     * @param stage Top level container for this window
     */
    public void init(Stage stage) {
        // Store current stage
        this.stage = stage;
        state = WindowState.getInstance();

        loadCASTableView(stage);
        initTableNameDropDown();
        updateTableNames();
        updateTable(state.getCurrentTable());

        stage.sizeToScene();

        // Adds context menu that allows for deleting table
        deleteContextMenu();

        // Listener for deleting tables from comboBox
        deleteItem.setOnAction(e -> {
            String selectedTable = existingTablesDropdown.getSelectionModel().getSelectedItem();

            // Prevent deletion of empty table or main data set
            String mainDataset = DatabaseManager.getInstance().protectedTableNames.get("DEFAULT_TABLE");
            if (selectedTable == null || selectedTable.equals(mainDataset)) {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error Dialog");
                errorAlert.setHeaderText("Invalid Operation");
                errorAlert.setContentText("You can't delete the " + (selectedTable == null ? "empty selection" : mainDataset) + ".");
                errorAlert.showAndWait();
            } else {
                // Show confirmation for deleting table
                Pair<Optional<ButtonType>, ButtonType> response = showDeleteConfirmation(selectedTable);
                Optional<ButtonType> result = response.getKey();
                ButtonType buttonYes = response.getValue();

                // If choice is yes
                if (result.isPresent() && result.get() == buttonYes) {
                    // Delete the selected table
                    DatabaseManager.getInstance().dropTable(selectedTable);
                    // Update dropdown
                    updateTableNames();
                    updateTable(DatabaseManager.getInstance().protectedTableNames.get("DEFAULT_TABLE"));
                }
            }
        });

        stage.setOnCloseRequest((WindowEvent event) -> {
            if (currentModal != null) {
                currentModal.close();
            }
        });

    }

    /**
     * Calls the load for the CAS Table view
     */
    @FXML
    public void selectCASView() {
        loadCASTableView(stage);
    }

    /**
     * Calls the load for the Raw Table view
     */
    @FXML
    public void selectRawTableView() {
        loadRawTableView(stage);
    }

    /**
     * Calls the load for the map view
     */
    @FXML
    public void selectMapView() {
        loadMapView(stage);
    }

    /**
     * Calls the load for the graph view
     */
    @FXML
    public void selectGraphView() {
        loadGraphView();
    }

    /**
     * Calls the load view method for the load data view
     */
    @FXML
    public void selectLoadDataView() {
        loadLoadDataModal();
    }

    /**
     * Calls the query modal.
     */
    @FXML
    public void selectQueryModal() {
        loadQueryModal();
    }

    /**
     * Refreshes the current table
     */
    @FXML
    public void refreshTable() {
        refresh();
    }

    /**
     * Loads the CAS table view into the centre pane of the main window.
     * This occurs on startup or when selected in side NAV pane
     *
     * @param stage the stage to load
     */
    public void loadCASTableView(Stage stage) {
        try {
            FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/simpledata.fxml"));
            Parent root = baseLoader.load();

            SimpleDataController CASController = baseLoader.getController();
            // Initialise the controller
            CASController.init(stage);
            mainWindow.setCenter(root);
            state.setCurrentController(CASController);
        } catch (IOException e) {
            log.warn(e);
            setLastLogLevel("ERROR");

        }
    }

    /**
     * Loads the raw data table view into the centre pane of the main window.
     * This occurs when selected in the side NAV pane.
     *
     * @param stage the stage to load
     */
    public void loadRawTableView(Stage stage) {
        try {
            FXMLLoader rawLoader = new FXMLLoader(getClass().getResource("/fxml/rawdata.fxml"));
            Parent root = rawLoader.load();

            RawDataController rawController = rawLoader.getController();
            // Initialise the controller
            rawController.init(stage);
            mainWindow.setCenter(root);
            state.setCurrentController(rawController);
        } catch (IOException e) {
            log.warn(e);
            e.printStackTrace();
            setLastLogLevel("ERROR");
        }
    }

    /**
     * Loads the mapping view into the centre pane of the main window.
     * This occurs when mapping is selected on the side NAV pane.
     *
     * @param stage the stage to load
     */
    public void loadMapView(Stage stage) {
        try {
            FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("/fxml/mapping.fxml"));
            Parent mapViewParent = mapLoader.load();

            MappingController mapViewController = mapLoader.getController();
            // Initialise the controller
            mapViewController.init(stage);
            mainWindow.setCenter(mapViewParent);
            state.setCurrentController(mapViewController);

        } catch (IOException e) {
            log.warn(e);
            setLastLogLevel("WARNING");
        }
    }


    /**
     * Loads the graphing view into the centre pane of the main window.
     * This occurs when mapping is selected on the side NAV pane.
     */
    public void loadGraphView() {
        try {
            FXMLLoader graphLoader = new FXMLLoader(getClass().getResource("/fxml/graphing.fxml"));
            Parent graphViewParent = graphLoader.load();

            GraphingController graphViewController = graphLoader.getController();
            // Initialise the controller
            graphViewController.init();
            mainWindow.setCenter(graphViewParent);

        } catch (IOException e) {
            log.warn(e);
            setLastLogLevel("WARNING");
        }
    }

    /**
     * Loads the query modal when it is selected in the menu bar.
     * The current table name is given to the modal.
     * Along with the current table. To refresh on query execute.
     */
    public void loadQueryModal() {
        // Close any currently open modal
        if (currentModal != null) {
            currentModal.fireEvent(new WindowEvent(currentModal, WindowEvent.WINDOW_CLOSE_REQUEST));
        }

        try {
            // Create a new modal instance every time
            FXMLLoader queryModalLoader = new FXMLLoader(getClass().getResource("/fxml/querymodal.fxml"));
            Parent root = queryModalLoader.load();
            QueryModalController queryModalController = queryModalLoader.getController();

            // Set up the modal stage
            currentModal = new Stage();
            Scene modalScene = new Scene(root);
            queryModalController.init(currentModal, this);
            currentModal.setScene(modalScene);
            currentModal.setResizable(false);
            currentModal.setTitle("Apply Query");
            currentModal.initModality(Modality.WINDOW_MODAL);


            currentModal.show();
        } catch (IOException e) {
            log.warn(e);
            e.printStackTrace();
            setLastLogLevel("ERROR");
        }

        // Update modal status
        currentModalType = ModalType.QUERY;
    }

    /**
     * Loads the data modal when the corresponding button
     * is selected in the menu bar.
     */
    public void loadLoadDataModal() {
        // Close any currently open modal
        if (currentModal != null) {
            currentModal.fireEvent(new WindowEvent(currentModal, WindowEvent.WINDOW_CLOSE_REQUEST));
        }

        try {
            // Create a new modal instance every time
            FXMLLoader loadDataModalLoader = new FXMLLoader(getClass().getResource("/fxml/loaddata.fxml"));
            Parent root = loadDataModalLoader.load();
            LoadDataController loadDataController = loadDataModalLoader.getController();

            // Set up the modal stage
            currentModal = new Stage();
            Scene modalScene = new Scene(root);
            loadDataController.init(currentModal, this);
            currentModal.setScene(modalScene);
            currentModal.setResizable(false);
            currentModal.setTitle("Load Data");
            currentModal.initModality(Modality.WINDOW_MODAL);

            // Set onClose event
            currentModal.setOnCloseRequest(e -> {
                currentModalType = ModalType.NONE;
                currentModal.close();
                currentModal = null;
            });

            currentModal.show();
        } catch (IOException e) {
            log.warn(e);
            e.printStackTrace();
            setLastLogLevel("ERROR");
        }

        // Update modal status
        currentModalType = ModalType.LOAD_DATA;
    }


    /**
     * Update the table names in the combo box
     * Fetches straight from the DB.
     * Sets comboBox to default table after table deleted
     */
    void initTableNameDropDown() {
        // Add a listener to the ComboBox for updating table data
        existingTablesDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                state.setCurrentQuery(null);
                CrashManager.getInstance().setQueryTable();
                state.setCurrentTable(newValue);
                state.getCurrentController().refreshView();
                QueryManager.getInstance().setCriteria(new ArrayList<>());
            }
        });
    }

    /**
     * Updates the table names in the combo box
     */
    public void updateTableNames() {
        List<String> tableNames = DatabaseManager.getInstance().getTableNames();
        existingTablesDropdown.setItems(FXCollections.observableArrayList(tableNames));
    }

    /**
     * Updates the data with the new table name
     *
     * @param tableName the name of the table to update the data from
     */
    public void updateTable(String tableName) {

        WindowState.getInstance().setCurrentTable(tableName);
        existingTablesDropdown.getSelectionModel().select(tableName);

    }


    /**
     * Creation of ContextMenu for right-click delete functionality
     */
    private void deleteContextMenu() {
        // ContextMenu for right-click delete functionality
        ContextMenu contextMenu = new ContextMenu();
        deleteItem = new MenuItem("Delete");
        // Add the delete function to context menu
        contextMenu.getItems().add(deleteItem);
        // Set the context menu to the combobox
        existingTablesDropdown.setContextMenu(contextMenu);
    }

    /**
     * Creates a confirmation alert with specified parameters
     */
    private Pair<Optional<ButtonType>, ButtonType> showDeleteConfirmation(String tableName) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Table?");
        alert.setContentText("Are you sure you want to delete " + tableName + "?");

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);

        alert.getDialogPane().lookupButton(buttonCancel).setVisible(false);

        return new Pair<>(alert.showAndWait(), buttonYes);
    }


    /**
     * Quits the app when the quit button is pressed
     */
    public void quit() {
        if (currentModal != null) {
            currentModal.close();
        }
        stage.close();
    }


    /**
     * Sets the current modal
     *
     * @param currentModal the current modal
     */
    public void setCurrentModal(Stage currentModal) {
        this.currentModal = currentModal;
    }

    /**
     * Sets the current modal type
     *
     * @param currentModalType the type of modal
     */
    public void setCurrentModalType(ModalType currentModalType) {
        this.currentModalType = currentModalType;
    }

    /**
     * Called by the refreshTable() method to refresh the table
     * and propagate this change across all pages (except for
     * the graphing page)
     */
    public void refresh() {
        if (currentModal != null) {
            currentModal.close();
            currentModalType = ModalType.NONE;
            currentModal = null;
        }
        state.setCurrentQuery(null);
        CrashManager.getInstance().setQueryTable();
        state.getCurrentController().refreshView();
        QueryManager.getInstance().setCriteria(new ArrayList<>());
    }

    /**
     * Opens the help documentation
     */
    public void openDocument() {
        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/manual/CQS Manual.pdf");

                    if (inputStream != null) {
                        File tempFile = File.createTempFile("CQS_Manual", ".pdf");

                        try (FileOutputStream out = new FileOutputStream(tempFile)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                out.write(buffer, 0, bytesRead);
                            }
                        }

                        Desktop.getDesktop().open(tempFile);
                    } else {
                        log.error("Resource not found");
                    }
                } catch (IOException ex) {
                    log.error("Error opening document: " + ex.getMessage());
                }
            }).start();
        } else {
            log.error("Desktop is not supported");
        }

    }
}

