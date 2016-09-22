package com.example.manisharana.todoapp.Activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.manisharana.todoapp.Adapters.ContactListAdapter;
import com.example.manisharana.todoapp.Data.UserEntry;
import com.example.manisharana.todoapp.Fragments.ContactListFragment;
import com.example.manisharana.todoapp.R;

public class ContactActivity extends AppCompatActivity {

//    SimpleCursorAdapter adapter;

    // List View Widget
//    ListView lvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setElevation(0);

    }









//    // onTouchEvent () method gets called when User performs any touch event on screen
//    // Method to handle touch event like left to right swap and right to left swap
//    float x1, x2;
//    float y1, y2;
//
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        onTouchEvent(ev);
//        return super.dispatchTouchEvent(ev);
//    }
//
//
//    public boolean onTouchEvent(MotionEvent touchevent) {
//        switch (touchevent.getAction()) {
//            // when user first touches the screen we get x and y coordinate
//            case MotionEvent.ACTION_DOWN: {
//                x1 = touchevent.getX();
//                y1 = touchevent.getY();
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                x2 = touchevent.getX();
//                y2 = touchevent.getY();
//
//                //if left to right sweep event on screen
//                if (x1 < x2) {
//
//                    Intent intent = new Intent(this, TaskActivity.class);
//                    startActivity(intent);
//                   finish();
//                }
//
//                // if right to left sweep event on screen
//                if (x1 > x2) {
//                    // Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
//
//                    // Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
//
//                }
//
//                // if UP to Down sweep event on screen
//                if (y1 < y2) {
//                    // Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
//                }
//
//                //if Down to UP sweep event on screen
//                if (y1 > y2) {
//                    //Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
//                }
//                break;
//            }
//        }
//        return false;
//    }


}
