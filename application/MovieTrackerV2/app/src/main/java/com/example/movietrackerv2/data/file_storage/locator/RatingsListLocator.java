package com.example.movietrackerv2.data.file_storage.locator;

import android.content.Context;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.data.component.User;

import java.io.File;

// the purpose of this class is to provide methods for locating a file which holds ratings list information
public class RatingsListLocator extends UserFolderLocator implements FileLocator {

    private Context context;
    private User loggedInUser;

    public RatingsListLocator(Context context, User loggedInUser) {
        this.context = context;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public File locateFile() {
        return getFile();
    }

    // returns a File object which holds a path to the xml file which contains a RatingsList object of the logged in user
    private File getFile() {
        String filename = context.getResources().getString(R.string.ratings_file);
        File directory = getUserFolder(context, loggedInUser);
        return new File(directory, filename);
    }
}
