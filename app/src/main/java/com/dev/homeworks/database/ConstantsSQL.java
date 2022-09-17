package com.dev.homeworks.database;

public class ConstantsSQL {
    public static final String NAME_DATABASE = "HOMEWORKS";
    public static final int VERSION = 4;

    public static final String TABLE_NAME_LESSONS = "lessons";
    public static final String TABLE_LESSONS_ID = "_id";
    public static final String TABLE_LESSONS_LESSON = "lesson";
    public static final String TABLE_LESSONS_HOMEWORK_DESCRIPTION = "homework_description";
    public static final String TABLE_LESSONS_DAYS = "days";
    public static final String TABLE_LESSONS_TIME = "time";
    public static final String CREATE_TABLE_LESSON = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_LESSONS + "(_id INTEGER" +
            " PRIMARY KEY AUTOINCREMENT, " + TABLE_LESSONS_LESSON + " TEXT, " + TABLE_LESSONS_HOMEWORK_DESCRIPTION +
            " TEXT, " + TABLE_LESSONS_DAYS + " TEXT, " + TABLE_LESSONS_TIME + " TEXT);";
    public static final String DEFAULT_ELEMENT = "INSERT INTO " + TABLE_NAME_LESSONS + "(" + TABLE_LESSONS_LESSON +
            ", " + TABLE_LESSONS_HOMEWORK_DESCRIPTION + ", " + TABLE_LESSONS_DAYS +
            ") VALUES ('EXAMPLE', 'EXAMPLE', '');";

    public static final String TABLE_PHOTOS_NAME = "photos";
    public static final String TABLE_PHOTOS_ID = "_id";
    public static final String TABLE_PHOTOS_LESSON_ID = "lesson_id";
    public static final String TABLE_PHOTOS_PATH = "path";
    public static final String CREATE_TABLE_PHOTOS = String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT);",
            TABLE_PHOTOS_NAME, TABLE_PHOTOS_ID, TABLE_PHOTOS_LESSON_ID, TABLE_PHOTOS_PATH);
}
