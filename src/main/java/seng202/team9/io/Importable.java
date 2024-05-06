package seng202.team9.io;

import java.io.File;
import java.util.List;

/**
 * Interface that allows for multiple types of input file to be added to the app.
 * As long as whatever class that implements it returns a List of Crash objects.
 * Code repurposed from <a href="https://eng-git.canterbury.ac.nz/men63/seng202-advanced-fx-public/-/blob/master/src/main/java/seng202/demo/io/Importable.java">Morgan's Advanced JavaFX code</a>
 * @author Morgan English
 */
public interface Importable<Crash> {
    /**
     * The size of the batch to load in
     */
    int LOAD_BATCH_NO = 100000;
    /**
     * Reads a file input by the user.
     * @param file The file to read from
     * @param numBatch which batch to read from the file
     * @return List of Crash objects stored in the file
     */
    List<Crash> readFileBatch(File file, int numBatch);

    /**
     * The first population of the database using an input stream. So that the user is met with a fully populated database
     * @param filePath The path to the CAS crash data file
     * @param numBatch which batch to read from the file
     * @return A list of crashes to insert into the database.
     */
    List<Crash> readInitialFileBatch(String filePath, int numBatch);
}
