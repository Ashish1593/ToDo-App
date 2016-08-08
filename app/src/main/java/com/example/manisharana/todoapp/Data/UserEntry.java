package com.example.manisharana.todoapp.Data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class UserEntry implements BaseColumns {
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_NAME = "name";
  //  public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String USER_PATH = "user";

    public static final String CONTENT_AUTHORITY = "com.example.manisharana.todoapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(USER_PATH).build();



    public static String getCreateUserEntry() {
        return "CREATE TABLE "+ TABLE_NAME +" ( "
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT UNIQUE NOT NULL, "
                 + COLUMN_PHONE_NUMBER + " TEXT );";
    }

    public static Uri buildUserUri(long insert) {
        return ContentUris.withAppendedId(CONTENT_URI,insert);
    }

}
