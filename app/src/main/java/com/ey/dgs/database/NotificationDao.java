package com.ey.dgs.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ey.dgs.model.Notification;
import com.ey.dgs.model.User;

import java.util.List;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM Notification")
    List<Notification> getAll();

    @Query("SELECT * FROM Notification WHERE accountNumber=:accountNumber ")
    List<Notification> getAccountNotifications(String accountNumber);

    @Insert
    void insert(Notification notification);

    @Delete
    void delete(Notification notification);

    @Update
    void update(Notification notification);

}
