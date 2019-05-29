package com.ey.dgs.views;

import com.ey.dgs.R;
import com.ey.dgs.adapters.chart.BarsAdapter;
import com.ey.dgs.adapters.chart.LegendsAdapter;
import com.ey.dgs.model.chart.ChartData;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
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
    boolean isSelectionRequired;

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

    private void chartEntryPoint() {
        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.bar_chart, this, true);

        LinearLayout bars_container = view.findViewById(R.id.bars_container);
        titleText = view.findViewById(R.id.titleText);

        highLightedValue = view.findViewById(R.id.highLightedValue);

        removeHighLightedValue();
        bars_container.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalViewListenerClass());
    }

    public void fillChartBarData(int parentLayoutWidth, int parentLayoutHeight) {
        RecyclerView barsRecylerView = view.findViewById(R.id.bars);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        BarsAdapter barsAdapter = new BarsAdapter(this, context, chartDatum, parentLayoutWidth, parentLayoutHeight, isSelectionRequired);
        barsRecylerView.setLayoutManager(layoutManager);
        barsRecylerView.setAdapter(barsAdapter);
        barsAdapter.notifyDataSetChanged();

        RecyclerView xAxisRecylerView = view.findViewById(R.id.xmarks);
        RecyclerView.LayoutManager legendlayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        LegendsAdapter xAxisAdapter = new LegendsAdapter(context, chartDatum, parentLayoutWidth);
        xAxisRecylerView.setLayoutManager(legendlayoutManager);
        xAxisRecylerView.setAdapter(xAxisAdapter);
        xAxisAdapter.notifyDataSetChanged();
    }

    int layoutWidth = 0;
    public void setHighLightedValue(String highLightedValue, int position) {
        this.highLightedValue.setVisibility(View.VISIBLE);
        this.highLightedValue.setText(highLightedValue);
        float xVal = layoutWidth*position/chartDatum.size();
        //this.highLightedValue.setX(xVal);
        this.highLightedValue.setWidth(layoutWidth/chartDatum.size());
        this.highLightedValue.setGravity(Gravity.CENTER);

        final ObjectAnimator oa = ObjectAnimator.ofFloat(this.highLightedValue, "x", xVal);
        oa.setDuration(500);
        oa.start();
    }

    public void removeHighLightedValue() {
        this.highLightedValue.setVisibility(View.GONE);
    }

    boolean widthCreated = false;
    class GlobalViewListenerClass implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            View vw = findViewById(R.id.bars_container);
            if(!widthCreated) {
                layoutWidth = vw.getWidth();
                fillChartBarData(vw.getWidth(), vw.getHeight());
            }
            widthCreated = (vw.getWidth() > 0);
        }
    }

}
