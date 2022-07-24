package com.example.movietrackerv2.data.component;

// the purpose of this class is to hold information regarding a movie event of a movie theatre
public class MovieEvent {

    private String movieTitle;
    private String theatreName;
    private String startTime;
    private String endTime;
    private String date;

    public MovieEvent(String movieTitle, String theatreName, String startTime, String endTime, String date) {
        this.movieTitle = movieTitle;
        this.theatreName = theatreName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDate() {
        return date;
    }

}
