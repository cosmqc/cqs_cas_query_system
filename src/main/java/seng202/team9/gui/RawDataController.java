package seng202.team9.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import seng202.team9.business.*;
import seng202.team9.models.*;

import java.io.IOException;
import java.util.ArrayList;

import org.controlsfx.control.CheckComboBox;

import static seng202.team9.io.CSVImporter.setLastLogLevel;
import static seng202.team9.gui.SimpleDataController.rowOnSelected;


/**
 * Class that controllers the raw data viewer. It populates the table with all the crash data.
 * @author ist46
 */
public class RawDataController implements ViewController {
    private static final Logger log = LogManager.getLogger(RawDataController.class);

    @FXML
    private CheckComboBox<AttributeType> filterCheckComboBox;
    @FXML
    private Pagination pageSelect;
    @FXML
    private TextField specificPageSelect;

    private WindowState state;

    private Stage currentModal;


    /**
     * TableView object with all the crashes in it
     */
    private TableView<Crash> table;


    /**
     * Initialises the raw data controller with the current data and table
     * @param stage the current stage
     */
    public void init(Stage stage) {
        state = WindowState.getInstance();
        initializeFilterCheckbox();
        initializePagination();
        this.refreshView();
    }

    /**
     * Helper function to initialize each column with its header and callback function
     * @param attrType AttributeType the column will be displaying
     * @return fully set up TableColumn with correct header and callback
     */
    @NotNull
    private static TableColumn<Crash, String> initializeColumn(AttributeType attrType) {
        TableColumn<Crash, String> col = new TableColumn<>(attrType.prettyPrint);
        col.setCellValueFactory(cellData -> {
            String formattedData = "";
            if (cellData.getValue().isCustom) {
                formattedData += "*";
            }
            formattedData += cellData.getValue().getAttribute(attrType).toString();
            return new SimpleStringProperty(formattedData);
        });
        return col;
    }

    /**
     * Initializes the filter checkbox with all the crash attributes.
     */
    public void initializeFilterCheckbox() {

        // add all attributes to the checkbox
        for (AttributeType attrType : AttributeType.values()) {
            filterCheckComboBox.getItems().add(attrType);
        }

        // select them all to start with
        for (AttributeType attrType : filterCheckComboBox.getItems()) {
            filterCheckComboBox.getCheckModel().check(attrType);
        }

        // add tick / un-tick listeners to each checkbox
        filterCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends AttributeType> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (AttributeType item : c.getAddedSubList()) {
                        addColumn(item);
                    }
                } else if (c.wasRemoved()) {
                    for (AttributeType item : c.getRemoved()) {
                        removeColumn(item);
                    }
                }
            }
        });
    }

    /**
     * Initialise the table with all the crashes
     * @param table TableView to initialize
     */
    public void initializeTable(TableView<Crash> table) {
        this.table = table;
        for (AttributeType attrType : AttributeType.values()) {
            TableColumn<Crash, String> col = initializeColumn(attrType);
            table.getColumns().add(col);
            if (!filterCheckComboBox.getCheckModel().isChecked(attrType)) {
                removeColumn(attrType);
            }
        }

        table.setRowFactory( tv -> {
            TableRow<Crash> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Crash selectedCrash = table.getSelectionModel().getSelectedItem();
                    int crashID = (int) selectedCrash.getAttribute(AttributeType.OBJECTID);
                    try {
                        openRowEditor(CrashManager.getInstance().getCrashById(crashID, selectedCrash.isCustom, state.getCurrentTable()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    table.setVisible(false);
                    table.refresh();
                    table.setVisible(true);
                }
            });

            return rowOnSelected(row);
        });
    }

    /**
     * Sets the table view to the newly queried data
     *
     */
    @Override
    public void refreshView() {
        initializePagination();
    }

    /**
     * Opens a modal for editing a row
     * @param selectedCrash The crash that is to be edited
     * @throws IOException If a load error occurs
     */
    public void openRowEditor(Crash selectedCrash) throws IOException {
        if (currentModal != null) {
            currentModal.fireEvent(new WindowEvent(currentModal, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        try {
            FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/tableRowEdit.fxml"));
            Parent root = baseLoader.load();
            EditDataModalController editDataController = baseLoader.getController();

            this.currentModal = new Stage();
            Scene editRow = new Scene(root, 500, 400);
            editDataController.init(currentModal, selectedCrash, state.getCurrentTable());
            currentModal.setScene(editRow);
            currentModal.setTitle("Edit Row");
            currentModal.initModality(Modality.WINDOW_MODAL);
            currentModal.show();
        } catch (IOException e) {
            log.warn(e);
            setLastLogLevel("ERROR");
        }

    }

    /**
     * Creates a stage for the add crash modal.
     * Makes the call to initialise the scene.
     *
     */
    public void openAddNewCrash() {
        if (currentModal != null) {
            currentModal.fireEvent(new WindowEvent(currentModal, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        try {
            FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/tableAdd.fxml"));
            Parent root = baseLoader.load();

            AddDataModalController baseController = baseLoader.getController();

            Scene addCrash = new Scene(root, 500, 400);
            this.currentModal = new Stage();

            baseController.init(currentModal, state.getCurrentTable());

            currentModal.setScene(addCrash);
            currentModal.setTitle("Add Crash");
            currentModal.initModality(Modality.WINDOW_MODAL);
            currentModal.show();
        } catch (IOException e) {
            log.warn(e);
            setLastLogLevel("ERROR");
        }
    }


    /**
     * Shows the column to the current crash table.
     * Used by the filter checkbox.
     * @param attrType the attributeType to be shown
     */
    private void addColumn(AttributeType attrType) {
        if (attrType != null) {
            for (TableColumn<Crash, ?> column : table.getColumns()) {
                if (column.getText().equals(attrType.prettyPrint)) {
                    column.setVisible(true);
                }
            }
        }
    }

    /**
     * Removes the column from the current crash table.
     * Used by the filter checkbox.
     * @param attributeType the attributeType to be added
     */
    private void removeColumn(AttributeType attributeType) {
        if (attributeType != null) {
            for (TableColumn<Crash, ?> column : table.getColumns()) {
                if (column.getText().equals(attributeType.prettyPrint)) {
                    column.setVisible(false);
                }
            }
        }
    }


    /**
     * Deletes a crash object from the table.
     */
    public void deleteCrash() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Crash crashToDelete = table.getSelectionModel().getSelectedItem();
            Alert.AlertType type = Alert.AlertType.CONFIRMATION;
            Alert alert = new Alert(type, "You will be deleting the selected crash.");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                CrashManager.getInstance().deleteCrash(crashToDelete, state.getCurrentTable());
                refreshView();
            }
        }
    }


    /**
     * Sets up listeners and factories for the pagination control.
     */
    private void initializePagination() {
        CrashManager.getInstance().refreshTable();
        pageSelect.setPageCount(CrashManager.getInstance().getNumBatches());
        pageSelect.setPageFactory(this::createPage);

        // updates page if user clicks off the page change field
        specificPageSelect.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                specificPageSelect.setText("");
            }
        });

        // updates page if user hits enter while in the page change field
        specificPageSelect.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                setPage(specificPageSelect.getText());
            }
        });
    }

    /**
     * Switches to the table given by pageString. If pageString is bad, warn is logged and call ignored.
     * @param pageString 1-based int-able String to switch the table to
     */
    private void setPage(String pageString) {
        if (pageString.isEmpty()) { return; } // stay on current page if string empty
        try {
            // user input is 1-based, currentPageIndex is 0-based
            int page = Integer.parseInt(pageString) - 1;
            pageSelect.setCurrentPageIndex(page);
        }  catch (NumberFormatException e) {
            log.warn("Warning: Page number needs to be an integer. Got %s".formatted(pageString));
        }
    }

    /**
     * FXML binding for the 'first page' button. Finds the first page and switches to it.
     */
    @FXML
    public void selectFirstPage() {
        pageSelect.setCurrentPageIndex(0);
    }

    /**
     * FXML binding for the 'last page' button. Finds the last page and switches to it.
     */
    @FXML
    public void selectLastPage() {
        pageSelect.setCurrentPageIndex(CrashManager.getInstance().getNumBatches() - 1);
    }

    /**
     * Defines a TableView that gets shown by the pagination control.
     * @param pageIndex Page number to display. 1-based.
     * @return TableView Node containing the requested batch data.
     */
    private Node createPage(int pageIndex) {
        TableView<Crash> pageTableView = new TableView<>();
        initializeTable(pageTableView);
        ArrayList<Crash> crashes = CrashManager.getInstance().getCrashBatch(pageIndex);
        pageTableView.setItems(FXCollections.observableArrayList(crashes));
        return pageTableView;
    }

    /**
     * Opens and applies the updates made to a single crash entry.
     * Called when the update button is pushed with a row select
     */
    public void updateCrash() {
        Crash selectedCrash = table.getSelectionModel().getSelectedItem();
        if (selectedCrash != null) {
            int crashID = (int) selectedCrash.getAttribute(AttributeType.OBJECTID);
            try {
                 openRowEditor(CrashManager.getInstance().getCrashById(crashID, selectedCrash.isCustom, state.getCurrentTable()));


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            table.setVisible(false);
            table.refresh();
            table.setVisible(true);
        } else {
            Alert.AlertType type = Alert.AlertType.INFORMATION;
            Alert alert = new Alert(type, "First select a crash you would like to update");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }

    }

}