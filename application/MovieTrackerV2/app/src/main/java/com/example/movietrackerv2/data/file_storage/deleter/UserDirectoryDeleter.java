package com.example.movietrackerv2.data.file_storage.deleter;

import android.content.Context;

import com.example.movietrackerv2.data.component.User;
import com.example.movietrackerv2.data.file_storage.locator.UserFolderLocator;

import java.io.File;

// the purpose of this class is to provide methods for deleting a user's directory
public class UserDirectoryDeleter extends UserFolderLocator implements FileDeleter {

    private User loggedInUser;
    private Context context;

    public UserDirectoryDeleter(User loggedInUser, Context context) {
        this.context = context;
        this.loggedInUser = loggedInUser;
    }

    // calls deleteRecursive to delete the logged in user's directory and it's contents
    @Override
    public void deleteFile() {
        File dir = getUserFolder(context, loggedInUser);
        if (dir == null) {
            return;
        }
        deleteRecursive(dir);
    }

    // this method is copied from user teedyay's answer from here: https://stackoverflow.com/questions/4943629/how-to-delete-a-whole-folder-and-content
    // the method is unchanged
    // deletes the given file. If it is a directory, recursively deletes every entry inside.
    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}
