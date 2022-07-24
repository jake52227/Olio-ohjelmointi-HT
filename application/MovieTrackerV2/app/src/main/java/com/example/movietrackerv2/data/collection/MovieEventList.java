package com.example.movietrackerv2.data.collection;


import com.example.movietrackerv2.data.component.MovieEvent;

import java.util.ArrayList;
import java.util.List;

// a class which holds MovieEvent objects in an ArrayList
public class MovieEventList {

    private String date;

    private ArrayList<MovieEvent> list;

    public MovieEventList() {
        list = new ArrayList<>();
    }

    public void addEvent(MovieEvent event) {
        list.add(event);
    }

    public List<MovieEvent> getList() {
        return list;
    }

    public void clearList() {
        getList().clear();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MovieEvent getEventByIndex(int index) {
        return list.get(index);
    }
}
