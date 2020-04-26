package com.example.healthfinder.model;

import android.net.Uri;

public class CurrentUser {

    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private Uri profilePic;

    public CurrentUser(String userFirstName, String userLastName, String userEmail, Uri profilePic){
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }
}
