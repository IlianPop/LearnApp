package com.example.myapplication;

public class Lesson {
    public Lesson() {
    }

    private String theme, goal, description, url, id, author_mail, author_name, search_name, tests;

    public Lesson(String theme, String goal, String description, String url, String id, String author_mail, String author_name, String tests) {
        this.theme = theme;
        this.goal = goal;
        this.description = description;
        this.url = url;
        this.id = id;
        this.author_mail=author_mail;
        this.author_name=author_name;
        this.search_name=theme.toUpperCase();
        this.tests = tests;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getSearch_name() {
        return search_name;
    }

    public void setSearch_name(String search_name) {
        this.search_name = search_name;
    }

    public String getAuthor_mail() {
        return author_mail;
    }

    public void setAuthor_mail(String author_mail) {
        this.author_mail = author_mail;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
