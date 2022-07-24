package com.example.movietrackerv2.ui.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movietrackerv2.data.api.finnkino.FinnkinoDocumentLoader;
import com.example.movietrackerv2.data.api.finnkino.FinnkinoDocumentParser;
import com.example.movietrackerv2.data.component.MovieEventFilter;
import com.example.movietrackerv2.data.utility.TimeMethodsInterface;
import com.example.movietrackerv2.data.xml_tool.DocumentLoader;
import com.example.movietrackerv2.data.xml_tool.Parser;
import com.example.movietrackerv2.data.utility.TimeUtility;
import com.example.movietrackerv2.data.component.MovieEvent;
import com.example.movietrackerv2.data.initializer.EventListInitializer;
import com.example.movietrackerv2.data.collection.MovieEventList;

import org.w3c.dom.NodeList;

import java.util.List;

// a view model for preparing movie event data
public class MovieEventViewModel extends ViewModel implements EventInterface {

    private MutableLiveData<MovieEventList> mEventListUnfiltered;
    private MutableLiveData<MovieEventList> mEventListFiltered;
    private MovieEventList list;
    private MovieEventList listFiltered;
    private MovieEventFilter filter;
    private TimeMethodsInterface timeUtil;

    @Override
    public List<MovieEvent> getFilteredEventList() {
        return getFilteredEvents().getValue().getList();
    }

    // applies user given filters for the shown content
    @Override
    public void applyFilters(String theatreName, String date, String startTime, String endTime) {
        if (filter == null)
            filter = new MovieEventFilter();

        if (timeUtil == null)
            timeUtil = new TimeUtility();

        boolean somethingChanged = filter.setEventFilters(theatreName, date, startTime, endTime, timeUtil);

        if (somethingChanged)
            setFilteredEvents();
    }

    // sets movie events to mEventListFiltered based on the filters set in the MovieEventFilter object
    private void setFilteredEvents() {
        if (mEventListFiltered == null) {
            mEventListFiltered = new MutableLiveData<>();
        }
        if (listFiltered == null) {
            listFiltered = new MovieEventList();
        }

        // If the given date is different, we have to fetch a new event list corresponding to the given date
        if ( !getEvents().getValue().getDate().equals(filter.getDate()) ) {
            mEventListUnfiltered.getValue().clearList();
            mEventListUnfiltered = null;
            getEvents( filter.getDate() );
        }

        // add events which pass the check to the filtered list
        listFiltered.clearList();
        for (MovieEvent me : getEvents().getValue().getList()) {
            if (checkFilters(me))
                listFiltered.addEvent(me);
        }
        mEventListFiltered.setValue(listFiltered);
    }


    // gets a list of filtered events
    public LiveData<MovieEventList> getFilteredEvents() {
        if (mEventListFiltered == null) {
            mEventListFiltered = new MutableLiveData<>();
        }
        if (listFiltered == null) {
            listFiltered = new MovieEventList();
        }
        mEventListFiltered.setValue(listFiltered);
        return mEventListFiltered;
    }

    // checks if the given movie event passes the filters
    private boolean checkFilters(MovieEvent me) {

        // check theatre name
        if ( !me.getTheatreName().equalsIgnoreCase(filter.getTheatreName()) )
            return false;

        // check date
        if ( !me.getDate().equals(filter.getDate()) )
            return false;

        // check time interval
        Integer filterStartMin  = timeUtil.timeToMinutes( filter.getStartTime() );
        Integer filterEndMin    = timeUtil.timeToMinutes( filter.getEndTime() );
        Integer movieStartMin   = timeUtil.timeToMinutes( me.getStartTime() );
        Integer movieEndMin     = timeUtil.timeToMinutes( me.getEndTime() );

        if ( !( (movieStartMin >= filterStartMin) && (movieEndMin <= filterEndMin) ) )
            return false;

        return true;
    }

    public LiveData<MovieEventList> getEvents() {
        if (mEventListUnfiltered == null) {
            mEventListUnfiltered = new MutableLiveData<>();
            loadEvents(null);
        }
        return mEventListUnfiltered;
    }

    @Override
    public MovieEvent getEventByIndex(int index) {
        return getFilteredEvents().getValue().getEventByIndex(index);
    }

    // returns a LiveData list of events from the given date
    public LiveData<MovieEventList> getEvents(String date) {
        if (mEventListUnfiltered == null) {
            mEventListUnfiltered = new MutableLiveData<>();
            loadEvents(date);
        }
        return mEventListUnfiltered;
    }

    // calls FinnkinoDocumentLoader to load new event document. Call EventListInitializer to initialize a new event list. Set the unfiltered list's value to the new list
    private void loadEvents(String date) {

        if (list == null) {
            list = new MovieEventList();
        }
        FinnkinoDocumentParser parser = new FinnkinoDocumentParser(new Parser());
        FinnkinoDocumentLoader loader = new FinnkinoDocumentLoader(new DocumentLoader());
        loader.loadScheduleDocument(date);
        NodeList scheduleInfo = parser.parseScheduleInfo(loader.getResult());

        EventListInitializer initializer = new EventListInitializer(new TimeUtility());
        initializer.initialize(list, scheduleInfo);
        mEventListUnfiltered.setValue(list);
    }
}
