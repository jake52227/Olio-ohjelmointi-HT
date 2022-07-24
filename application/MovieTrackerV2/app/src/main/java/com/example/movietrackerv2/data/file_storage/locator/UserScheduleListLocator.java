package com.example.movietrackerv2.data.file_storage.locator;

import android.content.Context;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.data.component.User;

import java.io.File;

// the purpose of this class is to provide methods for locating a file which holds a user's schedule list information
public class UserScheduleListLocator extends UserFolderLocator implements FileLocator {

    private Context context;
    private User loggedInUser;

    public UserScheduleListLocator(Context context, User loggedInUser) {
        this.context = context;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public File locateFile() {
        return getFile();
    }

    // returns a File object which holds a path to the xml file which contains a UserScheduleList object of the logged in user
    private File getFile() {
        String filename = context.getResources().getString(R.string.user_schedule_file);
        File directory = getUserFolder(context, loggedInUser);
        return new File(directory, filename);
    }
}
