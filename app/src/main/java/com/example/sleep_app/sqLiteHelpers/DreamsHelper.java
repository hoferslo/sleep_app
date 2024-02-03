package com.example.sleep_app.sqLiteHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.sleep_app.Dream;

public class DreamsHelper extends DatabaseHelper {
    private static final String TABLE_NAME = DatabaseTableEnum.DREAMS.getTableName();

    public DreamsHelper(Context context) {
        super(context, DatabaseTableEnum.DREAMS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (_id INTEGER PRIMARY KEY, " +
                Dream.Attribute.title + " TEXT, " +
                Dream.Attribute.lucidity + " int, " +
                Dream.Attribute.clarity + " int, " +
                Dream.Attribute.feeling + " TEXT, " +
                Dream.Attribute.description + " TEXT, " +
                Dream.Attribute.date_created + " DATETIME);";
        db.execSQL(createTableQuery);
    }
}

