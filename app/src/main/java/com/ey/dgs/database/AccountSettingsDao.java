package com.ey.dgs.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountSettings;

import java.util.List;

@Dao
public interface AccountSettingsDao {

    @Query("SELECT * FROM AccountSettings")
    List<AccountSettings> getAllAccountSettings();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccountSettings accountSettings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AccountSettings> accountSettings);

    @Query("DELETE FROM AccountSettings WHERE accountNumber=:accountNumber")
    void delete(String accountNumber);

    @Update
    void update(AccountSettings accountSettings);

    @Query("SELECT * FROM AccountSettings WHERE accountNumber=:accountNumber")
    AccountSettings getAccountSettings(String accountNumber);

}
