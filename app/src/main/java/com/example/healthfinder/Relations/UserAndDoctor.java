package com.example.healthfinder.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.healthfinder.entities.Doctor;
import com.example.healthfinder.entities.User;

public class UserAndDoctor {
    @Embedded public User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "doctorId"
    )
    public Doctor doctor;
}
