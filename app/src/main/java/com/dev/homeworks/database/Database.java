package com.dev.homeworks.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.dev.homeworks.model.Lesson;
import com.dev.homeworks.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private final SQLiteDatabase database;
    private final SQLConfig sqlConfig;

    public Database(Context context) {
        sqlConfig = new SQLConfig(context);
        database = sqlConfig.getWritableDatabase();

//        Cursor cursor = database.rawQuery("SELECT * FROM photos;", null);
//        while(cursor.moveToNext())
//            Log.i("db info", cursor.getString(1));
//        cursor.close();
    }

    public void close() {
        database.close();
        sqlConfig.close();
    }

    public void insert(String nameLesson, String homeworkDescription) {
        database.execSQL("INSERT INTO " + ConstantsSQL.TABLE_NAME_LESSONS + "(" + ConstantsSQL.TABLE_LESSONS_LESSON +
                ", " + ConstantsSQL.TABLE_LESSONS_HOMEWORK_DESCRIPTION + ") VALUES ('" + nameLesson +
                "', '" + homeworkDescription + "');");
    }

    public Lesson insert(String nameLesson, String homeworkDescription, String days, String time) {
        database.execSQL("INSERT INTO " + ConstantsSQL.TABLE_NAME_LESSONS + "(" + ConstantsSQL.TABLE_LESSONS_LESSON +
                ", " + ConstantsSQL.TABLE_LESSONS_HOMEWORK_DESCRIPTION + ", '" + ConstantsSQL.TABLE_LESSONS_DAYS
                + "', '" + ConstantsSQL.TABLE_LESSONS_TIME + "') VALUES ('" + nameLesson +
                "', '" + homeworkDescription + "', '" + days + "', '" + time + "');");
        List<Lesson> lessons = selectAll(null, nameLesson, homeworkDescription);
        return lessons.size() > 0 ? lessons.get(0) : new Lesson("0", "error", "error", "error", "error");
    }

    public List<Lesson> selectAll(String id, String lesson, String lessonDescription) {
        List<Lesson> lessons = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(ConstantsSQL.TABLE_NAME_LESSONS);
        String sqlRequest;
        if ((id == null) && (lesson == null) && (lessonDescription == null))
            sqlRequest = sql.toString();
        else {
            sql.append(" WHERE");
            if (id != null)
                sql.append(" ").append(ConstantsSQL.TABLE_LESSONS_ID).append(" = ").append(id).append(" and");
            if (lesson != null)
                sql.append(" ").append(ConstantsSQL.TABLE_LESSONS_LESSON).append(" = '").append(lesson).append("' and");
            if (lessonDescription != null)
                sql.append(" ").append(ConstantsSQL.TABLE_LESSONS_HOMEWORK_DESCRIPTION).append(" = '").append(lessonDescription).append("' and");
            sqlRequest = sql.substring(0, sql.length() - 4) + ";";
        }
        Cursor cursor = database.rawQuery(sqlRequest, null);
        while (cursor.moveToNext())
            lessons.add(new Lesson(cursor.getString(cursor.getColumnIndex(ConstantsSQL.TABLE_LESSONS_ID)),
                    cursor.getString(cursor.getColumnIndex(ConstantsSQL.TABLE_LESSONS_LESSON)),
                    cursor.getString(cursor.getColumnIndex(ConstantsSQL.TABLE_LESSONS_HOMEWORK_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(ConstantsSQL.TABLE_LESSONS_DAYS)),
                    cursor.getColumnName(cursor.getColumnIndex(ConstantsSQL.TABLE_LESSONS_TIME))));
        cursor.close();
        return lessons;
    }

    public List<Photo> selectPhotoByLessonId(int lessonId) {
        List<Photo> photos = new ArrayList<>();
        try (Cursor cursor = database.rawQuery(String.format("SELECT * FROM photos WHERE %s = %s", ConstantsSQL.TABLE_PHOTOS_LESSON_ID, lessonId), null)) {
            while (cursor.moveToNext())
                photos.add(new Photo(Uri.parse(cursor.getString(cursor.getColumnIndex(ConstantsSQL.TABLE_PHOTOS_PATH)))));
        }
        return photos;
    }

    public void insertPhotoByLessonId(String path, int lessonId) {
        database.execSQL(String.format("INSERT INTO %s (%s, %s) VALUES (%s, '%s');", ConstantsSQL.TABLE_PHOTOS_NAME, ConstantsSQL.TABLE_PHOTOS_LESSON_ID, ConstantsSQL.TABLE_PHOTOS_PATH, lessonId, path));
    }

    public void update(String id, String lesson, String description) {
        database.execSQL("UPDATE " + ConstantsSQL.TABLE_NAME_LESSONS + " SET " + ConstantsSQL.TABLE_LESSONS_LESSON +
                " = '" + lesson + "', " + ConstantsSQL.TABLE_LESSONS_HOMEWORK_DESCRIPTION + " = '" + description +
                "' WHERE " + ConstantsSQL.TABLE_LESSONS_ID + " = " + id + ";");
    }

    public void delete(String id) {
        database.execSQL("DELETE FROM " + ConstantsSQL.TABLE_NAME_LESSONS + " WHERE " + ConstantsSQL.TABLE_LESSONS_ID
                + " = " + id + ";");
        database.execSQL(String.format("DELETE FROM %s WHERE %s = %s;", ConstantsSQL.TABLE_PHOTOS_NAME, ConstantsSQL.TABLE_PHOTOS_LESSON_ID, id));
    }
}