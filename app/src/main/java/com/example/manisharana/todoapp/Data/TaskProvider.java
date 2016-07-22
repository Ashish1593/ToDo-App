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
    private static SQLiteQueryBuilder sQueryTaskAndTagBuilder;
    private static SQLiteQueryBuilder sQueryTaskAndTagAndUserBuilder;

    static {
        sQueryTaskAndTagBuilder = new SQLiteQueryBuilder();
        String tableQuery =
                TaskEntry.TABLE_NAME + " INNER JOIN "
                        + TaskTagEntry.TABLE_NAME + " ON "
                        + TaskEntry.TABLE_NAME + "."
                        + TaskEntry.COLUMN_TAG_ID + " = "
                        + TaskTagEntry.TABLE_NAME + "."
                        + TaskTagEntry._ID;
        sQueryTaskAndTagBuilder.setTables(tableQuery);
        sQueryTaskAndTagAndUserBuilder = new SQLiteQueryBuilder();
        String newTableQuery = TaskEntry.TABLE_NAME + " INNER JOIN "
                + TaskTagEntry.TABLE_NAME + " ON "
                + TaskEntry.TABLE_NAME + "."
                + TaskEntry.COLUMN_TAG_ID + " = "
                + TaskTagEntry.TABLE_NAME + "."
                + TaskTagEntry._ID + " INNER JOIN "
                + UserEntry.TABLE_NAME + " ON "
                + UserEntry.TABLE_NAME + "." + UserEntry._ID +" = "
                + TaskEntry.TABLE_NAME + "." + TaskEntry.COLUMN_USER_ID ;

        sQueryTaskAndTagAndUserBuilder.setTables(newTableQuery);

    }


    static final int TASK = 100;
    static final int TASK_WITH_TAG = 101;
    static final int TASK_WITH_TAG_AND_USER = 102;

    static final int TAG = 200;

    public static UriMatcher getUriMatcher() {
        return uriMatcher;
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
        String groupByCondition="";

        switch (uriMatcher.match(uri)) {
            case TASK:
                query = readDb.query(TaskEntry.TABLE_NAME, columns, selection, selectArgs, null, null, sortOrder);
                break;
            case TAG:
                query = readDb.query(TaskTagEntry.TABLE_NAME, columns, selection, selectArgs, null, null, sortOrder);
                break;
            case TASK_WITH_TAG:
                query = sQueryTaskAndTagBuilder.query(taskDbHelper.getReadableDatabase(), columns, selection, selectArgs, groupByCondition, null, sortOrder);
                break;
            case TASK_WITH_TAG_AND_USER:
                query = sQueryTaskAndTagAndUserBuilder.query(taskDbHelper.getReadableDatabase(), columns, selection, selectArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return query;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TASK:
                return TaskEntry.CONTENT_ITEM_TYPE;
            case TASK_WITH_TAG:
                return TaskEntry.CONTENT_LIST_TYPE;
            case TASK_WITH_TAG_AND_USER:
                return TaskEntry.CONTENT_ITEM_TYPE;
            case TAG:
                return TaskTagEntry.CONTENT_LIST_TYPE;
        }
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
            case TAG:
                long insertedId = db.insert(TaskEntry.TABLE_NAME, null, contentValues);
                if (insertedId > 0)
                    returnUri = TaskTagEntry.buildTagUri(insertedId);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case TASK_WITH_TAG:
                returnUri = null;
                break;
            case TASK_WITH_TAG_AND_USER:
                returnUri = null;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String where, String[] selectArgs) {
        final SQLiteDatabase db = taskDbHelper.getWritableDatabase();
        if (null == where) where = "1";
        int count;
        switch (uriMatcher.match(uri)) {
            case TASK:
                count = db.delete(TaskEntry.TABLE_NAME, where, selectArgs);
                break;

            case TAG:
                count = db.delete(TaskTagEntry.TABLE_NAME, where, selectArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (count != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = taskDbHelper.getWritableDatabase();
        int rowsUpdated;

        switch (uriMatcher.match(uri)) {
            case TASK:
                rowsUpdated = db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TAG:
                rowsUpdated = db.update(TaskTagEntry.TABLE_NAME, values, selection, selectionArgs);
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
