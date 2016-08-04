package com.example.manisharana.todoapp.Adapters;

import android.content.Context;

import com.example.manisharana.todoapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utility {


    private static final TimeZone timeZone;

    static {
        timeZone = TimeZone.getTimeZone("Asia/Calcutta");
    }

    public static String getFormattedTime(long dateInMillis) {
        Date date = new Date(dateInMillis);
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("HH:mm a");
        shortenedDateFormat.setTimeZone(timeZone);
        String day = shortenedDateFormat.format(date.getTime());
        return day;
    }

    public static String getFriendlyDayString(Context context, long dateInMillis) {
        Calendar taskCal = Calendar.getInstance(timeZone);
        taskCal.setTimeInMillis(dateInMillis);

        long currentTimeMillis = System.currentTimeMillis();

        //OverDue
        if (taskCal.getTimeInMillis() < currentTimeMillis)
            return "OverDue";
        else if (taskCal.getTimeInMillis() < getTimeInMillis(currentTimeMillis,1)) {
            return "Today";
        }else if(taskCal.getTimeInMillis() < getTimeInMillis(currentTimeMillis,2)){
            return "Tomorrow";
        }else if(taskCal.getTimeInMillis() < getUpComingSundayTimeInMillis()){
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE");
            shortenedDateFormat.setTimeZone(timeZone);
            return shortenedDateFormat.format(dateInMillis);
        }else{
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            shortenedDateFormat.setTimeZone(timeZone);
            return shortenedDateFormat.format(dateInMillis);
        }
     }

    private static long getTimeInMillis(long currentTimeMillis, int addDate) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTimeInMillis(currentTimeMillis);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH,addDate);

        return cal.getTimeInMillis();
    }

    private static long getUpComingSundayTimeInMillis() {
        Calendar currentCal = Calendar.getInstance(timeZone);
        currentCal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        currentCal.set(Calendar.HOUR_OF_DAY,0);
        currentCal.set(Calendar.MINUTE,0);
        currentCal.set(Calendar.SECOND,0);
        currentCal.add(Calendar.DATE,7);
        return currentCal.getTimeInMillis();
    }

}
