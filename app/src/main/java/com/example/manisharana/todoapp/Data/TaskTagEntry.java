package com.example.manisharana.todoapp.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class TaskTagEntry implements BaseColumns {
    public static final String TABLE_NAME = "task_tag";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_COLOR = "color";
    private static final String DEFAULT_VALUE_HOME = "Home";
    private static final String DEFAULT_COLOR_HOME = "###";
    private static final String DEFAULT_VALUE_WORK = "Work";
    private static final String DEFAULT_COLOR_WORK = "###";
    private static final String DEFAULT_VALUE_PERSONAL = "Personal";
    private static final String DEFAULT_COLOR_PERSONAL = "####";
    private static final String DEFAULT_VALUE_SHOPPING = "Shopping";
    private static final String DEFAULT_COLOR_SHOPPING = "####";

    public static final String CONTENT_AUTHORITY = "com.example.manisharana.sunshine";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    private static final String TASK_TAG_PATH = "taskTag";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TASK_TAG_PATH).build();
    public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+ CONTENT_AUTHORITY+"/"+TASK_TAG_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+ CONTENT_AUTHORITY+"/"+TASK_TAG_PATH;


    public static String getCreateTaskTagEntry() {
        return "CREATE TABLE "+ TABLE_NAME + " ( "
                + _ID + "INTEGER PRIMARY KEY"
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_COLOR + " TEXT NOT NULL );";
    }

    public static String getInsertIntialData() {
        return "INSERT INTO "+ TABLE_NAME + " ( "+COLUMN_TITLE+" , "+COLUMN_COLOR
                +") VALUES ( "+DEFAULT_VALUE_HOME+", "+ DEFAULT_COLOR_WORK +" ), ( "
                + DEFAULT_VALUE_WORK+", "+ DEFAULT_COLOR_WORK +" ), ( "
                + DEFAULT_VALUE_PERSONAL+", "+ DEFAULT_COLOR_PERSONAL +" ), ( "
                + DEFAULT_VALUE_SHOPPING+", "+ DEFAULT_COLOR_SHOPPING +" );" ;
    }

    public static Uri buildTagUri(long insertedId) {
        return ContentUris.withAppendedId(CONTENT_URI,insertedId);
    }
}
