package com.dev.homeworks.model;

import android.net.Uri;

import java.io.Serializable;

public class Photo implements Serializable {
    private int id, lessonId;
    private final Uri path;

    public Photo(Uri path) {
        this.path = path;
    }

    public Uri getPath() {
        return path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }
}
