package com.example.movietrackerv2.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.movietrackerv2.data.component.User;
import com.example.movietrackerv2.data.file_storage.converter.ObjectConverter;
import com.example.movietrackerv2.data.file_storage.converter.XmlObjectConverter;
import com.example.movietrackerv2.data.file_storage.deleter.FileDeleter;
import com.example.movietrackerv2.data.file_storage.deleter.UserDirectoryDeleter;
import com.example.movietrackerv2.data.file_storage.locator.FileLocator;
import com.example.movietrackerv2.data.file_storage.locator.UserListLocator;
import com.example.movietrackerv2.data.file_storage.reader.FileReaderInterface;
import com.example.movietrackerv2.data.file_storage.writer.ObjectWriter;
import com.example.movietrackerv2.data.file_storage.reader.FileReader;
import com.example.movietrackerv2.data.file_storage.writer.XmlObjectWriter;
import com.example.movietrackerv2.data.collection.UserList;
import com.example.movietrackerv2.data.utility.Sha256Encryptor;
import com.example.movietrackerv2.data.utility.EncryptorInterface;
import com.thoughtworks.xstream.XStream;

public class UserViewModel extends AndroidViewModel implements LoginInterface, ConfirmationInterface {

    private MutableLiveData<UserList> mUserList;
    private UserList userList;
    private MutableLiveData<User> mLoggedInUser;
    private MutableLiveData<Boolean> mIsLoggedIn;

    private FileReaderInterface reader;
    private ObjectWriter writer;
    private ObjectConverter converter;

    public UserViewModel(@NonNull Application application) {
        super(application);
    }


    // returns an observable value of the logged in user object.
    @Override
    public LiveData<User> getLoggedInUser() {
        if (mLoggedInUser == null) {
            mLoggedInUser = new MutableLiveData<>();
            mLoggedInUser.setValue(null);
            if (mIsLoggedIn == null) {
                mIsLoggedIn = new MutableLiveData<>();
            }
            mIsLoggedIn.setValue(false);
        }
        return mLoggedInUser;
    }


    public LiveData<UserList> getUsers() {
        if (mUserList == null) {
            mUserList = new MutableLiveData<>();
            loadUsers();
        }
        userList = mUserList.getValue();
        return mUserList;
    }

    // checks the given credentials and logs in the correct user if a match is found. Returns an observable boolean value indicating success or failure
    @Override
    public LiveData<Boolean> attemptLogIn(String username, String givenPassword) {

        if (mIsLoggedIn == null) {
            mIsLoggedIn = new MutableLiveData<>();
            mIsLoggedIn.setValue(false);
        }

        if (username.trim().isEmpty() || givenPassword.trim().isEmpty()) {
            return mIsLoggedIn;
        }

        EncryptorInterface encryptor = new Sha256Encryptor();
        User user = getUsers().getValue().getUser(username);
        if (user == null) {
            return mIsLoggedIn;
        }
        // use Encryptor object to see if the given password matches this users passwords hash after adding the salt to the given one and hashing it
        if (encryptor.checkMatch(givenPassword, user.getSalt(), user.getHash())) {
            if (mLoggedInUser == null) {
                mLoggedInUser = new MutableLiveData<>();
            }
            mLoggedInUser.setValue(user);
            mIsLoggedIn.setValue(true);
            return mIsLoggedIn;
        }
        return mIsLoggedIn;
    }

    // checks if a password meets the requirements. Returns true if that is the case and false otherwise
    @Override
    public boolean checkPasswordValidity(String password) {
        if (password.length() < 12) {
            return false;
        }
        if (!password.matches(".*\\p{Lu}.*")) // at least one upper case letter (utf-8)
            return false;
        if (!password.matches(".*\\p{Ll}.*")) // at least one lower case letter (utf-8)
            return false;
        if (!password.matches(".*\\d.*"))    // at least one number
            return false;
        if (!password.matches(".*\\W.*"))    // at least one special character
            return false;

        return true;
    }

    // logs out the current user by setting value of mLoggedInUser to null and mIsLoggedIn to false. This triggers an observer observing the logged in user.
    @Override
    public void logOutUser() {
        mLoggedInUser.setValue(null);
        mIsLoggedIn.setValue(false);
    }

    // adds a a new user with given username and password. Returns an observable boolean value indicating success or failure.
    @Override
    public LiveData<Boolean> addUser(String username, String password) {

        if (mIsLoggedIn == null) {
            mIsLoggedIn = new MutableLiveData<>();
            mIsLoggedIn.setValue(false);
        }

        if (userList == null) {
            getUsers();
        }

        // if the username is already taken
        if (userList.getUser(username) != null) {
            return mIsLoggedIn;
        }

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            return mIsLoggedIn;
        }

        EncryptorInterface encryptor = new Sha256Encryptor();
        encryptor.encrypt(password); // encrypt the user's password. The result is stored in the encryptor object

        userList.addUser(new User(username, encryptor.getHash(), encryptor.getSalt()));
        mUserList.setValue(userList);
        writeUsers();

        if (mLoggedInUser == null) {
            mLoggedInUser = new MutableLiveData<>();
        }

        mLoggedInUser.setValue(mUserList.getValue().getUser(username));
        mIsLoggedIn.setValue(true);

        return mIsLoggedIn;
    }

    // deletes the currently logged in user. Removes the user from users list and deletes the user specific directory.
    @Override
    public void deleteUser() {
        getUsers();
        userList.deleteUser(mLoggedInUser.getValue().getUsername());
        mUserList.setValue(userList);
        removeUserFolder();
        writeUsers();
        mLoggedInUser.setValue(null);
        mIsLoggedIn.setValue(false);
    }

    // loads user list from file and sets the value of mUserList and userList
    private void loadUsers() {

        if (reader == null) {
            reader = new FileReader();
        }
        if (converter == null) {
            converter = new XmlObjectConverter(new XStream());
        }

        FileLocator fileLocator = new UserListLocator(getApplication().getApplicationContext());

        String content = reader.readFormFile(fileLocator);
        userList = (UserList) converter.convertToObject(content);
        if (userList == null) {
            userList = new UserList();
        }
        mUserList.setValue(userList);
    }

    // writes user list to file
    private void writeUsers() {

        if (writer == null) {
            writer = new XmlObjectWriter(new XStream());
        }

        FileLocator fileLocator = new UserListLocator(getApplication().getApplicationContext());

       writer.writeToFile(mUserList.getValue(), fileLocator);
    }

    // remove currently logged in user's directory
    private void removeUserFolder() {
        FileDeleter userDirectoryDeleter = new UserDirectoryDeleter(mLoggedInUser.getValue(), getApplication().getApplicationContext());
        userDirectoryDeleter.deleteFile();
    }

    // a method which will be invoked when the user wants to delete the profile and clicks yes in the confirmation dialog fragment
    @Override
    public void executeConfirmableAction() {
        deleteUser();
    }
}
