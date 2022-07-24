package com.example.movietrackerv2.data.component;

// a class which holds information about a user
public class User {
    private String username;
    private String hash;
    private String salt;

    public User(String username, String password, String salt) {
        this.username = username;
        this.hash = password;
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }
}
