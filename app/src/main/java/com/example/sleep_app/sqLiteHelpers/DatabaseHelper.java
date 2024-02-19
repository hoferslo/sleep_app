package com.example.sleep_app.sqLiteHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private final String databaseName;
    private final String tableName;

    public DatabaseHelper(Context context, DatabaseTableEnum databaseTableEnum) {
        super(context, databaseTableEnum.getDbName(), null, DATABASE_VERSION);
        this.databaseName = databaseTableEnum.getDbName();
        this.tableName = databaseTableEnum.getTableName();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Override this method in subclasses to create tables
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Override this method in subclasses for database upgrades
    }

    public static String DateTimeToDateTimeString(LocalDateTime dateTime){
        return(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    public static String DateTimeToDateString(LocalDateTime dateTime){
        return(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public static LocalDateTime dateTimeStringToDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public static LocalDateTime dateStringToDateTime(String dateTimeStr) {
        LocalDate localDate = LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return localDate.atStartOfDay();
    }
}
