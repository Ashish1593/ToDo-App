package com.example.manisharana.todoapp.AsyncTasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.manisharana.todoapp.Activities.MyApplication;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.socket.client.Socket;

public class SaveTask extends AsyncTask<Task, String, String> {

    private static final String LOG_TAG = SaveTask.class.getSimpleName();
    private final Activity mContext;
    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final Socket mSocket;

    public SaveTask(Activity context) {
        mContext = context;
        mSocket = ((MyApplication)context.getApplication()).getSocket();

    }

    @Override
    protected String doInBackground(final Task... args) {
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
                Log.i("Error", "Cannot add tasks");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Intent intent = getIntentData();
                mContext.startActivity(intent);
                mSocket.emit("newTask",getJsonObject(args[0].getAssignToName(),args[0].getAssignByName()));
            }
        });

        return null;
    }

    private void registerSocketListeners(final Task task) {
        Log.i(LOG_TAG,"On connected");
    }

    private JSONObject getJsonObject(String assignToName, String assignByName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("assgnTo",assignToName);
            jsonObject.put("from",assignByName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private Intent getIntentData() {
        return new Intent(mContext, TaskListActivity.class);
    }

}