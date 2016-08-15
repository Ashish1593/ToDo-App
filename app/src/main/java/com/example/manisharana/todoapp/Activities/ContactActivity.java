package com.example.manisharana.todoapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.R;
import com.example.manisharana.todoapp.Sync.ToDoSyncAdapter;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setElevation(0);
        ProgressDialog progressDialog = new Utility(this).getProgressDialog("Fetching Contacts");
        progressDialog.show();
        ToDoSyncAdapter.initializeSyncAdapter(this);
        progressDialog.dismiss();
    }


}
