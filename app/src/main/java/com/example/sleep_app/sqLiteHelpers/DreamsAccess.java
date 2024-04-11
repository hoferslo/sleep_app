package com.example.sleep_app.sqLiteHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
        values.put(Dream.Attribute.happiness.toString(), dream.getHappiness());
        values.put(Dream.Attribute.recurringDream.toString(), dream.getRecurringDream());
        values.put(Dream.Attribute.nightmare.toString(), dream.getNightmare());
        values.put(Dream.Attribute.description.toString(), dream.getDescription());
        values.put(Dream.Attribute.dateCreated.toString(), DreamsHelper.DateTimeToDateTimeString(dream.getDateCreated()));


        long insertedRowId = db.insert(DatabaseTableEnum.DREAMS.getTableName(), null, values);

        if (insertedRowId != -1) {
            // Log success
            Log.d("DreamDatabase", "Dream saved successfully. Row ID: " + insertedRowId);
        } else {
            // Log failure
            Log.e("DreamDatabase", "Failed to save dream to the database");
        }

        return insertedRowId;
    }

    public boolean deleteDream(long dreamId) {
        String whereClause = "_id = ?";
        String[] whereArgs = {String.valueOf(dreamId)};

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
                Dream.Attribute.dateCreated + " DESC" // Order by date_created in descending order
        );

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    long dreamId = cursor.getLong(cursor.getColumnIndexOrThrow(Dream.Attribute._id.toString()));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(Dream.Attribute.title.toString()));
                    int lucidity = cursor.getInt(cursor.getColumnIndexOrThrow(Dream.Attribute.lucidity.toString()));
                    int clarity = cursor.getInt(cursor.getColumnIndexOrThrow(Dream.Attribute.clarity.toString()));
                    int happiness = cursor.getInt(cursor.getColumnIndexOrThrow(Dream.Attribute.happiness.toString()));
                    int recurringDream = cursor.getInt(cursor.getColumnIndexOrThrow(Dream.Attribute.recurringDream.toString()));
                    int nightmare = cursor.getInt(cursor.getColumnIndexOrThrow(Dream.Attribute.nightmare.toString()));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(Dream.Attribute.description.toString()));
                    LocalDateTime dateCreated = DreamsHelper.dateTimeStringToDateTime(cursor.getString(cursor.getColumnIndexOrThrow(Dream.Attribute.dateCreated.toString())));

                    Dream dream = new Dream(dreamId, title, lucidity, clarity, happiness, recurringDream, nightmare, description, dateCreated);
                    dreamList.add(dream);
                }
            } finally {
                cursor.close();
            }
        }

        return dreamList;
    }

    public boolean editDream(Dream dream) {
        ContentValues values = new ContentValues();
        values.put(Dream.Attribute.title.toString(), dream.getTitle());
        values.put(Dream.Attribute.lucidity.toString(), dream.getLucidity());
        values.put(Dream.Attribute.clarity.toString(), dream.getClarity());
        values.put(Dream.Attribute.happiness.toString(), dream.getHappiness());
        values.put(Dream.Attribute.recurringDream.toString(), dream.getRecurringDream());
        values.put(Dream.Attribute.nightmare.toString(), dream.getNightmare());
        values.put(Dream.Attribute.description.toString(), dream.getDescription());
        values.put(Dream.Attribute.dateCreated.toString(), DreamsHelper.DateTimeToDateTimeString(dream.getDateCreated()));

        String whereClause = Dream.Attribute._id + " = ?";
        String[] whereArgs = {String.valueOf(dream.getId())};

        int rowsUpdated = getDatabase().update(DatabaseTableEnum.DREAMS.getTableName(), values, whereClause, whereArgs);

        // If rowsUpdated > 0, update was successful
        return rowsUpdated > 0;
    }

}
