package com.example.movietrackerv2.data.file_storage.locator;

import android.content.Context;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.data.component.User;

import java.io.File;

// the purpose of this class is to provide methods for locating a file which holds users list information
public class UserListLocator implements FileLocator {

    private Context context;

    public UserListLocator(Context context) {
        this.context = context;
    }

    @Override
    public File locateFile() {
        return getFile();
    }

    // returns a File object which holds a path to the xml file which contains a UserList object
    private File getFile() {
        String filename = context.getResources().getString(R.string.users_file);
        return new File(context.getFilesDir() + File.separator + filename);
    }
}
