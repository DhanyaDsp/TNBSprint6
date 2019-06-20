package com.ey.dgs.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.ey.dgs.model.Account;
import com.ey.dgs.model.AccountDetails;
import com.ey.dgs.model.AccountSettings;
import com.ey.dgs.model.BillingHistory;
import com.ey.dgs.model.EnergyConsumptions;
import com.ey.dgs.model.Notification;
import com.ey.dgs.model.User;
import com.ey.dgs.model.UserSettings;

import java.util.Arrays;
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
                Account[] accountDetails = user.getAccountDetails();

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

    public void addBillingHistories(int requestCode, List<BillingHistory> billingHistories, DatabaseCallback databaseCallback) {
        class AddBillingHistoryTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {


                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getBillingHistoryDao()
                        .clear();

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getBillingHistoryDao()
                        .insert(billingHistories);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onInsert(billingHistories, requestCode, 0);
            }
        }

        AddBillingHistoryTask st = new AddBillingHistoryTask();
        st.execute();
    }

    public void addBillingHistory(int requestCode, BillingHistory billingHistory, DatabaseCallback databaseCallback) {
        class AddBillingHistoryTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getBillingHistoryDao()
                        .delete(billingHistory.getAccountNumber());

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getBillingHistoryDao()
                        .insert(billingHistory);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onInsert(billingHistory, requestCode, 0);
            }
        }

        AddBillingHistoryTask st = new AddBillingHistoryTask();
        st.execute();
    }

    public void getBillingHistories(int requestCode, DatabaseCallback databaseCallback) {
        class GetAccountsTask extends AsyncTask<Void, Void, List<BillingHistory>> {

            @Override
            protected List<BillingHistory> doInBackground(Void... voids) {

                return DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getBillingHistoryDao()
                        .getAllBillingHistory();
            }

            @Override
            protected void onPostExecute(List<BillingHistory> billingHistories) {
                super.onPostExecute(billingHistories);
                databaseCallback.onReceived(billingHistories, requestCode, 0);
            }
        }

        GetAccountsTask st = new GetAccountsTask();
        st.execute();
    }

    public void getBillingHistory(String accountNumber, int requestCode, DatabaseCallback databaseCallback) {
        class GetAccountsTask extends AsyncTask<Void, Void, BillingHistory> {

            @Override
            protected BillingHistory doInBackground(Void... voids) {

                return DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getBillingHistoryDao()
                        .getBillingHistory(accountNumber);
            }

            @Override
            protected void onPostExecute(BillingHistory billingHistory) {
                super.onPostExecute(billingHistory);
                databaseCallback.onReceived(billingHistory, requestCode, 0);
            }
        }

        GetAccountsTask st = new GetAccountsTask();
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

    public void getAccount(int requestCode, Account account, DatabaseCallback databaseCallback) {

        class GetAccount extends AsyncTask<Void, Void, Account> {

            @Override
            protected Account doInBackground(Void... voids) {

                return DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .accountDao()
                        .getAccount(account.getAccountNumber());
            }

            @Override
            protected void onPostExecute(Account account) {
                super.onPostExecute(account);
                databaseCallback.onReceived(account, requestCode, 0);
            }
        }

        GetAccount st = new GetAccount();
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

    public void getNotifications(int requestCode, int user_id, String accountNumber, DatabaseCallback databaseCallback) {
        class GetNotificationsTask extends AsyncTask<Void, Void, List<Notification>> {

            @Override
            protected List<Notification> doInBackground(Void... voids) {

                if (TextUtils.isEmpty(accountNumber)) {
                    return DatabaseClient.getInstance(mCtx).getAppDatabase()
                            .notificationDao()
                            .getAll();
                } else {
                    return DatabaseClient.getInstance(mCtx).getAppDatabase()
                            .notificationDao()
                            .getAccountNotifications(accountNumber);
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

    public void deleteAllNotifications(int requestCode, DatabaseCallback databaseCallback) {
        class DeleteAllNotificationsTask extends AsyncTask<Void, Void, List<Notification>> {

            @Override
            protected List<Notification> doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .notificationDao().deleteAll();
                return null;
            }

            @Override
            protected void onPostExecute(List<Notification> notifications) {
                super.onPostExecute(notifications);
                databaseCallback.onReceived(notifications, requestCode, 0);
            }
        }

        DeleteAllNotificationsTask st = new DeleteAllNotificationsTask();
        st.execute();
    }

    public void deleteNotification(int requestCode, Notification notification, DatabaseCallback databaseCallback) {
        class DeleteNotificationsTask extends AsyncTask<Void, Void, List<Notification>> {

            @Override
            protected List<Notification> doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .notificationDao().delete(notification);
                return null;
            }

            @Override
            protected void onPostExecute(List<Notification> notifications) {
                super.onPostExecute(notifications);
                databaseCallback.onReceived(notifications, requestCode, 0);
            }
        }

        DeleteNotificationsTask st = new DeleteNotificationsTask();
        st.execute();
    }

    public void getAccountSettings(int requestCode, String accountNumber, DatabaseCallback databaseCallback) {
        class GetAccountSettingsTask extends AsyncTask<Void, Void, AccountSettings> {

            @Override
            protected AccountSettings doInBackground(Void... voids) {

                AccountSettings accountSettings = DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getAccountSettingsDao()
                        .getAccountSettings(accountNumber);

                return accountSettings;

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
                        .delete(accountSettings.getAccountNumber());

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getAccountSettingsDao()
                        .insert(accountSettings);

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

    public void addUserSettings(int requestCode, UserSettings userSettings, DatabaseCallback databaseCallback) {
        class AddUserSettingsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getUserSettingsDao()
                        .delete(userSettings);

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getUserSettingsDao()
                        .insert(userSettings);

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getAccountSettingsDao()
                        .clear();

                List<AccountSettings> accountSettings = Arrays.asList(userSettings.getAccountSettings());
                for (AccountSettings settings : accountSettings) {
                    settings.setAccountId(Integer.parseInt(settings.getAccountNumber()));
                }
                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getAccountSettingsDao()
                        .insert(accountSettings);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onInsert(userSettings, requestCode, 0);
            }
        }

        AddUserSettingsTask st = new AddUserSettingsTask();
        st.execute();
    }

    public void updateUserSettings(int requestCode, UserSettings userSettings, DatabaseCallback databaseCallback) {
        class UpdateUserSettingsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getUserSettingsDao()
                        .update(userSettings);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onUpdate(userSettings, requestCode, 0);
            }
        }

        UpdateUserSettingsTask st = new UpdateUserSettingsTask();
        st.execute();
    }

    public void getUserSettings(int requestCode, int userId, DatabaseCallback databaseCallback) {
        class GetUserSettingsTask extends AsyncTask<Void, Void, UserSettings> {

            @Override
            protected UserSettings doInBackground(Void... voids) {
                return DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getUserSettingsDao()
                        .getUserSettings(userId);
            }

            @Override
            protected void onPostExecute(UserSettings userSettings) {
                super.onPostExecute(userSettings);
                databaseCallback.onReceived(userSettings, requestCode, 0);
            }
        }

        GetUserSettingsTask st = new GetUserSettingsTask();
        st.execute();
    }

    public void updateAccountSettings(int requestCode, AccountSettings accountSettings, DatabaseCallback databaseCallback) {
        class UpdateAccountSettingsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getAccountSettingsDao()
                        .update(accountSettings);

                List<AccountSettings> accountSettingsList = DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getAccountSettingsDao()
                        .getAllAccountSettings();

                UserSettings userSettings = DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .getUserSettingsDao()
                        .getUserSettings(1);

                if (accountSettings.isPushNotificationFlag()) {
                    if (!userSettings.isPushNotificationFlag()) {
                        userSettings.setPushNotificationFlag(true);
                        DatabaseClient.getInstance(mCtx).getAppDatabase()
                                .getUserSettingsDao()
                                .update(userSettings);
                    }
                } else {
                    boolean isAnyAccountPushEnabled = false;
                    for (AccountSettings accountSettings : accountSettingsList) {
                        if (accountSettings.isPushNotificationFlag()) {
                            isAnyAccountPushEnabled = true;
                        }
                    }
                    if (!isAnyAccountPushEnabled) {
                        if (userSettings.isPushNotificationFlag()) {
                            userSettings.setPushNotificationFlag(false);
                            DatabaseClient.getInstance(mCtx).getAppDatabase()
                                    .getUserSettingsDao()
                                    .update(userSettings);
                        }
                    }

                }

                if (accountSettings.isSmsNotificationFlag()) {
                    if (!userSettings.isSmsNotificationFlag()) {
                        userSettings.setSmsNotificationFlag(true);
                        DatabaseClient.getInstance(mCtx).getAppDatabase()
                                .getUserSettingsDao()
                                .update(userSettings);
                    }
                } else {
                    boolean isAnyAccountSMSEnabled = false;
                    for (AccountSettings accountSettings : accountSettingsList) {
                        if (accountSettings.isSmsNotificationFlag()) {
                            isAnyAccountSMSEnabled = true;
                        }
                    }
                    if (!isAnyAccountSMSEnabled) {
                        if (userSettings.isSmsNotificationFlag()) {
                            userSettings.setSmsNotificationFlag(false);
                            DatabaseClient.getInstance(mCtx).getAppDatabase()
                                    .getUserSettingsDao()
                                    .update(userSettings);
                        }
                    }

                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onUpdate(accountSettings, requestCode, 0);
            }
        }

        UpdateAccountSettingsTask st = new UpdateAccountSettingsTask();
        st.execute();
    }

    public void toggleNotificationForAllAccounts(int requestCode, boolean pushToggle, boolean smsToggle, List<Account> accounts, DatabaseCallback databaseCallback) {

        class ToogleNotificationsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                for (Account account : accounts) {
                    DatabaseClient.getInstance(mCtx).getAppDatabase()
                            .getAccountSettingsDao()
                            .togglePush(pushToggle, smsToggle, account.getAccountNumber());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onUpdate(accounts, requestCode, 0);
            }
        }

        ToogleNotificationsTask st = new ToogleNotificationsTask();
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

    public void updateAccount(int requestCode, Account account, DatabaseCallback databaseCallback) {
        class UpdateAccountTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .accountDao()
                        .update(account);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onUpdate(account, requestCode, 0);
            }
        }

        UpdateAccountTask st = new UpdateAccountTask();
        st.execute();
    }


    public void getAccountDetails(int requestCode, User user, DatabaseCallback databaseCallback) {
        class GetAccountDetailsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                Account accounts[] = user.getAccountDetails();
                for (Account account : accounts) {
                    DatabaseClient.getInstance(mCtx).getAppDatabase()
                            .accountDao()
                            .updateDetail(account.getAccountNumber(), account.getLastBilledDate(), account.getLastBilledAmount(),
                                    account.getBillingCycleStartDate(), account.getBillingCycleEndDate(),
                                    account.getUserThreshold(), account.getUserThreshold(),
                                    account.getCurrentDayConsumption(), account.getCurrentWeekConsumption(),
                                    account.getCurrentDayConsumption(), account.getPeopleInProperty());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onUpdate(user, requestCode, 0);
            }
        }

        GetAccountDetailsTask st = new GetAccountDetailsTask();
        st.execute();
    }

    public void updateAccounts(int requestCode, List<Account> accounts, DatabaseCallback databaseCallback) {
        class UpdateAccountTasks extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .accountDao()
                        .update(accounts);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onUpdate(accounts, requestCode, 0);
            }
        }

        UpdateAccountTasks st = new UpdateAccountTasks();
        st.execute();
    }

    public void updateSingleAccountThreshold(int requestCode, AccountDetails[] accounts,
                                             DatabaseCallback databaseCallback) {
        class updateSingleAccountThreshold extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                for (AccountDetails account : accounts) {
                    DatabaseClient.getInstance(mCtx).getAppDatabase()
                            .accountDao()
                            .updateAccountDetail(account.getAccountNumber(), account.getUserThreshold(), account.getPeopleInProperty());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                databaseCallback.onUpdate(accounts, requestCode, 0);
            }
        }

        updateSingleAccountThreshold st = new updateSingleAccountThreshold();
        st.execute();
    }

    public void updateNotification(Notification notification) {
        class UpdateNotificationTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(mCtx).getAppDatabase().notificationDao().update(notification);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        UpdateNotificationTask st = new UpdateNotificationTask();
        st.execute();
    }
}


