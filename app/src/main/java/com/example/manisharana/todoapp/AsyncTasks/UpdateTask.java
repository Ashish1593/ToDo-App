package com.example.manisharana.todoapp.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class UpdateTask extends AsyncTask<Task, Void, Void> implements Callback {
    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Context mContext;

    public UpdateTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Task... tasks) {
        String json = TaskUtility.getUpdatedTaskJson(tasks[0]);
        RequestBody body = RequestBody.create(JSON, json);
        OkHttpClient mClient = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(mContext.getString(R.string.sample_api_base_url)).newBuilder()
                .addPathSegment("api")
                .addPathSegment("tasks")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        mClient.newCall(request).enqueue(this);

        return null;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        Log.i("Error", "Error in updating task");
    }

    @Override
    public void onResponse(Response response) throws IOException {
        Log.i("Succes", "Success in updating task");
    }
}

