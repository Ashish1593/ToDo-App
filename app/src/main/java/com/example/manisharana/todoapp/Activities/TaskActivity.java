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
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.manisharana.todoapp.Data.TaskDbHelper;
import com.example.manisharana.todoapp.Data.TaskEntry;
import com.example.manisharana.todoapp.Data.UserEntry;
import com.example.manisharana.todoapp.R;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TaskActivity extends AppCompatActivity {

    private static final int PICK_CONTACT = 0;
    private TextView mDateView;
    private TextView mTimeView;
    private TextView mDescriptionView;
    private Switch mRemindMe;
    private TextView mTitleView;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String userName;
    private String photoUri;
    private String phoneNumber;

    private Long selectedTimeInMillis;

    private FloatingActionButton fabButton;

    public TaskActivity() {
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
        fabButton = (FloatingActionButton) findViewById(R.id.fab_new_task);

    }

    public long saveTask(View view) {
        boolean remindMe;
        long taskId;
        String active = "active";
        String title = mTitleView.getText().toString();
        String desc = mDescriptionView.getText().toString();
        selectedTimeInMillis = getSelectedTime(mYear, mMonth, mDay, mHour, mMinute);
        long userId = getUserID();
        if (mRemindMe.isChecked()) {
            remindMe = true;
        } else
            remindMe = false;

        String[] selectionArgs = {title, String.valueOf(selectedTimeInMillis), desc, active, String.valueOf(remindMe), String.valueOf(userId)};
        Cursor cursor = this.getContentResolver().query(TaskEntry.CONTENT_URI, new String[]{TaskEntry._ID}, getDefaultSelectionQuery(), selectionArgs, null);

        if (cursor.moveToFirst()) {
            int taskIdIndex = cursor.getColumnIndex(TaskEntry._ID);
            taskId = cursor.getLong(taskIdIndex);
        } else {
            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_TITLE, title);
            values.put(TaskEntry.COLUMN_DATE, selectedTimeInMillis);
            values.put(TaskEntry.COLUMN_DESC, desc);
            values.put(TaskEntry.COLUMN_STATUS, active);
            values.put(TaskEntry.COLUMN_USER_ID, userId);
            values.put(TaskEntry.COLUMN_REMIND_ME, remindMe);

            Uri insertedUri = this.getContentResolver().insert(TaskEntry.CONTENT_URI, values);
            taskId = ContentUris.parseId(insertedUri);
        }

        cursor.close();
        return taskId;

    }

    private String getDefaultSelectionQuery() {
        return TaskEntry.COLUMN_TITLE + " = ? and "
                + TaskEntry.COLUMN_DATE + " = ? and "
                + TaskEntry.COLUMN_DESC + " = ? and "
                + TaskEntry.COLUMN_STATUS + " = ? and "
                + TaskEntry.COLUMN_REMIND_ME + " = ? and "
                + TaskEntry.COLUMN_USER_ID + " = ? ";
    }

    private long getUserID() {
        Long insertedUserId;

        String selection = UserEntry.COLUMN_NAME + " = ? and "+ UserEntry.COLUMN_PHONE_NUMBER +" = ? ";
        String[] selectionArgs = new String[]{userName,phoneNumber};
        Cursor cursor = this.getContentResolver().query(UserEntry.CONTENT_URI, new String[]{UserEntry._ID}, selection, selectionArgs, null);

        if (cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(UserEntry._ID);
            insertedUserId = cursor.getLong(userIdIndex);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(UserEntry.COLUMN_NAME, userName);
            contentValues.put(UserEntry.COLUMN_PHONE_NUMBER, phoneNumber);
            contentValues.put(UserEntry.COLUMN_PHOTO, photoUri);
            insertedUserId = ContentUris.parseId(this.getContentResolver().insert(UserEntry.CONTENT_URI,contentValues));
        }
        return insertedUserId;
    }

    void getDateAndTime(View view) {
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.MyDialogTheme ,onDateSetListener, year, month, day);
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

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.MyDialogTheme, timeSetListener, hour, minute, false);
            timePickerDialog.show();
        }
    }

    private Long getSelectedTime(int year, int month, int day, int hour, int minute) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"), Locale.US);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        return c.getTimeInMillis();
    }


    void saveButtonClicked(View view) {
        long l = saveTask(view);
        if (l > 0) {
            Intent intent = new Intent(this, TaskListActivity.class);
            startActivity(intent);
        } else {
            Log.i("Error In saving Task", "TaskActivity");
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_add_task_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_select_contact :
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                return  true;
        }
        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactData = data.getData();
            Cursor query = this.getContentResolver().query(contactData, null, null, null, null);
            if (query.moveToFirst()) {
                String userId = query.getString(query.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                userName = query.getString(query.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                photoUri = query.getString(query.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                String hasPhone = query.getString(query.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equals("1")) {
                    Cursor phoneQuery = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.Contacts.DISPLAY_NAME + " = ? ", new String[]{userName}, null);
                    if (phoneQuery.moveToFirst()) {
                        phoneNumber = phoneQuery.getString(phoneQuery.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phoneQuery.close();
                }
            }
            query.close();
        }
        TextView userNameView = (TextView) this.findViewById(R.id.text_view_user_name);
        userNameView.setText(userName);
        TextView phoneNumberView = (TextView) this.findViewById(R.id.text_view_user_phone_number);
        phoneNumberView.setText(phoneNumber);
    }
}

