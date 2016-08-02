package com.example.manisharana.todoapp.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

public class TaskProvider extends ContentProvider {

    private TaskDbHelper taskDbHelper;
    private static UriMatcher uriMatcher = getUriMatcher();

    static final int TASK = 100;
    static final int ALL_TASKS = 101;

    static final int USERS = 300;


    public static UriMatcher getUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TaskEntry.CONTENT_AUTHORITY;

        matcher.addURI(authority, TaskEntry.TASK_PATH + "/*", ALL_TASKS);
        matcher.addURI(authority, UserEntry.USER_PATH, USERS);
        matcher.addURI(authority, TaskEntry.TASK_PATH, TASK);

        return matcher;

    }

    @Override
    public boolean onCreate() {
        taskDbHelper = new TaskDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectArgs, String sortOrder) {
        SQLiteDatabase readDb = taskDbHelper.getReadableDatabase();
        Cursor query;
        String groupByCondition=TaskEntry.COLUMN_DATE;

        switch (uriMatcher.match(uri)) {
            case TASK:
                query = readDb.query(TaskEntry.TABLE_NAME, columns, selection, selectArgs, null, null, sortOrder);
                break;
            case ALL_TASKS:
                query = readDb.query(TaskEntry.TABLE_NAME, columns, selection, selectArgs, groupByCondition, null, sortOrder);
                break;
            case USERS:
                query = readDb.query(UserEntry.TABLE_NAME,columns,selection,selectArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return query;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = taskDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case TASK:
                long insert = db.insert(TaskEntry.TABLE_NAME, null, contentValues);
                if (insert > 0)
                    returnUri = TaskEntry.buildTaskUri(insert);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case ALL_TASKS:
                returnUri = null;
                break;
            case USERS:
                long insertedUserId = db.insert(UserEntry.TABLE_NAME, null, contentValues);
                if (insertedUserId > 0)
                    returnUri = UserEntry.buildUserUri(insertedUserId);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String where, String[] selectArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = taskDbHelper.getWritableDatabase();
        int rowsUpdated;

        switch (uriMatcher.match(uri)) {
            case TASK:
                rowsUpdated = db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
