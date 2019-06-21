package com.ey.dgs.adapters.chart;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ey.dgs.R;
import com.ey.dgs.dashboard.DashboardFragment;
import com.ey.dgs.model.Account;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.utils.Utils;
import com.ey.dgs.views.BarChart;

import java.util.ArrayList;


public class BarsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    BarChart barChart;
    ArrayList<ChartData> chartDatum;
    int parentLayoutWidth;
    int parentLayoutHeight;
    boolean isSelectionRequired;
    float highlightTextViewHeight = 50f;
    float largestValWithBuffer = 0f;

    public BarsAdapter(BarChart barChart, Context context, ArrayList<ChartData> chartDatum,
                       int parentLayoutWidth, int parentLayoutHeight, boolean isSelectionRequired) {
        this.context = context;
        this.barChart = barChart;
        this.chartDatum = chartDatum;
        this.parentLayoutWidth = parentLayoutWidth;
        this.parentLayoutHeight = parentLayoutHeight;
        this.isSelectionRequired = isSelectionRequired;
        this.largestValWithBuffer = barChart.getLargestValWithBuffer();
    }

    public class BarsHolder extends RecyclerView.ViewHolder {
        LinearLayout bar_line;
        View bar_line_structure;
        AppCompatTextView highLightedValue;

        public BarsHolder(View v) {
            super(v);
            bar_line = v.findViewById(R.id.bar_line);
            bar_line_structure = v.findViewById(R.id.bar_line_structure);
            highLightedValue = v.findViewById(R.id.highLightedValue);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bar_chart_line, parent, false);
        return new BarsHolder(itemView);
    }

    public void toggleSelection(int position) {
        boolean isCurrentlySelected = chartDatum.get(position).getIsSelected();

        if (!isCurrentlySelected) {
            // Change all other selections to false
            int i = 0;
            for (ChartData chartData : chartDatum) {
                if (i != position) {
                    chartData.setIsSelected(false);
                }
                i++;
            }
            barChart.selectLegends(position);
            //enableBarAndAssociated(chartDatum.get(position), position);
            // barChart.setHighLightedValue(chartDatum.get(position).getVal() + "");
        } else {
            barChart.selectLegends(position);
            // This means, a currently selected bar is being unselected
            barChart.removeHighLightedValue();
        }

        chartDatum.get(position).setIsSelected(!isCurrentlySelected);
        notifyDataSetChanged();
    }

    public void enableBarAndAssociated(ChartData chartData, int position) {
        barChart.setHighLightedValue(chartData.getVal() + "", position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BarsHolder barsHolder = (BarsHolder) holder;
        RecyclerView.LayoutParams lineHolderParams = (RecyclerView.LayoutParams) barsHolder.bar_line.getLayoutParams();
        //lineHolderParams.width = parentLayoutWidth/chartDatum.size();
        //barsHolder.bar_line.setLayoutParams(lineHolderParams);

        boolean isSelected = chartDatum.get(position).getIsSelected();
        //barsHolder.highLightedValue.getLayoutParams().height = (int) highlightTextViewHeight;
        if (isSelected) {
            barsHolder.highLightedValue.setVisibility(View.VISIBLE);
        } else {
            barsHolder.highLightedValue.setVisibility(View.INVISIBLE);
        }

        barsHolder.highLightedValue.setText("RM " + Utils.formatThreshold(chartDatum.get(position).getVal()));
        barsHolder.bar_line_structure.setBackground(context.getResources().getDrawable(
                isSelected ? R.drawable.bg_chart_bar_selected : R.drawable.bg_chart_bar_default));

        if (isSelectionRequired) {
            barsHolder.bar_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    barChart.onItemSelected(position);
                    toggleSelection(position);
                }
            });
        }

        ViewGroup.LayoutParams layoutParams = barsHolder.bar_line_structure.getLayoutParams();
        layoutParams.height = Math.round(getBarHeight(chartDatum.get(position)));
        //barsHolder.bar_line_structure.setLayoutParams(layoutParams);
        barsHolder.bar_line.getLayoutParams().height = parentLayoutHeight;
        barsHolder.bar_line.getLayoutParams().width = parentLayoutWidth / 7;
        /*final Account account = this.accounts.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_ACCOUNT:
                AccountHolder accountHolder = (AccountHolder) holder;
                accountHolder.tvAcountID.setText(account.getUserName());
                accountHolder.tvAccountName.setText(account.getNickName());
                accountHolder.tvLastBilledAmount.setText("RM " + account.getLastBilledAmount() + ".00");
                accountHolder.tvDate.setText(Utils.formatAccountDate(account.getLastBilledDate()));
                break;

            case TYPE_ADD_ACCOUNT:
                AddAccountHolder chartHolder = (AddAccountHolder) holder;

                break;
        }*/
    }

    /*private float getLargestValWithBuffer() {
        float largestVal = 0f;
        for(ChartData chartData: chartDatum) {
            if(largestVal <= chartData.getVal()) {
                largestVal = chartData.getVal();
            }
        }
        largestVal += largestVal * 0.1f;
        return largestVal;
    }*/

    private float getBarHeight(ChartData chartData) {
        return (chartData.getVal() * parentLayoutHeight / largestValWithBuffer);
    }

    @Override
    public int getItemCount() {
        return chartDatum.size();
    }

}
