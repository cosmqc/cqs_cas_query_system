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
import seng202.team9.models.Attribute;
import seng202.team9.models.AttributeType;
import seng202.team9.models.Crash;
import seng202.team9.models.OperatorType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This modal allows users to edit data
 *
 * @author jfl80 zwa100
 */
public class EditDataModalController {
    private Stage stage;

    private Crash rowCrash;
    @FXML
    private Button crashBtn;

    private int crashID;

    private boolean isCustom;

    private CrashManager crashManager;

    @FXML
    private TableColumn<Attribute, String> attributeCol;

    @FXML
    private TableColumn<Attribute, String> dataCol;

    @FXML
    private TableView<Attribute> selectedCategoryTable;

    private final HashMap<String, String> validChanges = new HashMap<>();

    private String tableName;


    /**
     * Initialises the edit data modal
     * @param stage the modal stage
     * @param selectedCrash the crash that was selected to be edited
     * @param tableName the currently selected table
     */
    public void init(Stage stage, Crash selectedCrash, String tableName) {
        this.crashManager = CrashManager.getInstance();
        this.stage = stage;
        this.crashID = (int) selectedCrash.getAttribute(AttributeType.OBJECTID);
        this.isCustom = selectedCrash.isCustom;
        this.tableName = tableName;
        this.rowCrash = selectedCrash;
        Image appIcon = new Image("icons/appIcon.png");
        stage.getIcons().add(appIcon);
        selectedCategoryTable.setEditable(true);
        setSelectedCategoryTable(rowCrash.getCrashDetailAttributes());
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
     * Discards the changes made to current crash.
     *
     */
    public void discardChanges() {
        stage = (Stage) crashBtn.getScene().getWindow();
        stage.close();
    }

    /**
     *
     */
    public void showCrash() {
        ArrayList<Attribute> crash = rowCrash.getCrashDetailAttributes();
        setSelectedCategoryTable(crash);
    }

    /**
     * Sets the table with data relating to the Location category.
     */
    public void showLocation() {
        ArrayList<Attribute> location = rowCrash.getLocation().getLocationAttributes();
        setSelectedCategoryTable(location);
    }

    /**
     * Sets the table with data relating to the Condition category.
     */
    public void showCondition() {
        ArrayList<Attribute> condition = rowCrash.getCondition().getConditionAttributes();
        setSelectedCategoryTable(condition);
    }

    /**
     * Sets the table with data relating to the Participants category.
     */
    public void showParticipants() {
        ArrayList<Attribute> participants = rowCrash.getParticipants().getParticipantsAttributes();
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
     * Update the Crash object in the database with the new value inputted.
     * @param editedCell A CellEditEvent containing information about the edited cell, including the updated value.
     */
    public void changeData(TableColumn.CellEditEvent <Attribute, String> editedCell) {

        // Gets the attributeType of the crash data to update
        AttributeType attributeType = editedCell.getTableView().getSelectionModel().getSelectedItem().getType();

        String newValue = editedCell.getNewValue();


        if (attributeType == AttributeType.OBJECTID) { // First check if this ID is in use
            if ( ! validateIDChange(newValue)) {
                showAlert("This crashID is already in use.");
                // Reset current cell
                editedCell.getTableView().edit(editedCell.getTablePosition().getRow(), editedCell.getTableColumn());
                editedCell.getTableView().getColumns().get(0).setVisible(false);
                editedCell.getTableView().getColumns().get(0).setVisible(true);
                return;
            }
        }

        if (validateChanges(attributeType, newValue)) {
            validChanges.put(attributeType.toString(), newValue);
        } else {
            switch (attributeType.classType) {
                case STRING -> {
                    showAlert("No special characters allowed.");
                    editedCell.getTableView().edit(editedCell.getTablePosition().getRow(), editedCell.getTableColumn());
                    editedCell.getTableView().getColumns().get(0).setVisible(false);
                    editedCell.getTableView().getColumns().get(0).setVisible(true);
                }
                case INT -> {
                    showAlert("New value must be an integer.");
                    editedCell.getTableView().edit(editedCell.getTablePosition().getRow(), editedCell.getTableColumn());
                    editedCell.getTableView().getColumns().get(0).setVisible(false);
                    editedCell.getTableView().getColumns().get(0).setVisible(true);
                }
                case FLOAT -> {
                    showAlert("New value must be a float.");
                    editedCell.getTableView().edit(editedCell.getTablePosition().getRow(), editedCell.getTableColumn());
                    editedCell.getTableView().getColumns().get(0).setVisible(false);
                    editedCell.getTableView().getColumns().get(0).setVisible(true);
                }
            }
        }
    }


    /**
     * Checks if the ID is already in use in the current table
     * @param newValue the id the user wishes to change the ID to.
     * @return true if new value is allowed, false otherwise
     */
    public boolean validateIDChange(String newValue) {
        QueryManager queryManager = new QueryManager();
        queryManager.addCriterion(AttributeType.OBJECTID, newValue, OperatorType.EQUAL_TO);
        int matchingCrashes = queryManager.executeQuery(tableName).size();
        return matchingCrashes == 0;
    }


    /**
     * Validates the changes made by the user
     * @param attributeType The type of attribute trying to be changed
     * @param newValue the new value of attr
     * @return whether the change was valid
     */
    public boolean validateChanges(AttributeType attributeType, String newValue) {


        switch (attributeType.classType) {
            case STRING -> {
                return newValue.matches("[a-zA-Z0-9-/\\s]+");
            }
            case INT -> {
                try {
                    Integer.parseInt(newValue);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            case FLOAT -> {
                try {
                    Float.parseFloat(newValue);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Display a warning alert dialog with the specified error message.
     *
     * @param errorString The error message to be displayed in the alert.
     */
    public void showAlert(String errorString) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(errorString);
        alert.showAndWait();
    }

    /**
     * Execute updates to the Crash object in the database based on valid changes stored in the 'validChanges' map.
     * Refresh the view and close the current stage.
     */
    public void executeUpdates() {
        for (Map.Entry<String, String> entry : validChanges.entrySet()) {
            String attribute = entry.getKey();
            String newValue = entry.getValue();
            crashManager.updateCrash(crashID, isCustom, attribute, newValue, tableName);
        }

        WindowState.getInstance().getCurrentController().refreshView();
        stage = (Stage) crashBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Set the name of the database table for which data updates will be performed.
     *
     * @param newTable The name of the database table.
     */
    public void setTableName(String newTable) {
        tableName = newTable;
    }
}
