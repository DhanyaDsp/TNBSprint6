package com.ey.dgs.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
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

    @Insert
    void insert(EnergyConsumptions energyConsumptions);

    @Insert
    void insert(List<EnergyConsumptions> energyConsumptions);

    @Delete
    void delete(EnergyConsumptions energyConsumptions);

    @Update
    void update(EnergyConsumptions energyConsumptions);

    @Query("DELETE FROM EnergyConsumptions")
    void clear();

}
