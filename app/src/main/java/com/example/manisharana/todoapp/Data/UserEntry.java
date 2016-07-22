package com.example.manisharana.todoapp.Data;

import android.provider.BaseColumns;

public class UserEntry implements BaseColumns {
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHOTO = "photo";


    public static String getCreateUserEntry() {
        return "CREATE TABLE "+ TABLE_NAME +" ( "
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PHOTO+ " NOT NULL );";
    }
}
