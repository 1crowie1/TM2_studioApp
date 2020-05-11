package com.example.healthfinder.entities;

public class Prescription {
    public String docEmail;
    public String patEmail;
    public String details;

    public Prescription() {
    }

    public Prescription(String docEmail, String patEmail, String details) {
        this.docEmail = docEmail;
        this.patEmail = patEmail;
        this.details = details;
    }
}
