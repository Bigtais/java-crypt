package cryptography.javacrypt.services;

import javafx.concurrent.Task;

public interface IDecryptionService {

    /**
     * Returns a JavaFX Task that decrypts a file according to a certain algorithm and encryption mode and padding.
     * @param inputFilepath     The filepath of the input file.
     * @param outputFilepath    The filepath of the output file.
     * @param keyLength         The key size used for the algorithm.
     * @param algorithm         The algorithm to which the file is encrypted.
     * @param decryptionMode    The decryption mode of the file.
     * @param decryptionPadding The decryption padding of the file.
     * @param password          The password used to encrypt the file.
     * @return The task decrypting the input file.
     */
    Task<Void> getFileEncryptionTask(String inputFilepath,
                                     String outputFilepath,
                                     String algorithm,
                                     int keyLength,
                                     String decryptionMode,
                                     String decryptionPadding,
                                     char[] password);

}
