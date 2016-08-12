package com.example.manisharana.todoapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Calendar.getInstance;

public class Utility {

    private static final TimeZone timeZone;
    private static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String KEY_SESSION_ID = "sessionID";
    private static final String FLAG_MESSAGE = "message";
    private SharedPreferences sharedPreferences;

    static {
        timeZone = TimeZone.getDefault();
    }

    public Utility(Context context){
        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
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

    public void saveToPreferences(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getFromPreferences(String key){
        return sharedPreferences.getString(key, null);
    }

    public static Calendar getCalendarInstance(){
        return getInstance(timeZone);
    }

    public void storeSessionId(String sessionId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.commit();
    }

    public String getSessionId() {
        return sharedPreferences.getString(KEY_SESSION_ID, null);
    }

    public String getSendMessageJSON(String message) {
        String json = null;

        try {
            JSONObject jObj = new JSONObject();
            jObj.put("flag", FLAG_MESSAGE);
            jObj.put("sessionId", getSessionId());
            jObj.put("message", message);

            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

}
