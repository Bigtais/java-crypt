package cryptography.javacrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main {
    public static void main(String[] args) {
        if (Security.getProvider("BC") == null)
            Security.addProvider(new BouncyCastleProvider());
        MainApplication.main(args);
    }
}
