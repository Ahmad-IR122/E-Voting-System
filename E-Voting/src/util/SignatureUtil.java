package util;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class SignatureUtil {

    public static void signAndVerify(String data) {
        try {
            // Generate RSA key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            // Sign data
            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(privateKey);
            privateSignature.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signature = privateSignature.sign();

            System.out.println("Signature: " + Base64.getEncoder().encodeToString(signature));

            // Verify signature
            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            publicSignature.initVerify(publicKey);
            publicSignature.update(data.getBytes(StandardCharsets.UTF_8));
            boolean isCorrect = publicSignature.verify(signature);

            System.out.println("Signature correct: " + isCorrect);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}