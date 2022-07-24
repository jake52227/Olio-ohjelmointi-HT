package com.example.movietrackerv2.data.component;
import com.example.movietrackerv2.data.utility.TimeMethodsInterface;

// the purpose of this class is to set and store filters on what to show to the user in the "Movie Events" tab recyclerview
public class MovieEventFilter {
    private String theatreName;
    private String date;
    private String startTime;
    private String endTime;


    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    // this method sets the given filters and checks if anything changes from previous configuration.
    // Returns true if something does change and false otherwise. Uses TimeMethodsInterface to format date strings
    public boolean setEventFilters(String theatreName, String date, String startTime, String endTime, TimeMethodsInterface timeUtil) {
        boolean somethingChanged = false;

        // theatre
        if (doCheck(getTheatreName(), theatreName)) {
            somethingChanged = true;
        }
        setTheatreName(theatreName);

        // date
        String formattedDate = timeUtil.formatDate(date);

        if (doCheck(getDate(), formattedDate)) {
            somethingChanged = true;
        }
        setDate(formattedDate);


        // starting time
        String formattedStartTime = timeUtil.parseTime(startTime);
        if (formattedStartTime.trim().isEmpty())
            formattedStartTime = "00:00"; // if the starting time is empty, set start to 00:00

        if (doCheck(getStartTime(), formattedStartTime)) {
            somethingChanged = true;
        }
        setStartTime(formattedStartTime);

        // ending time
        String formattedEndTime = timeUtil.parseTime(endTime);
        if (formattedEndTime.trim().isEmpty())
            formattedEndTime = "23:59"; // if the ending time is empty, set end to 23:59

        if (doCheck(getEndTime(), formattedEndTime)) {
            somethingChanged = true;
        }
        setEndTime(formattedEndTime);

        return somethingChanged;
    }

    // returns true if oldS is null or if oldS and newS are not equal ignoring case.
    private boolean doCheck(String oldS, String newS) {
        if (oldS == null) {
            return true;
        } else return !oldS.equalsIgnoreCase(newS);
    }

}
