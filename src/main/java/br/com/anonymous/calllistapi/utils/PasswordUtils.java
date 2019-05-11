package br.com.anonymous.calllistapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordUtils.class);

    public static String ConvertByteArrayToHexString(byte[] byteArray, int size) {
        String format = "%".concat(String.valueOf(size)).concat("x");

        return String.format(format, new BigInteger(1, byteArray)).toUpperCase();
    }

    public static byte[] ConvertHexStringToByteArray(String hexString) {
        if (!StringUtils.hasText(hexString)) {
            return new byte[0];
        }

        byte[] buffer = new byte[hexString.length() / 2];
        byte[] hexByteArray = new BigInteger(hexString, 16).toByteArray();
        int offset = hexByteArray.length - buffer.length;

        System.arraycopy(hexByteArray, offset, buffer, 0, buffer.length);

        return buffer;
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

    public static String generateSaltString() {
        byte[] salt = generateSalt();

        return ConvertByteArrayToHexString(salt, 32);
    }

    public static String generateHashedPassword(String salt, String password) {
        return generateHashedPassword(ConvertHexStringToByteArray(salt), password);
    }

    public static String generateHashedPassword(byte[] salt, String password) {
        String finalPassword = null;

        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-512");
            md.update(salt);

            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

            finalPassword = ConvertByteArrayToHexString(hashedPassword, 128);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("SHA-512 nao encontrado");
        }

        return finalPassword;
    }
}
