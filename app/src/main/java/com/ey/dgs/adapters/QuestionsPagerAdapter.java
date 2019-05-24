package com.ey.dgs.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ey.dgs.R;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.Question;

import java.util.ArrayList;

public class QuestionsPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<Question> questions;

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
        LinearLayout llMultiChoice = layout.findViewById(R.id.llMultiChoice);
        tvQuestion.setText(questions.get(position).getQuestion());
        if (question.getType() == Question.TYPE_TEXT) {
            etAnswer.setVisibility(View.VISIBLE);
            llDropDown.setVisibility(View.GONE);
            llMultiChoice.setVisibility(View.GONE);
        } else if (question.getType() == Question.TYPE_MULTI_CHOICE) {
            etAnswer.setVisibility(View.VISIBLE);
            llDropDown.setVisibility(View.GONE);
            llMultiChoice.setVisibility(View.VISIBLE);
        } else if (question.getType() == Question.TYPE_DROPDOWN) {
            etAnswer.setVisibility(View.GONE);
            llDropDown.setVisibility(View.VISIBLE);
            llMultiChoice.setVisibility(View.GONE);
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

}