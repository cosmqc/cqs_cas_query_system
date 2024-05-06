package seng202.team9.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng202.team9.business.CrashManager;
import seng202.team9.business.QueryManager;
import seng202.team9.business.WindowState;
import seng202.team9.io.CSVImporter;
import seng202.team9.models.*;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The modal which allows users to add their own crash entry's
 */
public class AddDataModalController {
    private Stage stage;

    @FXML
    private TableView<Attribute> selectedCategoryTable;
    private Crash crashToAdd;
    private CrashManager crashManager;
    @FXML
    private TableColumn<Attribute, String> attributeCol;
    @FXML
    private TableColumn<Attribute, String> dataCol;
    private String tableName;
    private WindowState state;

    /**
     * Initialises the add data modal
     * @param stage the modal stage
     * @param tableName the currently selected table
     */
    public void init(Stage stage, String tableName) {
        crashManager = CrashManager.getInstance();
        this.stage = stage;
        this.tableName = tableName;
        this.state = WindowState.getInstance();
        Image appIcon = new Image("icons/appIcon.png");
        stage.getIcons().add(appIcon);
        crashToAdd = generateEmptyCrash();
        selectedCategoryTable.setEditable(true);
        setSelectedCategoryTable(crashToAdd.getCrashDetailAttributes());
        dataCol.setEditable(true);
        selectedCategoryTable.setRowFactory( tv -> {
            TableRow<Attribute> row = new TableRow<>();
            row.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    row.setStyle("-fx-background-color: #22bad9;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });
    }

    /**
     * Generates a manual crash instance ready to be altered to the users input.
     * @return crash an empty crash object.
     */
    public Crash generateEmptyCrash() {
        String[] sampleString = new String[]{Integer.toString(crashManager.getNextCustomID(tableName)),
                "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "-43.3121", "172.34504"}; // Default location of UC campus
        CSVImporter importer = new CSVImporter();
        Crash generatedCrash = importer.readCrashFromLine(sampleString);
        generatedCrash.isCustom = true;
        return generatedCrash;
    }

    /**
     * Discards the changes made to current crash.
     *
     */
    public void discardChanges() {
        stage.close();
    }

    /**
     * Add's crashToAdd to the selected dataset.
     * All attributes of crash have been checked and are not erroneous.
     */
    public void addCrash() {
        try {
            crashManager.addCrash(crashToAdd, tableName);
            state.getCurrentController().refreshView(); // Used to refresh the table view.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Crash Entry Added");
            alert.setHeaderText(null);
            alert.setContentText("The crash entry has been successfully added to " + tableName + ". You can find it at the bottom with an * to show that it is your entry");
            alert.showAndWait();
            stage.close();
        } catch (SQLException e) {
            showAlert(e.getMessage());
        }
    }


    /**
     * Shows the crash attributes (those that are not in a category)
     */
    public void showCrash() {
        ArrayList<Attribute> crash = crashToAdd.getCrashDetailAttributes();
        setSelectedCategoryTable(crash);
    }

    /**
     * Sets the table with data relating to the Location category.
     */
    public void showLocation() {
        ArrayList<Attribute> location = crashToAdd.getLocation().getLocationAttributes();
        setSelectedCategoryTable(location);
    }

    /**
     * Sets the table with data relating to the Condition category.
     */
    public void showCondition() {
        ArrayList<Attribute> condition = crashToAdd.getCondition().getConditionAttributes();
        setSelectedCategoryTable(condition);
    }

    /**
     * Sets the table with data relating to the Participants category.
     */
    public void showParticipants() {
        ArrayList<Attribute> participants = crashToAdd.getParticipants().getParticipantsAttributes();
        setSelectedCategoryTable(participants);
    }

    /**
     * Function that takes an ArrayList of attributes containing attributes of crash depending on category.
     * @param categoryAttributes ArrayList of attributes relating to category.
     */
    public void setSelectedCategoryTable(ArrayList<Attribute> categoryAttributes) {
        selectedCategoryTable.getItems().clear();
        selectedCategoryTable.setItems(FXCollections.observableArrayList(categoryAttributes));

        attributeCol.setCellValueFactory(cellData -> {
            Attribute attribute = cellData.getValue();
            AttributeType attributeType = attribute.getType();
            return new SimpleStringProperty(attributeType.toString());
        });

        dataCol.setCellValueFactory(cellData -> {
            Attribute attribute = cellData.getValue();
            return new SimpleStringProperty(attribute.value.toString());
        });
        dataCol.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    /**
     * Checks that the changes to the cell are valid.
     * Sets the relevant attribute value in the crashToAdd
     * Or gives the user a warning of an incorrect entry
     * @param cell an observable table cell object
     */
    public void setAttribute(TableColumn.CellEditEvent <Attribute, String> cell) {
        // Gets the attributeType of the crash data to update
        AttributeType attributeType = cell.getTableView().getSelectionModel().getSelectedItem().getType();
        //The value input into the editable cell.
        String value = cell.getNewValue();

        if (attributeType == AttributeType.OBJECTID){
            try {
                Integer.parseInt(value);
            } catch (Exception e) {
                showAlert("This value must be an integer.");

                cell.getTableView().edit(cell.getTablePosition().getRow(), cell.getTableColumn());
                cell.getTableView().getColumns().get(0).setVisible(false);
                cell.getTableView().getColumns().get(0).setVisible(true);
            }
            if (validateID(value)) {
                crashToAdd.setAttribute(attributeType, value);
            } else {
                showAlert("This ID is already in use");
                cell.getTableView().edit(cell.getTablePosition().getRow(), cell.getTableColumn());
                cell.getTableView().getColumns().get(0).setVisible(false);
                cell.getTableView().getColumns().get(0).setVisible(true);
            }
        } else {
            switch (attributeType.classType) {
                case STRING -> {

                    if (value.matches("[a-zA-Z0-9\\s]+")) {
                        crashToAdd.setAttribute(attributeType, value);
                    } else {
                        showAlert("No special characters allowed.");
                        cell.getTableView().edit(cell.getTablePosition().getRow(), cell.getTableColumn());
                        cell.getTableView().getColumns().get(0).setVisible(false);
                        cell.getTableView().getColumns().get(0).setVisible(true);
                    }
                }
                case INT -> {
                    try {
                        Integer.parseInt(value); // Checking to see if the newly input
                        crashToAdd.setAttribute(attributeType, value);
                    } catch (Exception e) {
                        showAlert("This value must be an integer.");

                        cell.getTableView().edit(cell.getTablePosition().getRow(), cell.getTableColumn());
                        cell.getTableView().getColumns().get(0).setVisible(false);
                        cell.getTableView().getColumns().get(0).setVisible(true);
                    }
                }
                case FLOAT -> {
                    try {
                        Float.parseFloat(value);
                        crashToAdd.setAttribute(attributeType, value);
                    } catch (Exception e) {
                        showAlert("New value must be a float.\nIt may have a + or -, but no other characters.");
                        cell.getTableView().edit(cell.getTablePosition().getRow(), cell.getTableColumn());
                        cell.getTableView().getColumns().get(0).setVisible(false);
                        cell.getTableView().getColumns().get(0).setVisible(true);
                    }
                }
            }
        }


    }

    /**
     * Checks if the ID is already in use in the current table
     * @param newValue the id the user wishes to change the ID to.
     * @return true if new value is allowed, false otherwise
     */
    public boolean validateID(String newValue) {
        QueryManager queryManager = new QueryManager();
        queryManager.addCriterion(AttributeType.OBJECTID, newValue, OperatorType.EQUAL_TO);
        int matchingCrashes = queryManager.executeQuery(tableName).size();
        return matchingCrashes == 0;
    }


    /**
     * Shows the user a warning alert. To let them know they have entered a wrong value.
     * @param errorString string to be shown in the alert.
     */
    public void showAlert(String errorString) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(errorString);
        alert.showAndWait();
    }
}
