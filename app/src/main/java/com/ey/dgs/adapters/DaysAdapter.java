package com.ey.dgs.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.ey.dgs.R;
import com.ey.dgs.databinding.DashboardFragmentBinding;

import java.util.ArrayList;

public class DaysAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> days = new ArrayList<>();
    private LayoutInflater mInflater;
    private int selectedItem;

    public DaysAdapter(Context context, ArrayList<String> days) {
        mContext = context;
        this.days = days;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return days.size();
    }

    public String getItem(int position) {
        return days.get(position);
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

        String day = getItem(index);
        if (selectedItem == index) {
            holder.tvDay.setTextColor(mContext.getResources().getColor(R.color.purple));
        } else {
            holder.tvDay.setTextColor(mContext.getResources().getColor(R.color.purple_50));
        }
        holder.tvDay.setText(day);

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