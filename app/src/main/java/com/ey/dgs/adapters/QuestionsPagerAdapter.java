package com.ey.dgs.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ey.dgs.R;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.BillingDetails;
import com.ey.dgs.model.BillingHistory;
import com.ey.dgs.model.EnergyConsumptions;
import com.ey.dgs.model.Question;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.views.BarChart;
import com.google.gson.Gson;

import java.util.ArrayList;

public class QuestionsPagerAdapter extends PagerAdapter implements TextWatcher, View.OnKeyListener {


    private String thresholdSuggestions;
    private String[] thresholdSuggestionsArray;
    private Context mContext;
    private ArrayList<Question> questions;
    private View.OnClickListener btnClickListener;
    private BillingHistory billingHistory;
    private EnergyConsumptions energyConsumptions;
    private BarChart bar_chart;

    public QuestionsPagerAdapter(Context context, ArrayList<Question> questions) {
        mContext = context;
        this.questions = questions;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        Question question = questions.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.questions_pager_item, viewGroup, false);
        AppCompatTextView tvQuestion = layout.findViewById(R.id.tvQuestion);
        LinearLayout llDropDown = layout.findViewById(R.id.llDropDown);
        AppCompatEditText etAnswer = layout.findViewById(R.id.etAnswer);
        AppCompatEditText etMonths = layout.findViewById(R.id.spMonths);
        etAnswer.setOnKeyListener(this);
        etMonths.setOnKeyListener(this);
        etAnswer.setText(question.getResponse().trim());
        etAnswer.addTextChangedListener(this);
        etMonths.setText(question.getResponse().trim());
        LinearLayout llMultiChoice = layout.findViewById(R.id.llMultiChoice);
        AppCompatButton btnNext = layout.findViewById(R.id.btnNext);
        bar_chart = layout.findViewById(R.id.bar_chart);
        AppCompatButton btnOne = layout.findViewById(R.id.btnOne);
        AppCompatButton btnTwo = layout.findViewById(R.id.btnTwo);
        AppCompatButton btnThree = layout.findViewById(R.id.btnThree);
        AppCompatButton btnFour = layout.findViewById(R.id.btnFour);
        if (thresholdSuggestionsArray != null && thresholdSuggestionsArray.length > 0) {
            btnOne.setText(thresholdSuggestionsArray[0]);
            btnTwo.setText(thresholdSuggestionsArray[1]);
            btnThree.setText(thresholdSuggestionsArray[2]);
            btnFour.setText(thresholdSuggestionsArray[3]);

            btnOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAnswer.setText(thresholdSuggestionsArray[0].trim());
                }
            });
            btnTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAnswer.setText(thresholdSuggestionsArray[1].trim());
                }
            });
            btnThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAnswer.setText(thresholdSuggestionsArray[2].trim());
                }
            });
            btnFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAnswer.setText(thresholdSuggestionsArray[3].trim());
                }
            });
        }

        setChartData(bar_chart, billingHistory);

        tvQuestion.setText(questions.get(position).getQuestion());
        if (question.getQuestionId() == 1) {
            etAnswer.setVisibility(View.VISIBLE);
            llDropDown.setVisibility(View.GONE);
            llMultiChoice.setVisibility(View.GONE);
            bar_chart.setVisibility(View.GONE);
        } else if (question.getQuestionId() == 2) {
            etAnswer.setVisibility(View.VISIBLE);
            llDropDown.setVisibility(View.GONE);
            llMultiChoice.setVisibility(View.GONE);
            bar_chart.setVisibility(View.GONE);
        } else if (question.getQuestionId() == 3) {
            etAnswer.setVisibility(View.GONE);
            llDropDown.setVisibility(View.VISIBLE);
            llMultiChoice.setVisibility(View.GONE);
            bar_chart.setVisibility(View.GONE);
        } else if (question.getQuestionId() == 4) {
            etAnswer.setVisibility(View.VISIBLE);
            llDropDown.setVisibility(View.GONE);
            llMultiChoice.setVisibility(View.VISIBLE);
            bar_chart.setVisibility(View.VISIBLE);
        }
        viewGroup.addView(layout);
        return layout;
    }

    private void setChartData(BarChart bar_chart, BillingHistory billingHistory) {
        Gson gson = new Gson();
        BillingDetails[] billingDetails = gson.fromJson(billingHistory.getBillingDetails(), BillingDetails[].class);

        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        String startDate = Utils.formatAccountDate(billingHistory.getAccount().getBillingCycleStartDate());
        String endDate = Utils.formatAccountDate(billingHistory.getAccount().getBillingCycleEndDate());
        for (int i = 0; i < billingDetails.length; i++) {
            BillingDetails billingDetail = billingDetails[i];
            chartData = new ChartData();
            chartData.setTag(Utils.formatAccountDate(billingDetail.getBilledDate()));
            chartData.setVal(billingDetail.getBilledValue());
            chartDatum.add(chartData);
        }
        if (billingHistory.getAccount().isThreshold()) {
            bar_chart.setData(chartDatum)
                    .setTitle(startDate + " - " + endDate)
                    .setBarUnit("RM")
                    .setThreshold(true, Float.parseFloat(energyConsumptions.getUserThreshold()))
                    .setSelectionRequired(true);
        }
    }

    private void setChartData(BarChart bar_chart, BillingHistory billingHistory, float threshold) {
        Gson gson = new Gson();
        BillingDetails[] billingDetails = gson.fromJson(billingHistory.getBillingDetails(), BillingDetails[].class);

        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        String startDate = Utils.formatAccountDate(billingHistory.getAccount().getBillingCycleStartDate());
        String endDate = Utils.formatAccountDate(billingHistory.getAccount().getBillingCycleEndDate());
        for (int i = 0; i < billingDetails.length; i++) {
            BillingDetails billingDetail = billingDetails[i];
            chartData = new ChartData();
            chartData.setTag(Utils.formatAccountDate(billingDetail.getBilledDate()));
            chartData.setVal(billingDetail.getBilledValue());
            chartDatum.add(chartData);
        }
        if (billingHistory.getAccount().isThreshold()) {
            bar_chart.setData(chartDatum)
                    .setTitle(startDate + " - " + endDate)
                    .setBarUnit("RM")
                    .setThreshold(true, threshold)
                    .setSelectionRequired(true);
        }
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object view) {
        viewGroup.removeView((View) view);
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setThresholdSuggestions(String thresholdSuggestions) {
        if (thresholdSuggestions != null) {
            this.thresholdSuggestions = thresholdSuggestions;
            this.thresholdSuggestions = this.thresholdSuggestions.replace("[", "");
            this.thresholdSuggestions = this.thresholdSuggestions.replace("]", "");
            this.thresholdSuggestionsArray = this.thresholdSuggestions.split(",");
            notifyDataSetChanged();
        }
    }

    public String getThresholdSuggestions() {
        return thresholdSuggestions;
    }

    public void setBillingHistory(BillingHistory billingHistory) {
        this.billingHistory = billingHistory;
    }

    public BillingHistory getBillingHistory() {
        return billingHistory;
    }

    public void setEnergyConsumptions(EnergyConsumptions energyConsumptions) {
        this.energyConsumptions = energyConsumptions;
    }

    public EnergyConsumptions getEnergyConsumptions() {
        return energyConsumptions;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String strThreshold = s.toString();
        if (!TextUtils.isEmpty(strThreshold)) {
            Float threshold = Float.parseFloat(strThreshold);
            setChartData(bar_chart, billingHistory, threshold);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    Utils.hideKeyBoard((Activity) mContext);
                    return true;
                default:
                    break;
            }
        }
        return false;
    }
}