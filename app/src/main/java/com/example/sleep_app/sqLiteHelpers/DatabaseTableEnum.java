package com.example.sleep_app.sqLiteHelpers;

public enum DatabaseTableEnum {
    DREAMS("dreams.db", "dreams");


    private final String dbName;
    private final String tableName;

    DatabaseTableEnum(String dbName, String tableName) {
        this.dbName = dbName;
        this.tableName = tableName;
    }

    public String getDbName() {
        return dbName;
    }

    public String getTableName() {
        return tableName;
    }
}

