package com.ey.dgs.views;

import com.ey.dgs.R;
import com.ey.dgs.adapters.chart.BarsAdapter;
import com.ey.dgs.model.chart.ChartData;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
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

    private void chartEntryPoint() {
        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.bar_chart, this, true);

        LinearLayout bars_container = view.findViewById(R.id.bars_container);
        titleText = view.findViewById(R.id.titleText);

        bars_container.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalViewListenerClass());

        LayoutInflater barInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View barLabel;
        /*for(int i=0; i<chartData.getBarXValues().length; i++) {
            barLabel = barInflater.inflate(R.layout.bar_chart_xmarks, (ViewGroup)view.findViewById(R.id.xmarks), true);
            ((AppCompatTextView)barLabel.findViewById(R.id.x_txt_label)).setText(chartData.getBarXValues()[i]);
            // ((AppCompatTextView)barLabel.findViewById(R.id.xmarks).findViewById(R.id.xmarks)).setText(chartData.getBarXValues()[i]);
        }*/
    }

    public void fillChartBarData(int parentLayoutWidth) {
        RecyclerView barsRecylerView = view.findViewById(R.id.bars);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        BarsAdapter barsAdapter = new BarsAdapter(context, chartDatum, parentLayoutWidth);
        barsRecylerView.setLayoutManager(layoutManager);
        barsRecylerView.setAdapter(barsAdapter);
        barsAdapter.notifyDataSetChanged();
    }

    boolean widthCreated = false;
    class GlobalViewListenerClass implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            View vw = (View) findViewById(R.id.bars_container);
            if(!widthCreated) {
                fillChartBarData(vw.getWidth());
            }
            widthCreated = (vw.getWidth() > 0);
        }
    }

}
