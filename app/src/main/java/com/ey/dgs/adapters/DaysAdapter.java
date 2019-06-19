package com.ey.dgs.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ey.dgs.R;
import com.ey.dgs.model.chart.ChartData;
import com.ey.dgs.utils.Utils;

import java.util.ArrayList;

public class DaysAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<ChartData> chartDatum = new ArrayList<>();
    private LayoutInflater mInflater;
    private int selectedItem;

    public DaysAdapter(Context context, ArrayList<ChartData> chartDatum) {
        mContext = context;
        this.chartDatum = chartDatum;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return chartDatum.size();
    }

    public ChartData getItem(int position) {
        return chartDatum.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int index, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.day_item, null);
            holder = new ViewHolder();
            holder.tvDay = convertView.findViewById(R.id.tvDay);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChartData chartData = getItem(index);
        if (selectedItem == index) {
            holder.tvDay.setTextColor(mContext.getResources().getColor(R.color.purple));
        } else {
            holder.tvDay.setTextColor(mContext.getResources().getColor(R.color.purple_50));
        }
        holder.tvDay.setText(chartData.getTag());

        return convertView;
    }

    public void selectItem(int position) {
        this.selectedItem = position;
        notifyDataSetChanged();
    }

    class ViewHolder {
        AppCompatTextView tvDay;
    }


}