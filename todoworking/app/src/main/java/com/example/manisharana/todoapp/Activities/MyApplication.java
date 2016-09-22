package com.example.manisharana.todoapp.Activities;

import android.app.Application;

import com.auth0.lock.Lock;
import com.auth0.lock.LockProvider;
import com.example.manisharana.todoapp.R;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MyApplication extends Application implements LockProvider {

    private Lock lock;
    private Socket mSocket;

    public void onCreate() {
        super.onCreate();
        lock = new Lock.Builder()
                .loadFromApplication(this)
                .closable(true)
                .build();
        try {
            mSocket = IO.socket(getString(R.string.sample_api_base_url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
    @Override
    public Lock getLock() {
        return lock;
    }
}

