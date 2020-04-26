package com.example.healthfinder;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;
import android.content.Context;

import com.example.healthfinder.DAO.ConsultationDao;
import com.example.healthfinder.DAO.DoctorDao;
import com.example.healthfinder.DAO.UserDao;
import com.example.healthfinder.entities.Consultation;
import com.example.healthfinder.entities.Doctor;
import com.example.healthfinder.entities.User;

@Database(entities = {User.class, Doctor.class, Consultation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "AppDB";
    private static volatile AppDatabase instance;



    static synchronized  AppDatabase getInstance(Context context){
        if(instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context){
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public abstract UserDao userDao();
    public abstract DoctorDao doctorDao();
    public abstract ConsultationDao consultationDao();

}
