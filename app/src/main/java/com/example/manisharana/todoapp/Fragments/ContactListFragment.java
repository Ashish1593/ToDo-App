package com.example.manisharana.todoapp.Fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.manisharana.todoapp.Adapters.ContactListAdapter;
import com.example.manisharana.todoapp.Adapters.TaskListAdapter;
import com.example.manisharana.todoapp.Data.TaskEntry;
import com.example.manisharana.todoapp.Data.UserEntry;
import com.example.manisharana.todoapp.R;

public class ContactListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CONTACT_LIST_LOADER = 0;
    private ContactListAdapter contactListAdapter;
    private static final String[] CONTACT_LIST_COLUMNS = {
            UserEntry.TABLE_NAME + "." + TaskEntry._ID,
            UserEntry.COLUMN_NAME,
            UserEntry.COLUMN_PHONE_NUMBER,
            UserEntry.COLUMN_PHOTO,
            };
    public static final int COL_CONTACT_ID = 0;
    public static final int COL_CONTACT_NAME = 1;
    public static final int COL_CONTACT_PHONE_NUMBER = 2;
    public static final int COL_CONTACT_PHOTO = 3;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = UserEntry.COLUMN_NAME + " ASC";
        return new CursorLoader(getActivity(), UserEntry.CONTENT_URI, CONTACT_LIST_COLUMNS, null, null, sortOrder);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_contact_list);
        contactListAdapter = new ContactListAdapter(getActivity(), null, 0);
        listView.setAdapter(contactListAdapter);
        return rootView;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        contactListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contactListAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CONTACT_LIST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }
}
