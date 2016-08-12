package com.example.manisharana.todoapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.manisharana.todoapp.Adapters.ContactListAdapter;
import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.Data.UserEntry;
import com.example.manisharana.todoapp.Models.User;
import com.example.manisharana.todoapp.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ContactListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ContactListFragment.class.getSimpleName();
    private static final int CONTACT_LOADER = 0;
    private static final String[] CONTACT_COLUMNS = {
            UserEntry.TABLE_NAME + "." + UserEntry._ID,
            UserEntry.COLUMN_NAME,
            UserEntry.COLUMN_PHOTO_URI,
            UserEntry.COLUMN_PHONE_NUMBER
    };
    public static final int COL_CONTACT_ID = 0;
    public static final int COL_CONTACT_NAME = 1;
    public static final int COL_CONTACT_PHOTO = 2;
    public static final int COL_CONTACT_PHONE_NUMBER = 3;

    private ContactListAdapter contactListAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_contact_list);

        contactListAdapter = new ContactListAdapter(getActivity(), null, 0);
        listView.setAdapter(contactListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                Intent intent = new Intent();
                intent.putExtra("UserName", cursor.getString(ContactListFragment.COL_CONTACT_NAME));
                intent.putExtra("UserPhone", cursor.getString(ContactListFragment.COL_CONTACT_PHONE_NUMBER));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = UserEntry.COLUMN_NAME + " ASC";
        return new CursorLoader(getActivity(), UserEntry.CONTENT_URI, CONTACT_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        contactListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contactListAdapter.swapCursor(null);
    }
}
