package com.example.sleep_app.sqLiteHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sleep_app.Dream;

public class DreamsHelper extends DatabaseHelper {
    private static final String TABLE_NAME = DatabaseTableEnum.DREAMS.getTableName();

    public DreamsHelper(Context context) {
        super(context, DatabaseTableEnum.DREAMS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        rewriteDatabase(db);
    }

    public void rewriteDatabase(SQLiteDatabase db) {
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (_id INTEGER PRIMARY KEY, " +
                    Dream.Attribute.title + " TEXT, " +
                    Dream.Attribute.lucidity + " int, " +
                    Dream.Attribute.clarity + " int, " +
                    Dream.Attribute.happiness + " int, " +
                    Dream.Attribute.recurringDream + " int, " +
                    Dream.Attribute.nightmare + " int, " +
                    Dream.Attribute.description + " TEXT, " +
                    Dream.Attribute.dateCreated + " DATETIME);";
            db.execSQL(createTableQuery);

            //db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + Dream.Attribute.happiness + " int DEFAULT 0;");
            //db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + Dream.Attribute.recurringDream + " int DEFAULT 0;");
            //db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + Dream.Attribute.nightmare + " int DEFAULT 0;");
            //db.execSQL("ALTER TABLE " + TABLE_NAME + " RENAME COLUMN " + "date_created" + " TO dateCreated;");
        } catch (Exception e) {
            Log.e("rewriteDatabase", e.toString());
        }
    }
}

