package com.ey.dgs.dashboard.manageAccounts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ey.dgs.HomeActivity;
import com.ey.dgs.R;
import com.ey.dgs.adapters.AccountsQuestionsPagerAdapter;
import com.ey.dgs.adapters.ThresholdQuestionAdapter;
import com.ey.dgs.utils.FragmentUtils;
import com.ey.dgs.utils.Utils;

import java.util.ArrayList;

import static com.ey.dgs.utils.FragmentUtils.INDEX_MANAGE_ACCOUNTS;

public class MMCManageAccountsFragment extends Fragment{

    public static final String NICKNAMES = "nicknames";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    ArrayList<String> nicknames = new ArrayList<>();
    ArrayList<String> accountNumber = new ArrayList<>();
    private ViewPager vpQuestions;
    private TabLayout tlDots;
    private AccountsQuestionsPagerAdapter adapter;
    private Button btnNext;
    View view;

    public static MMCManageAccountsFragment newInstance(ArrayList<String> nicknames, ArrayList<String> accountNumber) {
        MMCManageAccountsFragment fragment = new MMCManageAccountsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(NICKNAMES, nicknames);
        args.putStringArrayList(ACCOUNT_NUMBER, accountNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nicknames = getArguments().getStringArrayList(NICKNAMES);
            accountNumber = getArguments().getStringArrayList(ACCOUNT_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manage_account_people, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        vpQuestions = view.findViewById(R.id.vpQuestionItems);
        tlDots = view.findViewById(R.id.tlDots);
        adapter = new AccountsQuestionsPagerAdapter(getChildFragmentManager());
        adapter.addFrag(PeopleQuestionFragment.newInstance(nicknames, accountNumber));
        adapter.addFrag(ThresholdQuestionFragment.newInstance(nicknames, accountNumber));
        vpQuestions.setAdapter(adapter);
        tlDots.setupWithViewPager(vpQuestions, true);
        btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewForQuestion2();
            }
        });
    }

    private void setViewForQuestion2() {
        if (vpQuestions.getCurrentItem() >= 2) {
            callAccountDetailsAPiService();
        } else {
            vpQuestions.setCurrentItem(vpQuestions.getCurrentItem() + 1);
            btnNext.setVisibility(View.GONE);
        }
    }

    private void callAccountDetailsAPiService() {
    }
}
