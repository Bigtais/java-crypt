package cryptography.javacrypt.services;

import java.util.List;

public interface IAlgorithmsProviderService {

    /**
     * Returns the list of available encryption algorithms of this service.
     * @return The list of available algorithms.
     */
    List<String> getAvailableEncryptionAlgorithms();

    /**
     * Returns the list of available key lengths for an algorithms.
     * @param algorithm The algorithm to which the key lengths are searched.
     * @return The list of available key lengths.
     */
    List<Integer> getAvailableKeyLength(String algorithm);

    /**
     * Returns the list of available encryption modes.
     * @return The available encryption modes.
     */
    List<String> getAvailableEncryptionModes();

    /**
     * Returns the list of available encryption paddings.
     * @return The available encryption paddings.
     */
    List<String> getAvailablePaddingModes();

    /**
     * Returns the list of available hashing functions to use.
     * @return List of available hashing functions.
     */
    List<String> getAvailableHashingFunctions();

}
