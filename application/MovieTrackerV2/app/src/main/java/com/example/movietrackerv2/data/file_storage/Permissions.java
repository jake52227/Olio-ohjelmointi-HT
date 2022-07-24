package com.example.movietrackerv2.data.file_storage;

import com.example.movietrackerv2.data.component.Actor;
import com.example.movietrackerv2.data.component.Director;
import com.example.movietrackerv2.data.component.Movie;
import com.example.movietrackerv2.data.component.MovieEvent;
import com.example.movietrackerv2.data.component.Person;
import com.example.movietrackerv2.data.component.Rating;
import com.example.movietrackerv2.data.component.Theatre;
import com.example.movietrackerv2.data.component.User;
import com.example.movietrackerv2.data.collection.MovieList;
import com.example.movietrackerv2.data.collection.RatingsList;
import com.example.movietrackerv2.data.collection.TheatreList;
import com.example.movietrackerv2.data.collection.UserList;
import com.example.movietrackerv2.data.collection.UserScheduleList;
import com.thoughtworks.xstream.XStream;

// The purpose of this class is to provide a method for setting permissions to an xstream object
public class Permissions {
    // gives xstream permissions to write the specified types of classes
    public static void setPermissions(XStream xstream) {
        xstream.allowTypes(new Class[] {
                Rating.class,
                Movie.class,
                Theatre.class,
                RatingsList.class,
                MovieList.class,
                TheatreList.class,
                UserList.class,
                User.class,
                Person.class,
                UserScheduleList.class,
                MovieEvent.class,
                Actor.class,
                Director.class
        });
    }
}
