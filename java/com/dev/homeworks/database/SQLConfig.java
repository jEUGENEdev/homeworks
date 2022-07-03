package com.dev.homeworks.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLConfig extends SQLiteOpenHelper {
    public SQLConfig(@Nullable Context context) {
        super(context, ConstantsSQL.NAME_DATABASE, null, ConstantsSQL.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstantsSQL.CREATE_TABLE_LESSON);
        db.execSQL(ConstantsSQL.DEFAULT_ELEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
