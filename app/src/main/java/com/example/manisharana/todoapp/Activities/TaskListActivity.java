package com.example.manisharana.todoapp.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.R;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class TaskListActivity extends AppCompatActivity {

    private static final String LOG_TAG = TaskListActivity.class.getSimpleName();
    private Socket mSocket;
    private Utility mUtility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list );
        getSupportActionBar().setElevation(0);
        mUtility = new Utility(this);
        mSocket = ((MyApplication)getApplication()).getSocket();
        registerSocketListeners();

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
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText("You have a new task assigned by "+name+"!!");
        int notificationID = 100;
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    private void playBeep() {
        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(this, defaultUri);
        ringtone.play();
    }

    public void  navigateToTaskActivity(View view){
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }


}
