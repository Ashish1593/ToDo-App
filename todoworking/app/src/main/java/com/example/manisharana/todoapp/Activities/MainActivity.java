package com.example.manisharana.todoapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.auth0.core.Token;
import com.auth0.core.UserProfile;
import com.auth0.lock.Lock;
import com.auth0.lock.passwordless.LockPasswordlessActivity;
import com.example.manisharana.todoapp.Adapters.Utility;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUtility = new Utility(this);

        String accessToken = mUtility.getFromPreferences("AccessToken");
        if (accessToken != null) {
            Intent taskListIntent = new Intent(this, TaskListActivity.class);
            startActivity(taskListIntent);
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

}
