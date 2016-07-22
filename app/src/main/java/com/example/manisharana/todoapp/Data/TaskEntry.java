package com.example.manisharana.todoapp.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class TaskEntry implements BaseColumns{
    public static final String TABLE_NAME = "task";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_REMIND_ME = "remind_me_flag";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_STATUS = "state";
    public static final String COLUMN_TAG_ID = "task_tag_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_ATTACHMENT_ID = "attachment_id";

    private static final String TASK_PATH = "task";

    public static final String CONTENT_AUTHORITY = "com.example.manisharana.todoapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TASK_PATH).build();
    public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+ CONTENT_AUTHORITY+"/"+TASK_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+ CONTENT_AUTHORITY+"/"+TASK_PATH;


    public static String getCreateTaskEntry() {
        return "CREATE TABLE "+ TABLE_NAME + " ( "
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_DATE + "INTEGER NOT NULL, "
                + COLUMN_DESC + " TEXT, "
                + COLUMN_STATUS + " TEXT, "
                + COLUMN_REMIND_ME + " BOOLEAN NOT NULL );";
    }

    public static Uri buildTaskUri(long insert) {
        return ContentUris.withAppendedId(CONTENT_URI,insert);
    }
}
