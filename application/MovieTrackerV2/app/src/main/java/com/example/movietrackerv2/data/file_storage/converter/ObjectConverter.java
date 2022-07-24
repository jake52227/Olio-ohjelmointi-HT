package com.example.movietrackerv2.data.file_storage.converter;

public interface ObjectConverter<T> {
    T convertToObject(String content);
}
