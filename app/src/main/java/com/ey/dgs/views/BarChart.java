package com.ey.dgs.views;

import com.ey.dgs.R;
import com.ey.dgs.adapters.chart.BarsAdapter;
import com.ey.dgs.adapters.chart.LegendsAdapter;
import com.ey.dgs.model.chart.ChartData;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Sooraj.HS on 20-05-2019.
 */

public class BarChart extends LinearLayout {

    ArrayList<ChartData> chartDatum;
    Context context;
    View view;
    AppCompatTextView titleText;
    AppCompatTextView highLightedValue;
    AppCompatTextView thresholdText;
    LinearLayout thresholdHolder;
    boolean isSelectionRequired;
    boolean isThresholdRequired;
    float thresholdValue = 0f;
    String unit = "RM";

    public BarChart(Context context) {
        super(context);
        this.context = context;
    }

    public BarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BarChart setData(ArrayList<ChartData> chartDatum) {
        this.chartDatum = chartDatum;
        chartEntryPoint();
        return this;
    }

    public BarChart setTitle(String titleText) {
        this.titleText.setText(titleText);
        return this;
    }

    public BarChart setSelectionRequired(boolean isSelectionRequired) {
        this.isSelectionRequired = isSelectionRequired;
        return this;
    }

    public BarChart setBarUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public BarChart setThreshold(boolean isThresholdRequired, float thresholdValue) {
        this.isThresholdRequired = isThresholdRequired;
        this.thresholdValue = thresholdValue;
        return this;
    }

    private void chartEntryPoint() {
        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.bar_chart, this, true);

        RelativeLayout bars_container = view.findViewById(R.id.bars_container);
        titleText = view.findViewById(R.id.titleText);

        highLightedValue = view.findViewById(R.id.highLightedValue);

        thresholdHolder = view.findViewById(R.id.thresholdHolder);
        thresholdText = view.findViewById(R.id.thresholdText);

        thresholdHolder.setVisibility(View.GONE);
        removeHighLightedValue();
        bars_container.getViewTreeObserver().addOnGlobalLayoutListener(new BarsViewListenerClass());
    }

    LegendsAdapter xAxisAdapter;

    public void fillChartBarData(int parentLayoutWidth, int parentLayoutHeight) {
        RecyclerView barsRecylerView = view.findViewById(R.id.bars);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        BarsAdapter barsAdapter = new BarsAdapter(this, context, chartDatum, parentLayoutWidth, parentLayoutHeight, isSelectionRequired);
        barsRecylerView.setLayoutManager(layoutManager);
        barsRecylerView.setAdapter(barsAdapter);
        barsAdapter.notifyDataSetChanged();

        RecyclerView xAxisRecylerView = view.findViewById(R.id.xmarks);
        RecyclerView.LayoutManager legendlayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        xAxisAdapter = new LegendsAdapter(context, chartDatum, parentLayoutWidth);
        xAxisRecylerView.setLayoutManager(legendlayoutManager);
        xAxisRecylerView.setAdapter(xAxisAdapter);
        xAxisAdapter.notifyDataSetChanged();

        barsRecylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                xAxisRecylerView.scrollBy(dx, dy);
            }
        });

        if (isThresholdRequired) {
            thresholdHolder.setVisibility(View.VISIBLE);
            thresholdText.setText(unit + " " + this.thresholdValue);
            thresholdHolder.getViewTreeObserver().addOnGlobalLayoutListener(new ThresholdViewListenerClass());
            /*float yVal = getThresholdY(this.thresholdValue) - 100;
            final ObjectAnimator oa = ObjectAnimator.ofFloat(this.thresholdHolder, "y", yVal);
            oa.setDuration(800);
            oa.start();*/

        } else {
            thresholdHolder.setVisibility(View.GONE);
        }
    }

    public void animateThresholdItem() {
        float yVal = getThresholdY(this.thresholdValue) - (thresholdHolder.getHeight() * 1f) + 20f;
        final ObjectAnimator oa = ObjectAnimator.ofFloat(this.thresholdHolder, "y", yVal);
        oa.setDuration(1000);
        oa.start();
    }

    public void selectLegends(int position) {
        xAxisAdapter.toggleSelection(position);
    }

    public float getLargestValWithBuffer() {
        float largestVal = 0f;
        for (ChartData chartData : chartDatum) {
            if (largestVal <= chartData.getVal()) {
                largestVal = chartData.getVal();
            }
        }
        largestVal += largestVal * 0.1f;
        return largestVal + 20f;
    }

    private float getThresholdY(float thresholdValue) {
        float maxValueInHolder = getLargestValWithBuffer();
        return ((maxValueInHolder - thresholdValue) * layoutHeight / maxValueInHolder);
    }

    int layoutWidth = 0;
    int layoutHeight = 0;

    public void setHighLightedValue(String highLightedValue, int position) {
        this.highLightedValue.setVisibility(View.VISIBLE);
        this.highLightedValue.setText(unit + " " + highLightedValue);
        float xVal = layoutWidth * position / chartDatum.size();
        //this.highLightedValue.setX(xVal);
        this.highLightedValue.setWidth(layoutWidth / chartDatum.size());
        this.highLightedValue.setGravity(Gravity.CENTER);

        final ObjectAnimator oa = ObjectAnimator.ofFloat(this.highLightedValue, "x", xVal);
        oa.setDuration(500);
        oa.start();
    }

    public void removeHighLightedValue() {
        this.highLightedValue.setVisibility(View.GONE);
    }

    boolean widthCreated = false;

    class BarsViewListenerClass implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            View vw = findViewById(R.id.bars_container);
            if (!widthCreated) {
                layoutWidth = vw.getWidth();
                layoutHeight = vw.getHeight();
                fillChartBarData(layoutWidth, layoutHeight);
            }
            widthCreated = (vw.getWidth() > 0);
        }
    }

    boolean heightCreated = false;

    class ThresholdViewListenerClass implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            View vw = findViewById(R.id.thresholdHolder);

            if (!heightCreated) {
                animateThresholdItem();
               /* layoutWidth = vw.getWidth();
                layoutHeight = vw.getHeight();
                fillChartBarData(layoutWidth, layoutHeight);*/
            }
            heightCreated = (vw.getHeight() > 0);
        }
    }

}
