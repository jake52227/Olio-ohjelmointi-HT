package com.example.movietrackerv2.data.component;

import java.util.ArrayList;

// the purpose of this class is to hold information regarding a movie and provide related methods
public class Movie {
    private String title;
    private String genres;
    private ArrayList<Actor> actors;
    private ArrayList<Director> directors;

    public Movie(String title, String genres) {
        this.title = title;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getGenres() {
        return genres;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public ArrayList<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(ArrayList<Director> directors) {
        this.directors = directors;
    }

    // returns genres as an array of strings
    public String[] getGenresAsArray() {
        return getGenres().split(",");
    }

    // returns a string representation of the actors. The actor names will be separated by a comma ',' and a space
    public String getActorNamesAsString() {
        if (getActors() == null || getActors().isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < getActors().size() - 1; i++) {
            sb.append(getActors().get(i).getFirstName()).append(" ").append(getActors().get(i).getLastName()).append(", ");
        }

        sb.append(getActors().get(i).getFirstName()).append(" ").append(getActors().get(i).getLastName());

        return sb.toString();
    }

    // returns a string representation of the directors. The director names will be separated by a comma ',' and a space
    public String getDirectorNamesAsString() {
        if (getDirectors() == null || getDirectors().isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < getDirectors().size() - 1; i++) {
            sb.append(getDirectors().get(i).getFirstName()).append(" ").append(getDirectors().get(i).getLastName()).append(", ");
        }
        sb.append(getDirectors().get(i).getFirstName()).append(" ").append(getDirectors().get(i).getLastName());
        return sb.toString();
    }

    // checks to see if this movie has an actor with the given first name. Returns true if found, else returns false
    public boolean hasActorName(String firstName) {
        if (firstName == null) {
            return false;
        }

        for (Person p : getActors()) {
            if (p.getFirstName().equalsIgnoreCase(firstName)) {
                return true;
            }
        }
        return false;
    }

    // checks to see if this movie has an actor with the given first and last name. Returns true if found, else returns false
    // if the first name is null, the search considers only the last name
    public boolean hasActorName(String firstName, String lastName) {
        if (lastName == null) {
            return false;
        }

        for (Actor a : getActors()) {
            if (firstName != null) {
                if (a.getFirstName().equalsIgnoreCase(firstName) && a.getLastName().equalsIgnoreCase(lastName)) {
                    return true;
                }
            } else {
                if (a.getLastName().equalsIgnoreCase(lastName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasDirectorName(String firstName) {
        if (firstName == null) {
            return false;
        }

        for (Director d : getDirectors()) {
            if (d.getFirstName().equalsIgnoreCase(firstName)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDirectorName(String firstName, String lastName) {
        if (lastName == null) {
            return false;
        }

        for (Director d : getDirectors()) {
            if (firstName != null) {
                if (d.getFirstName().equalsIgnoreCase(firstName) && d.getLastName().equalsIgnoreCase(lastName)) {
                    return true;
                }
            } else {
                if (d.getLastName().equalsIgnoreCase(lastName)) {
                    return true;
                }
            }
        }
        return false;
    }


}