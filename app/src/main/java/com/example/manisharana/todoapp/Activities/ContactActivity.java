package com.example.manisharana.todoapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.manisharana.todoapp.R;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setElevation(0);
    }


}
