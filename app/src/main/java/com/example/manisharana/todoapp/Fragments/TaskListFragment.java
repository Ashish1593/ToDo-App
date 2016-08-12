package com.example.manisharana.todoapp.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.manisharana.todoapp.Activities.TaskActivity;
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

public class TaskListFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = TaskListFragment.class.getSimpleName();
    private TaskListAdapter taskListAdapter;
    private Utility mUtility;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        mUtility = new Utility(getActivity());

        taskListAdapter = new TaskListAdapter(getActivity(),new ArrayList<Task>());

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_view_task_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(taskListAdapter);

        mRecyclerView.setOnClickListener(this);


//       ListView listView = (ListView) rootView.findViewById(R.id.list_view_task_list);
//       listView.setAdapter(taskListAdapter);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                        swipeRefreshLayout.setRefreshing(true);
                        String phoneNumber = mUtility.getFromPreferences("PhoneNumber");
                        FetchTaskListForUser fetchTaskListForUser = new FetchTaskListForUser(swipeRefreshLayout);
                        fetchTaskListForUser.execute(phoneNumber);
                    }
                }
        );


        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        startActivity(intent);
    }

    public class FetchTaskListForUser extends AsyncTask<String, String, Void> implements Callback {

        private final SwipeRefreshLayout swipeRefreshLayout;
        private OkHttpClient client;
        private ArrayList<Task> tasks;

        public FetchTaskListForUser(SwipeRefreshLayout swipeRefreshLayout) {
            tasks = new ArrayList<>();
            this.swipeRefreshLayout = swipeRefreshLayout;

        }

        @Override
        protected Void doInBackground(String... args) {
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
            client.newCall(request).enqueue(this);

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            swipeRefreshLayout.setRefreshing(true);
            taskListAdapter.swap(tasks);
            swipeRefreshLayout.setRefreshing(false);
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
                    long date = taskData.getLong("date");
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

}
