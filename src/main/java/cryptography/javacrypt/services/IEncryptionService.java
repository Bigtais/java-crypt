package cryptography.javacrypt.services;

import javafx.concurrent.Task;

public interface IEncryptionService {

    /**
     * Returns a JavaFX Task that encrypts a file according to a certain algorithm and encryption mode and padding.
     * @param inputFilepath     The filepath of the input file.
     * @param outputFilepath    The filepath of the output file.
     * @param keyLength           The key length used for the algorithm.
     * @param algorithm         The algorithm to which the file is encrypted.
     * @param encryptionMode    The encryption mode of the file.
     * @param encryptionPadding The encryption padding of the file.
     * @param password          The password used to encrypt the file.
     * @return The task encrypting the input file.
     */
    Task<Void> getFileEncryptionTask(String inputFilepath,
                                     String outputFilepath,
                                     String algorithm,
                                     int keyLength,
                                     String encryptionMode,
                                     String encryptionPadding,
                                     char[] password);
}
