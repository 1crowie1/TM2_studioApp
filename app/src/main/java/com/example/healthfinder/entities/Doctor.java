package com.example.healthfinder.entities;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity
       /* (foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "userId",
        childColumns = "doctorId",
        onDelete = 5)) //cascade*/
public class Doctor implements Serializable{
    @PrimaryKey
        public long doctorId;

    @ColumnInfo
    public String clinic;

    @ColumnInfo
    public long regNum;

    @Ignore
    public Doctor(String clinic, long regNum){
        this.clinic = clinic;
        this.regNum = regNum;
    }

    public Doctor(long regNum){
        this.regNum = regNum;
    }

    public long getDoctorId() {return doctorId;}

    public String getClinic(){return clinic;}

    public void setClinic(String clinic){this.clinic = clinic;}

    public long getRegNum(){return regNum;}

    public void setRegNum(long regNum){this.regNum = regNum;}
}
