package com.example.manisharana.todoapp.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.example.manisharana.todoapp.Adapters.CommentListAdapter;
import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.Models.Comment;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class TaskCommentListActivity extends AppCompatActivity {

    private static final String TAG = TaskCommentListActivity.class.getSimpleName();
    private Button mSendBtn;
    private EditText mInputMsg;
    private ListView mListview;
    private ArrayList<Comment> mCommentList;
    private CommentListAdapter mAdapter;
    private Task mTask;
    private Socket mSocket;
    private String mUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            Intent intent = getIntent();
            mTask = (Task) intent.getSerializableExtra("Task");
            if (mTask != null) {
                setTitle(mTask.getTitle());
                if(mTask.getComments() == null){
                    mCommentList = new ArrayList<Comment>();
                }else{
                    mCommentList = mTask.getComments();
                }
            }

        }

        setContentView(R.layout.activity_task_comments);
        mSendBtn = (Button) findViewById(R.id.btnSend);
        mInputMsg = (EditText) findViewById(R.id.inputMsg);
        mListview = (ListView) findViewById(R.id.list_view_messages);
        mUserName = new Utility(this).getFromPreferences("UserName");
        mSocket = ((MyApplication)getApplication()).getSocket();


        connectWebSocket();
        mAdapter = new CommentListAdapter(this, mCommentList);
        mListview.setAdapter(mAdapter);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mInputMsg.getText().toString();
                mCommentList.add(new Comment(mUserName,input,true));
                mAdapter.notifyDataSetChanged();
                sendMessageToServer(mUserName+":"+input+"\n");
                mInputMsg.setText("");
            }
        });
    }

    private void connectWebSocket() {
        mSocket.on("private", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("on new message",args[0].toString());
            }

        }).on("notifyComment", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i("on new comment",args[0].toString());
                parseComment(args[0].toString());
            }
        });
    }

    private void createNewCommentNotification(String commentFrom) {
        playBeep();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText("You have a new comment from "+commentFrom+"!!");
        int notificationID = 100;
        mNotificationManager.notify(notificationID, mBuilder.build());

    }

    private void playBeep() {
        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(this, defaultUri);
        ringtone.play();
    }


    private void parseComment(String commentString) {
        boolean isSelf = false;
        try {
            JSONObject jsonObject = new JSONObject(commentString);
            String id = jsonObject.getString("id");
            String commentData = jsonObject.getString("comment");
            String commentFrom = jsonObject.getString("commentFrom");

            if (commentFrom.equals(mUserName)) {
                isSelf = true;
            }
            int colonIndex = commentData.trim().indexOf(":");
            commentData = commentData.substring(colonIndex+1).trim();
            createNewCommentNotification(commentFrom);

            Comment comment = new Comment(commentFrom, commentData, isSelf);
            appendMessage(id,comment);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void sendMessageToServer(String inputString) {
        String commentTo="";
        if( mSocket != null) {
            if(mTask.getAssignByName().equals(mUserName)){
                commentTo = mTask.getAssignToName();
            }else if(mTask.getAssignToName().equals(mUserName)){
                commentTo = mTask.getAssignByName();
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id",mTask.getId());
                jsonObject.put("comment",inputString);
                jsonObject.put("commentTo",commentTo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mSocket.emit("newComment", jsonObject);
        }
    }

    private void appendMessage(final String id, final Comment comment) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mTask.getId().equals(id)){
                mCommentList.add(comment);
                mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mSocket != null){
            mSocket.close();
        }
    }

}
