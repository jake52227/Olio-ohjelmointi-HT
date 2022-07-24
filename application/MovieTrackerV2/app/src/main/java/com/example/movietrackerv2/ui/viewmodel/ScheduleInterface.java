package com.example.movietrackerv2.ui.viewmodel;

import com.example.movietrackerv2.data.component.MovieEvent;
import com.example.movietrackerv2.data.component.User;

import java.util.List;

public interface ScheduleInterface {
    List<String> getListOfEventDatesByMonth(int year, int month, User loggedInUser);
    List<String> getEventsAsStringByDate(int year, int month, int dayOfMonth);
    List<String> getInitialContent(User loggedInUser);
    boolean addEvent(MovieEvent selectedEvent, User loggedInUser);
}
