package com.example.movietrackerv2.data.api.finnkino;

import com.example.movietrackerv2.data.xml_tool.AsyncDownloader;
import com.example.movietrackerv2.data.xml_tool.LoaderInterface;

import org.w3c.dom.Document;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// The purpose of this class is to download Finnkino's xml documents asynchronously
public class FinnkinoDocumentLoader extends AsyncDownloader {

    private final String theatresURL = "https://www.finnkino.fi/xml/TheatreAreas/";
    private final String scheduleURL = "https://www.finnkino.fi/xml/Schedule/";
    private final String eventsURL = "https://www.finnkino.fi/xml/Events/";

    public FinnkinoDocumentLoader(LoaderInterface loader) {
        super(loader);
    }

    // this method gets the result of the async task. On success, returns a document object with the result. Else, returns null
    public Document getResult() {
        Document doc = null;
        try {
            // waits at most 15 seconds for the result
            doc = get(15, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public void loadEventsDocument() { execute(eventsURL); }

    // calls the async task class to load a document from the given address.
    public void loadTheatreDocument() {
        execute(theatresURL);
    }

    public void loadScheduleDocument() {
        execute(scheduleURL);
    }

    // fetches a schedule document from the given date
    public void loadScheduleDocument(String date) {
        if (date == null) {
            loadScheduleDocument();
        } else {
            execute(scheduleURL+"?dt="+date);
        }
    }
}
