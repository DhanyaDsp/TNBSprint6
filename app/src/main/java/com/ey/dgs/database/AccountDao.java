package com.ey.dgs.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ey.dgs.model.Account;
import com.ey.dgs.model.Account;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AccountDao {

    @Query("SELECT * FROM Account ")
    List<Account> getAll();

    @Insert
    void insert(Account account);

    @Query("SELECT * FROM Account WHERE isPrimaryAccount=:flag")
    Account getPrimaryAccount(boolean flag);

    @Delete
    void delete(Account account);

    @Query("DELETE FROM Account")
    void clear();

    @Update
    void update(Account account);

    @Update
    void update(List<Account> accounts);

    @Insert
    void insert(List<Account> accounts);

    @Query("SELECT * FROM Account WHERE user_id=:user_id ORDER BY isPrimaryAccount DESC, nickName")
    List<Account> getUserAccounts(int user_id);

}
