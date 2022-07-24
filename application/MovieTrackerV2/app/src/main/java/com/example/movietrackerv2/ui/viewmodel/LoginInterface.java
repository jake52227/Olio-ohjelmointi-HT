package com.example.movietrackerv2.ui.viewmodel;

import androidx.lifecycle.LiveData;

import com.example.movietrackerv2.data.component.User;

public interface LoginInterface {
    LiveData<User> getLoggedInUser();
    LiveData<Boolean> attemptLogIn(String username, String givenPassword);
    boolean checkPasswordValidity(String password);
    LiveData<Boolean> addUser(String username, String password);
    void logOutUser();
    void deleteUser();
}
