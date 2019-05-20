package com.ey.dgs.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.EnergyConsumptions;
import com.ey.dgs.model.Notification;
import com.ey.dgs.model.User;

@Database(entities = {User.class, Account.class, Notification.class, AccountSettings.class, EnergyConsumptions.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao taskDao();

    public abstract AccountDao accountDao();

    public abstract NotificationDao notificationDao();

    public abstract AccountSettingsDao getAccountSettingsDao();

    public abstract EnergyConsumptionsDao getEnergyConsumptionsDao();
}