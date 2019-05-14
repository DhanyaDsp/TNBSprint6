package com.ey.dgs.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.ey.dgs.model.Account;
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

    public void getUsers(int requestCode, int userId, int accountId, DatabaseCallback databaseCallback) {
        class GetNotificationsTask extends AsyncTask<Void, Void, List<Notification>> {

            @Override
            protected List<Notification> doInBackground(Void... voids) {

                return DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .notificationDao()
                        .getAccountNotification(userId, accountId);
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
                        .insert(accounts);
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

    public void addNotifications(int requestCode, List<Notification> notifications, DatabaseCallback databaseCallback) {
        class AddNotificationsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .notificationDao()
                        .insert(notifications);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onInsert(notifications, requestCode, 0);
            }
        }

        AddNotificationsTask st = new AddNotificationsTask();
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
                            .getAccountNotification(user_id, accountId);
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
}
