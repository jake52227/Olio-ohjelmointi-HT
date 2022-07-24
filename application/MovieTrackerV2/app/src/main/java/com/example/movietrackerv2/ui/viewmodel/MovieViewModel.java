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
import com.example.movietrackerv2.data.file_storage.locator.MovieListLocator;
import com.example.movietrackerv2.data.file_storage.reader.FileReaderInterface;
import com.example.movietrackerv2.data.file_storage.writer.ObjectWriter;
import com.example.movietrackerv2.data.xml_tool.DocumentLoader;
import com.example.movietrackerv2.data.xml_tool.Parser;
import com.example.movietrackerv2.data.component.Movie;
import com.example.movietrackerv2.data.component.MovieSearchFilter;
import com.example.movietrackerv2.data.file_storage.reader.FileReader;
import com.example.movietrackerv2.data.file_storage.writer.XmlObjectWriter;
import com.example.movietrackerv2.data.initializer.MovieListInitializer;
import com.example.movietrackerv2.data.collection.MovieList;
import com.thoughtworks.xstream.XStream;

import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// a view model responsible for preparing movie data
public class MovieViewModel extends AndroidViewModel implements MovieSearchInterface, SelectionInterface {

    private MutableLiveData<MovieList> mMovieList;
    private MutableLiveData<MovieList> mFilteredMovieList;
    private MovieList list;
    private MovieList filteredList;
    private MovieSearchFilter filter;

    private ObjectWriter writer;
    private FileReaderInterface reader;
    private ObjectConverter converter;

    private MutableLiveData<ArrayList<Integer>> mSelectedGenres; // a list which holds indices of selected items at genre selection dialog
    private MutableLiveData<boolean[]> mCheckedGenres;           // this is also for genre dialog

    public MovieViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<MovieList> getMovies() {
        if (mMovieList == null) {
            mMovieList = new MutableLiveData<>();
            loadMovies();
        }
        return mMovieList;
    }

    // returns a LiveData MovieList with events filtered according to MovieSearchFilter's set filters
    public LiveData<MovieList> getFilteredMovies() {
        if (mFilteredMovieList == null) {
            mFilteredMovieList = new MutableLiveData<>();
            if (mMovieList == null) {
                mMovieList = new MutableLiveData<>();
                loadMovies();
            }
            mFilteredMovieList.setValue(mMovieList.getValue());
        }
        return mFilteredMovieList;
    }


    // loads movie information. If there is no file to be found, downloads new information. Checks to see if the list needs updates.
    private void loadMovies() {
        loadMoviesFromFile(); // fetch movies from file
        boolean fetch = false;

        if (list == null) {
            list = new MovieList();
        }

        // if no file was found or the list requires an update, fetch data from web
        if (mMovieList.getValue() == null ||
                mMovieList.getValue().getLastUpdate() == null ||
                !mMovieList.getValue().getLastUpdate().equals(new SimpleDateFormat("dd.MM").format(new Date()))) {
            fetch = true;
        }


        if (fetch) {
            FinnkinoDocumentLoader loader   = new FinnkinoDocumentLoader(new DocumentLoader());
            FinnkinoDocumentLoader loader1  = new FinnkinoDocumentLoader(new DocumentLoader());
            FinnkinoDocumentParser parser   = new FinnkinoDocumentParser(new Parser());

            loader.loadScheduleDocument();
            loader1.loadEventsDocument();

            NodeList scheduleList   = parser.parseScheduleInfo(loader.getResult());
            NodeList eventList      = parser.parseEventsInfo(loader1.getResult());

            MovieListInitializer movieListInitializer = new MovieListInitializer();
            movieListInitializer.initialize(list, scheduleList, eventList);
            mMovieList.setValue(list);
            writeMovies();
        }
    }

    // reads movie list from file. If no such file was found, the value of mMovieList will be null and otherwise the list which was read.
    private void loadMoviesFromFile() {
        if (reader == null) {
            reader = new FileReader();
        }
        if (converter == null) {
            converter = new XmlObjectConverter(new XStream());
        }
        if (mMovieList == null) {
            mMovieList = new MutableLiveData<>();
        }

        FileLocator fileLocator = new MovieListLocator(getApplication().getApplicationContext());

        String content = reader.readFormFile(fileLocator);
        list = (MovieList) converter.convertToObject(content);
        mMovieList.setValue(list);
    }

    // writes movie list to file using XmlObjectWriter
    private void writeMovies() {
        if (writer == null) {
            writer = new XmlObjectWriter(new XStream());
        }
        FileLocator fileLocator = new MovieListLocator(getApplication().getApplicationContext());
        writer.writeToFile(getMovies().getValue(), fileLocator);
    }

    // sets new search filters. If something changed, updates filtered list of movies
    @Override
    public void setSearchFilters(String title, String actorFirst, String actorLast, String directorFirst, String directorLast ) {
        boolean somethingChanged = false;

        if (filter == null) {
            filter = new MovieSearchFilter();
        }

        somethingChanged = filter.setSearchFilters(title, actorFirst, actorLast, directorFirst, directorLast, getSelectedGenreNames());

        if (somethingChanged)
            updateFilteredMovies();
    }

    // updates filtered movies list to correspond to the search filters
    private void updateFilteredMovies() {
        if (mFilteredMovieList == null) {
            mFilteredMovieList = new MutableLiveData<>();
        }
        if (filteredList == null) {
            filteredList = new MovieList();
        } else {
            filteredList.emptyList();
        }

        getMovies(); // make sure there are movies in the unfiltered list first

        if (filter == null) { // if there isn't a filter in place, set value as unfiltered list and return
            filteredList.setList(getMovies().getValue().getList());
            mFilteredMovieList.setValue(filteredList);
            return;
        }

        for (Movie m : getMovies().getValue().getList()) { // else, go through list of all movies and check if a movies passes the filters
            if (checkFilters(m)) {
                filteredList.addMovie(m);
            }
        }
        mFilteredMovieList.setValue(filteredList);
    }

    @Override
    public List<Movie> geFilteredListOfMovies() {
        return getFilteredMovies().getValue().getList();
    }

    @Override
    public Movie getMovieByTitle(String title) {
        return getMovies().getValue().getMovieByTitle(title);
    }

    // returns a list of strings of titles
    @Override
    public List<String> getListOfTitles() {
        return getMovies().getValue().getListOfTitles();
    }

    // returns a list of strings of movie genres
    private List<String> getListOfGenres() {
        return getMovies().getValue().getListOfGenres();
    }

    public LiveData<ArrayList<Integer>> getSelectedGenres() {
        if (mSelectedGenres == null) {
            mSelectedGenres = new MutableLiveData<>();
            mSelectedGenres.setValue(new ArrayList<>());
        }
        return mSelectedGenres;
    }

    // returns an array of strings containing genre names that correspond to the indices of user selected genres
    private String[] getSelectedGenreNames() {
        ArrayList<String> items = new ArrayList<>();
        List<String> allGenres = getListOfGenres();
        for (Integer i : getSelectedGenres().getValue()) {
            items.add(allGenres.get(i));
        }
        return items.toArray(new String[items.size()]);
    }

    // returns a list of genres as strings
    @Override
    public List<String> getListOfOptions() {
        return getListOfGenres();
    }

    // adds an integer representing an index to the list of selected genres
    @Override
    public void addSelectedItem(int i) {
        getSelectedGenres().getValue().add(i);
    }

    // removes an integer value from the list of selected genres containing indices
    @Override
    public void removeSelectedItem(Integer i) {
        if (i == null)
            return;
        getSelectedGenres().getValue().remove(i);
    }

    // returns the list of selected genres containing indices
    @Override
    public List<Integer> getSelectionIndices() {
        if (mSelectedGenres == null) {
            mSelectedGenres = new MutableLiveData<>();
            mSelectedGenres.setValue(new ArrayList<>());
        }
        return mSelectedGenres.getValue();
    }

    // returns an observable array of booleans which indicate which genres the user has selected
    @Override
    public LiveData<boolean[]> getCheckedItems() {
        if (mCheckedGenres == null) {
            mCheckedGenres = new MutableLiveData<boolean[]>();
            mCheckedGenres.setValue(new boolean[getListOfGenres().size()]);
        }
        return mCheckedGenres;
    }

    // goes through the filter objects filters and checks if the movie fits the criteria. If it does, return true, else false.
    private boolean checkFilters(Movie movie) {

        // check title
        if (!filter.checkMovieTitleMatch(movie.getTitle())) {
            return false;
        }

        // check actor names
        if (filter.hasBothActorNamesSet()) {
            if ( !movie.hasActorName(filter.getActorFirstName(), filter.getActorLastName()) )
                return false;
        } else if (filter.hasFirstActorNameSet()) {
            if ( !movie.hasActorName(filter.getActorFirstName()))
                return false;
        } else if (filter.hasLastActorNameSet()) {
            if ( !movie.hasActorName(null, filter.getActorLastName()))
                return false;
        }

        // check director names
        if (filter.hasBothDirectorNamesSet()) {
            if ( !movie.hasDirectorName(filter.getDirectorFirstName(), filter.getDirectorLastName()) )
                return false;
        } else if (filter.hasFirstDirectorNameSet()) {
            if ( !movie.hasDirectorName(filter.getDirectorFirstName()))
                return false;
        } else if (filter.hasLastDirectorNameSet()) {
            if ( !movie.hasDirectorName(null, filter.getDirectorLastName()) )
                return false;
        }

        // check genres
        if (!filter.hasAtLeastOneOfGenres(movie.getGenresAsArray())) {
            return false;
        }

        return true;
    }
}
