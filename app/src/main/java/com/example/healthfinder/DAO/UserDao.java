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
    @Query("SELECT first_name FROM User WHERE userId LIKE :uid")
    String getfNameByID(long uid);

    @Query("SELECT last_name FROM User WHERE userId LIKE :uid")
    String getlNameByID(long uid);

    @Query("SELECT email FROM User WHERE userId LIKE :uid")
    String getemailByID(long uid);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Transaction
    @Query("SELECT * FROM User WHERE doctorStatus")
    List<User> getUsersAndDoctors();

    @Query("SELECT * FROM User WHERE email like :email")
    List<User> getExistingUsers(String email);

    @Query("SELECT userId FROM User Where email like :email")
    long getUserIdByEmail(String email);

    @Query("SELECT * FROM User WHERE userId = :uid")
    User getUserById(long uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("UPDATE User SET first_name=:userFirstName WHERE userId = :uID")
    void updateFirstName(String userFirstName, long uID);

    @Query("UPDATE User SET last_name=:userLastName WHERE userId = :uID")
    void updateLastName(long uID, String userLastName);

    @Delete
    void delete(User user);
}
