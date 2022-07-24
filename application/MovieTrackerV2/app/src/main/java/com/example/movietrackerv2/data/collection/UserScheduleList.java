package com.example.movietrackerv2.data.collection;

import com.example.movietrackerv2.data.component.MovieEvent;

import java.util.ArrayList;

// a class which holds MovieEvent objects in an ArrayList. These events are for the user's calendar view
public class UserScheduleList {

    private ArrayList<MovieEvent> eventList;

    public ArrayList<MovieEvent> getEventList() {
        return this.eventList;
    }

    public UserScheduleList() {
        eventList = new ArrayList<>();
    }

    public void addEvent(MovieEvent event) {
        if (event == null)
            return;
        eventList.add(event);
    }

    public boolean containsEvent(MovieEvent event) {
        return eventList.contains(event);
    }

}
