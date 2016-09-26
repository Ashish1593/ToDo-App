//package com.example.manisharana.todoapp.Activities;
//
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.MatrixCursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.support.annotation.Nullable;
//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.CursorLoader;
//import android.support.v4.content.Loader;
//import android.support.v4.widget.SimpleCursorAdapter;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Menu;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
////import com.example.manisharana.todoapp.Adapters.ContactListAdapter;
//import com.example.manisharana.todoapp.Data.UserEntry;
////import com.example.manisharana.todoapp.Fragments.ContactListFragment;
//import com.example.manisharana.todoapp.R;
//
//import java.io.File;
//import java.io.FileOutputStream;
//
//public class ContactActivity extends AppCompatActivity {
//
//    // Cursor Adapter for storing contacts data
//    SimpleCursorAdapter adapter;
//
//    // List View Widget
//    ListView lvContacts;
//
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_contact_list);
////        getSupportActionBar().setElevation(0);
////
////
////    }
//
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contact_list);
//
//        // Init ListView
//        lvContacts = (ListView) findViewById(R.id.list_view_contact_list);
//
//        // Initialize Content Resolver object to work with content Provider
//        ContentResolver cr = getContentResolver();
//
//        // Read Contacts
//        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                new String[] { ContactsContract.Contacts._ID,
//                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME  , ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null,
//                null);
//
//        // Attached with cursor with Adapter
//        adapter = new SimpleCursorAdapter(this, R.layout.contact_list_item_left, c,
//                new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER },
//                new int[] { R.id.text_view_contact_name,R.id.text_view_contact_phone_number});
//
//        // Display data in listview
//        lvContacts.setAdapter(adapter);
//
//

//        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
//                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
//



        // On Click of each row of contact display next screen with contact
        // number
//        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View v,
//                                    int position, long id) {
//
//                Cursor c = (Cursor) adapter.getItemAtPosition(position);
//
//                String cid = c.getString(c
//                        .getColumnIndex(ContactsContract.Contacts._ID));

                // Explicit Intent Example
//                Intent iCInfo = new Intent(getApplicationContext(), CInfo.class);
//                iCInfo.putExtra("cid", cid);
//                startActivity(iCInfo);
//
//            }
//        });
//    }
//}
