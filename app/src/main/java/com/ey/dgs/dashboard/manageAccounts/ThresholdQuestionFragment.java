package com.ey.dgs.dashboard.manageAccounts;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ey.dgs.R;
import com.ey.dgs.adapters.ThresholdQuestionAdapter;
import com.ey.dgs.dashboard.questions.QuestionsViewModel;
import com.ey.dgs.model.AccountDetails;
import com.ey.dgs.model.AccountDetailsRequest;
import com.ey.dgs.utils.Utils;

import java.util.ArrayList;

public class ThresholdQuestionFragment extends Fragment {


    ArrayList<String> nicknames = new ArrayList<>();
    ArrayList<String> accountNumber = new ArrayList<>();
    private static AccountDetails accountDetailsArray[];
    private static ArrayList<String> accNumberFromQuestion;
    ThresholdQuestionAdapter adapter;
    private LinearLayoutManager rvLayoutManager;
    RecyclerView rvQuestions;
    View view;
    View loader;
    private QuestionsViewModel questionsViewModel;

    public static ThresholdQuestionFragment newInstance(ArrayList<String> nicknames, ArrayList<String> accountNumber) {
        ThresholdQuestionFragment fragment = new ThresholdQuestionFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(MMCManageAccountsFragment.NICKNAMES, nicknames);
        args.putStringArrayList(MMCManageAccountsFragment.ACCOUNT_NUMBER, accountNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nicknames = getArguments().getStringArrayList(MMCManageAccountsFragment.NICKNAMES);
            accountNumber = getArguments().getStringArrayList(MMCManageAccountsFragment.ACCOUNT_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.threshold_question, container, false);
        initViews();
        subscribe();
        return view;
    }

    private void initViews() {
        loader = view.findViewById(R.id.loader);
        rvQuestions = view.findViewById(R.id.rvThreshold);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rvQuestions.setLayoutManager(rvLayoutManager);
        adapter = new ThresholdQuestionAdapter(this, nicknames, accountNumber);
        rvQuestions.setAdapter(adapter);
    }

    private void subscribe() {
        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel.class);
        questionsViewModel.setContext(getActivity());
    }

    public void setThresholdValues(ArrayList<String> accountNumber, String[] threshold) {
        Utils.showToast(getActivity(), "Clicked");
        if (accNumberFromQuestion.equals(accountNumber)) {
            for (int i = 1; i < accountNumber.size(); i++) {
                accountDetailsArray[i].setUserThreshold(threshold[i]);
            }
        }
        callAccountDetailsAPiService(accountDetailsArray);
    }

    private void callAccountDetailsAPiService(AccountDetails[] accountDetails) {
        AccountDetailsRequest accountDetailsRequest =
                new AccountDetailsRequest("Admin@xyzmail.com", accountDetails);

        //showProgress(true);
        questionsViewModel.updateAccountSettingsInServer(accountDetailsRequest);
        questionsViewModel.getLoaderData().observe(getViewLifecycleOwner(), showProgress -> {
            //showProgress(showProgress);
        });
    }

    public static void setValues(ArrayList<String> accountNumbers, ArrayList<Integer> peopleInProperty) {
        accNumberFromQuestion = accountNumbers;
        ArrayList<String> peopleInPropertyArray = new ArrayList<>();
        for (Integer i: peopleInProperty) {
            peopleInPropertyArray.add(String.valueOf(i));
        }
        accountDetailsArray = new AccountDetails[accountNumbers.size()];
        if(accountNumbers.size() ==  peopleInProperty.size()) {
            for (int i = 1; i < accountNumbers.size(); i++) {
                accountDetailsArray[i] = new AccountDetails(accountNumbers.get(i),
                        peopleInPropertyArray.get(i), null);
            }
        }
    }

    public void showProgress(boolean isProgress) {
        if (isProgress) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
            if(questionsViewModel.isSuccess()) {
                //showSuccessPopup();
            }
        }
    }
}
