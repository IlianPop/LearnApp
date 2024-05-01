package com.example.myapplication;

public class Result {
    private String name, lesson_theme, result, id;

    public Result() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLesson_theme() {
        return lesson_theme;
    }

    public void setLesson_theme(String lesson_theme) {
        this.lesson_theme = lesson_theme;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Result(String name, String lesson_theme, String result, String id) {
        this.name = name;
        this.lesson_theme = lesson_theme;
        this.result = result;
        this.id = id;
    }
}
