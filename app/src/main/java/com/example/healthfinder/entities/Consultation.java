package com.example.healthfinder.entities;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import java.io.Serializable;


@Entity
        (foreignKeys = {@ForeignKey(entity = User.class,
                                    parentColumns = "userId",
                                    childColumns = "patId",
                                    onDelete = 5), //cascade

                       @ForeignKey(entity = Doctor.class,
                                    parentColumns = "doctorId",
                                    childColumns = "docId",
                                    onDelete = 5) //cascade
})

public class Consultation implements Serializable {
    @PrimaryKey(autoGenerate = true)
        public long consultID;

    @ColumnInfo(name = "patId")
    public long patId;

    @ColumnInfo(name = "docId")
    public long docId;

    public long getConsultID() {return consultID;}

    public long getPatId(){return patId;}

    public Consultation(){}

}
