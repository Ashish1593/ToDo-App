package com.example.manisharana.todoapp.AsyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.manisharana.todoapp.Activities.TaskListActivity;
import com.example.manisharana.todoapp.Adapters.TaskUtility;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;

public class SaveTask extends AsyncTask<Task, String, String> {

    private final Context mContext;
    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public SaveTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(Task... args) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(mContext.getString(R.string.sample_api_base_url)).newBuilder()
                .addPathSegment("api")
                .addPathSegment("tasks")
                .build();
        Request.Builder builder = new Request.Builder().url(url);
        Request request;

        if(args[0].getId().equals("")){
            String newTaskJson = TaskUtility.getTaskJson(args[0]);
            RequestBody body = RequestBody.create(JSON, newTaskJson);
            request = builder
                    .post(body)
                    .build();
        }else{
            String updateTaskJson = TaskUtility.getUpdatedTaskJson(args[0]);
            RequestBody body = RequestBody.create(JSON, updateTaskJson);
            request = builder
                    .put(body)
                    .build();
        }


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Errror", "Cannot add tasks");
                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.error)
                        .setMessage("Error Inserting Record")
                        .setCancelable(true)
                        .create().show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Intent intent = getIntentData();
                mContext.startActivity(intent);
            }
        });
        return null;
    }


    private Intent getIntentData() {
        return new Intent(mContext, TaskListActivity.class);
    }

}