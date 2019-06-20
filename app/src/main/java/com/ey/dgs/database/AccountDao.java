package com.ey.dgs.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ey.dgs.model.Account;

import java.util.List;

@Dao
public interface AccountDao {

    @Query("SELECT * FROM Account")
    List<Account> getAll();

    @Insert
    void insert(Account account);

    @Query("SELECT * FROM Account WHERE accountNumber=:accountNo")
    Account getAccount(String accountNo);

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

    @Query("SELECT * FROM Account WHERE user_id=:user_id ORDER BY nickName")
    List<Account> getUserAccounts(int user_id);

    @Query("UPDATE Account SET lastBilledDate=:lastBilledDate,lastBilledAmount=:lastBilledAmount,billingCycleStartDate=:billingCycleStartDate,billingCycleEndDate=:billingCycleEndDate,userThreshold=:userThreshold,userThreshold=:userThreshold1,currentDayConsumption=:currentDayConsumption,currentMonthConsumption=:currentMonthConsumption,currentWeekConsumption=:currentWeekConsumption,peopleInProperty=:peopleInProperty WHERE accountNumber=:accountNumber")
    void updateDetail(String accountNumber, String lastBilledDate, Float lastBilledAmount, String billingCycleStartDate, String billingCycleEndDate, String userThreshold, String userThreshold1, String currentMonthConsumption, String currentWeekConsumption, String currentDayConsumption, String peopleInProperty);

    @Query("UPDATE Account SET userThreshold=:userThreshold, peopleInProperty=:peopleInProperty WHERE accountNumber=:accountNumber")
    void updateAccountDetail(String accountNumber,String userThreshold, String peopleInProperty);
}
