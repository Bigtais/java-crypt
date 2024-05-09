package cryptography.javacrypt.services;

import javafx.concurrent.Task;

import java.util.List;

/**
 * General interface for hash calculation.
 */
public interface IHashingService {

    /**
     * Returns the list of available hashing algorithms to use.
     * @return List of available hashing algorithms
     */
    List<String> getAvailableAlgorithms();

    /**
     * Returns a JavaFX Task that calculates the hash of the input file.
     * @param filepath The input filepath to calculate the hash.
     * @return The task calculating the hash of the input file.
     */
    Task<String> getHashCalculationTask(String filepath, String algorithm);
}
