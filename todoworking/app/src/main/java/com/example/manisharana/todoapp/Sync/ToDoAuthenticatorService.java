package com.example.manisharana.todoapp.Sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ToDoAuthenticatorService extends Service {
    private ToDoAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        mAuthenticator = new ToDoAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}

