package util;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class SignatureUtil {


    public static boolean verifySignature(String data, String signature, PublicKey publicKey) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes());

            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return sig.verify(signatureBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String generateSignature(String data) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(privateKey);
            privateSignature.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signature = privateSignature.sign();

            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            publicSignature.initVerify(publicKey);
            publicSignature.update(data.getBytes(StandardCharsets.UTF_8));
            boolean isCorrect = publicSignature.verify(signature);

            System.out.println("Signature correct: " + isCorrect);

            return Base64.getEncoder().encodeToString(signature);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}