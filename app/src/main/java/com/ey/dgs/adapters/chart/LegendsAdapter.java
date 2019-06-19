package com.ey.dgs.adapters.chart;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ey.dgs.R;

import com.ey.dgs.model.chart.ChartData;

import java.util.ArrayList;

/**
 * Created by Sooraj.HS on 24-05-2019.
 */

public class LegendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ChartData> chartDatum;
    int parentLayoutWidth;

    public LegendsAdapter(Context context, ArrayList<ChartData> chartDatum, int parentLayoutWidth) {
        this.context = context;
        this.chartDatum = chartDatum;
        this.parentLayoutWidth = parentLayoutWidth;
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
        }

        chartDatum.get(position).setIsSelected(!isCurrentlySelected);
        notifyDataSetChanged();
    }


    public class LegendHolder extends RecyclerView.ViewHolder {
        AppCompatTextView x_txt_label;

        public LegendHolder(View v) {
            super(v);
            x_txt_label = v.findViewById(R.id.x_txt_label);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bar_chart_xmarks, parent, false);
        return new LegendHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LegendHolder legendHolder = (LegendHolder) holder;
        //legendHolder.x_txt_label.setLayoutParams(new LinearLayout.LayoutParams(parentLayoutWidth/chartDatum.size(), LinearLayout.LayoutParams.MATCH_PARENT));

        legendHolder.x_txt_label.setText(chartDatum.get(position).getTag());

        legendHolder.x_txt_label.setTextColor(chartDatum.get(position).getIsSelected() ?
                context.getResources().getColor(R.color.colorAccent) : context.getResources().getColor(R.color.legend_disabled));
        legendHolder.x_txt_label.getLayoutParams().width = parentLayoutWidth / 7;
        /*barsHolder.bar_line.setLayoutParams(new LinearLayout.LayoutParams(parentLayoutWidth/6, LinearLayout.LayoutParams.MATCH_PARENT));

        boolean isSelected = chartDatum.get(position).getIsSelected();

        barsHolder.bar_line_structure.setBackground(context.getResources().getDrawable(
                isSelected?R.drawable.bg_chart_bar_selected: R.drawable.bg_chart_bar_default));
        barsHolder.bar_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSelection(position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return chartDatum.size();
    }


}
