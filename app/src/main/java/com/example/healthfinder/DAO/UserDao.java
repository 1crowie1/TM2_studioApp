package com.example.healthfinder.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.healthfinder.Relations.UserAndDoctor;
import com.example.healthfinder.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE first_name LIKE :fName AND last_name LIKE :lName")
    List<User> getUserName(String fName, String lName);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Transaction
    @Query("SELECT * FROM User WHERE doctorStatus")
    List<User> getUsersAndDoctors();

    @Query("SELECT * FROM User WHERE email like :email")
    List<User> getExistingUsers(String email);

    @Query("SELECT * FROM User WHERE userId = :uid")
    List<User> getUserId(final int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(User user);

    @Delete
    void delete(User user);
}
