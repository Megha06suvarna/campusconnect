
package com.campusconnect.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {

    private static final String PREFIX = "PBKDF2";
    private static final int ITERATIONS = 210000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
        // Utility class; do not create objects.
    }

    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password cannot be empty.");
        }

        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);

        byte[] hash = deriveKey(
                password.toCharArray(), salt, ITERATIONS);

        return PREFIX + "$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt)
                + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(
            String password, String storedPassword) {

        if (password == null || storedPassword == null
                || storedPassword.isEmpty()) {
            return false;
        }

        if (storedPassword.startsWith(PREFIX + "$")) {
            try {
                String[] parts = storedPassword.split("\\$");

                if (parts.length != 4) {
                    return false;
                }

                int iterations = Integer.parseInt(parts[1]);

                if (iterations < 10000 || iterations > 1000000) {
                    return false;
                }

                byte[] salt = Base64.getDecoder().decode(parts[2]);
                byte[] expectedHash =
                        Base64.getDecoder().decode(parts[3]);

                byte[] actualHash = deriveKey(
                        password.toCharArray(), salt, iterations);

                return MessageDigest.isEqual(
                        expectedHash, actualHash);

            } catch (Exception e) {
                return false;
            }
        }

        // Temporary compatibility for old demo accounts.
        // Plain-text password storage is not safe for production.
        return MessageDigest.isEqual(
                password.getBytes(StandardCharsets.UTF_8),
                storedPassword.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] deriveKey(
            char[] password, byte[] salt, int iterations) {

        PBEKeySpec spec = new PBEKeySpec(
                password, salt, iterations, KEY_LENGTH);

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256");

            return factory.generateSecret(spec).getEncoded();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to process password securely.", e);

        } finally {
            spec.clearPassword();
        }
    }
}
