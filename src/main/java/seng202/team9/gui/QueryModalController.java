package seng202.team9.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import seng202.team9.business.CrashManager;
import seng202.team9.business.QueryManager;
import seng202.team9.business.WindowState;
import seng202.team9.models.*;
import seng202.team9.models.stringenums.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A modal where users can input a query to change the table being displayed
 *
 * @author ist46
 */
public class QueryModalController {
    private QueryManager queryManager;
    private WindowState state;

    @FXML
    ComboBox<String> attributeComboBox;
    @FXML
    ComboBox<String> operatorComboBox;
    @FXML
    TextField valueTextField;
    @FXML
    ListView<QueryCriterion> currentCriteria;

    @FXML
    Button removeQueryBtn;

    @FXML
    Button executeQueryBtn;

    @FXML
    Button addToQueryBtn;

    @FXML
    Label possibleValuesLabel;

    @FXML
    Button clearQueryBtn;

    private MainController mainController;

    private Stage stage;

    private QueryCriterion selectedQuery;

    private List<QueryCriterion> oldCriteria;

    /**
     * Initialise the modal by setting the two combo boxes with their respective values
     * @param stage the modal stage
     * @param mainController reference to the current mainController instance
     */
    public void init(Stage stage, MainController mainController) {
        this.state = WindowState.getInstance();
        this.mainController = mainController;
        this.stage = stage;
        this.queryManager = QueryManager.getInstance();
        Image appIcon = new Image("icons/appIcon.png");
        stage.getIcons().add(appIcon);
        for (AttributeType attribute: AttributeType.values()) {
            attributeComboBox.getItems().add(attribute.prettyPrint);
        }
        for (OperatorType operator : OperatorType.values()) {
            operatorComboBox.getItems().add(operator.simplePrint);
        }
        Collections.sort(attributeComboBox.getItems());
        removeQueryBtn.setDisable(true);
        executeQueryBtn.setDisable(true);
        addToQueryBtn.setDisable(true);
        clearQueryBtn.setDisable(true);
        oldCriteria = new ArrayList<>();
        if(!queryManager.getCriteria().isEmpty()) {
            currentCriteria.setItems(FXCollections.observableArrayList(queryManager.getCriteria()));
            oldCriteria = new ArrayList<>(queryManager.getCriteria());
            clearQueryBtn.setDisable(false);
            executeQueryBtn.setDisable(false);
        }
        possibleValuesLabel.setWrapText(true);
        currentCriteria.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedQuery = newValue;
                removeQueryBtn.setDisable(false);
            } else {
                removeQueryBtn.setDisable(true);
            }
        });

        attributeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            checkAddToQueryBtn();
            populateLabel(newValue);
        });

        operatorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> checkAddToQueryBtn());

        valueTextField.textProperty().addListener((observable, oldValue, newValue) -> checkAddToQueryBtn());

        stage.setOnCloseRequest((WindowEvent event) -> {
            if(!queryManager.getCriteria().equals(oldCriteria)) {
                queryManager.setCriteria(oldCriteria);
            }
            mainController.setCurrentModal(null);
            mainController.setCurrentModalType(ModalType.NONE);
        });
    }

    /**
     * When the user presses the "Add to Query" button it gets all the current values in the two combo boxes and the
     * text field and combines them into a QueryCriterion and adds it to the current QueryBuilder
     */
    public void addToQuery() {
        String stringAttribute = this.attributeComboBox.getValue();
        String stringOperator = this.operatorComboBox.getValue();
        AttributeType attribute = AttributeType.fromString(stringAttribute);
        OperatorType operator = OperatorType.fromString(stringOperator);
        String value = this.valueTextField.getText();
        switch (attribute.classType) {
            case STRING -> {
                if (operator == OperatorType.GREATER_THAN || operator == OperatorType.LESS_THAN) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Words cannot be compared with < or >. Please change the operator to = or !=");
                    alert.showAndWait();
                }
                else if (!(value.matches("[a-zA-Z0-9\\s\\-/'ū]+"))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Strings can only be compared to strings. Please enter a valid string");
                    alert.showAndWait();
                } else {
                    value = "'" + value + "'";
                    queryManager.addCriterion(attribute, value, operator);
                    currentCriteria.setItems(FXCollections.observableArrayList(queryManager.getCriteria()));
                    executeQueryBtn.setDisable(false);
                    clearQueryBtn.setDisable(false);
                }
            }
            case INT, FLOAT -> {
                if (!value.matches("\\d+(\\.\\d+)?")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Integers can only be compared to integers. Please enter a valid integer");
                    alert.showAndWait();
                } else {
                    queryManager.addCriterion(attribute, value, operator);
                    currentCriteria.setItems(FXCollections.observableArrayList(queryManager.getCriteria()));
                    executeQueryBtn.setDisable(false);
                    clearQueryBtn.setDisable(false);
                }
            }
        }
    }

    /**
     * Executes the query and updates the cas table view
     */
    public void executeQuery() {

        if(!(queryManager.getCriteria().isEmpty())) {
            state.setCurrentQuery(queryManager.getQuery());
            CrashManager.getInstance().setQueryTable();
            state.getCurrentController().refreshView();
        }
        mainController.setCurrentModal(null);
        mainController.setCurrentModalType(ModalType.NONE);
        stage.close();
    }

    /**
     * Removes the selected query from the list view
     */
    public void removeQuery() {
        queryManager.removeCriterion(selectedQuery);
        currentCriteria.setItems(FXCollections.observableArrayList(queryManager.getCriteria()));

        if(queryManager.getCriteria().isEmpty()) {
            executeQueryBtn.setDisable(true);
            clearQueryBtn.setDisable(true);
        }
    }

    /**
     * Checks to see if all fields are selected and if so enables the add to query button
     */
    public void checkAddToQueryBtn() {
        addToQueryBtn.setDisable(attributeComboBox.getSelectionModel().isEmpty() || operatorComboBox.getSelectionModel().isEmpty() || valueTextField.getText().isEmpty());
    }

    /**
     * Clears the query
     */
    public void clearQuery() {
        for (QueryCriterion criterion: currentCriteria.getItems()) {
            queryManager.removeCriterion(criterion);
        }
        currentCriteria.setItems(FXCollections.observableArrayList(queryManager.getCriteria()));
        clearQueryBtn.setDisable(true);
        executeQueryBtn.setDisable(true);
    }

    /**
     * Populates the label with the possible values for the attribute
     * @param stringAttribute the attribute in string format
     */
    public void populateLabel(String stringAttribute) {
        AttributeType attributeType = AttributeType.fromString(stringAttribute);
        String possibleValues = "Possible Values: ";
        int index = 0;
        switch(attributeType) {
            case CRASHSEVERITY -> {
                for (Severity severity : Severity.values()) {
                    possibleValues += severity.printForm;
                    index ++;
                    if(index != Severity.values().length) {
                        possibleValues += ", ";
                    }
                }
            }
            case LIGHT -> {
                for (Light light : Light.values()) {
                    possibleValues += light.printForm;
                    index ++;
                    if(index != Light.values().length) {
                        possibleValues += ", ";
                    }
                }
            }
            case REGION -> {
                for (Region region : Region.values()) {
                    possibleValues += region.printForm;
                    index ++;
                    if(index != Region.values().length) {
                        possibleValues += ", ";
                    }
                }
            }
            case WEATHERA -> {
                for (Weather weather: Weather.values()) {
                    possibleValues += weather.printForm;
                    index ++;
                    if(index != 5) {
                        possibleValues += ", ";
                    } else {
                        break;
                    }
                }
            }
            case WEATHERB -> {
                possibleValues += "Strong wind, Frost";
            }
            case TRAFFICCONTROL -> {
                for (TrafficControl trafficControl : TrafficControl.values()) {
                    possibleValues += trafficControl.printForm;
                    index ++;
                    if(index != TrafficControl.values().length) {
                        possibleValues += ", ";
                    }
                }
            }
            case HOLIDAY -> {
                for (Holiday holiday : Holiday.values()) {
                    possibleValues += holiday.printForm;
                    index ++;
                    if(index != Holiday.values().length) {
                        possibleValues += ", ";
                    }
                }
            }
            case CRASHYEAR -> {
                for(int year = 2000; year< 2023; year++) {
                    possibleValues += year + ", ";
                }
                possibleValues += "2023";
            }
        }
        if(possibleValues.equals("Possible Values: ")) {
            switch(attributeType.classType) {
                case INT -> {
                    possibleValues += "Integers";
                }
                case FLOAT -> {
                    possibleValues += "Floats";
                }
                case STRING -> {
                    possibleValues += "Strings";
                }
                default -> throw new IllegalStateException("Unexpected value: " + attributeType.classType);
            }
        }
        possibleValuesLabel.setText(possibleValues);
    }

    /**
     * Closes the query modal.
     * If the user changes the query but doesn't execute it then it keeps the old query.
     */
    public void close() {
        if(!queryManager.getCriteria().equals(oldCriteria)) {
            queryManager.setCriteria(oldCriteria);
        }
        mainController.setCurrentModal(null);
        mainController.setCurrentModalType(ModalType.NONE);
        stage.close();
    }

}
