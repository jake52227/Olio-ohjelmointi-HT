package com.example.movietrackerv2.data.component;

import java.util.Arrays;

// the purpose of this class is to set and store filters on what to show to the user in the "Search Movies" tab recyclerview
public class MovieSearchFilter {
    private String movieTitle;
    private String actorFirstName;
    private String actorLastName;
    private String directorFirstName;
    private String directorLastName;
    private String[] genres;


    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getActorFirstName() {
        return actorFirstName;
    }

    public void setActorFirstName(String actorFirstName) {
        this.actorFirstName = actorFirstName;
    }

    public String getActorLastName() {
        return actorLastName;
    }

    public void setActorLastName(String actorLastName) {
        this.actorLastName = actorLastName;
    }

    public String getDirectorFirstName() {
        return directorFirstName;
    }

    public void setDirectorFirstName(String directorFirstName) {
        this.directorFirstName = directorFirstName;
    }

    public String getDirectorLastName() {
        return directorLastName;
    }

    public void setDirectorLastName(String directorLastName) {
        this.directorLastName = directorLastName;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public boolean hasBothActorNamesSet() {
        return !getActorFirstName().isEmpty() && !getActorLastName().isEmpty();
    }

    public boolean hasBothDirectorNamesSet() {
        return !getDirectorFirstName().isEmpty() && !getDirectorLastName().isEmpty();
    }

    public boolean hasFirstActorNameSet() {
        return !getActorFirstName().isEmpty();
    }

    public boolean hasLastActorNameSet() {
        return !getActorLastName().isEmpty();
    }

    public boolean hasFirstDirectorNameSet() {
        return !getDirectorFirstName().isEmpty();
    }

    public boolean hasLastDirectorNameSet() {
        return !getDirectorLastName().isEmpty();
    }

    // returns true if compareTo equals the currently set movie title of the filter or if the current title is empty. Otherwise returns false. Ignores case
    public boolean checkMovieTitleMatch(String compareTo) {
        if (getMovieTitle().isEmpty())
            return true;
        return getMovieTitle().equalsIgnoreCase(compareTo);
    }

    // checks the given array of strings for a match against at least one of the filters own genres. If a match was found, return true, otherwise returns false
    public boolean hasAtLeastOneOfGenres(String[] genres) {
        if ((getGenres() == null) || (getGenres().length == 0))
            return true;

        boolean foundMatch = false;
        for (String s : genres) {
            if ( Arrays.asList(getGenres()).contains(s.trim()) ) {
                foundMatch = true;
            }
        }
        return foundMatch;
    }


    // this method sets the given filters and checks if something changes from previous configuration. Returns true if something does change and false otherwise.
    public boolean setSearchFilters(String title, String actorFirst, String actorLast, String directorFirst, String directorLast, String[] genres) {
        boolean somethingChanged = false;

        // movie title
        if (checkStringDiff(getMovieTitle(), title)) {
            somethingChanged = true;
        }

        if ( title == null || title.isEmpty() ) {
            setMovieTitle("");
        }  else {
            setMovieTitle(title.trim());
        }

        // actor first
        if (checkStringDiff(getActorFirstName(), actorFirst)) {
            somethingChanged = true;
        }
        if ( actorFirst == null || actorFirst.isEmpty() ) {
            setActorFirstName("");
        } else {
            setActorFirstName(actorFirst.trim());
        }

        // actor last
        if (checkStringDiff(getActorLastName(), actorLast)) {
            somethingChanged = true;
        }
        if ( actorLast == null || actorLast.isEmpty() ) {
            setActorLastName("");
        } else {
            setActorLastName(actorLast.trim());
        }

        // director first
        if (checkStringDiff(getDirectorFirstName(), directorFirst)) {
            somethingChanged = true;
        }
        if (directorFirst == null || directorFirst.isEmpty()) {
            setDirectorFirstName("");
        } else {
            setDirectorFirstName(directorFirst.trim());
        }

        // director last
        if (checkStringDiff(getDirectorLastName(), directorLast)) {
            somethingChanged = true;
        }
        if (directorLast == null || directorLast.isEmpty()) {
            setDirectorLastName("");
        } else {
            setDirectorLastName(directorLast.trim());
        }

        // genre
        if (checkGenreDiff(genres)) {
            somethingChanged = true;
        }

        return somethingChanged;
    }

    // a helper method for comparing strings. If  the strings are not equal, returns true, else returns false. Ignores case and whitespace
    private boolean checkStringDiff(String oldString, String newString) {
        if (oldString == null) {
            return true;
        } else return !oldString.trim().equalsIgnoreCase(newString.trim());
    }

    // a helper method for comparing old and new genre array. If something changes, returns true, else returns false
    private boolean checkGenreDiff(String[] newArr) {

        if (newArr == null || newArr.length == 0) {
            setGenres(newArr);
            return true;
        }

        if (getGenres() == null) {
            setGenres(newArr);
            return true;
        }

        if (!Arrays.equals(newArr, getGenres())) {
            setGenres(newArr);
            return true;
        }
        return false;
    }

}