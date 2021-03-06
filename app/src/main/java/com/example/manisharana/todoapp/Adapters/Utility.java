package com.example.manisharana.todoapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import com.example.manisharana.todoapp.Models.Comment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Calendar.getInstance;

public class Utility {

    private static final TimeZone timeZone;
    private static final String MY_PREFS_NAME = "MyPrefsFile";
    private final Context mContext;
    private SharedPreferences sharedPreferences;

    static {
        timeZone = TimeZone.getDefault();
    }

    public Utility(Context context) {
        this.mContext = context;
        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
    }

    public static String getFormattedTime(String dateInMillis) {

        Date date = getDateFromString(dateInMillis);
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEEEEE, d MMM yyyy ");
        shortenedDateFormat.setTimeZone(timeZone);
        String day = shortenedDateFormat.format(date.getTime());
        return day;
    }

    public static String getFriendlyDayString(String dateInMillis) {
        Date taskDate = getDateFromString(dateInMillis);
        Calendar taskCal = getCalendarInstance();
        taskCal.setTime(taskDate);

        long currentTimeMillis = System.currentTimeMillis();
        Calendar cal = getCalendarInstance();

        if (taskCal.getTimeInMillis() < currentTimeMillis)
            return "Overdue";
        else if (taskCal.getTimeInMillis() < getTimeInMillis(cal, 1)) {
            return "Today";
        } else if (taskCal.getTimeInMillis() < getTimeInMillis(cal, 2)) {
            return "Tomorrow";
        } else if (taskCal.getTimeInMillis() < getUpComingMondayTimeInMillis(cal)) {
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEEE");
            shortenedDateFormat.setTimeZone(timeZone);
            return shortenedDateFormat.format(taskDate);
        } else {
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            shortenedDateFormat.setTimeZone(timeZone);
            return shortenedDateFormat.format(taskDate);
        }
    }

    private static long getTimeInMillis(Calendar cal, int addDate) {
        int currentDate = cal.get(Calendar.DATE);
        cal.set(Calendar.DATE, currentDate + addDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    private static long getUpComingMondayTimeInMillis(Calendar currentCal) {
        currentCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        currentCal.set(Calendar.HOUR_OF_DAY, 0);
        currentCal.set(Calendar.MINUTE, 0);
        currentCal.set(Calendar.SECOND, 0);
        currentCal.add(Calendar.DATE, 8);
        return currentCal.getTimeInMillis();
    }

    public void saveToPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getFromPreferences(String key) {
        return sharedPreferences.getString(key, null);
    }

    public static Calendar getCalendarInstance() {
        return getInstance(timeZone);
    }

    public void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setTitle("Error")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public ProgressDialog getProgressDialog(String message) {
        ProgressDialog progress = new ProgressDialog(mContext);
        progress.setTitle("Downloading");
        progress.setCancelable(false);
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        return progress;
    }

    public static Date getDateFromString(String input) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        df.setTimeZone(timeZone);
        try {
            return df.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDateFromMillis(long timeInMillis) {
        Date date = new Date(timeInMillis);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        df.setTimeZone(timeZone);
        return df.format(date);
    }

    public ArrayList<Comment> getCommentList(String comments) {
        ArrayList<Comment> commentList = new ArrayList<>();

        if (comments.isEmpty()) {
            commentList = null;
        } else {
            String[] commentArray = comments.trim().split("\n");
            for (String comment : commentArray) {
                String[] splitCom = comment.trim().split(":");
                String fromName = splitCom[0];
                String msg = splitCom[1];
                boolean isSelf = true;
                if (!fromName.equals(getFromPreferences("UserName"))) {
                    isSelf = false;
                }

                commentList.add(new Comment(fromName, msg, isSelf));
            }
        }
        return commentList;
    }
}

