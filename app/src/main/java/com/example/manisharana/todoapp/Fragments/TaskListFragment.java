package com.example.manisharana.todoapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.manisharana.todoapp.Activities.TaskActivity;
import com.example.manisharana.todoapp.Adapters.TaskListAdapter;
import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.AsyncTasks.FetchTaskListForUser;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;

import java.util.ArrayList;

public class TaskListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Task>> {


    private static final int TASKS_LOADER_ID = 100;
    private TaskListAdapter taskListAdapter;
    private String mPhoneNumber;
    private TextView mTextViewError;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        mPhoneNumber = new Utility(getActivity()).getFromPreferences("PhoneNumber");
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        taskListAdapter = new TaskListAdapter(getActivity(), new ArrayList<Task>());

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_view_task_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(taskListAdapter);
        mTextViewError = (TextView)rootView.findViewById(R.id.text_view_error);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(TASKS_LOADER_ID,null,this).forceLoad();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<ArrayList<Task>> onCreateLoader(int id, Bundle args) {
        return new FetchTaskListForUser(getActivity(),mPhoneNumber);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Task>> loader, ArrayList<Task> data) {
        if(data != null && !data.isEmpty()) {
            mTextViewError.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            taskListAdapter.swap(data);
        }else{
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mTextViewError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Task>> loader) {
        taskListAdapter.clearData();
    }

}
