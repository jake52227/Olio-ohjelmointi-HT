package com.example.movietrackerv2.data.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

// this class has utility methods for encrypting a string with SHA-256
public class Sha256Encryptor implements EncryptorInterface {

    private String password;
    private String salt;

    @Override
    public String getHash() {
        return password;
    }

    @Override
    public String getSalt() {
        return salt;
    }

    // help for this method gotten from here: https://www.baeldung.com/sha-256-hashing-java
    // salts and encrypts the given password string using SHA-256 algorithm. The result is set in the password and salt data fields.
    @Override
    public void encrypt(String password) {
        byte[] salt = generateSalt();
        byte[] hash = null;

        String plusSalt = password + bytesToHex(salt);
        hash = sha256Encrypt(plusSalt);

        this.password =  bytesToHex(hash);
        this.salt = bytesToHex(salt);
    }

    // Checks if the password with the salt matches the hash after encryption
    @Override
    public boolean checkMatch(String givenPassword, String salt, String correctHash) {
        byte[] test = sha256Encrypt(givenPassword + salt);
        if (bytesToHex(test).equals(correctHash)) {
            return true;
        }
        return false;
    }

    // Uses MessageDigest to hash the given string. Returns the hash
    private byte[] sha256Encrypt(String hashable) {
        MessageDigest digest;
        byte[] hash = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest( hashable.getBytes(StandardCharsets.UTF_8) );

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    // Generates a 128-bit salt for the hash using SecureRandom
    private byte[] generateSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    // this whole method is copied from an answer given by the user "maybeWeCouldStealAVan" from here: https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
    // the method remains unchanged
    // Converts the given byte sequence to string
    private String bytesToHex(byte[] bytes) {
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
