package com.ey.dgs.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ey.dgs.model.UserSettings;

import java.util.List;

@Dao
public interface UserSettingsDao {

    @Query("SELECT * FROM UserSettings")
    List<UserSettings> getAll();

    @Query("SELECT * FROM UserSettings WHERE userId=:userId")
    UserSettings getUserSettings(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserSettings userSettings);

    @Delete
    void delete(UserSettings userSettings);

    @Update
    void update(UserSettings userSettings);
}
