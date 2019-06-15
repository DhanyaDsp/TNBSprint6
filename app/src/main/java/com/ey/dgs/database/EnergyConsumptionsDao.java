package com.ey.dgs.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ey.dgs.model.EnergyConsumptions;

import java.util.List;

@Dao
public interface EnergyConsumptionsDao {

    @Query("SELECT * FROM EnergyConsumptions")
    List<EnergyConsumptions> getAll();

    @Query("SELECT * FROM EnergyConsumptions WHERE accountNumber=:accountNumber")
    EnergyConsumptions getEnergyConsumption(String accountNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EnergyConsumptions energyConsumptions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<EnergyConsumptions> energyConsumptions);

    @Query("DELETE FROM EnergyConsumptions WHERE accountNumber=:accountNumber")
    void delete(String accountNumber);

    @Update
    void update(EnergyConsumptions energyConsumptions);

    @Query("DELETE FROM EnergyConsumptions")
    void clear();

}
