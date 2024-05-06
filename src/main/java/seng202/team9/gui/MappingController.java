package seng202.team9.gui;

import javafx.concurrent.Worker;
import javafx.fxml.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team9.business.*;
import seng202.team9.models.*;
import java.util.*;

/**
 * The controller that handles all the logic for the mapping.fxml file
 */
public class MappingController implements ViewController {

    private static final Logger log = LogManager.getLogger(MappingController.class);

    private Stage stage;

    @FXML
    private WebView mapWebView;

    private WebEngine webEngine;

    private CrashManager crashManager;

    private JSObject javaScriptConnector;

    private JavaScriptBridge javaScriptBridge;

    private String currentTable;

    /**
     * Initialises the view controllers to allow ease of functionality in Main Controller
     * @param stage the current stage
     */
    public void init(Stage stage) {
        this.stage = stage;
        this.currentTable = WindowState.getInstance().getCurrentTable();
        crashManager = CrashManager.getInstance();

        initMap();
    }

    /**
     * Refreshes the screen to show the newly set data
     */
    @Override
    public void refreshView() {
        javaScriptBridge.changeTableName(WindowState.getInstance().getCurrentTable());
        javaScriptConnector.call("clearMap");
        addCrashesOnMap();
    }

    private void initMap() {
        webEngine = mapWebView.getEngine();
        mapWebView.prefWidthProperty().bind(stage.widthProperty());
        mapWebView.prefHeightProperty().bind(stage.heightProperty());

        javaScriptBridge = new JavaScriptBridge(currentTable, crashManager);
        webEngine.setJavaScriptEnabled(true);
        try {
            webEngine.load(Objects.requireNonNull(getClass().getClassLoader().getResource("html/mapping.html")).toExternalForm());

            webEngine.getLoadWorker().stateProperty().addListener(
                    (ov, oldState, newState) -> {
                        // if javascript loads successfully
                        if (newState == Worker.State.SUCCEEDED) {

                            JSObject window = (JSObject) webEngine.executeScript("window");
                            window.setMember("javaScriptBridge", javaScriptBridge);
                            javaScriptConnector = (JSObject) webEngine.executeScript("jsConnector");
                            javaScriptConnector.call("initMap");
                            addCrashesOnMap();
                        }
                    });
        } catch (NullPointerException e) {
            log.error("File mapping.html cannot be found. %s".formatted(e.getMessage()));
        }

    }

    /**
     * Gets the table's crashes and calls addCrashPin function to individually add pins on the map.
     */
    private void addCrashesOnMap() {
        ArrayList<MapKey> mapKeys;
        int currBatch = 0;
        do {
            mapKeys = CrashManager.getInstance().getMapKeyBatch(currBatch);
            for (MapKey mapKey : mapKeys) {
                int crashID = mapKey.getId();
                float lat = mapKey.getLat();
                float lon = mapKey.getLon();
                boolean isCustom = mapKey.getIsCustom();
                javaScriptConnector.call(
                        "addPinToMap", crashID, isCustom, lat, lon);
//                addCrashPin(mapKey);
            }
            currBatch++;
        } while (mapKeys.size() > 0);
        javaScriptConnector.call("processView");
    }
}
