package seng202.team9.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.CheckComboBox;
import seng202.team9.business.CrashManager;
import seng202.team9.business.WindowState;
import seng202.team9.models.stringenums.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The controller class responsible for the graphing.fxml that allows a user to choose which categories and attributes
 * they wish to display on a desired graph type
 *
 * @author ist46
 */
public class GraphingController {

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private CheckComboBox<String> attributesCheckComboBox;

    @FXML
    private ComboBox<String> graphTypeComboBox;

    @FXML
    private StackPane chartContainer;

    /**
     * Local CrashManager object
     */
    private CrashManager crashManager;

    private List<String> xValues = new ArrayList<>();

    private List<Integer> yValues = new ArrayList<>();

    /**
     * Stores the currently selected table
     */
    private WindowState state;

    /**
     * Initialises the graphing to allow functionality in Main Controller
     */
    public void init() {
        this.state = WindowState.getInstance();

        categoryComboBox.setItems(FXCollections.observableArrayList("Participants", "Obstacles", "Severity",
                "Light", "Holiday", "Region", "Traffic Control", "Weather", "Year"));

        crashManager = CrashManager.getInstance();

        // Put all graph types
        graphTypeComboBox.setItems(FXCollections.observableArrayList(
                "Bar Chart","Line Chart", "Area Chart"
                , "Scatter Chart", "Pie Chart"));
    }

    /**
     * General generate graph function. Will call appropriate generate method given the graph type
     */
    public void generateGraph() {
        // Get Selected category
        String category = categoryComboBox.getValue();
        // Get selected attributes
        List<String> selectedValues = attributesCheckComboBox.getCheckModel().getCheckedItems();
        // Create array to store attributes for checking if none are selected
        List<String> notNullSelectedValues = new ArrayList<>();
        // Check for empty category
        if(category == null) {
            showWarning("Please select a category!");
            return;
        }
        // Store the unselected attributes
        for(String value : selectedValues) {
            if(value != null) {
                notNullSelectedValues.add(value);
            }
        }
        // Inform user to select 1 attribute
        if (notNullSelectedValues.isEmpty()) {
            showWarning("Please select at least one attribute!");
            return;
        }

        // Check if a table has been selected,
        if (state.getCurrentTable() == null || state.getCurrentTable().trim().isEmpty()) {
            showWarning("Please select at a table one attribute!");
            return;
        }

        // Get selected graph
        String graphType = graphTypeComboBox.getValue();

        // Check a graph has been selected
        if (graphType == null || graphType.isEmpty()) {
            // Inform user to select a graph type
            showWarning("Please select a graph type!");
            return;
        }

        // Get the data from selected table
        xValues = notNullSelectedValues;
        yValues = crashManager.getCounts(category, xValues);

        // Decide which generate graph function to use
        if (graphType.equals("Pie Chart")) {
            generateGraphPie();
        } else {
            generateGraphXY(graphType);
        }
    }

    /**
     * Creates any graph that has a xy axis with a category and number axis and then calls a add data and display function
     * @param graphType the selected graphType
     */
    private void generateGraphXY(String graphType) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        XYChart<String, Number> chart = null;
        switch (graphType) {
            case "Bar Chart" -> chart = new BarChart<>(xAxis, yAxis);
            case "Line Chart" -> chart = new LineChart<>(xAxis, yAxis);
            case "Area Chart" -> {
                chart = new AreaChart<>(xAxis, yAxis);
                // Adds custom style for css
                chart.getStyleClass().add("my-area-chart");
            }
            case "Scatter Chart" -> chart = new ScatterChart<>(xAxis, yAxis);
        }

        addDataAndDisplayChart(chart);
    }

    /**
     * Creates a pie graph and then calls a add data and display function
     *
     */
    private void generateGraphPie() {
        PieChart pieChart = new PieChart();
        addDataAndDisplayChart(pieChart);
    }

    /**
     * Function that adds the data and displays the graph
     * @param chart the chart to display
     */
    private void addDataAndDisplayChart(Chart chart) {
        // Clear any previous charts
        chartContainer.getChildren().clear();

        // Get selected category
        String category = categoryComboBox.getValue();

        if (chart instanceof PieChart) {
            List<PieChart.Data> pieChartData = new ArrayList<>();
            for (int i = 0; i < xValues.size(); i++) {
                PieChart.Data data = new PieChart.Data(xValues.get(i), yValues.get(i));
                pieChartData.add(data);
            }
            ((PieChart) chart).setData(FXCollections.observableArrayList(pieChartData));

        } else {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (int i = 0; i < xValues.size(); i++) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(xValues.get(i), yValues.get(i));
                series.getData().add(data);
            }
            ((XYChart<String, Number>) chart).getData().add(series);
        }

        // Setting the title for the chart
        chart.setTitle("Number Of Crashes by " + category);

        // Assigning X and Y Axis Labels
        if(chart instanceof XYChart) {
            ((XYChart<String, Number>) chart).getXAxis().setLabel(category);
            ((XYChart<String, Number>) chart).getYAxis().setLabel("Number Of Crashes");
            chart.setLegendVisible(false);
        }

        chart.layout();
        chartContainer.getChildren().add(chart);
    }

    /**
     * Generic alert generator
     * @param message warning message
     */
    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * When a category is selected it updates the CheckCombobox with the corresponding attributes
     *
     */
    public void updateAttributes() {
        attributesCheckComboBox.getItems().clear();
        switch (categoryComboBox.getValue()) {
            case "Severity" -> {
                for (Severity severity : Severity.values()) {
                    attributesCheckComboBox.getItems().add(severity.printForm);
                }
            }
            case "Participants" -> {
                for (PeopleAndVehicles peopleAndVehicles : PeopleAndVehicles.values()) {
                    attributesCheckComboBox.getItems().add(peopleAndVehicles.printForm);
                }
            }
            case "Obstacles" -> {
                for (Obstacles obstacle : Obstacles.values()) {
                    attributesCheckComboBox.getItems().add(obstacle.printForm);
                }
            }
            case "Light" -> {
                for (Light light : Light.values()) {
                    attributesCheckComboBox.getItems().add(light.printForm);
                }
            }
            case "Holiday" -> {
                for (Holiday holiday : Holiday.values()) {
                    attributesCheckComboBox.getItems().add(holiday.printForm);
                }
            }
            case "Region" -> {
                for (Region region : Region.values()) {
                    attributesCheckComboBox.getItems().add(region.printForm);
                }
            }
            case "Traffic Control" -> {
                for (TrafficControl trafficControl : TrafficControl.values()) {
                    attributesCheckComboBox.getItems().add(trafficControl.printForm);
                }
            }
            case "Weather" -> {
                for (Weather weather : Weather.values()) {
                    attributesCheckComboBox.getItems().add(weather.printForm);
                }
            }
            case "Year" -> {
                for (int year = 2000; year <= 2023; year++) {
                    attributesCheckComboBox.getItems().add("" + year + "");
                }
            }
        }

        for (int i = 0; i < attributesCheckComboBox.getItems().size(); i++) {
            attributesCheckComboBox.getCheckModel().check(i);
        }
    }




}
