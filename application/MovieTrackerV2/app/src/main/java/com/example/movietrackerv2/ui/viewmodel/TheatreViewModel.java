package com.example.movietrackerv2.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.movietrackerv2.data.api.finnkino.FinnkinoDocumentLoader;
import com.example.movietrackerv2.data.api.finnkino.FinnkinoDocumentParser;
import com.example.movietrackerv2.data.file_storage.converter.ObjectConverter;
import com.example.movietrackerv2.data.file_storage.converter.XmlObjectConverter;
import com.example.movietrackerv2.data.file_storage.locator.FileLocator;
import com.example.movietrackerv2.data.file_storage.locator.TheatreListLocator;
import com.example.movietrackerv2.data.file_storage.reader.FileReaderInterface;
import com.example.movietrackerv2.data.file_storage.writer.ObjectWriter;
import com.example.movietrackerv2.data.xml_tool.DocumentLoader;
import com.example.movietrackerv2.data.xml_tool.Parser;
import com.example.movietrackerv2.data.file_storage.reader.FileReader;
import com.example.movietrackerv2.data.file_storage.writer.XmlObjectWriter;
import com.example.movietrackerv2.data.initializer.TheatreListInitializer;
import com.example.movietrackerv2.data.collection.TheatreList;
import com.thoughtworks.xstream.XStream;

import org.w3c.dom.NodeList;

import java.util.List;

// a view model which prepares TheatreList data
public class TheatreViewModel extends AndroidViewModel implements TheatreInterface {
    private MutableLiveData<TheatreList> mTheatreList;
    private TheatreList list;

    private FileReaderInterface reader;
    private ObjectWriter writer;
    private ObjectConverter converter;

    public TheatreViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<TheatreList> getTheatres() {
        if (mTheatreList == null) {
            mTheatreList = new MutableLiveData<>();
            loadTheatres();
        }
        return mTheatreList;
    }

    // loads theatres data from file. If no file was found, fetch the data from the web
    private void loadTheatres() {

        loadTheatresFromFile();
        if (mTheatreList.getValue() == null) {
            list = new TheatreList();
            FinnkinoDocumentLoader loader = new FinnkinoDocumentLoader(new DocumentLoader());
            FinnkinoDocumentParser parser = new FinnkinoDocumentParser(new Parser());
            loader.loadTheatreDocument();
            NodeList theatreInfo = parser.parseTheatreInfo(loader.getResult());

            TheatreListInitializer initializer = new TheatreListInitializer();
            initializer.initialize(list, theatreInfo);
            mTheatreList.setValue(list);
        }
        writeTheatreTable();
    }

    // reads the theatre list from a file. If no file was found, implicitly sets the value of mTheatreList to null. Otherwise the value is the object which was read from file
    private void loadTheatresFromFile() {
        if (reader == null) {
            reader = new FileReader();
        }
        if (converter == null) {
            converter = new XmlObjectConverter(new XStream());
        }

        FileLocator fileLocator = new TheatreListLocator(getApplication().getApplicationContext());

        String content = reader.readFormFile(fileLocator);
        list = (TheatreList) converter.convertToObject(content);
        mTheatreList.setValue(list);
    }

    // writes the value of mTheatreList to file.
    private void writeTheatreTable() {
        if (writer == null) {
            writer = new XmlObjectWriter(new XStream());
        }

        FileLocator fileLocator = new TheatreListLocator(getApplication().getApplicationContext());

        writer.writeToFile(mTheatreList.getValue(), fileLocator);
    }

    @Override
    public List<String> getTheatreNamesAsList() {
        return getTheatres().getValue().getTheatreNamesAsList();
    }
}
