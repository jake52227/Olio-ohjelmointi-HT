package com.example.movietrackerv2.data.utility;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// the purpose of this class is to provide time related utility methods
public class TimeUtility implements TimeMethodsInterface {

    @Override
    public String parseTime(String time) {
        return getParsedTime(time);
    }

    @Override
    public Integer timeToMinutes(String time) {
        return getTimeInMinutes(time);
    }

    @Override
    public String formatDate(String date) {
        return getFormattedDate(date);
    }

    @Override
    public String[] formatTimeAndDate(String timeAndDate) {return parseTimeAndDate(timeAndDate);}

    // returns an integer array [day, month, year] corresponding to the current date
    @Override
    public int[] getCurrentDateNumbers() {
        int[] nums = {-1, -1, -1};
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        nums = splitDate(date);
        return nums;
    }

    // splits a date into it's day, month and year parts. Returns these as an integer array
    @Override
    public int[] splitDate(String date) {
        int nums[] = {-1, -1, -1};
        try {
            String parts[] = date.split("\\.");
            nums[0] = Integer.parseInt(parts[0]);
            nums[1] = Integer.parseInt(parts[1]);
            nums[2] = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            Log.e(null, "Number format exception at splitDate, UserScheduleViewModel");
        }
        return nums;
    }


    /* This function takes a string representing date and turns it to the form dd.mm.YYYY.
    If no valid date can be extracted, returns an empty string */
    private String getFormattedDate(String date) {

        if (date.isEmpty()) {
            return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        }

        String formatted = "";
        // Help for implementing this gotten from here: https://www.w3schools.com/java/java_regex.asp
        /*
        Possible valid combinations:
        dd/mm/YYYY
        dd.mm/YYYY
        dd/mm.YYYY
        dd.mm.YYYY

        d.mm.YYYY
        dd.m.YYYY
        d.m.YYYY
         */

        String[] firstSeparator = {"\\.", "/"};
        String[] secondSeparator = {"\\.", "/"};
        // go through combinations:
        Pattern pattern;
        Matcher matcher;
        for (String s : firstSeparator) {
            for (String s1 : secondSeparator) {
                String toMatch = "[0-9]+" + s + "[0-9]+" + s1 + "....";
                pattern = Pattern.compile(toMatch);
                matcher = pattern.matcher(date);
                if ( matcher.find() ) { // valid combination found. Send it to helper method with separators and break loop
                    formatted = formatDateHelper(date, s, s1);
                    break;
                }
            }
        }
        return formatted;
    }

    // a helper method which takes the user given date and formats it to the form dd.mm.YYYY. Returns the formatted string
    private String formatDateHelper(String date, String firstSeparator, String secondSeparator) {
        // set up arrays for holding parts of the date string separated by either firstSeparator or secondSeparator
        String[] firstParts;
        String[] secondParts;
        StringBuilder finalDate = new StringBuilder();


        firstParts = date.split(firstSeparator);
        // go through first parts
        for (int i = 0; i < firstParts.length - 1; i++) {

            // if the day or month or both contain just one number, a "0" needs to be added
            if (firstParts[i].length() < 2) {
                finalDate.append("0").append(firstParts[i]).append(".");
            } else if (firstParts[i].length() == 2) {
                finalDate.append(firstParts[i]).append(".");
            } else {
                // if the length is greater than two, pick only the first two numbers.
                finalDate.append(firstParts[i].substring(0, 2)).append(".");
            }
        }

        // if the separators are the same, the first loop handled both the day and month
        if (!firstSeparator.equals(secondSeparator)) {

            // otherwise, split again.
            secondParts = date.split(secondSeparator);

            // the second part also contains the first separator: dd.mm/YYYY -> split "/" -> [ dd.mm, YYYY ]
            String[] fistPartOfSecond = secondParts[0].split(firstSeparator); // [ dd, mm ]
            // the first loop added the date, so just check the month
            if (fistPartOfSecond[1].length() < 2) {
                finalDate.append("0").append(fistPartOfSecond[1]).append(".");
            } else if (fistPartOfSecond[1].length() == 2) {
                finalDate.append(fistPartOfSecond[1]).append(".");
            } else {
                finalDate.append(fistPartOfSecond[1].substring(0, 2)).append(".");
            }

            // and finally add the year
            finalDate.append(secondParts[secondParts.length - 1]);

        } else {
            finalDate.append(firstParts[firstParts.length - 1]);
        }

        return finalDate.toString();
    }


    // takes a string representing time with a ":" as a separator. Returns a formatted version of the form HH:MM of the time string.
    // If no valid time could be extracted, returns an empty string
    private String getParsedTime(String time) {
        String[] parts;
        String parsed = "";

        if (time.contains(":")) {
            parts = time.split(":");
        } else {
            return "";
        }

        if (parts[0].length() < 2) {
            parsed += "0"+parts[0];
        } else {
            parsed += parts[0];
        }

        parsed += ":" + parts[1];

        return parsed;
    }

    // takes a string representing time in the form 10:30 and returns an integer number of the minutes
    private Integer getTimeInMinutes(String time) {
        String[] parts = time.split(":");
        if (parts.length == 0) {
            return null;
        }

        int hoursToMin;
        int min;

        try {
            hoursToMin = Integer.parseInt(parts[0]) * 60;
            if (parts.length >= 2) {
                min = Integer.parseInt(parts[1]);
            } else {
                min = 0;
            }
        } catch (NumberFormatException e) {
            Log.e(null, "Number format exception at TimeUtility, timeToMinutes. Given string: " + time);
            return 0;
        }

        return hoursToMin + min;
    }

    // from this: 2022-07-05T10:30:00 to this: [10:30, 05.07.2022]. Returns this array or null on failure.
    private String[] parseTimeAndDate(String timeAndDate) {
        String[] parts = timeAndDate.split("T");    // [2022-07-05 , 10:30:00]
        if (parts.length != 2) {
            return null;
        }
        String[] dateParts = parts[0].split("-");   // [2022, 07, 05]
        String[] timeParts = parts[1].split(":");   // [10, 30, 00]

        if ((dateParts.length != 3) || (timeParts.length != 3)) {
            return null;
        }

        String[] result = new String[2];

        result[0] = timeParts[0] + ":" + timeParts[1];  // 10:30
        result[1] = dateParts[2] + "." + dateParts[1] + "." +dateParts[0];  // 05.07.2022
        return result;
    }
}
