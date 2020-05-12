package com.example.healthfinder.entities;


import java.util.Calendar;
import java.util.Date;

public class Consultation{
    public String patEmail;
    public String docEmail;
    public String details;
    public boolean urgency;
    public String docId;

    public Consultation() {
    }

    public Consultation(String patEmail, String docEmail, String details, Boolean urgency, String docId) {
        this.patEmail = patEmail;
        this.docEmail = docEmail;
        this.details = details;
        this.urgency = urgency;
        this.docId = docId;
    }
}
