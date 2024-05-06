package seng202.team9.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class starts the javaFX application window
 * @author seng202 teaching team
 */
public class MainWindow extends Application {


    /**
     * Opens the gui with the fxml content specified in resources/fxml/main.fxml
     * @param primaryStage The current fxml stage, handled by javaFX Application class
     * @throws IOException if there is an issue loading fxml file
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = mainLoader.load();

        MainController mainController = mainLoader.getController();
        mainController.init(primaryStage);

        Image appIcon = new Image("icons/appIcon.png");
        primaryStage.getIcons().add(appIcon);
        primaryStage.setTitle("CQS");
        primaryStage.setMinWidth(1350);
        primaryStage.setMinHeight(850);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the FXML application, this must be called from another class (in this cass App.java) otherwise JavaFX
     * errors out and does not run
     * @param args command line arguments
     */
    public static void main(String [] args) {
        launch(args);
    }

}
