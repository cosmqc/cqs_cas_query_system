package seng202.team9.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import org.controlsfx.control.CheckComboBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import seng202.team9.business.CrashManager;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;
import seng202.team9.models.stringenums.SimpleColumns;

import java.util.ArrayList;

/**
 * Controller of the simple CAS viewer.
 *
 */
public class SimpleDataController implements ViewController {

    @FXML
    private Pagination pageSelect;

    @FXML
    private TextField specificPageSelect;

    private TableView<Crash> table;
    /**
     * Holds the simple table columns
     */
    @FXML
    public CheckComboBox<String> filterCASCheckComboBox;
    private static final Logger log = LogManager.getLogger(SimpleDataController.class);

    /**
     * Initialises the view controllers to allow ease of functionality in Main Controller
     * @param stage the current stage
     */
    public void init(Stage stage) {
        initializePagination();
        this.refreshView();
        // Populate filter comboBox
        populateComboBox();
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
        table = pageTableView;
        return pageTableView;
    }

    /**
     * Sets the table view to the newly queried data
     */
    @Override
    public void refreshView() {
        initializePagination();
    }

    /**
     * Initializes the CAS data table, on load up of the scene.
     * Contains a mixture of columns populated via attribute type, and category toString()
     * This only configures the table value factories. The population is done by the pagination control.
     * @param table an empty TableView to be populated
     */
    public void initializeTable(TableView<Crash> table) {
        TableColumn<Crash, String> columnDate = new TableColumn<>("Year");
        columnDate.setCellValueFactory(cellData-> {
            String formattedData = cellData.getValue().getAttribute(AttributeType.CRASHYEAR).toString();
            return new SimpleStringProperty(formattedData);
        });
        table.getColumns().add(columnDate);

        TableColumn<Crash, String> columnLocation = new TableColumn<>("Location");
        columnLocation.setCellValueFactory(cellData-> {
            String formattedData = cellData.getValue().getLocation().getLocationString();
            return new SimpleStringProperty(formattedData);
        });
        table.getColumns().add(columnLocation);

        TableColumn<Crash, String> columnSev = new TableColumn<>("Severity");
        columnSev.setCellValueFactory(cellData-> {
            String formattedData = cellData.getValue().getAttribute(AttributeType.CRASHSEVERITY).toString();
            return new SimpleStringProperty(formattedData);
        });
        table.getColumns().add(columnSev);

        TableColumn<Crash, String> columnVehicles = new TableColumn<>("People and Vehicles Involved");
        columnVehicles.setCellValueFactory(cellData-> {
            String formattedData = cellData.getValue().getParticipants().getPeopleVehiclesInvolved();
            return new SimpleStringProperty(formattedData);
        });
        table.getColumns().add(columnVehicles);

        TableColumn<Crash, String> columnObj = new TableColumn<>("Objects Involved");
        columnObj.setCellValueFactory(cellData-> {
            String formattedData = cellData.getValue().getParticipants().getObstaclesString();
            return new SimpleStringProperty(formattedData);
        });
        table.getColumns().add(columnObj);

        TableColumn<Crash, String> columnSpeed = new TableColumn<>("Speed Conditions");
        columnSpeed.setCellValueFactory(cellData-> {
            String formattedData = cellData.getValue().getCondition().getSpeedString();
            return new SimpleStringProperty(formattedData);
        });
        table.getColumns().add(columnSpeed);

        TableColumn<Crash, String> columnWeather = new TableColumn<>("Weather");
        columnWeather.setCellValueFactory(cellData-> {
            String formattedData = cellData.getValue().getCondition().getWeatherString();
            return new SimpleStringProperty(formattedData);
        });
        table.getColumns().add(columnWeather);

        TableColumn<Crash, String> columnControl = new TableColumn<>("Traffic Control");
        columnControl.setCellValueFactory(cellData-> {
            String formattedData = cellData.getValue().getAttribute(AttributeType.TRAFFICCONTROL).toString();
            return new SimpleStringProperty(formattedData);
        });
        table.getColumns().add(columnControl);

        table.setRowFactory( tv -> {
            TableRow<Crash> row = new TableRow<>();
            return rowOnSelected(row);
        });
    }

    /**
     * Changes the color of a row depending selection (clicking on rows consecutively)
     * @param row a data entry in the table
     * @return row with color change
     */
    @NotNull
    public static TableRow<Crash> rowOnSelected(TableRow<Crash> row) {
        row.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                row.setStyle("-fx-background-color: #22bad9;");
            } else {
                row.setStyle("");
            }
        });
        return row;
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
     * Populates the comboBox for the simple filter
     * Adds event listener to add and remove columns
     */
    public void populateComboBox() {
        for (SimpleColumns column : SimpleColumns.values()) {
            filterCASCheckComboBox.getItems().add(column.getDisplayName());
        }
        // Check all items by default
        for (int i = 0; i < filterCASCheckComboBox.getItems().size(); i++) {
            filterCASCheckComboBox.getCheckModel().check(i);
        }

        // Add a listener to the checked items
        filterCASCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (String item : c.getAddedSubList()) {
                        addColumn(item);
                    }
                } else if (c.wasRemoved()) {
                    for (String item : c.getRemoved()) {
                        removeColumn(item);
                    }
                }
            }
        });
    }

    /**
     * Adds the column to the current crash table.
     * Used by the filter checkbox.
     * @param attributeType the attributeType to be added
     */
    private void addColumn(String attributeType) {
        if (attributeType != null) {
            for (TableColumn<Crash, ?> column : table.getColumns()) {
                if (column.getText().equals(attributeType)) {
                    column.setVisible(true);
                }

            }
        }
    }

    /**
     * Removes the column to the current crash table.
     * Used by the filter checkbox.
     * @param attributeType the attributeType to be added
     */
    private void removeColumn(String attributeType) {
        if (attributeType != null) {
            for (TableColumn<Crash, ?> column : table.getColumns()) {
                if (column.getText().equals(attributeType)) {
                    column.setVisible(false);
                }
            }
        }
    }

    /**
     * FXML binding for the 'last page' button. Finds the last page and switches to it.
     */
    @FXML
    public void selectLastPage() {
        pageSelect.setCurrentPageIndex(CrashManager.getInstance().getNumBatches() - 1);
    }
}
