package com.ey.dgs.dashboard.manageAccounts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ey.dgs.R;
import com.ey.dgs.adapters.AccountsQuestionsPagerAdapter;

import java.util.ArrayList;

public class MMCManageAccountsFragment extends Fragment{

    public static final String NICKNAMES = "nicknames";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    public static final String PEOPLEINPROPERTY = "peopleInProperty";
    public static final String USERTHRESHOLD = "userThreshold";
    public static final String AVGTHRESHOLD = "avgThreshold";
    ArrayList<String> nicknames = new ArrayList<>();
    ArrayList<String> accountNumber = new ArrayList<>();
    ArrayList<String> peopleInProperty = new ArrayList<>();
    ArrayList<String> userThreshold = new ArrayList<>();
    ArrayList<String> avgThreshold = new ArrayList<>();
    private static ViewPager vpQuestions;
    private TabLayout tlDots;
    private AccountsQuestionsPagerAdapter adapter;
    View view;

    public static MMCManageAccountsFragment newInstance(ArrayList<String> nicknames, ArrayList<String> accountNumber,
                                                        ArrayList<String> peopleInProperty, ArrayList<String> userThreshold,
                                                        ArrayList<String> avgThreshold) {
        MMCManageAccountsFragment fragment = new MMCManageAccountsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(NICKNAMES, nicknames);
        args.putStringArrayList(ACCOUNT_NUMBER, accountNumber);
        args.putStringArrayList(PEOPLEINPROPERTY, peopleInProperty);
        args.putStringArrayList(USERTHRESHOLD, userThreshold);
        args.putStringArrayList(AVGTHRESHOLD, avgThreshold);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nicknames = getArguments().getStringArrayList(NICKNAMES);
            accountNumber = getArguments().getStringArrayList(ACCOUNT_NUMBER);
            peopleInProperty = getArguments().getStringArrayList(PEOPLEINPROPERTY);
            userThreshold = getArguments().getStringArrayList(USERTHRESHOLD);
            avgThreshold = getArguments().getStringArrayList(AVGTHRESHOLD);
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
        adapter.addFrag(PeopleQuestionFragment.newInstance(nicknames, accountNumber, peopleInProperty));
        adapter.addFrag(ThresholdQuestionFragment.newInstance(nicknames, accountNumber, userThreshold, avgThreshold));
        vpQuestions.setAdapter(adapter);
        tlDots.setupWithViewPager(vpQuestions, true);
    }

    public static void moveNext() {
        vpQuestions.setCurrentItem(vpQuestions.getCurrentItem() + 1);
    }
}
