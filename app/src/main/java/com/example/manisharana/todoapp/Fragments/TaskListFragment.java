package com.example.manisharana.todoapp.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.manisharana.todoapp.Adapters.TaskListAdapter;
import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class TaskListFragment extends Fragment{

    private TaskListAdapter taskListAdapter;
    private FetchTaskListForUser fetchTaskListForUser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        taskListAdapter = new TaskListAdapter(getActivity(), R.layout.list_item_task);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_task_list);
        listView.setAdapter(taskListAdapter);

        fetchTaskListForUser = new FetchTaskListForUser(getActivity());
        String phoneNumber = Utility.getFromPreferences(getActivity(), "PhoneNumber");
        fetchTaskListForUser.execute(phoneNumber);


        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class FetchTaskListForUser extends AsyncTask<String, String, ArrayList<Task>> {

        private OkHttpClient client;

        public FetchTaskListForUser(Context context) {

        }

        @Override
        protected ArrayList<Task> doInBackground(String... args) {
            ArrayList<Task> tasks = new ArrayList<>();
            client = new OkHttpClient();
            final HttpUrl url = HttpUrl.parse(getString(R.string.sample_api_base_url)).newBuilder()
                    .addPathSegment("api")
                    .addPathSegment("tasks")
                    .addEncodedPathSegment(args[0])
                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JSONArray jsonArray = new JSONArray(response.body().string());
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String taskId = object.getString("id");
                    JSONObject taskData = object.getJSONObject("data");
                    String title = taskData.getString("title");
                    String date = taskData.getString("date");
                    boolean status = taskData.getBoolean("status");
                  //  String assgnByName = taskData.getString("assgnByName");
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

            return tasks;
        }

        @Override
        protected void onPostExecute(ArrayList<Task> s) {
            super.onPostExecute(s);
            taskListAdapter.clear();
            taskListAdapter.addAll(s);
        }
    }

}
