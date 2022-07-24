package com.example.movietrackerv2.ui.viewmodel;

import com.example.movietrackerv2.data.collection.RatingsList;
import com.example.movietrackerv2.data.component.Rating;
import com.example.movietrackerv2.data.component.User;

import java.util.List;

public interface RatingInterface {
    List<Rating> getRatingsList(User loggedInUser);
    void addRating(Rating rating, User loggedInUser);
    void deleteRating(int id, User loggedInUser);
    int getNextRatingId();
    Rating getRatingByIndex(User loggedInUser, int index);
}
