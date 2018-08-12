package com.example.czd.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper {
    public static final String recordurldb = "recorddb.db";
    public static final String creat_table = "create table ourl("
            + "id integer primary key autoincrement,"
            + "title varchar(100) not null,"
            + "url varchar(150) not null"
            + ");";


    public SQLiteDbHelper(Context context){
        super(context, recordurldb, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creat_table);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
