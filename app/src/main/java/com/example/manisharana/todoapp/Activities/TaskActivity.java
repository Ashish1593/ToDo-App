package com.example.manisharana.todoapp.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.AsyncTasks.SaveTask;
import com.example.manisharana.todoapp.Models.Comment;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;
import com.example.manisharana.todoapp.Sync.ToDoSyncAdapter;

import java.util.ArrayList;
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
    private String mContactName = "";
    private String mPhotoUri;
    private String mPhoneNumber = "";
    private String mSelectedTimeInMillis;
    private ImageButton mAddContactButton;
    private Task mTask;
    private TextView mContactNameView;
    private TextView mContactPhoneNumberView;
    private Utility mUtility;
    private TextView mTaskAssignedToView;



    float x1, x2;
    float y1, y2;


    public TaskActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().setElevation(0);
        ToDoSyncAdapter.initializeSyncAdapter(this);

        Calendar cal = Utility.getCalendarInstance();
        mUtility = new Utility(this);

        mTitleView = (TextView) this.findViewById(R.id.editText_task_title);
        mDateView = (EditText) this.findViewById(R.id.editText_task_date);
        mTimeView = (EditText) this.findViewById(R.id.editText_task_time);
        mAddContactButton = (ImageButton) this.findViewById(R.id.image_button_assign_contact);
        mContactNameView = (TextView) this.findViewById(R.id.text_view_assigned_contact_name);
        mContactPhoneNumberView = (TextView) this.findViewById(R.id.text_view_assigned_contact_phone_number);
        mTaskAssignedToView = (TextView) findViewById(R.id.text_view_set_reminder);
        mAddContactButton.setOnClickListener(this);




        if (getIntent() != null) {
            Intent intent = getIntent();
            mTask = (Task) intent.getSerializableExtra("Task");
            if (mTask != null) {
                mAddContactButton.setVisibility(View.INVISIBLE);
                prepopulateTaskData(mTask, cal);
                setDefaultDateTimeView(mDateView, mTimeView, cal);

            }

            else {
                mTask = new Task();
                setDefaultDateTimeView(mDateView, mTimeView, cal);
            }

        }


    }

    private void prepopulateTaskData(Task taskToBeEdited, Calendar cal) {
        cal.setTime(mUtility.getDateFromString(taskToBeEdited.getDate()));
        mTitleView.setTextColor(getResources().getColor(R.color.colorAccent));
        mTaskAssignedToView.setText("Task Assigned To");
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

    private String getSelectedTime(int year, int month, int day, int hour, int minute) {
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
        return mUtility.getDateFromMillis(c.getTimeInMillis());
    }


    public void saveButtonClicked(View view) {
        Task newTask = getTask();
        if (newTask != null) {
            new SaveTask(this).execute(newTask);
        } else {
            String message = "Error occured during save task";
            mUtility.showAlertDialog(message);
        }

    }

    private Task getTask() {
        String title = mTitleView.getText().toString();
        if (title.isEmpty()) {
            return null;
        }
        mSelectedTimeInMillis = getSelectedTime(mYear, mMonth, mDay, mHour, mMinute);
        String myPhoneNumber = mUtility.getFromPreferences("PhoneNumber");
        String me = mUtility.getFromPreferences("UserName");
        if (mContactName.equals("") && mPhoneNumber.equals("")) {
            mContactName = me;
            mPhoneNumber = myPhoneNumber;
        }


        if (mTask.getId().equals("")) {
            return new Task(title, mSelectedTimeInMillis, true, null, me, myPhoneNumber, mContactName, mPhoneNumber);
        } else {
            return new Task(title, mTask.getId(), mSelectedTimeInMillis, true, null, mTask.getAssignByName(), mTask.getAssignToName(), mContactName, mPhoneNumber);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            mContactName = data.getStringExtra("UserName");
            mPhoneNumber = data.getStringExtra("UserPhone");
            mTaskAssignedToView.setText("Task Assigned To");
            mContactNameView.setText(mContactName);
            mContactPhoneNumberView.setText(mPhoneNumber);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivityForResult(intent, PICK_CONTACT);
        finish();
    }

    // onTouchEvent () method gets called when User performs any touch event on screen
    // Method to handle touch event like left to right swap and right to left swap
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN: {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                //if left to right sweep event on screen
                if (x1 < x2) {
                    Intent intent = new Intent(this, TaskListActivity.class);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(this,"Left to Right Swap Performed",Toast.LENGTH_LONG).show();
                }

                // if right to left sweep event on screen
                if (x1 > x2) {
                    Intent intent = new Intent(this, ContactActivity.class);
                    startActivityForResult(intent, PICK_CONTACT);
                   finish();

                    //Toast.makeText(this,"Right to Left Swap Performed",Toast.LENGTH_LONG).show();
                }

                // if UP to Down sweep event on screen
                if (y1 < y2) {
                    //  Toast.makeText(this,"UP to Down Swap Performed",Toast.LENGTH_LONG).show();
                }

                //if Down to UP sweep event on screen
                if (y1 > y2) {
                    //Toast.makeText(this,"Down to UP Swap Performed",Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }


}

