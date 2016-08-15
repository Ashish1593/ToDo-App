package com.example.manisharana.todoapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.example.manisharana.todoapp.Adapters.TaskListAdapter;
import com.example.manisharana.todoapp.Adapters.Utility;
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

public class FetchTaskListForUser extends AsyncTask<String, String, Void> implements Callback {

    private ProgressDialog dialog;
    private TaskListAdapter taskListAdapter;
    private Context context;
    private OkHttpClient client;
    private ArrayList<Task> tasks;

    public FetchTaskListForUser(Context activity, TaskListAdapter taskListAdapter) {
        tasks = new ArrayList<>();
        this.context = activity;
        this.taskListAdapter = taskListAdapter;
        this.dialog = new Utility(context).getProgressDialog("Fetching Tasklist");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    @Override
    protected Void doInBackground(String... args) {
        client = new OkHttpClient();
        final HttpUrl url = HttpUrl.parse(context.getString(R.string.sample_api_base_url)).newBuilder()
                .addPathSegment("api")
                .addPathSegment("tasks")
                .addEncodedPathSegment(args[0])
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(this);

        return null;
    }

    @Override
    protected void onPostExecute(Void s) {
        super.onPostExecute(s);
        taskListAdapter.swap(tasks);
        dialog.cancel();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        Log.i("Error","Error in getting tasklist");
    }

    @Override
    public void onResponse(Response response) throws IOException {
        try {
            JSONArray jsonArray = new JSONArray(response.body().string());
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String taskId = object.getString("id");
                JSONObject taskData = object.getJSONObject("data");
                String title = taskData.getString("title");
                String date = taskData.getString("date");
                boolean status = taskData.getBoolean("status");
                String assgnByPhon = taskData.getString("assgnByPhon");
                String assgnToName = taskData.getString("assgnToName");
                String assgnToPhon = taskData.getString("assgnToPhon");
                Task task = new Task(title,taskId,date,status,"",assgnByPhon,assgnToName,assgnToPhon);
                tasks.add(task);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}