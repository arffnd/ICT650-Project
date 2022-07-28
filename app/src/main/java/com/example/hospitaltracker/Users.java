package com.example.hospitaltracker;



public class Users {
    String name;
    String email;
    String time;

    public Users(String name, String email, String time) {
        this.name = name;
        this.email = email;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTime() {
        return time;
    }
}

