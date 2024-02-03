package com.example.sleep_app.sqLiteHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseAccess {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public BaseAccess(Context context, DatabaseTableEnum databaseTableEnum) {
        dbHelper = new DatabaseHelper(context, databaseTableEnum);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
