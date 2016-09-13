package com.example.manisharana.todoapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.example.manisharana.todoapp.Adapters.TaskListAdapter;
import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.Models.Comment;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class FetchTaskListForUser extends AsyncTaskLoader<ArrayList<Task>> {

    private final String mPhoneNumber;
    private Context context;
    private OkHttpClient client;
    private ArrayList<Task> tasks;

    public FetchTaskListForUser(Context activity, String mPhoneNumber) {
        super(activity);
        tasks = new ArrayList<>();
        this.context = activity;
        this.mPhoneNumber = mPhoneNumber;
    }

    @Override
    public ArrayList<Task> loadInBackground() {
        client = new OkHttpClient();
        final HttpUrl url = HttpUrl.parse(context.getString(R.string.sample_api_base_url)).newBuilder()
                .addPathSegment("api")
                .addPathSegment("tasks")
                .addEncodedPathSegment(mPhoneNumber)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String taskId = object.getString("id");
                JSONObject taskData = object.getJSONObject("data");
                String title = taskData.getString("title");
                String date = taskData.getString("date");
                boolean status = taskData.getBoolean("status");
                String commentString = taskData.getString("comments");
                String assgnByName = taskData.getString("assgnByName");
                String assgnByPhon = taskData.getString("assgnByPhon");
                String assgnToName = taskData.getString("assgnToName");
                String assgnToPhon = taskData.getString("assgnToPhon");
                ArrayList<Comment> comments = new Utility(context).getCommentList(commentString);
                Task task = new Task(title, taskId, date, status, comments, assgnByName, assgnByPhon, assgnToName, assgnToPhon);
                tasks.add(task);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tasks;

    }
}