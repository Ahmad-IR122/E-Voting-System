package util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;

public class KeyManager {

    private static final Path KEY_DIR = resolveKeyDirectory();
    private static final Path PUBLIC_KEY_PATH = KEY_DIR.resolve("public.key");
    private static final Path PRIVATE_KEY_PATH = KEY_DIR.resolve("private.key");

    private static Path resolveKeyDirectory() {
        List<Path> candidates = Arrays.asList(
                Paths.get("src", "data", "keys"),
                Paths.get("E-Voting-System", "src", "data", "keys"),
                Paths.get("data", "keys")
        );

        for (Path candidate : candidates) {
            Path absoluteCandidate = candidate.toAbsolutePath().normalize();
            try {
                Files.createDirectories(absoluteCandidate);
                return absoluteCandidate;
            } catch (IOException ignored) {
            }
        }

        throw new UncheckedIOException(
                new IOException("Could not create or resolve any valid key directory.")
        );
    }

    public static void generateAndSaveKeysIfNotExist() {
        try {
            Files.createDirectories(KEY_DIR);

            if (Files.exists(PUBLIC_KEY_PATH) && Files.exists(PRIVATE_KEY_PATH)) {
                System.out.println("Using existing keys from: " + KEY_DIR.toAbsolutePath());
                return;
            }

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();

            Files.write(PUBLIC_KEY_PATH, pair.getPublic().getEncoded());
            Files.write(PRIVATE_KEY_PATH, pair.getPrivate().getEncoded());

            System.out.println("RSA key pair generated and saved in: " + KEY_DIR.toAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate/save RSA keys", e);
        }
    }

    public static PublicKey getPublicKey() {
        try {
            byte[] keyBytes = Files.readAllBytes(PUBLIC_KEY_PATH);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key from: " + PUBLIC_KEY_PATH.toAbsolutePath(), e);
        }
    }

    public static PrivateKey getPrivateKey() {
        try {
            byte[] keyBytes = Files.readAllBytes(PRIVATE_KEY_PATH);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(spec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key from: " + PRIVATE_KEY_PATH.toAbsolutePath(), e);
        }
    }

    public static void printKeyPaths() {
        System.out.println("Public key path: " + PUBLIC_KEY_PATH.toAbsolutePath());
        System.out.println("Private key path: " + PRIVATE_KEY_PATH.toAbsolutePath());
    }
}