package seng202.team9.io;

import seng202.team9.models.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Allows importing of CSV files into crash objects.<br>
 * Code repurposed from <a href="https://eng-git.canterbury.ac.nz/men63/seng202-advanced-fx-public/-/blob/master/src/main/java/seng202/demo/io/Importable.java">Morgan's Advanced JavaFX code</a>
 *
 * @author Morgan English
 * @author Jake Dalton
 */
public class CSVImporter implements Importable<Crash> {
    private static final Logger log = LogManager.getLogger(CSVImporter.class);

    /**
     * Stores the any ERROR level logs for the class
     */
    private static String lastLogLevel;

    /**
     * @return the last log level
     */
    public static String getLastLogLevel() {
        return lastLogLevel;
    }

    /**
     * @param level String containing the ERROR level log
     */
    public static void setLastLogLevel(String level) {
        lastLogLevel = level;
    }

    /**
     * Loads CAS data from a CSV file using OpenCSV's CSVReader
     *
     * @param file The file to read from
     * @return List of crash objects read from the file, excluding all entries that failed conversions.
     */
    public List<Crash> readFileBatch(File file, int numBatch) {
        try {
            return readFile(new CSVReader(new FileReader(file)), numBatch);
        } catch (FileNotFoundException e) {
            log.error("Error opening file at path %s: %s".formatted(file.getPath(), e.getMessage()));
        }
        return Collections.emptyList();
    }


    /**
     * The first population of the database using an input stream. So that the user is met with a fully populated database
     * @param filePath The path to the CAS crash data file
     * @return A list of crashes to insert into the database.
     */
    public List<Crash> readInitialFileBatch(String filePath, int numBatch) {
        CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath))));
        return readFile(reader, numBatch);
    }

    private List<Crash> readFile(CSVReader reader, int numBatch) {
        ArrayList<Crash> crashes = new ArrayList<>();
        try {
            int entriesToSkip = LOAD_BATCH_NO * numBatch;
            reader.skip(1 + entriesToSkip); // +1 Jumps the header line
            String[] line;

            // Iterate over each CSV line, convert to crash object and add to final list if successful
            int count = 0;
            while ((line = reader.readNext()) != null && count < LOAD_BATCH_NO) {
                Crash crash = readCrashFromLine(line);
                if (crash != null) {
                    crashes.add(crash);
                }
                count++;
            }
        } catch (IOException | CsvValidationException e) {
            log.error(e);
            setLastLogLevel("ERROR");
        }
        return crashes;
    }

    /**
     * @param line array of String objects, each string being the value of one CAS attribute
     * @return Crash object containing the data from the given line, or null if a conversion failed.
     */
    public Crash readCrashFromLine(String[] line) {
        try {
            ArrayList<Attribute> data = new ArrayList<>();
            if (line.length != AttributeType.values().length) {
                log.error("CSV File has illegal number of arguments: should be %d, got %d".formatted(
                        AttributeType.values().length, line.length));
                setLastLogLevel("ERROR");
                return null;
            }
            for (int i = 0; i < line.length; i++) {
                data.add(new Attribute(AttributeType.values()[i], line[i]));
            }
            return new Crash(data);
        } catch (NumberFormatException e) {
            log.error("Couldn't format number:" + e);
            setLastLogLevel("ERROR");
        } catch (StringIndexOutOfBoundsException e) {
            log.error("ID Field is invalid length: " + e);
            setLastLogLevel("ERROR");
        }
        return null;
    }
}
