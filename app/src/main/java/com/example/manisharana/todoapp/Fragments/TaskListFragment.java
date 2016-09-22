package com.example.manisharana.todoapp.Fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.manisharana.todoapp.Activities.MyApplication;
import com.example.manisharana.todoapp.Activities.TaskActivity;
import com.example.manisharana.todoapp.Activities.TaskListActivity;
import com.example.manisharana.todoapp.Adapters.TaskListAdapter;
import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.AsyncTasks.FetchTaskListForUser;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

//import static com.example.manisharana.todoapp.R.id.fab_add_task;

public class TaskListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Task>>, View.OnClickListener {


    private static final String LOG_TAG = TaskListActivity.class.getSimpleName();
    private Socket mSocket;
    private Utility mUtility;


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
        mUtility = new Utility(getActivity());
        mSocket = ((MyApplication)getActivity().getApplication()).getSocket();
        registerSocketListeners();

//        FloatingActionButton fab =(FloatingActionButton) rootView.findViewById(R.id.fab_add_task);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navigateToTaskActivity();
//            }
//        });
            return rootView;
    }


    private void registerSocketListeners() {
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i(LOG_TAG,"On call connected");
                mSocket.emit("joinroom", mUtility.getFromPreferences("UserName"));
            }

        }).on("notify", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i(LOG_TAG,"On new Task - notify");
                createNotification(args[0].toString());
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i(LOG_TAG,"On call disconnected");
            }

        });
        mSocket.connect();
    }

    private void createNotification(String name) {
        playBeep();
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText("You have a new task assigned by "+name+"!!");
        int notificationID = 100;
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    private void playBeep() {
        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), defaultUri);
        ringtone.play();
    }


    public void navigateToTaskActivity() {

        Fragment fragment = new TaskFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_task_list_view, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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


    @Override
    public void onClick(View v) {

    }
}
