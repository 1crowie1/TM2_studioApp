package com.example.healthfinder.entities;

public class Doctor {
    public String clinic;
    public String regNo;
    public String specialties;

    public Doctor() {
    }

    public Doctor(String regNo, String clinic, String specialties) {
        this.regNo = regNo;
        this.clinic = clinic;
        this.specialties = specialties;
    }

}


