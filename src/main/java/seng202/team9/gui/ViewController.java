package seng202.team9.gui;

import javafx.stage.Stage;

/**
 * A controller responsible for running exactly one view of the application.
 */
public interface ViewController {

    /**
     * Initialises the view controllers to allow ease of functionality in Main Controller
     * @param stage the current stage
     */
    void init(Stage stage);

    /**
     * Refreshes the view to show some new crash records.
     */
    void refreshView();
}
