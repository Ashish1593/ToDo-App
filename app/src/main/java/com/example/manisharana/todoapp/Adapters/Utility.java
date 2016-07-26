package com.example.manisharana.todoapp.Adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utility {

    public static String getFormattedDateAndTime(long dateInMillis) {
        Date date = new Date(dateInMillis);
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd, HH:mm");
        shortenedDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        String day = shortenedDateFormat.format(date.getTime());
        return day;
    }
}
