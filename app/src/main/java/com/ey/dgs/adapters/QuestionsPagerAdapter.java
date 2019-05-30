package com.ey.dgs.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ey.dgs.R;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.Question;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.views.BarChart;

import java.util.ArrayList;

public class QuestionsPagerAdapter extends PagerAdapter {


    private String thresholdSuggestions;
    private String[] thresholdSuggestionsArray;
    private Context mContext;
    private ArrayList<Question> questions;
    private View.OnClickListener btnClickListener;

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
        etAnswer.setText(question.getResponse());
        etMonths.setText(question.getResponse());
        LinearLayout llMultiChoice = layout.findViewById(R.id.llMultiChoice);
        AppCompatButton btnNext = layout.findViewById(R.id.btnNext);
        BarChart bar_chart = layout.findViewById(R.id.bar_chart);
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
                    etAnswer.setText(thresholdSuggestionsArray[0]);
                }
            });
            btnTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAnswer.setText(thresholdSuggestionsArray[1]);
                }
            });
            btnThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAnswer.setText(thresholdSuggestionsArray[2]);
                }
            });
            btnFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etAnswer.setText(thresholdSuggestionsArray[3]);
                }
            });
        }
        ArrayList<ChartData> chartDatum = new ArrayList<>();
        ChartData chartData;

        for (int i = 0; i < 6; i++) {
            chartData = new ChartData();
            chartData.setTag("LB" + (i + 1));
            chartData.setVal(20.0f);
            chartDatum.add(chartData);
        }

        bar_chart.setData(chartDatum).setTitle(null);
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
}