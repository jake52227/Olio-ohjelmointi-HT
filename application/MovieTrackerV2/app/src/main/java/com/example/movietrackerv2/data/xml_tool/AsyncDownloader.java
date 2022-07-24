package com.example.movietrackerv2.data.xml_tool;

import android.os.AsyncTask;

import org.w3c.dom.Document;

// source for async task: https://developer.android.com/reference/android/os/AsyncTask

// The purpose of this class is to perform a LoaderInterface implementing class's task asynchronously.
public class AsyncDownloader extends AsyncTask<String, Integer, Document> {

    private LoaderInterface loader;
    private Document doc;

    public AsyncDownloader(LoaderInterface loader) {
        this.loader = loader;
    }

    // Executes Loader's readToDocument method asynchronously. Only uses the first url string if more than one is given. Returns the document or null on failure
    @Override
    protected Document doInBackground(String... urls) {
        doc = loader.readToDocument(urls[0]);
        return doc;
    }
}
