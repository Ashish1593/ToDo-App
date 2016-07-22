package com.example.manisharana.todoapp.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class UserEntry implements BaseColumns {
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHOTO = "photo";
    public static final String USER_PATH = "user";

    public static final String CONTENT_AUTHORITY = "com.example.manisharana.todoapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(USER_PATH).build();
    private static final String DEFAULT_VALUE_MONIKA = "Monika";
    private static final String DEFAULT_VALUE_SANDHYA = "Sandhya";
    private static final String DEFAULT_VALUE_NEHA = "Neha";
    private static final String DEFAULT_VALUE_NIKHIL = "Nikhil";
    private static final String DEFAULT_VALUE_NITHIN = "Nithin";


    public static String getCreateUserEntry() {
        return "CREATE TABLE "+ TABLE_NAME +" ( "
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME + " TEXT NOT NULL);";
    }

    public static String getInsertIntialData() {
        return "INSERT INTO "+ TABLE_NAME + " ( "+COLUMN_NAME
                +" ) VALUES ( "+DEFAULT_VALUE_MONIKA+" ), ( "
                + DEFAULT_VALUE_SANDHYA+ " ), ( "
                + DEFAULT_VALUE_NEHA+ " ), ( "
                + DEFAULT_VALUE_NIKHIL+ " ), ( "
                + DEFAULT_VALUE_NITHIN+" );" ;
    }

}
