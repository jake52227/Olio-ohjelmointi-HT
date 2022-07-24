package com.example.movietrackerv2.data.file_storage.locator;


import android.content.Context;

import com.example.movietrackerv2.R;

import java.io.File;

// the purpose of this class is to provide methods for locating a file which holds movie list information
public class MovieListLocator implements FileLocator {

    private Context context;

    public MovieListLocator(Context context) {
        this.context = context;
    }

    @Override
    public File locateFile() {
        return getFile();
    }

    // returns a File object which holds a path to the xml file which contains a MovieList object
    private File getFile() {
        String filename = context.getResources().getString(R.string.movies_file);
        return new File(context.getFilesDir() + File.separator + filename);
    }
}
