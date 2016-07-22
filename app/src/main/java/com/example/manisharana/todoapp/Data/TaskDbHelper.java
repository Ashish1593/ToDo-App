package com.example.manisharana.todoapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class TaskDbHelper extends SQLiteOpenHelper {

    public static int DB_VERSION = 1;
    public static final String DB_NAME = "todoApp.db";
    public TaskDbHelper(Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        List<String> queries = new LinkedList<>();
        queries.add(UserEntry.getCreateUserEntry());
        queries.add(TaskTagEntry.getCreateTaskTagEntry());
        queries.add(TaskTagEntry.getInsertIntialData());
        queries.add(TaskEntry.getCreateTaskEntry());
        for(String query: queries){
            sqLiteDatabase.execSQL(query);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
    }
}
