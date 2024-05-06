package seng202.team9;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team9.gui.MainWindow;

/**
 * Default entry point class
 * @author team - 9
 */
public class App {
    private static final Logger log = LogManager.getLogger(App.class);

    /**
     * Entry point which runs the javaFX application
     * Also shows off some different logging levels
     * @param args program arguments from command line
     */
    public static void main(String[] args) {
        log.info("Welcome to the CQS app. Any logged output will appear here");

        MainWindow.main(args);
    }
}
