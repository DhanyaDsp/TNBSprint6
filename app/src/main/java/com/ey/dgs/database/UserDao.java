package com.ey.dgs.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ey.dgs.model.Account;
import com.ey.dgs.model.Notification;
import com.ey.dgs.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    List<User> getAll();


    @Query("SELECT * FROM User WHERE email=:email")
    List<User> getAll(String email);

    @Query("SELECT * FROM User WHERE userId=:user_id")
    User getUser(int user_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);
}
