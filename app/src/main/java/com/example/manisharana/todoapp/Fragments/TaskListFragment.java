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

import com.example.manisharana.todoapp.Adapters.TaskListAdapter;
import com.example.manisharana.todoapp.Data.TaskEntry;
import com.example.manisharana.todoapp.R;

public class TaskListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int TASK_LIST_LOADER = 0;
    private TaskListAdapter taskListAdapter;
    private static final String[] TASK_LIST_COLUMNS = {
            TaskEntry.TABLE_NAME + "." + TaskEntry._ID,
            TaskEntry.COLUMN_TITLE,
            TaskEntry.COLUMN_DATE,
            TaskEntry.COLUMN_DESC,
            TaskEntry.COLUMN_REMIND_ME,
            TaskEntry.COLUMN_STATUS,
            };
    public static final int COL_TASK_ID = 0;
    public static final int COL_TASK_TITLE = 1;
    public static final int COL_TASK_DATE = 2;
    public static final int COL_TASK_DESC = 3;
    public static final int COL_TASK_REMIND_ME = 4;
    public static final int COL_TASK_STATUS = 5;
    public static final int COL_USER_NAME = 8;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_task_list);
        taskListAdapter = new TaskListAdapter(getActivity(), null, 0);
        listView.setAdapter(taskListAdapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_edit_tag_list:
//                Intent intent = new Intent(getActivity(), TagListActivity.class);
//                startActivity(intent);
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.activity_main_menu,menu);
//    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = TaskEntry.COLUMN_DATE + " ASC";
        Uri contentUri = TaskEntry.CONTENT_URI.buildUpon().appendPath("all").build();
        return new CursorLoader(getActivity(), contentUri, TASK_LIST_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        taskListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        taskListAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(TASK_LIST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }
}
