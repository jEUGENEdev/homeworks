package com.dev.homeworks.database;

public class ConstantsSQL {
    public static final String NAME_DATABASE = "HOMEWORKS";
    public static final int VERSION = 1;

    public static final String TABLE_NAME_LESSONS = "lessons";
    public static final String TABLE_LESSONS_ID = "_id";
    public static final String TABLE_LESSONS_LESSON = "lesson";
    public static final String TABLE_LESSONS_HOMEWORK_DESCRIPTION = "homework_description";
    public static final String TABLE_LESSONS_DAYS = "days";
    public static final String TABLE_LESSONS_TIME = "time";
    public static final String CREATE_TABLE_LESSON = "CREATE TABLE " + TABLE_NAME_LESSONS + "(_id INTEGER" +
            " PRIMARY KEY AUTOINCREMENT, " + TABLE_LESSONS_LESSON + " TEXT, " + TABLE_LESSONS_HOMEWORK_DESCRIPTION +
            " TEXT, " + TABLE_LESSONS_DAYS + " TEXT, " + TABLE_LESSONS_TIME + " TEXT);";
    public static final String DEFAULT_ELEMENT = "INSERT INTO " + TABLE_NAME_LESSONS + "(" + TABLE_LESSONS_LESSON +
            ", " + TABLE_LESSONS_HOMEWORK_DESCRIPTION + ", " + TABLE_LESSONS_DAYS +
            ") VALUES ('EXAMPLE', 'EXAMPLE', '');";
}
