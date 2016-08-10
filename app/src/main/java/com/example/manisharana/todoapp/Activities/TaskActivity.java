package com.example.manisharana.todoapp.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.AsyncTasks.SaveTask;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;

import java.util.Calendar;
import java.util.TimeZone;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_CONTACT = 0;
    private EditText mDateView;
    private EditText mTimeView;
    private TextView mTitleView;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String mContactName;
    private String mPhotoUri;
    private String mPhoneNumber;
    private long mSelectedTimeInMillis;
    private ImageButton mAddContactButton;
    private Task mTask;
    private TextView mContactNameView;
    private TextView mContactPhoneNumberView;

    public TaskActivity() {
        mTask = new Task();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().setElevation(0);
        Calendar cal = Utility.getCalendarInstance();

        mTitleView = (TextView) this.findViewById(R.id.editText_task_title);
        mDateView = (EditText) this.findViewById(R.id.editText_task_date);
        mTimeView = (EditText) this.findViewById(R.id.editText_task_time);
        mAddContactButton = (ImageButton) this.findViewById(R.id.image_button_assign_contact);
        mContactNameView = (TextView) this.findViewById(R.id.text_view_assigned_contact_name);
        mContactPhoneNumberView = (TextView) this.findViewById(R.id.text_view_assigned_contact_phone_number);

        mAddContactButton.setOnClickListener(this);

        if (getIntent() != null) {
            Intent intent = getIntent();
            mTask = (Task) intent.getSerializableExtra("Task");
            if (mTask != null) {
                prepopulateTaskData(mTask, cal);
                setDefaultDateTimeView(mDateView, mTimeView, cal);

            } else {
                setDefaultDateTimeView(mDateView, mTimeView, cal);
            }
        }
    }

    private void prepopulateTaskData(Task taskToBeEdited, Calendar cal) {
        cal.setTimeInMillis(taskToBeEdited.getDate());
        mTitleView.setText(taskToBeEdited.getTitle());
        mTitleView.setEnabled(false);
        mContactNameView.setText(taskToBeEdited.getAssignToName());
        mContactPhoneNumberView.setText(taskToBeEdited.getAssignToPhone());
    }

    private void setDefaultDateTimeView(EditText dateView, EditText timeView, Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH) + 1;
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        dateView.setText(year + "-" + (month + 1) + "-" + day);
        timeView.setText(String.format("%02d:%02d", hour, minute));
        setOnClickListenerOnView(dateView, cal);
        setOnClickListenerOnView(timeView, cal);
    }

    private void setOnClickListenerOnView(EditText view, final Calendar cal) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateAndTime(view, cal);
            }
        });
    }

    void getDateAndTime(View view, Calendar cal) {
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyDialogTheme, onDateSetListener, year, month, day + 1);
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

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.MyDialogTheme, timeSetListener, hour, minute, false);
            timePickerDialog.show();
        }
    }

    private long getSelectedTime(int year, int month, int day, int hour, int minute) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        if (year != 0 && month != 0 && day != 0) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
        } else {
            c.set(Calendar.DAY_OF_MONTH, day + 1);
        }
        if (hour != 0 && minute != 0) {
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
        }
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }


    void saveButtonClicked(View view) {
        Task newTask = getTask();
        new SaveTask(this).execute(newTask);
    }

    private Task getTask() {
        String title = mTitleView.getText().toString();
        mSelectedTimeInMillis = getSelectedTime(mYear, mMonth, mDay, mHour, mMinute);
        String myPhoneNumber = Utility.getFromPreferences(this, "PhoneNumber");
        String me = Utility.getFromPreferences(this, "UserName");
        if (mContactName.equals("") && mPhoneNumber.equals("")) {
            mContactName = me;
            mPhoneNumber = myPhoneNumber;
        }

        if (mTask.getId().equals("")) {
            return new Task(title, mSelectedTimeInMillis, true, me, myPhoneNumber, mContactName, mPhoneNumber);
        } else {
            return new Task(title, mTask.getId(), mSelectedTimeInMillis, true, mTask.getAssignByName(), mTask.getAssignToName(), mContactName, mPhoneNumber);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            mContactName = data.getStringExtra("UserName");
            mPhoneNumber = data.getStringExtra("UserPhone");
            mContactNameView.setText(mContactName);
            mContactNameView.setText(mPhoneNumber);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivityForResult(intent, PICK_CONTACT);
    }

}

