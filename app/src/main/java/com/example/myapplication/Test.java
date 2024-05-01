package com.example.myapplication;

public class Test {
    private String question, a_answer, b_answer, v_answer, answer, id, author_mail, lesson_theme;

    public Test() {
    }

    public Test(String question, String a_answer, String b_answer, String v_answer, String answer, String id, String author_mail, String lesson_theme) {
        this.question = question;
        this.a_answer = a_answer;
        this.b_answer = b_answer;
        this.v_answer = v_answer;
        this.answer = answer;
        this.id = id;
        this.author_mail = author_mail;
        this.lesson_theme = lesson_theme;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor_mail() {
        return author_mail;
    }

    public void setAuthor_mail(String author_mail) {
        this.author_mail = author_mail;
    }

    public String getLesson_theme() {
        return lesson_theme;
    }

    public void setLesson_theme(String lesson_theme) {
        this.lesson_theme = lesson_theme;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getA_answer() {
        return a_answer;
    }

    public void setA_answer(String a_answer) {
        this.a_answer = a_answer;
    }

    public String getB_answer() {
        return b_answer;
    }

    public void setB_answer(String b_answer) {
        this.b_answer = b_answer;
    }

    public String getV_answer() {
        return v_answer;
    }

    public void setV_answer(String v_answer) {
        this.v_answer = v_answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
