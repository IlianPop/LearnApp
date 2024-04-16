package com.example.myapplication;

public class Black_list {
    private String id, mail;

    public Black_list() {
    }

    public Black_list(String id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
