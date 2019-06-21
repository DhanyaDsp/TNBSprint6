package com.ey.dgs.dashboard;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.adapters.AccountAdapter;
import com.ey.dgs.adapters.OffersAdapter;
import com.ey.dgs.authentication.LoginViewModel;
import com.ey.dgs.dashboard.billing.BillingHistoryViewModel;
import com.ey.dgs.dashboard.manageAccounts.ManageAccountsFragment;
import com.ey.dgs.dashboard.myaccount.MyAccountFragment;
import com.ey.dgs.dashboard.questions.MMCQuestionsFragment;
import com.ey.dgs.databinding.DashboardFragmentBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.User;
import com.ey.dgs.model.UserSettings;
import com.ey.dgs.notifications.settings.NotificationSettingsActivity;
import com.ey.dgs.offers.OffersViewModel;
import com.ey.dgs.usersettings.UserSettingsViewModel;
import com.ey.dgs.utils.AppPreferences;
import com.ey.dgs.utils.DialogHelper;
import com.ey.dgs.utils.FragmentUtils;

import java.io.Serializable;
import java.util.ArrayList;

import static com.ey.dgs.utils.FragmentUtils.INDEX_MANAGE_ACCOUNTS;
import static com.ey.dgs.utils.FragmentUtils.INDEX_MY_ACCOUNT;


public class DashboardFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener, AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE_SET_THRESHOLD = 101;
    public static boolean IS_THRESHOLD_SET = false;
    public static boolean IN_DASHBOARD = false;
    private View rootView;
    private RecyclerView rvAccounts, rvOffers;
    private LinearLayoutManager rvLayoutManager;
    ArrayList<Account> accounts = new ArrayList<>();
    private DividerItemDecoration itemDecorator;
    private Context context;
    private DashboardViewModel dashboardViewModel;
    private OffersViewModel offersViewModel;
    private BillingHistoryViewModel billingHistoryViewModel;
    private OnFragmentInteractionListener mListener;
    AppCompatTextView tvSubscribe;
    AppPreferences appPreferences;
    private LoginViewModel loginViewModel;
    private User user;
    Account selectedAccount;
    private DashboardFragmentBinding loginFragmentBinding;
    AppCompatImageView ivBanner;
    View loader;
    //private AccountPagerAdapter accountPagerAdapter;
    private boolean billingDetailsServiceCalled;
    AccountAdapter accountAdapter;
    OffersAdapter offersAdapter;
    UserSettingsViewModel userSettingsViewModel;
    private UserSettings userSettings;
    private static boolean showDisruptionAlert = false;
    private static boolean showRestorationAlert = false;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false);
        rootView = loginFragmentBinding.getRoot();
        loginFragmentBinding.setSelectedAccount(selectedAccount);
        appPreferences = new AppPreferences(getActivity());
        initView();
        subscribe();
        return rootView;
    }

    private void initView() {
        loader = rootView.findViewById(R.id.loader);
        ivBanner = rootView.findViewById(R.id.ivBanner);
        rvAccounts = rootView.findViewById(R.id.rvAccounts);
        rvAccounts.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvAccounts.setLayoutManager(rvLayoutManager);
        itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        tvSubscribe = rootView.findViewById(R.id.tvSubscribe);
        tvSubscribe.setOnClickListener(this);
        rvAccounts.addItemDecoration(itemDecorator);
        rvAccounts.setItemAnimator(new DefaultItemAnimator());
        if (mListener != null) {
            mListener.onFragmentInteraction("");
        }
        rvOffers = rootView.findViewById(R.id.rvOffers);
        rvOffers.setHasFixedSize(true);
        rvOffers.setLayoutManager((new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true)));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void subscribe() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getUserDetail(appPreferences.getUser_id());
        dashboardViewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        dashboardViewModel.setContext(getActivity());
        billingHistoryViewModel = ViewModelProviders.of(getActivity()).get(BillingHistoryViewModel.class);
        billingHistoryViewModel.setContext(getActivity());
        offersViewModel = ViewModelProviders.of(getActivity()).get(OffersViewModel.class);
        offersViewModel.setContext(getActivity());
        userSettingsViewModel = ViewModelProviders.of(this).get(UserSettingsViewModel.class);
        userSettingsViewModel.setContext(getActivity());
        userSettingsViewModel.getUserSettingsFromLocalDB(1);
        userSettingsViewModel.getUserSettings().observe(getViewLifecycleOwner(), userSettings -> {
            this.userSettings = userSettings;
            if (userSettings != null) {
                if (!userSettings.isRestoreAlertAcknowledgementFlag()) {
                    showRestorationAlert = true;
                }
                if (!userSettings.isOutageAlertAcknowledgementFlag()) {
                    showDisruptionAlert = true;
                }
            }
        });
        showProgress(true);
        loginViewModel.getUserDetail().observe(getViewLifecycleOwner(), user -> {
            onUserDetailsLoaded(user);
        });
        dashboardViewModel.getLoaderData().observe(getViewLifecycleOwner(), showProgress -> {
            showProgress(showProgress);
        });
        dashboardViewModel.loadAccountsFromLocalDB(appPreferences.getUser_id());
        dashboardViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
            showProgress(false);
            if (accounts.size() > 0) {
                this.accounts.clear();
                Account addAccount = new Account();
                addAccount.setAccount(true);
                accounts.add(addAccount);
                this.accounts.addAll(accounts);
                accountAdapter = new AccountAdapter(this, getActivity(), this.accounts);
                rvAccounts.setAdapter(accountAdapter);
                for (Account account : this.accounts) {
                    if (account.isPrimaryAccount()) {
                        this.selectedAccount = account;
                    }
                }
                if (selectedAccount == null) {
                    selectedAccount = accounts.get(0);
                }

                loginFragmentBinding.setSelectedAccount(selectedAccount);
                dashboardViewModel.setSelectedAccount(selectedAccount);
               /* if (!billingDetailsServiceCalled) {
                    getBillingDetailsForAccount(accounts);
                    billingDetailsServiceCalled = true;
                }*/

               if(showRestorationAlert && IN_DASHBOARD) {
                   setServiceRestorationPopup(this.accounts);
                   showRestorationAlert = false;
               }

                if (showDisruptionAlert && IN_DASHBOARD) {
                    setServiceDisruptionPopup(this.accounts);
                    showDisruptionAlert = false;
                }

            }
        });

        offersViewModel.getOfferItems().observe(getViewLifecycleOwner(), offers -> {
            offersAdapter = new OffersAdapter(getActivity(), this, offers);
            rvOffers.setAdapter(offersAdapter);
        });
    }

    private void getBillingDetailsForAccount(ArrayList<Account> accounts) {
        if (user != null) {
            for (Account account : accounts) {
                billingHistoryViewModel.loadBillingHistoryFromLocalDB(account.getAccountNumber());
            }
        }
    }

    private void onUserDetailsLoaded(User user) {
        if (user != null) {
            this.user = user;
            loginFragmentBinding.setUser(this.user);
            /*if (user.isPrimaryAccountSet()) {
                btnSetPrimaryAccount.setVisibility(View.GONE);
            } else {
                btnSetPrimaryAccount.setVisibility(View.VISIBLE);
            }*/
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void openFragment(int index, Object obj) {
        if (index == INDEX_MY_ACCOUNT) {
            dashboardViewModel.setSelectedAccount((Account) obj);
            FragmentUtils.newInstance(((HomeActivity) getActivity()).getSupportFragmentManager())
                    .addFragment(index, obj, MyAccountFragment.class.getName(), this, R.id.homeFlContainer);
        }
    }

    public void openManageAccountsFragment(int index) {
        if (index == INDEX_MANAGE_ACCOUNTS) {
            FragmentUtils.newInstance(((HomeActivity) getActivity()).getSupportFragmentManager())
                    .addFragment(index, null, ManageAccountsFragment.class.getName(), this, R.id.homeFlContainer);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubscribe:
                moveToNotificationSettingsPage();
                break;
            default:
                break;
        }
    }


    private void moveToNotificationSettingsPage() {
        if (accounts != null && accounts.size() > 0) {
            Intent intent = new Intent(getActivity(), NotificationSettingsActivity.class);
            intent.putExtra("isComingFromPopup", true);
            intent.putExtra("account", (Serializable) selectedAccount);
            getActivity().startActivityForResult(intent, REQUEST_CODE_SET_THRESHOLD);
        }
    }

    public void moveToMyDashboardPage(Account selectedAccount) {
        if (selectedAccount != null) {
           /* Intent intent = new Intent(getActivity(), QuestionActivity.class);
            intent.putExtra("account", (Serializable) selectedAccount);
            getActivity().startActivity(intent);*/
            FragmentUtils.newInstance(getFragmentManager())
                    .addFragment(FragmentUtils.INDEX_MY_DASHBOARD_FRAGMENT, selectedAccount, MyDashboardFragment.class.getName(), this, R.id.homeFlContainer);

        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        selectedAccount = accounts.get(i);
        loginFragmentBinding.setSelectedAccount(selectedAccount);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedAccount = accounts.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }

    public void showProgress(boolean isProgress) {
        if (isProgress) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (IS_THRESHOLD_SET) {
            dashboardViewModel.loadAccountsFromLocalDB(appPreferences.getUser_id());
            IS_THRESHOLD_SET = false;
        }
        IN_DASHBOARD = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        IN_DASHBOARD = false;
    }

    private void setServiceRestorationPopup(ArrayList<Account> accounts) {
        ArrayList<String> accountWithRestorationDisruption = new ArrayList<>();
        String namesWithServiceRestoration = "";
        for (Account account : accounts) {
            if (account.isRestoreAlertFlag()) {
                accountWithRestorationDisruption.add(account.getNickName());
            }
        }
        String name = getNames(accountWithRestorationDisruption, namesWithServiceRestoration);
        if (!TextUtils.isEmpty(name)) {
            DialogHelper.showUserAlert(getActivity(), getString(R.string.service_restoration), name,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogHelper.hidePopup();
                            userSettings.setRestoreAlertAcknowledgementFlag(true);
                            updateUserSettingInServer();
                        }
                    });
        }
    }

    private void setServiceDisruptionPopup(ArrayList<Account> accounts) {
        ArrayList<String> accountWithServiceDisruption = new ArrayList<>();
        String namesWithServiceDisruption = "";
        for (Account account : accounts) {
            if (account.isOutageAlertFlag()) {
                accountWithServiceDisruption.add(account.getNickName());
            }
        }
        String name = getNames(accountWithServiceDisruption, namesWithServiceDisruption);
        if (!TextUtils.isEmpty(name)) {
            DialogHelper.showUserAlert2(getActivity(), getString(R.string.service_disruption), name,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogHelper.hidePopupForDialog2();
                            userSettings.setOutageAlertAcknowledgementFlag(true);
                            updateUserSettingInServer();
                        }
                    });
        }
    }

    private String getNames(ArrayList<String> names, String name) {
        if (names.size() > 0) {
            if (names.size() == 1) {
                name = names.get(0);
            } else if (names.size() == 2) {
                name = TextUtils.join(" and ", names);
            } else {
                String firstPart = TextUtils.join(", ", names);
                String toReplace = ", ";
                int start = firstPart.lastIndexOf(toReplace);
                StringBuilder builder = new StringBuilder();
                builder.append(firstPart.substring(0, start));
                builder.append(" and ");
                builder.append(firstPart.substring(start + toReplace.length()));
                name = builder.toString();
            }
        }
        return name;
    }

    private void updateUserSettingInServer() {
        userSettingsViewModel.updateUserSettingsInServer(userSettings);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MMCQuestionsFragment.REQUEST_CODE_UPDATE_SINGLE_ACCOUNT_MMC && resultCode == Activity.RESULT_OK) {
            subscribe();
        }
    }
}
