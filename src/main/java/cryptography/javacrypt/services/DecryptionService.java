package cryptography.javacrypt.services;

import javafx.concurrent.Task;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DecryptionService implements IDecryptionService{
    @Override
    public Task<Void> getFileEncryptionTask(String inputFilepath,
                                            String outputFilepath,
                                            String algorithm,
                                            int keyLength,
                                            String decryptionMode,
                                            String decryptionPadding,
                                            char[] password) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                var fullAlgorithmName = algorithm + "/" + decryptionMode + "/" + decryptionPadding;

                Cipher cipher = Cipher.getInstance(fullAlgorithmName);

                FileInputStream fileInputStream = new FileInputStream(inputFilepath);
                FileChannel fileChannel = fileInputStream.getChannel();
                updateProgress(0, fileChannel.size());
                long progress = 0;

                byte[] salt = new byte[16];
                fileInputStream.read(salt);
                progress += 16;
                updateProgress(progress, fileChannel.size());

                SecretKey secretKey = getKeySpecFromPassword(password, salt, keyLength);
                SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), algorithm);


                if (decryptionMode.equalsIgnoreCase("ECB")) {
                    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                }
                else{
                    byte[] iv = new byte[cipher.getBlockSize()];
                    fileInputStream.read(iv);
                    progress += 16;
                    updateProgress(progress, fileChannel.size());
                    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
                }

                FileOutputStream fileOutputStream = new FileOutputStream(outputFilepath);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);

                ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);

                long read;
                while ((read = fileChannel.read(buffer)) != -1) {
                    buffer.flip();

                    byte[] toWrite = new byte[buffer.remaining()];
                    buffer.get(toWrite);
                    cipherOutputStream.write(toWrite);

                    buffer.clear();

                    progress += read;
                    updateProgress(progress, fileChannel.size());
                }

                cipherOutputStream.close();
                fileInputStream.close();

                return null;
            }
        };
    }

    /**
     * Returns a SecretKey from the password and salt with the according key size.
     * @param password  The password to use for the SecretKey generation.
     * @param salt      The salt array of bytes.
     * @param keyLength   The size of the key to generate.
     * @return A SecretKey generated from the password and salt with the size wanted.
     */
    private SecretKey getKeySpecFromPassword(char[] password, byte[] salt, int keyLength)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, 5000, keyLength);
        return secretKeyFactory.generateSecret(pbeKeySpec);
    }
}
