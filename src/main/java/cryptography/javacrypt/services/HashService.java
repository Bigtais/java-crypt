package cryptography.javacrypt.services;

import javafx.concurrent.Task;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HashService implements IHashingService {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    @Override
    public List<String> getAvailableAlgorithms() {
        return Arrays.stream(Security.getProviders())
                .flatMap(provider -> provider.getServices().stream())
                .filter(service -> "MessageDigest".equals(service.getType()))
                .map(Provider.Service::getAlgorithm)
                .collect(Collectors.toList());
    }

    @Override
    public Task<String> getHashCalculationTask(String filepath, String algorithm) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                FileInputStream fileInputStream = new FileInputStream(filepath);
                FileChannel fileChannel = fileInputStream.getChannel();
                updateProgress(0, fileChannel.size());

                MessageDigest digest = MessageDigest.getInstance(algorithm);
                ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);

                long read;
                long progress = 0;
                while ((read = fileChannel.read(buffer)) != -1) {
                    buffer.flip();
                    digest.update(buffer);
                    buffer.clear();
                    progress += read;
                    updateProgress(progress, fileChannel.size());
                }

                fileInputStream.close();

                byte[] result = digest.digest();

                return byteToHexString(result);
            }
        };
    }

    /**
     * Transforms a byte array into a hexadecimal representation of this array
     * @param bytes The byte array to transform into a hexadecimal string.
     * @return Hexadecimal string representation of the byte array.
     */
    private String byteToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
