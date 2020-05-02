package com.example.healthfinder.entities;

public class User {

    public String username;
    public String email;
    public boolean doctorStatus;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.doctorStatus = false;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail(){
        return email;
    }

    public boolean getDoctorStatus(){
        return doctorStatus;
    }

    public void setDoctorStatus(boolean doctorStatus){
        this.doctorStatus = doctorStatus;
    }
}
