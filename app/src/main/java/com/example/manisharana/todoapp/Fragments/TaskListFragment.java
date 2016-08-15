package com.example.manisharana.todoapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.manisharana.todoapp.Adapters.TaskListAdapter;
import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.AsyncTasks.FetchTaskListForUser;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;

import java.util.ArrayList;

public class TaskListFragment extends Fragment {

    private static final String LOG_TAG = TaskListFragment.class.getSimpleName();
    private TaskListAdapter taskListAdapter;
    private Utility mUtility;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        mUtility = new Utility(getActivity());

        taskListAdapter = new TaskListAdapter(getActivity(), new ArrayList<Task>());

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_view_task_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(taskListAdapter);

        String phoneNumber = mUtility.getFromPreferences("PhoneNumber");
        FetchTaskListForUser fetchTaskListForUser = new FetchTaskListForUser(getActivity(), taskListAdapter);
        fetchTaskListForUser.execute(phoneNumber);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
