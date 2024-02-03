package com.example.sleep_app.sqLiteHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sleep_app.Dream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DreamsAccess extends BaseAccess {

    Context context;
    SQLiteDatabase db;

    public DreamsAccess(Context context) {
        super(context, DatabaseTableEnum.DREAMS);
        this.context = context;

        DreamsHelper dreamsHelper = new DreamsHelper(context);
        db = dreamsHelper.getWritableDatabase();
    }

    public long saveDream(Dream dream) {
        ContentValues values = new ContentValues();
        values.put(Dream.Attribute.title.toString(), dream.getTitle());
        values.put(Dream.Attribute.lucidity.toString(), dream.getLucidity());
        values.put(Dream.Attribute.clarity.toString(), dream.getClarity());
        values.put(Dream.Attribute.feeling.toString(), dream.getFeeling());
        values.put(Dream.Attribute.description.toString(), dream.getDescription());
        values.put(Dream.Attribute.date_created.toString(), DreamsHelper.DateTimeToString(dream.getDateCreated()));

        return db.insert(DatabaseTableEnum.DREAMS.getTableName(), null, values);
    }

    public boolean deleteDream(long dreamId) {
        String whereClause = "_id = ?";
        String[] whereArgs = { String.valueOf(dreamId) };

        int rowsDeleted = getDatabase().delete(DatabaseTableEnum.DREAMS.getTableName(), whereClause, whereArgs);

        // If rowsDeleted > 0, deletion was successful
        return rowsDeleted > 0;
    }

    public List<Dream> getDreams() {
        List<Dream> dreamList = new ArrayList<>();

        String[] projection = new String[Dream.Attribute.values().length];

        for (int i = 0; i < Dream.Attribute.values().length; i++) {
            projection[i] = Dream.Attribute.values()[i].toString();
        }

        Cursor cursor = getDatabase().query(
                DatabaseTableEnum.DREAMS.getTableName(),
                projection,
                null,
                null,
                null,
                null,
                Dream.Attribute.date_created + " DESC" // Order by date_created in descending order
        );

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    long dreamId = cursor.getLong(cursor.getColumnIndexOrThrow(Dream.Attribute._id.toString()));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(Dream.Attribute.title.toString()));
                    int lucidity = cursor.getInt(cursor.getColumnIndexOrThrow(Dream.Attribute.lucidity.toString()));
                    int clarity = cursor.getInt(cursor.getColumnIndexOrThrow(Dream.Attribute.clarity.toString()));
                    String feeling = cursor.getString(cursor.getColumnIndexOrThrow(Dream.Attribute.feeling.toString()));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(Dream.Attribute.description.toString()));
                    LocalDateTime dateCreated = DreamsHelper.stringToDateTime(cursor.getString(cursor.getColumnIndexOrThrow(Dream.Attribute.date_created.toString())));

                    Dream dream = new Dream(dreamId, title, lucidity, clarity, feeling, description, dateCreated);
                    dreamList.add(dream);
                }
            } finally {
                cursor.close();
            }
        }

        return dreamList;
    }

    public boolean editDream(long dreamId, String title, int lucidity, int clarity, String feeling, String description, LocalDateTime date_created) {
        ContentValues values = new ContentValues();
        values.put(Dream.Attribute.title.toString(), title);
        values.put(Dream.Attribute.lucidity.toString(), lucidity);
        values.put(Dream.Attribute.clarity.toString(), clarity);
        values.put(Dream.Attribute.feeling.toString(), feeling);
        values.put(Dream.Attribute.description.toString(), description);
        values.put(Dream.Attribute.date_created.toString(), DreamsHelper.DateTimeToString(date_created));

        String whereClause = Dream.Attribute._id + " = ?";
        String[] whereArgs = { String.valueOf(dreamId) };

        int rowsUpdated = getDatabase().update(DatabaseTableEnum.DREAMS.getTableName(), values, whereClause, whereArgs);

        // If rowsUpdated > 0, update was successful
        return rowsUpdated > 0;
    }

}
