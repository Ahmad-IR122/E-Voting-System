package util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

public class KeyManager {

    private static PublicKey publicKey;

    static {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();

            publicKey = pair.getPublic();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }
}