package cryptography.javacrypt.services;

import javafx.concurrent.Task;

/**
 * General interface for hash calculation.
 */
public interface IHashingService {

    /**
     * Returns a JavaFX Task that calculates the hash of the input file.
     * @param filepath The input filepath to calculate the hash.
     * @return The task calculating the hash of the input file.
     */
    Task<String> getHashCalculationTask(String filepath, String algorithm);
}
