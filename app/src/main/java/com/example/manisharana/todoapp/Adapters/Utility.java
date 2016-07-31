package com.example.manisharana.todoapp.Adapters;

import android.content.Context;

import com.example.manisharana.todoapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utility {

    private static final int ONE_DAY =  24 * 60 * 60 * 1000;
    private static final int ONE_WEEK = 7 * 24 * 60 * 60 * 1000;

    public static String getFormattedDateAndTime(long dateInMillis) {
        Date date = new Date(dateInMillis);
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("HH:mm");
        shortenedDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        String day = shortenedDateFormat.format(date.getTime());
        return day;
    }

    public static String getFriendlyDayString(Context context, long dateInMillis) {
        Calendar taskCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        taskCal.setTimeInMillis(dateInMillis);
        int taskDate = taskCal.get(Calendar.DATE);

        long currentTimeMillis = System.currentTimeMillis();
        Calendar currentCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        currentCal.setTimeInMillis(currentTimeMillis);
        int currentDate = currentCal.get(Calendar.DATE);


        if (taskDate == currentDate) {
            return context.getString(R.string.today);

        } else if (taskDate == currentDate + 1) {
            return context.getString(R.string.tomorrow);

        } else if (taskDate < currentDate + 7) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            dayFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
            return dayFormat.format(dateInMillis);
        } else {
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            shortenedDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
            return shortenedDateFormat.format(dateInMillis);
        }
    }

}
