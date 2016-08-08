package com.example.manisharana.todoapp.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.Data.UserEntry;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TaskActivity extends AppCompatActivity {

    private static final int PICK_CONTACT = 0;
    private TextView mDateView;
    private TextView mTimeView;
    private TextView mTitleView;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String userName;
    private String photoUri;
    private String phoneNumber;
    private String selectedTimeInMillis;
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
        fabButton = (FloatingActionButton) findViewById(R.id.fab_new_task);
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.MyDialogTheme ,onDateSetListener, year, month, day+1);
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

    private String getSelectedTime(int year, int month, int day, int hour, int minute) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"), Locale.US);
        if(year != 0 && month != 0 && day != 0) {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
        }else{
            c.set(Calendar.DAY_OF_MONTH, day+1);
        }
        if (hour != 0  && minute != 0) {
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
        }
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        return String.valueOf(c.getTimeInMillis());
    }


    void saveButtonClicked(View view) {
        String title = mTitleView.getText().toString();
        selectedTimeInMillis = getSelectedTime(mYear, mMonth, mDay, mHour, mMinute);

        String myPhoneNumber = Utility.getFromPreferences(this, "PhoneNumber");
        String me = Utility.getFromPreferences(this, "UserName");
        if(userName.equals("") && phoneNumber.equals("")){
            userName = me;
            phoneNumber = myPhoneNumber;
        }

        Task task = new Task(title, selectedTimeInMillis, true, me, myPhoneNumber, userName,phoneNumber );

        SaveTask saveTask = new SaveTask(this);
        saveTask.execute(task);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_add_task_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_select_contact :
                //Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                Intent intent = new Intent(this,ContactActivity.class);
                startActivityForResult(intent, PICK_CONTACT);
                return  true;
        }
        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
//            Uri contactData = data.getData();
//            Cursor query = this.getContentResolver().query(contactData, null, null, null, null);
//            if (query.moveToFirst()) {
//                String userId = query.getString(query.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
//                userName = query.getString(query.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                photoUri = query.getString(query.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
//                String hasPhone = query.getString(query.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//
//                if (hasPhone.equals("1")) {
//                    Cursor phoneQuery = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.Contacts.DISPLAY_NAME + " = ? ", new String[]{userName}, null);
//                    if (phoneQuery.moveToFirst()) {
//                        phoneNumber = phoneQuery.getString(phoneQuery.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    }
//                    phoneQuery.close();
//                }
//            }
//            query.close();
            userName = data.getStringExtra("UserName");
            phoneNumber = data.getStringExtra("UserPhone");
        }

        TextView userNameView = (TextView) this.findViewById(R.id.text_view_user_name);
        userNameView.setText(userName);
        TextView phoneNumberView = (TextView) this.findViewById(R.id.text_view_user_phone_number);
        phoneNumberView.setText(phoneNumber);
    }

    public class SaveTask extends AsyncTask<Task,String,String> {

        private final Context context;
        private OkHttpClient client;
        public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        public SaveTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Task... args) {

            String json = getJsonString(args[0]);
            RequestBody body = RequestBody.create(JSON, json);

            client = new OkHttpClient();
            final HttpUrl url = HttpUrl.parse(getString(R.string.sample_api_base_url)).newBuilder()
                    .addPathSegment("api")
                    .addPathSegment("tasks")
                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.i("Errror","Cannot add tasks");
                        new AlertDialog.Builder(context)
                            .setTitle(R.string.error)
                            .setMessage("Error in inserting record")
                            .setCancelable(true)
                            .create().show();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Intent intent = getIntentData();
                        startActivity(intent);
                    }
                });

            return null;
        }

        private Intent getIntentData() {
            return new Intent( context,TaskListActivity.class);
        }


        private String getJsonString(Task task) {
            JSONObject jsonObject = new JSONObject();
             try {
                jsonObject.put("title",task.getTitle());
                jsonObject.put("date",task.getDate());
                jsonObject.put("assgnByName",task.getAssignByName());
                jsonObject.put("assgnByPhon",task.getAssignByPhone());
                jsonObject.put("assgnToName",task.getAssignToName());
                jsonObject.put("assgnToPhon",task.getAssignToPhone());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }



    }
}

