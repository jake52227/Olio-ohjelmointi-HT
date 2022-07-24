package com.example.movietrackerv2.data.file_storage.locator;

import android.content.Context;

import com.example.movietrackerv2.R;
import java.io.File;

// the purpose of this class is to provide methods for locating a file which holds theatre list information
public class TheatreListLocator implements FileLocator {

    private Context context;

    public TheatreListLocator(Context context) {
        this.context = context;
    }

    @Override
    public File locateFile() {
        return getFile();
    }

    // returns a File object which holds a path to the xml file which contains a TheatreList object
    private File getFile() {
        String filename = context.getResources().getString(R.string.theatres_file);
        return new File(context.getFilesDir() + File.separator + filename);
    }
}
