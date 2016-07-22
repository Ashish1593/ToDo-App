package com.example.manisharana.todoapp.Adapters;

import java.text.SimpleDateFormat;

public class Utility {
    public static String getFormattedDate(long time){
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        String day = shortenedDateFormat.format(time);
        return day;
    }
}
