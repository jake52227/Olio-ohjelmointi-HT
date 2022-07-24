package com.example.movietrackerv2.data.utility;

public interface TimeMethodsInterface {
    String parseTime(String time);
    Integer timeToMinutes(String time);
    String formatDate(String date);
    String[] formatTimeAndDate(String timeAndDate);
    int[] getCurrentDateNumbers();
    int[] splitDate(String date);
}
