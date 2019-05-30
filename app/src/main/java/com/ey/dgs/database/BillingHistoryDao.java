package com.ey.dgs.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.ey.dgs.model.Account;
import com.ey.dgs.model.BillingHistory;

import java.util.List;

@Dao
public interface BillingHistoryDao {

    @Query("SELECT * FROM BillingHistory")
    List<BillingHistory> getAllBillingHistory();

    @Query("SELECT * FROM BillingHistory WHERE accountNumber=:accountNumber")
    BillingHistory getBillingHistory(String accountNumber);

    @Insert
    void insert(List<BillingHistory> billingHistories);

    @Insert
    void insert(BillingHistory billingHistory);

    @Query("DELETE FROM BillingHistory WHERE accountNumber=:accountNumber")
    void delete(String accountNumber);

    @Query("DELETE FROM BillingHistory")
    void clear();


}
