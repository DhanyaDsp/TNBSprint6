package com.ey.dgs.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.EnergyConsumptions;
import com.ey.dgs.model.Notification;
import com.ey.dgs.model.User;

import java.util.List;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "DGS_DB").fallbackToDestructiveMigration().build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public void addUser(int requestCode, User user, DatabaseCallback databaseCallback) {
        class AddUserTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .taskDao()
                        .insert(user);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onInsert(user, requestCode, 0);
            }
        }

        AddUserTask st = new AddUserTask();
        st.execute();
    }

    public void deleteUser(int requestCode, User user, DatabaseCallback databaseCallback) {
        class DeleteUserTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .taskDao()
                        .delete(user);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //databaseCallback.onInsert(user, requestCode, 0);
            }
        }

        DeleteUserTask st = new DeleteUserTask();
        st.execute();
    }

    public void getUser(int requestCode, int user_id, DatabaseCallback databaseCallback) {
        class GetUserDetailTask extends AsyncTask<Void, Void, User> {

            @Override
            protected User doInBackground(Void... voids) {

                return DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .taskDao()
                        .getUser(user_id);
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                databaseCallback.onReceived(user, requestCode, 0);
            }
        }

        GetUserDetailTask st = new GetUserDetailTask();
        st.execute();
    }

    public void updateUser(int requestCode, User user, DatabaseCallback databaseCallback) {
        class UpdateUserTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .taskDao()
                        .update(user);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onUpdate(user, requestCode, 0);
            }
        }

        UpdateUserTask st = new UpdateUserTask();
        st.execute();
    }

    public void addAccounts(int requestCode, List<Account> accounts, DatabaseCallback databaseCallback) {
        class AddAccountsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {


                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .accountDao()
                        .clear();
                for (Account account : accounts) {
                    DatabaseClient.getInstance(mCtx).getAppDatabase()
                            .accountDao()
                            .insert(account);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onInsert(accounts, requestCode, 0);
            }
        }

        AddAccountsTask st = new AddAccountsTask();
        st.execute();
    }

    public void addEnergyConsumptionsToLocalDB(int requestCode, List<EnergyConsumptions> energyConsumptions, DatabaseCallback databaseCallback) {
        class AddEnergyConsumptionsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getEnergyConsumptionsDao().clear();

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getEnergyConsumptionsDao().insert(energyConsumptions);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onInsert(energyConsumptions, requestCode, 0);
            }
        }

        AddEnergyConsumptionsTask st = new AddEnergyConsumptionsTask();
        st.execute();
    }

    public void getAccounts(int requestCode, int user_id, DatabaseCallback databaseCallback) {
        class GetAccountsTask extends AsyncTask<Void, Void, List<Account>> {

            @Override
            protected List<Account> doInBackground(Void... voids) {

                return DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .accountDao()
                        .getUserAccounts(user_id);
            }

            @Override
            protected void onPostExecute(List<Account> accounts) {
                super.onPostExecute(accounts);
                databaseCallback.onReceived(accounts, requestCode, 0);
            }
        }

        GetAccountsTask st = new GetAccountsTask();
        st.execute();
    }

    public void addNotification(int requestCode, Notification notification, DatabaseCallback databaseCallback) {
        class AddNotificationTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .notificationDao()
                        .insert(notification);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (databaseCallback != null) {
                    databaseCallback.onInsert(notification, requestCode, 0);
                }
            }
        }

        AddNotificationTask st = new AddNotificationTask();
        st.execute();
    }

    public void getNotifications(int requestCode, int user_id, int accountId, DatabaseCallback databaseCallback) {
        class GetNotificationsTask extends AsyncTask<Void, Void, List<Notification>> {

            @Override
            protected List<Notification> doInBackground(Void... voids) {

                if (accountId == -1) {
                    return DatabaseClient.getInstance(mCtx).getAppDatabase()
                            .notificationDao()
                            .getAll();
                } else {
                    return DatabaseClient.getInstance(mCtx).getAppDatabase()
                            .notificationDao()
                            .getAll();
                }
            }

            @Override
            protected void onPostExecute(List<Notification> notifications) {
                super.onPostExecute(notifications);
                databaseCallback.onReceived(notifications, requestCode, 0);
            }
        }

        GetNotificationsTask st = new GetNotificationsTask();
        st.execute();
    }

    public void getAccountSettings(int requestCode, String accountNumber, DatabaseCallback databaseCallback) {
        class GetAccountSettingsTask extends AsyncTask<Void, Void, AccountSettings> {

            @Override
            protected AccountSettings doInBackground(Void... voids) {

                return DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getAccountSettingsDao()
                        .getAccountSettings(accountNumber);

            }

            @Override
            protected void onPostExecute(AccountSettings accountSettings) {
                super.onPostExecute(accountSettings);
                databaseCallback.onReceived(accountSettings, requestCode, 0);
            }
        }

        GetAccountSettingsTask st = new GetAccountSettingsTask();
        st.execute();
    }

    public void getEnergyConsumptions(int requestCode, String accountNumber, DatabaseCallback databaseCallback) {
        class GetAccountSettingsTask extends AsyncTask<Void, Void, EnergyConsumptions> {

            @Override
            protected EnergyConsumptions doInBackground(Void... voids) {

                return DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getEnergyConsumptionsDao()
                        .getEnergyConsumption(accountNumber);

            }

            @Override
            protected void onPostExecute(EnergyConsumptions energyConsumptions) {
                super.onPostExecute(energyConsumptions);
                databaseCallback.onReceived(energyConsumptions, requestCode, 0);
            }
        }

        GetAccountSettingsTask st = new GetAccountSettingsTask();
        st.execute();
    }

    public void addAccountSettings(int requestCode, AccountSettings accountSettings, DatabaseCallback databaseCallback) {
        class AddAccountsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getAccountSettingsDao()
                        .insert(accountSettings);

                accountSettings.getEnergyConsumptions().setAccountNumber(accountSettings.getAccountNumber());
                DatabaseClient.getInstance(mCtx).getAppDatabase().getEnergyConsumptionsDao()
                        .insert(accountSettings.getEnergyConsumptions());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onInsert(accountSettings, requestCode, 0);
            }
        }

        AddAccountsTask st = new AddAccountsTask();
        st.execute();
    }

    public void updateAccountSettings(int requestCode, AccountSettings accountSettings, DatabaseCallback databaseCallback) {
        class UpdateAccountsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getAccountSettingsDao()
                        .update(accountSettings);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onUpdate(accountSettings, requestCode, 0);
            }
        }

        UpdateAccountsTask st = new UpdateAccountsTask();
        st.execute();
    }

    public void updateEnergyConsumptions(int requestCode, EnergyConsumptions energyConsumptions, DatabaseCallback databaseCallback) {
        class UpdateAccountsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getEnergyConsumptionsDao()
                        .update(energyConsumptions);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onUpdate(energyConsumptions, requestCode, 0);
            }
        }

        UpdateAccountsTask st = new UpdateAccountsTask();
        st.execute();
    }
}


