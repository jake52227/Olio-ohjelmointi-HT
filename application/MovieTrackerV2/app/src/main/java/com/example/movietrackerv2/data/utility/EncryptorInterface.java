package com.example.movietrackerv2.data.utility;

public interface EncryptorInterface {
    String getHash();
    String getSalt();
    void encrypt(String encryptable);
    boolean checkMatch(String stringToCheck, String salt, String correctHash);
}
