package com.example.manisharana.todoapp.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
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
        queries.add(UserEntry.getInsertIntialData());
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

    public List<String> getAllTags(){
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TaskTagEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return labels;
    }

    public List<String> getAllUsers(){
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + UserEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return labels;
    }

    public int getTagId(String title) {
        int tagId = 0;
        String selectQuery = "SELECT  * FROM " + TaskTagEntry.TABLE_NAME+ " where "+TaskTagEntry.COLUMN_TITLE+" = ? limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,new String[]{title});

        if (cursor.moveToFirst()) {
                tagId = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return tagId;

    }

    public int getUserId(String name) {
        int userId = 0;
        String selectQuery = "SELECT  * FROM " + UserEntry.TABLE_NAME+ " where "+UserEntry.COLUMN_NAME+" = ? limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,new String[]{name});

        if (cursor.moveToFirst()) {
                userId = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return userId;

    }
}
