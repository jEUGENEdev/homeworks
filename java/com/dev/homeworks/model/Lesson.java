package com.dev.homeworks.model;

public class Lesson {
    private String nameLesson, homeworkDescription, days, time, id;

    public Lesson(String id, String nameLesson, String homeworkDescription, String days, String time) {
        this.nameLesson = nameLesson;
        this.homeworkDescription = homeworkDescription;
        this.days = days;
        this.time = time;
        this.id = id;
    }

    public String getNameLesson() {
        return nameLesson;
    }

    public void setNameLesson(String nameLesson) {
        this.nameLesson = nameLesson;
    }

    public String getHomeworkDescription() {
        return homeworkDescription;
    }

    public void setHomeworkDescription(String homeworkDescription) {
        this.homeworkDescription = homeworkDescription;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "nameLesson='" + nameLesson + '\'' +
                ", homeworkDescription='" + homeworkDescription + '\'' +
                ", days='" + days + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
