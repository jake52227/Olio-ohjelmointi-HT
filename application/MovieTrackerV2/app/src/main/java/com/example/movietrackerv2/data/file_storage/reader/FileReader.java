package com.example.movietrackerv2.data.file_storage.reader;

import android.util.Log;

import com.example.movietrackerv2.data.file_storage.locator.FileLocator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
// the purpose of this class is to provide methods for reading a file's contents to a string
public class FileReader implements FileReaderInterface {

    // uses the file locator to locate a File object. Calls readFile method on the File object and returns it's result
    @Override
    public String readFormFile(FileLocator fileLocator) {
        File file = fileLocator.locateFile();
        return readFile(file);
    }

    // reads the contents from a given file to a string. Upon success returns the contents of the file as a string built by StringBuilder. Otherwise returns an empty string.
    private String readFile(File file) {
        String line = "";
        String content = "";
        try {
            FileInputStream fis         = new FileInputStream(file);
            InputStreamReader isr       = new InputStreamReader(fis);
            BufferedReader bufReader    = new BufferedReader(isr);
            StringBuilder stringbuilder = new StringBuilder();

            while ( (line = bufReader.readLine()) != null)
                stringbuilder.append(line);

            isr.close();
            content = stringbuilder.toString();

        } catch (IOException exception) {
            Log.e(null, "error at readFile, FileReader");
        } catch (NullPointerException e) {
            return null;
        }
        return content;
    }
}
