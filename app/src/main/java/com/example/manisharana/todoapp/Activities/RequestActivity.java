package com.example.manisharana.todoapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RequestActivity extends AppCompatActivity {
    private static final String PICTURE_URL = "PICTURE_URL";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private OkHttpClient client;
    private String phoneNumber;
    private EditText username;
    private HttpUrl pingUrl;
    private Utility mUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Intent intent = getIntent();
        String url = intent.getStringExtra(PICTURE_URL);
        mUtility = new Utility(this);

        phoneNumber = mUtility.getFromPreferences("PhoneNumber");

        client = new OkHttpClient();
        username = (EditText) findViewById(R.id.username_label);
        ImageView profileImageView = (ImageView) findViewById(R.id.profile_image);
        Picasso.with(this).load(url).into(profileImageView);
        pingUrl = HttpUrl.parse(getString(R.string.sample_api_base_url)).newBuilder()
                .addPathSegment("api")
                .addPathSegment("users")
                .build();

    }

    private String getJsonString() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name",username.getText().toString());
            requestBody.put("phonenm", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody.toString();
    }

    @NonNull
    private Intent getNewIntent() {
        mUtility.saveToPreferences("UserName",username.getText().toString());
        return new Intent(this, TaskListActivity.class);
    }

    public static Intent newIntent(Context context, String pictureURL) {
        final Intent intent = new Intent(context, RequestActivity.class);
        intent.putExtra(PICTURE_URL, pictureURL);
        return intent;
    }

    public void sendUserDetailsToServer(View view){
        String json = getJsonString();
        RequestBody body = RequestBody.create(JSON, json);

        final Request request = new Request.Builder()
                .url(pingUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Eroooooorr",request.toString());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Intent newIntent = getNewIntent();
                    startActivity(newIntent);
                }
            });

    }


}
