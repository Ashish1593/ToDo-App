package com.example.manisharana.todoapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Calendar.getInstance;

public class Utility {


    private static final TimeZone timeZone;
    private static final String MY_PREFS_NAME = "MyPrefsFile";

    static {
        timeZone = TimeZone.getDefault();
    }

    public static String getFormattedTime(long dateInMillis) {
        Date date = new Date(dateInMillis);
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("HH:mm a");
        shortenedDateFormat.setTimeZone(timeZone);
        String day = shortenedDateFormat.format(date.getTime());
        return day;
    }

    public static String getFriendlyDayString(long dateInMillis) {
        Calendar taskCal = getCalendarInstance();
        taskCal.setTimeInMillis(dateInMillis);

        long currentTimeMillis = System.currentTimeMillis();
        Calendar cal = getCalendarInstance();

        if (taskCal.getTimeInMillis() < currentTimeMillis)
            return "Overdue";
        else if (taskCal.getTimeInMillis() < getTimeInMillis(cal,1)) {
            return "Today";
        }else if(taskCal.getTimeInMillis() < getTimeInMillis(cal,2)){
            return "Tomorrow";
        }else if(taskCal.getTimeInMillis() < getUpComingMondayTimeInMillis(cal)){
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEEE");
            shortenedDateFormat.setTimeZone(timeZone);
            return shortenedDateFormat.format(dateInMillis);
        }else{
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            shortenedDateFormat.setTimeZone(timeZone);
            return shortenedDateFormat.format(dateInMillis);
        }
     }

    private static long getTimeInMillis(Calendar cal, int addDate) {
        int currentDate = cal.get(Calendar.DATE);
        cal.set(Calendar.DATE,currentDate+addDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    private static long getUpComingMondayTimeInMillis(Calendar currentCal) {
        currentCal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        currentCal.set(Calendar.HOUR_OF_DAY,0);
        currentCal.set(Calendar.MINUTE,0);
        currentCal.set(Calendar.SECOND,0);
        currentCal.add(Calendar.DATE,8);
        return currentCal.getTimeInMillis();
    }

    public static void saveToPreferences(Context context, String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getFromPreferences(Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public static Calendar getCalendarInstance(){
        return getInstance(timeZone);
    }

}
