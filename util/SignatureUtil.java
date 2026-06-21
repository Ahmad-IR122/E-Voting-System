package util;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class SignatureUtil {

    public static String generateSignature(String data) {
        try {
            PrivateKey privateKey = KeyManager.getPrivateKey();

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            byte[] signedBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifySignature(String data, String signatureStr, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
            return signature.verify(signatureBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}