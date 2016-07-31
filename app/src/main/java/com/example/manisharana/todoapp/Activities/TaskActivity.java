package com.example.manisharana.todoapp.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.Data.TaskDbHelper;
import com.example.manisharana.todoapp.Data.TaskEntry;
import com.example.manisharana.todoapp.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TaskActivity extends AppCompatActivity{

    private TextView mDateView;
    private TextView mTimeView;
    private TextView mDescriptionView;
    private Switch mRemindMe;
    private Spinner mUserView;
    private Spinner mLabelView;
    private TextView mTitleView;
    private TaskDbHelper db ;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private Long selectedTimeInMillis;
    private FloatingActionButton fabButton;

    public TaskActivity() {
        db = new TaskDbHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().setElevation(0);

        mTitleView = (TextView) this.findViewById(R.id.editText_task_title);
        mDateView = (TextView) this.findViewById(R.id.editText_task_date);
        mTimeView = (TextView) this.findViewById(R.id.editText_task_time);
        mDescriptionView = (TextView) this.findViewById(R.id.editText_task_desc);
        mRemindMe = (Switch) this.findViewById(R.id.switch_remind_me);
        mUserView = (Spinner) this.findViewById(R.id.spinner_add_user);
        mLabelView = (Spinner) this.findViewById(R.id.spinner_add_tag);
        fabButton = (FloatingActionButton) findViewById(R.id.fab_new_task);

        loadSpinnerData();
        }

    public long saveTask(View view) {
        boolean remindMe;
        String active = "active";
        String title = mTitleView.getText().toString();
        String desc = mDescriptionView.getText().toString();
        if (mRemindMe.isChecked()) {
            remindMe = true;
        } else
            remindMe = false;

        long taskId;
       // Cursor cursor = this.getContentResolver().query(TaskEntry.CONTENT_URI, new String[]{TaskEntry._ID}, null, null, null);
      //  if (cursor.moveToFirst()) {
      //      int taskIdIndex = cursor.getColumnIndex(TaskEntry._ID);
      //      taskId = cursor.getLong(taskIdIndex);
      //  } else {
            selectedTimeInMillis = getSelectedTime(mYear, mMonth, mDay, mHour, mMinute);
            ContentValues values = new ContentValues();

            values.put(TaskEntry.COLUMN_TITLE, title);
            values.put(TaskEntry.COLUMN_DATE, selectedTimeInMillis);
            values.put(TaskEntry.COLUMN_DESC, desc);
            values.put(TaskEntry.COLUMN_STATUS, active);
            values.put(TaskEntry.COLUMN_USER_ID, getUserID());
            values.put(TaskEntry.COLUMN_TAG_ID, getTagID());
            values.put(TaskEntry.COLUMN_REMIND_ME, remindMe);

            Uri insertedUri = this.getContentResolver().insert(TaskEntry.CONTENT_URI, values);

            taskId = ContentUris.parseId(insertedUri);
    //  }

    //    cursor.close();
        return taskId;

    }

    private int getTagID() {
        return db.getTagId(mLabelView.getSelectedItem().toString());
    }

    private int getUserID() {
        return db.getUserId(mUserView.getSelectedItem().toString());
    }

    private void loadSpinnerData() {
        List<String> labels = db.getAllTags();
        List<String> users = db.getAllUsers();


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLabelView.setAdapter(dataAdapter);

        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserView.setAdapter(userAdapter);

    }

    void getDateAndTime(View view){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("India/Kolkata"));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        if (view == mDateView) {
            DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    mDateView.setText(year + "-" + (month + 1) + "-" + day);
                    mYear = year;
                    mMonth = month;
                    mDay = day;
                }
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            datePickerDialog.show();
        }
        if (view == mTimeView) {
            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int mins) {
                    mTimeView.setText(String.format("%02d:%02d", hour, mins));
                    mHour = hour;
                    mMinute = mins;
                }

            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, hour, minute, false);
            timePickerDialog.show();
        }
    }

    private Long getSelectedTime(int year, int month, int day, int hour, int minute) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"),Locale.US);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return c.getTimeInMillis();
    }


     void saveButtonClicked(View view) {
        long l = saveTask(view);
        if(l>0){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Log.i("Error In saving Task","TaskActivity");
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error)
                    .setMessage("Error in inserting record")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();

        }
    }
}
