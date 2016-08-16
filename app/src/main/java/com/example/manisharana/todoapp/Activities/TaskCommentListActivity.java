package com.example.manisharana.todoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
    private Utility mUtility;
    private ArrayList<Comment> mCommentList;
    private CommentListAdapter mAdapter;
    private Task mTask;
    private Socket mSocket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            Intent intent = getIntent();
            mTask = (Task) intent.getSerializableExtra("Task");
            if (mTask != null) {
                setTitle(mTask.getTitle());
            }

        }

        setContentView(R.layout.activity_task_comments);
        mSendBtn = (Button) findViewById(R.id.btnSend);
        mInputMsg = (EditText) findViewById(R.id.inputMsg);
        mListview = (ListView) findViewById(R.id.list_view_messages);
        mUtility = new Utility(this);
        mSocket = ((MyApplication)getApplication()).getSocket();


        connectWebSocket();
        mCommentList = new ArrayList<Comment>();
        mAdapter = new CommentListAdapter(this, mCommentList);
        mListview.setAdapter(mAdapter);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mInputMsg.getText().toString();
                mCommentList.add(new Comment(mTask.getAssignByName(),input,true));
                mAdapter.notifyDataSetChanged();
                sendMessageToServer(input);
                mInputMsg.setText("");
            }
        });
    }

    private void connectWebSocket() {
        mSocket.on("private", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("on new message",args[0].toString());
                parseMessage(args[0].toString());
            }

        });
        mSocket.connect();
    }

    private void parseMessage(String message) {
        boolean isSelf = false;
        try {
            JSONObject jsonObject = new JSONObject(message);
            String fromName = jsonObject.getString("name");
            String commentData = jsonObject.getString("msg");

            if (fromName.equals(mUtility.getFromPreferences("UserName"))) {
                isSelf = true;
            }
            int colonIndex = commentData.indexOf(":");
            commentData = commentData.substring(colonIndex+1);

            Comment comment = new Comment(fromName, commentData, isSelf);
            appendMessage(comment);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void sendMessageToServer(String inputString) {
        if( mSocket != null)
            mSocket.emit("sendmessage",inputString);
    }

    private void appendMessage(final Comment comment) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mCommentList.add(comment);
                mAdapter.notifyDataSetChanged();
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
