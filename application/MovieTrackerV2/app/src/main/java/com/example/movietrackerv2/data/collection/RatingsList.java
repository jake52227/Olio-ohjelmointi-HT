package com.example.movietrackerv2.data.collection;

import com.example.movietrackerv2.data.component.Rating;

import java.util.ArrayList;

// a class which holds Rating objects in an ArrayList
public class RatingsList {
    private ArrayList<Rating> list;

    public RatingsList() {
        list = new ArrayList<>();
    }

    public void addRating(Rating rating) {
        list.add(rating);
    }

    public ArrayList<Rating> getList() {
        return list;
    }

    public void setList(ArrayList<Rating> list) {
        this.list = list;
    }

    // Checks the list for rating ids. Returns the value of largest id integer + 1
    public int getNextId() {
        int latest = 0;
        for (Rating r : getList()) {
            if (r.getId() > latest) {
                latest = r.getId();
            }
        }
        return latest + 1;
    }

    // deletes a rating based on id
    public void deleteRating(int id) {
        for (int i = 0; i < getList().size(); i++) {
            if (getList().get(i).getId() == id) {
                getList().remove(i);
                break;
            }
        }
    }

    public Rating getRatingByIndex(int index) {
        if (getList().size() <= index) {
            return null;
        }
        return getList().get(index);
    }

}
