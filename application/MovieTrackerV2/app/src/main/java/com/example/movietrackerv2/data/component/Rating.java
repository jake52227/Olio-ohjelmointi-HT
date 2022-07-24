package com.example.movietrackerv2.data.component;

import java.text.SimpleDateFormat;
import java.util.Date;
// the purpose of this class is to hold data about a user given movie rating
public class Rating {

    private int id;
    private float stars;
    private Movie movie;
    private String comment;
    private String date;

    public Rating(int id, float stars, Movie movie, String comment) {
        this.id = id;
        this.stars = stars;
        this.movie = movie;
        this.comment = comment;
        setDate();
    }

    // sets the current date for the rating object. Only used when creating the object
    private void setDate() {
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getStars() {
        return stars;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

}
