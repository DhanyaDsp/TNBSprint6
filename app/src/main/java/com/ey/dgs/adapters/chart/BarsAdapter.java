package com.ey.dgs.adapters.chart;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    boolean isSelectionRequired;

    public BarsAdapter(BarChart barChart, Context context, ArrayList<ChartData> chartDatum, int parentLayoutWidth, boolean isSelectionRequired) {
        this.context = context;
        this.barChart = barChart;
        this.chartDatum = chartDatum;
        this.parentLayoutWidth = parentLayoutWidth;
        this.isSelectionRequired = isSelectionRequired;
    }

    public class BarsHolder extends RecyclerView.ViewHolder {
        LinearLayout bar_line;
        View bar_line_structure;
        public BarsHolder(View v) {
            super(v);
            bar_line = v.findViewById(R.id.bar_line);
            bar_line_structure = v.findViewById(R.id.bar_line_structure);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bar_chart_line, parent, false);
        return new BarsHolder(itemView);
    }

    public void toggleSelection(int position) {
        boolean isCurrentlySelected = chartDatum.get(position).getIsSelected();

        if(!isCurrentlySelected) {
            // Change all other selections to false
            int i = 0;
            for(ChartData chartData: chartDatum) {
                if(i!=position) {
                    chartData.setIsSelected(false);
                }
                i++;
            }
            enableBarAndAssociated(chartDatum.get(position), position);
            // barChart.setHighLightedValue(chartDatum.get(position).getVal() + "");
        }
        else {
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
        lineHolderParams.width = parentLayoutWidth/chartDatum.size();
        barsHolder.bar_line.setLayoutParams(lineHolderParams);
                //new LinearLayout.LayoutParams(parentLayoutWidth/chartDatum.size(), LinearLayout.LayoutParams.MATCH_PARENT));

        boolean isSelected = chartDatum.get(position).getIsSelected();

        barsHolder.bar_line_structure.setBackground(context.getResources().getDrawable(
                isSelected?R.drawable.bg_chart_bar_selected: R.drawable.bg_chart_bar_default));

        if(isSelectionRequired) {
            barsHolder.bar_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleSelection(position);
                }
            });
        }

        ViewGroup.LayoutParams layoutParams = barsHolder.bar_line_structure.getLayoutParams();
        //layoutParams.height = 30;
        barsHolder.bar_line_structure.setLayoutParams(layoutParams);

        /*final Account account = this.accounts.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_ACCOUNT:
                AccountHolder accountHolder = (AccountHolder) holder;
                accountHolder.tvAcountID.setText(account.getAccountNumber());
                accountHolder.tvAccountName.setText(account.getNickName());
                accountHolder.tvLastBilledAmount.setText("RM " + account.getLastBilledAmount() + ".00");
                accountHolder.tvDate.setText(Utils.formatAccountDate(account.getLastBilledDate()));
                break;

            case TYPE_ADD_ACCOUNT:
                AddAccountHolder chartHolder = (AddAccountHolder) holder;

                break;
        }*/
    }

    @Override
    public int getItemCount() {
        return chartDatum.size();
    }



}
