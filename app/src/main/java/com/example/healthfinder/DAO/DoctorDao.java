package com.example.healthfinder.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.healthfinder.entities.Doctor;

import java.util.List;

@Dao
public interface DoctorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Doctor doctor);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Doctor doctor);

    @Delete
    void delete(Doctor doctor);
}
