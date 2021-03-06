package com.ey.dgs.dashboard.myaccount;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.adapters.MyAccountTabsAdapter;
import com.ey.dgs.dashboard.myaccount.breakdown.BreakdownQuestionsFragment;
import com.ey.dgs.dashboard.myaccount.breakdown.EnergyInsightFragment;
import com.ey.dgs.dashboard.questions.MMCQuestionsFragment;
import com.ey.dgs.databinding.FragmentMyAccountBinding;
import com.ey.dgs.model.Account;
import com.ey.dgs.notifications.settings.NotificationSettingsActivity;
import com.ey.dgs.utils.CustomViewPager;

import java.io.Serializable;

public class MyAccountFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Account account;
    private View view;
    private MyAccountTabsAdapter accountTabsAdapter;
    private CustomViewPager vpAccountTabs;

    public MyAccountFragment() {
    }

    public static MyAccountFragment newInstance(Account account) {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();
        args.putSerializable("account", (Serializable) account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = (Account) getArguments().getSerializable("account");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mListener != null) {
            mListener.onFragmentInteraction(account.getNickName());
        }
        FragmentMyAccountBinding myAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false);
        myAccountBinding.setFragment(this);
        myAccountBinding.setAccount(account);
        view = myAccountBinding.getRoot();
        initViews();
        return view;
    }

    private void initViews() {
        setupViewPager();
        addCustomTabs();
    }

    private void setupViewPager() {
        vpAccountTabs = view.findViewById(R.id.vpAccountTabs);
        vpAccountTabs.setPagingEnabled(false);
        accountTabsAdapter = new MyAccountTabsAdapter(getChildFragmentManager(), getActivity());
        accountTabsAdapter.addFrag(ConsumptionFragment.newInstance(account), "Consumptions");
        accountTabsAdapter.addFrag(BreakdownQuestionsFragment.newInstance(null), "Energy Insights");
        vpAccountTabs.setAdapter(accountTabsAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String title);
    }

    public void addThreshold(View view) {
        moveToNotificationSettingsPage();
    }

    private void moveToNotificationSettingsPage() {
        if (account != null) {
            Intent intent = new Intent(getActivity(), NotificationSettingsActivity.class);
            intent.putExtra("isAddThreshold", true);
            intent.putExtra("account", account);
            startActivity(intent);
        }
    }

    public Account getAccount() {
        return account;
    }

    private void addCustomTabs() {
        TabLayout tabLayout = view.findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(vpAccountTabs);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(accountTabsAdapter.getTabView(i));
        }
        //((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1).setClickable(false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MMCQuestionsFragment.REQUEST_CODE_UPDATE_SINGLE_ACCOUNT_MMC && resultCode == Activity.RESULT_OK) {
            Account account = (Account) data.getSerializableExtra("account");
            ((ConsumptionFragment) accountTabsAdapter.getItem(0)).refresh(account);
            Fragment targetFragment = getTargetFragment();
            if (targetFragment != null) {
                targetFragment.onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        new Intent().putExtra("account", (Serializable) account));
            }
        }
    }
}
