package com.example.movietrackerv2.ui.viewmodel;

import com.example.movietrackerv2.data.component.Movie;

import java.util.List;

public interface MovieSearchInterface {
    void setSearchFilters(String title, String actorFirst, String actorLast, String directorFirst, String directorLast);
    List<Movie> geFilteredListOfMovies();
    Movie getMovieByTitle(String title);
    List<String> getListOfTitles();
}
