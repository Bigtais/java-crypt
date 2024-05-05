package cryptography.javacrypt.services;

import javafx.concurrent.Task;

import java.util.List;

public interface IHashingService {

    /**
     * Returns the list of available hashing algorithms to use.
     * @return List of available hashing algorithms
     */
    List<String> getAvailableAlgorithms();

    /**
     * Calculates the hash of the input file.
     * @param filename The input filename to calculate the hash.
     * @return The hash of the input file.
     */
    Task<String> calculateHash(String filename, String algorithm);
}
