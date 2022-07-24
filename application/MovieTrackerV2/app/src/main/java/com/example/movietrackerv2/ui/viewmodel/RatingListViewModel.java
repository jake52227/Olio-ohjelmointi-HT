package com.example.movietrackerv2.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movietrackerv2.data.component.Rating;
import com.example.movietrackerv2.data.component.User;
import com.example.movietrackerv2.data.file_storage.converter.ObjectConverter;
import com.example.movietrackerv2.data.file_storage.converter.XmlObjectConverter;
import com.example.movietrackerv2.data.file_storage.locator.FileLocator;
import com.example.movietrackerv2.data.file_storage.locator.RatingsListLocator;
import com.example.movietrackerv2.data.file_storage.reader.FileReaderInterface;
import com.example.movietrackerv2.data.file_storage.writer.ObjectWriter;
import com.example.movietrackerv2.data.file_storage.reader.FileReader;
import com.example.movietrackerv2.data.file_storage.writer.XmlObjectWriter;
import com.example.movietrackerv2.data.collection.RatingsList;
import com.thoughtworks.xstream.XStream;

import java.util.List;


// a view model which prepares RatingsList data
public class RatingListViewModel extends AndroidViewModel implements RatingInterface {

    private MutableLiveData<RatingsList> mRatingsList;
    private RatingsList ratingsList;

    private ObjectWriter writer;
    private FileReaderInterface reader;
    private ObjectConverter converter;

    public RatingListViewModel(@NonNull Application application) {
        super(application);
    }

    // ratings are user specific so all of the methods below need the logged in user as parameter

    public LiveData<RatingsList> getRatings(User loggedInUser) {
        if (mRatingsList == null) {
            mRatingsList = new MutableLiveData<>();
        }
        loadRatings(loggedInUser);
        return mRatingsList;
    }

    @Override
    public List<Rating> getRatingsList(User loggedInUser) {
        return getRatings(loggedInUser).getValue().getList();
    }

    @Override
    public void addRating(Rating rating, User loggedInUser) {
        if (mRatingsList == null) {
            getRatings(loggedInUser);
        }
        ratingsList.addRating(rating);
        mRatingsList.setValue(ratingsList);
        writeRatings(loggedInUser);
    }

    @Override
    public void deleteRating(int id, User loggedInUser) {
        if (mRatingsList == null) {
            getRatings(loggedInUser);
        }
        ratingsList.deleteRating(id);
        mRatingsList.setValue(ratingsList);
        writeRatings(loggedInUser);
    }

    // returns an integer representing the next available id for a rating
    @Override
    public int getNextRatingId() {
        return mRatingsList.getValue().getNextId();
    }

    @Override
    public Rating getRatingByIndex(User loggedInUser, int index) {
        return getRatings(loggedInUser).getValue().getRatingByIndex(index);
    }

    // help for getting context gotten from user devDeejay's answer from here: https://stackoverflow.com/questions/51451819/how-to-get-context-in-android-mvvm-viewmodel
    // this method loads the user's ratings from file. If there was no file, create new list
    private void loadRatings(User loggedInUser) {
        if (reader == null) {
            reader = new FileReader();
        }
        if (converter == null) {
            converter = new XmlObjectConverter(new XStream());
        }

        FileLocator locator = new RatingsListLocator(getApplication().getApplicationContext(), loggedInUser);

        String content = reader.readFormFile(locator);
        ratingsList = (RatingsList) converter.convertToObject(content);
        if (ratingsList == null) {
            ratingsList = new RatingsList();
        }
        mRatingsList.setValue(ratingsList);
    }

    // calls XmlObjectWriter object and a FileLocator object to write the RatingsList object to file
    private void writeRatings(User loggedInUser) {
        if (writer == null) {
            writer = new XmlObjectWriter(new XStream());
        }
        FileLocator locator = new RatingsListLocator(getApplication().getApplicationContext(), loggedInUser);
        writer.writeToFile(mRatingsList.getValue(), locator);
    }
}
