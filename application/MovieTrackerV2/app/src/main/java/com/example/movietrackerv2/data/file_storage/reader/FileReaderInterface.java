package com.example.movietrackerv2.data.file_storage.reader;

import com.example.movietrackerv2.data.file_storage.locator.FileLocator;

public interface FileReaderInterface<T> {
    String readFormFile(FileLocator fileLocator);
}
