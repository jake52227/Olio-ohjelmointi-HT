package com.example.movietrackerv2.data.collection;

import com.example.movietrackerv2.data.component.Theatre;

import java.util.ArrayList;
import java.util.List;

// a class which holds Theatre objects in an arraylist
public class TheatreList {

    private ArrayList<Theatre> list;

    public TheatreList() {
        list = new ArrayList<>();
    }

    public List<Theatre> getList() {
        return list;
    }

    public void addTheatre(Theatre t) {
        list.add(t);
    }

    public void emptyList() {
        list.clear();
    }

    // extracts the names of the theatre objects in the list and returns a list of these strings
    public List<String> getTheatreNamesAsList() {
        ArrayList<String> names = new ArrayList<>();
        for (Theatre t : getList()) { names.add(t.getName()); }
        return names;
    }

    // returns true if the list contains a theatre with the given name, otherwise returns false
    public boolean hasTheatreWithName(String name) {
        for (Theatre t : getList()) {
            if (t.getName().equalsIgnoreCase(name.trim())) {
                return true;
            }
        }
        return false;
    }
}
