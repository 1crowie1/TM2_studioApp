package com.example.healthfinder.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.healthfinder.entities.Consultation;

import java.util.List;

@Dao
public interface ConsultationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Consultation consultation);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Consultation consultation);

    @Delete
    void delete(Consultation consultation);
}
