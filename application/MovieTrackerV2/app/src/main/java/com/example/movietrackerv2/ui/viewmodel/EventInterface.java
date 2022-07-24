package com.example.movietrackerv2.ui.viewmodel;
import com.example.movietrackerv2.data.component.MovieEvent;


import java.util.List;

public interface EventInterface {
    List<MovieEvent> getFilteredEventList();
    void applyFilters(String theatreName, String date, String startTime, String endTime);
    MovieEvent getEventByIndex(int index);
}
