package com.example.myapplication;

public class User {
    public User() {
    }

    private String name, mail, password, log_pas, id, admin;
    public User(String name, String mail, String password, String id) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.log_pas = mail+"_"+password;
        this.id = id;
        this.admin="0";
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLog_pas() {
        return log_pas;
    }

    public void setLog_pas(String log_pas) {
        this.log_pas = log_pas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
