package com.example.movietrackerv2.data.file_storage.writer;

import com.example.movietrackerv2.data.file_storage.locator.FileLocator;

public interface ObjectWriter<T> {
    void writeToFile(T object, FileLocator fileLocator);
}
