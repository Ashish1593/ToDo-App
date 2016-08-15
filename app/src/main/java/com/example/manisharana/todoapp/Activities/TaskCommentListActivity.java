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
import android.widget.Toast;

import com.example.manisharana.todoapp.Adapters.CommentListAdapter;
import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.Models.Comment;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
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
    private WebSocketClient mWebSocketClient;
    private Task mTask;

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

        connectWebSocket();
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToServer(mUtility.getSendMessageJSON(mInputMsg.getText().toString()));
                mInputMsg.setText("");
            }
        });
        mCommentList = new ArrayList<Comment>();
        mAdapter = new CommentListAdapter(this, mCommentList);
        mListview.setAdapter(mAdapter);

    }

    private void connectWebSocket() {
        try {
            final Socket socket = IO.socket("http://192.168.42.174:3000/");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.i("LOgggggggg","On call connected");
                    socket.emit("joinroom", "stackoverflow");
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.i("LOgggggggg","On call connected");
                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


//        URI uri = null;
//        try {
//            uri = new URI("ws://192.168.42.174:3000/");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        mWebSocketClient = new WebSocketClient(uri) {
//
//            @Override
//            public void onOpen(ServerHandshake handshakedata) {
//                Log.i(TAG,"WebSocket Open");
//            }
//
//            @Override
//            public void onMessage(String message) {
//                Log.d(TAG, String.format("Got string message! %s", message));
//
//                parseMessage(message);
//            }
//
//            @Override
//            public void onClose(int code, String reason, boolean remote) {
//                String message = String.format("Disconnected! Code: %d Reason: %s", code, reason);
//                showToast(message);
//
//                mUtility.storeSessionId(null);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                Log.e(TAG, "Error! : " + error);
//
//                showToast("Error! : " + error);
//            }
//        };
//        mWebSocketClient.connect();
//    }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            String fromName = jsonObject.getString("name");
            String commentData = jsonObject.getString("message");
            String sessionId = jsonObject.getString("sessionId");
            boolean isSelf = true;

            if (!sessionId.equals(mUtility.getSessionId())) {
                fromName = jsonObject.getString("name");
                isSelf = false;
            }

            Comment comment = new Comment(fromName, commentData, isSelf);
            appendMessage(comment);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void sendMessageToServer(String sendMessageJSON) {
        if(mWebSocketClient != null)
            mWebSocketClient.send(sendMessageJSON);
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

        if(mWebSocketClient != null){
            mWebSocketClient.close();
        }
    }

}
