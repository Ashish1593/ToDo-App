package com.example.manisharana.todoapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.manisharana.todoapp.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
    }

    public void  navigateToTaskActivity(View view){
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

}
