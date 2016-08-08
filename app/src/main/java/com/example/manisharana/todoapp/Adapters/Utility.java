package com.example.manisharana.todoapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.manisharana.todoapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utility {


    private static final TimeZone timeZone;
    private static final String MY_PREFS_NAME = "MyPrefsFile";

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

    public static String getFriendlyDayString(long dateInMillis) {
        Calendar taskCal = Calendar.getInstance(timeZone);
        taskCal.setTimeInMillis(dateInMillis);

        long currentTimeMillis = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance(timeZone);

        if (taskCal.getTimeInMillis() < currentTimeMillis)
            return "Overdue";
        else if (taskCal.getTimeInMillis() < getTimeInMillis(cal,1)) {
            return "Today, "+getFormattedTime(taskCal.getTimeInMillis());
        }else if(taskCal.getTimeInMillis() < getTimeInMillis(cal,2)){
            return "Tomorrow, "+getFormattedTime(taskCal.getTimeInMillis());
        }else if(taskCal.getTimeInMillis() < getUpComingMondayTimeInMillis(cal)){
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEEE, HH:mm a");
            shortenedDateFormat.setTimeZone(timeZone);
            return shortenedDateFormat.format(dateInMillis);
        }else{
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd, HH:mm a");
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

}
