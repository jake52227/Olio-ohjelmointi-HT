package com.example.movietrackerv2.data.collection;

import com.example.movietrackerv2.data.component.User;

import java.util.ArrayList;

// a class which holds User objects in an ArrayList
public class UserList {

    private ArrayList<User> userList;

    public UserList() {
        userList = new ArrayList<>();
    }

    public void addUser(User user) {
        userList.add(user);
    }

    // attempts to find a user by username. If found, returns the User object, else returns null.
    public User getUser(String username) {
        for (User u : userList) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    // deletes a user with the given username if it exists
    public void deleteUser(String username) {
        if (username == null) {
            return;
        }
        if (getUser(username) == null)
            return;
        userList.remove(getUser(username));
    }

    public boolean isEmpty() {
        return userList.isEmpty();
    }
}
