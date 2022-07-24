package com.example.movietrackerv2.data.collection;

import com.example.movietrackerv2.data.component.Movie;

import java.util.ArrayList;
import java.util.List;

// a class which holds Movie objects in an arraylist
public class MovieList {

    private List<Movie> list;
    private String lastUpdate; // a date of the form dd.mm used for checking if the list should be updated

    public MovieList() {
        list = new ArrayList<>();
    }

    public List<Movie> getList() {
        return list;
    }

    public void setList(List<Movie> list) {
        this.list = list;
    }

    public void addMovie(Movie movie) {
        if (movie == null || movie.getTitle() == null)
            return;
        list.add(movie);
    }

    // returns a movie object matching the given title
    public Movie getMovieByTitle(String title) {
        for (Movie m : getList()) {
            if (m.getTitle().equalsIgnoreCase(title)) {
                return m;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return getList().isEmpty();
    }

    // if a movie object in the array list contains the given title, returns true, else returns false
    public boolean hasTitle(String title) {
        for (Movie m : getList()) {
            if (m.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }

    public void emptyList() {
        list.clear();
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    // returns a list of titles of the movies held in the movie list
    public List<String> getListOfTitles() {
        ArrayList<String> titleArray = new ArrayList<>();
        for (Movie m : getList()) {
            titleArray.add(m.getTitle());
        }
        return titleArray;
    }

    // returns a list of strings of unique genres found in the movie list
    public List<String> getListOfGenres() {
        ArrayList<String> list = new ArrayList<>();
        for (Movie m : getList()) {
            String genres = m.getGenres();
            String[] split = genres.split(","); // separate genres to an array
            for (String s : split) {
                if (!list.contains(s.trim())) { // if list does not contain the genre, add it
                    list.add(s.trim());
                }
            }
        }
        return list;
    }
}
