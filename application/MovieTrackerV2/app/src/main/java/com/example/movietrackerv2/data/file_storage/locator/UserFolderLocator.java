package com.example.movietrackerv2.data.file_storage.locator;

import android.content.Context;

import com.example.movietrackerv2.data.component.User;

import java.io.File;

// an abstract class providing a protected method for locating a logged in user's directory file
public abstract class UserFolderLocator {

    // help for this method gotten from user VigneshK's answer: https://stackoverflow.com/questions/48303280/write-a-file-to-a-folder-in-the-android-device-internal-storage
    // creates a new user specific folder if it does not already exist. Returns the folder
    protected File getUserFolder(Context context, User loggedInUser) {
        if (loggedInUser == null) {
            return null;
        }
        String directoryName = loggedInUser.getUsername();
        File directory = new File(context.getFilesDir()+File.separator+directoryName);

        if(!directory.exists())
            directory.mkdir();

        return directory;
    }
}
