package com.example.manisharana.todoapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.auth0.core.Token;
import com.auth0.core.UserProfile;
import com.auth0.lock.Lock;
import com.auth0.lock.passwordless.LockPasswordlessActivity;
import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.Adapters.ViewPagerAdapter;
import com.example.manisharana.todoapp.Fragments.ContactListFragment;
import com.example.manisharana.todoapp.Fragments.TaskFragment;
import com.example.manisharana.todoapp.Fragments.TaskListFragment;
import com.example.manisharana.todoapp.Models.User;
import com.example.manisharana.todoapp.R;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private final FetchUserTask fetchUserTask;
    private Utility mUtility;
    private LocalBroadcastManager broadcastManager;

    public MainActivity() {
        fetchUserTask = new FetchUserTask(this);
    }

    private String pictureURL;
    private BroadcastReceiver authenticationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UserProfile profile = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_PROFILE_PARAMETER);
            Token token = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_TOKEN_PARAMETER);
            mUtility.saveToPreferences("AccessToken", token.getIdToken());
            mUtility.saveToPreferences("PhoneNumber", profile.getName());
            pictureURL = profile.getPictureURL();
            fetchUserTask.execute(profile.getName(), token.getIdToken());
        }
    };

    @Override
    //1
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUtility = new Utility(this);
        TaskListFragment fragmentOne = new TaskListFragment();
        TaskFragment fragmentTwo= new TaskFragment();
        ContactListFragment fragmentThree = new ContactListFragment();

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(fragmentOne);
        pagerAdapter.addFragment(fragmentTwo);
        pagerAdapter.addFragment(fragmentThree);


        String accessToken = mUtility.getFromPreferences("AccessToken");
        if (accessToken != null) {
            //2





                ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                viewPager.setAdapter(pagerAdapter);

//            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//            viewPager.setAdapter(new ViewPagerAdapter(this));
//            Intent taskListIntent = new Intent(this, TaskListActivity.class);
//            startActivity(taskListIntent);


        } else {
            LockPasswordlessActivity.showFrom(MainActivity.this, LockPasswordlessActivity.MODE_SMS_CODE);
        }
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(authenticationReceiver, new IntentFilter(Lock.AUTHENTICATION_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(authenticationReceiver);
    }

    public class FetchUserTask extends AsyncTask<String, User, User> {

        private final Context context;
        private OkHttpClient client;
        public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        public FetchUserTask(Context context) {
            this.context = context;
        }


        @Override
        protected User doInBackground(String... args) {
            User user = new User();
            client = new OkHttpClient();
            String json = getJsonString(args[0], args[1]);
            RequestBody body = RequestBody.create(JSON, json);
            final HttpUrl pingUrl = HttpUrl.parse(getString(R.string.sample_api_base_url)).newBuilder()
                    .addPathSegment("api")
                    .addPathSegment("validate")
                    .build();
            final Request request = new Request.Builder()
                    .url(pingUrl)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                String name = jsonObject.getString("name");
                String phone = jsonObject.getString("phone");
                user.setName(name);
                user.setPhoneNumber(phone);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return user;
        }

        private String getJsonString(String phoneNumber, String accessToken) {
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("phone", phoneNumber);
                requestBody.put("accessToken", accessToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return requestBody.toString();
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user == null) {
                String title = "Server Error";
                String message = "Please retry later.";
                new AlertDialog.Builder(context,R.style.AppTheme)
                        .setCancelable(true)
                        .setTitle(title)
                        .setMessage(message).create();

            } else {
                if (user.getName().equals("")) {
                    startActivity(RequestActivity.newIntent(MainActivity.this, pictureURL));

                } else {
                    Intent taskListIntent = new Intent(context, TaskListActivity.class);
                    mUtility.saveToPreferences("UserName", user.getName());
                    startActivity(taskListIntent);
                }
            }
        }
    }




//    public enum ModelObject {
//
//        RED(R.string.red, R.layout.activity_task_list),
//        BLUE(R.string.blue, R.layout.activity_task),
//        GREEN(R.string.green, R.layout.activity_contact_list);
//
//        private int mTitleResId;
//        private int mLayoutResId;
//
//        ModelObject(int titleResId, int layoutResId) {
//            mTitleResId = titleResId;
//            mLayoutResId = layoutResId;
//        }
//
//        public int getTitleResId() {
//            return mTitleResId;
//        }
//
//        public int getLayoutResId() {
//            return mLayoutResId;
//        }
//
//    }


//    float x1,x2;
//    float y1, y2;
//
//
//
//
//    public boolean onTouchEvent(MotionEvent touchevent)
//    {
//        switch (touchevent.getAction())
//        {
//            // when user first touches the screen we get x and y coordinate
//            case MotionEvent.ACTION_DOWN:
//            {
//                x1 = touchevent.getX();
//                y1 = touchevent.getY();
//                break;
//            }
//            case MotionEvent.ACTION_UP:
//            {
//                x2 = touchevent.getX();
//                y2 = touchevent.getY();
//
//                //if left to right sweep event on screen
//                if (x1 < x2)
//                {
//                }
//
//                // if right to left sweep event on screen
//                if (x1 > x2)
//                {
//                    // Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
//
//                    // Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(this, TaskActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//                // if UP to Down sweep event on screen
//                if (y1 < y2)
//                {
//                    // Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
//                }
//
//                //if Down to UP sweep event on screen
//                if (y1 > y2)
//                {
//                    //Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
//                }
//                break;
//            }
//        }
//        return false;
//    }




}
