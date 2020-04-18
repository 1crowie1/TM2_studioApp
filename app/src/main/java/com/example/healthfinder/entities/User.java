package com.example.healthfinder.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Index;

import java.io.Serializable;

@Entity
        //(indices = {@Index(value = {"userId"}, unique = true)

//})
public class User implements Serializable{
        @PrimaryKey(autoGenerate = true)
                public long userId;

        @ColumnInfo(name = "first_name")
                @NonNull
                public String fName;

        @ColumnInfo(name = "last_name")
                @NonNull
                public String lName;

        @ColumnInfo(name = "email")
                @NonNull
                public String email;
        @ColumnInfo(name = "doctorStatus")
                public boolean doctorStatus;


        public User(String fName, String lName,String email){
                this.fName = fName;
                this.lName = lName;
                this.email = email;
        }

        public long getUserID() {
                return userId;
        }

        public String getfName() {
                return fName;
        }

        public void setfName(String fName) {
                this.fName = fName;
        }

        public String getlName() {
                return lName;
        }

        public void setlName(String lName) {
                this.lName = lName;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public boolean isDoctorStatus() {return doctorStatus;}
}
