package com.example.movietrackerv2.data.component;

// a class which holds information about a movie theatre
public class Theatre {
    private String area;
    private String name;
    private int id;

    public Theatre(String area, String name, int id) {
        this.area = area;
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
