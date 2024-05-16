package cryptography.javacrypt.services;

import java.security.Provider;
import java.security.Security;
import java.util.*;

public class AlgorithmsProviderService implements IAlgorithmsProviderService{

    private final Map<String, List<Integer>> algorithmToKeyLength;
    private final List<String> encryptionModes;
    private final List<String> paddingModes;

    public AlgorithmsProviderService() {
        algorithmToKeyLength = new HashMap<>();

//        Key lengths for AES keys
        var list = new ArrayList<Integer>();
        list.add(128);
        list.add(192);
        list.add(256);
        algorithmToKeyLength.put("AES", list);

//        Key lengths for DES keys
        list = new ArrayList<>();
        list.add(64);
        algorithmToKeyLength.put("DES", list);

//        Key lengths for DESede keys
        list = new ArrayList<>();
        list.add(128);
        list.add(192);
        algorithmToKeyLength.put("DESede", list);

//        Encryption modes
        encryptionModes = new ArrayList<>();
        encryptionModes.add("ECB");
        encryptionModes.add("CBC");
        encryptionModes.add("OFB");
        encryptionModes.add("CFB");
        encryptionModes.add("CTR");

//        Padding modes
        paddingModes = new ArrayList<>();
        paddingModes.add("NoPadding");
        paddingModes.add("PKCS5Padding");
        paddingModes.add("PKCS7Padding");
        paddingModes.add("ISO10126Padding");
        paddingModes.add("ISO10126-2Padding");
        paddingModes.add("ISO7816-4Padding");
        paddingModes.add("ISO9797-1Padding");
        paddingModes.add("X923Padding");
        paddingModes.add("TBCPadding");
        paddingModes.add("ZeroBytePadding");
    }

    @Override
    public List<String> getAvailableEncryptionAlgorithms() {
        return algorithmToKeyLength.keySet().stream().toList();
    }

    @Override
    public List<Integer> getAvailableKeyLength(String algorithm) {
        return algorithmToKeyLength.get(algorithm);
    }

    @Override
    public List<String> getAvailableEncryptionModes() {
        return encryptionModes;
    }

    @Override
    public List<String> getAvailablePaddingModes() {
        return paddingModes;
    }

    @Override
    public List<String> getAvailableHashingFunctions() {
        return Arrays.stream(Security.getProviders())
                .flatMap(provider -> provider.getServices().stream())
                .filter(service -> "MessageDigest".equals(service.getType()))
                .map(Provider.Service::getAlgorithm)
                .distinct().toList();
    }
}
