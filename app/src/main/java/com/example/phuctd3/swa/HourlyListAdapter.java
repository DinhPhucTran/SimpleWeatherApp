package com.example.phuctd3.swa;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PhucTD3 on 8/23/2017.
 */

public class HourlyListAdapter extends RecyclerView.Adapter<HourlyListAdapter.ViewHolder> {

    private List<HourDetail> mItems;

    public HourlyListAdapter() {

    }

    public HourlyListAdapter(List<HourDetail> list) {
        mItems = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourDetail detail = mItems.get(position);
        holder.temp.setText(detail.getTemp());
        holder.des.setText(detail.getDes());
        if(detail.getTime().contains("00h")) {
            holder.time.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.time.setTypeface(Typeface.DEFAULT);
        }
        holder.time.setText(detail.getTime());
        try{
            holder.icon.setImageResource(Utils.getWeatherIcon(detail.getIcon()));
        } catch (Exception e) {
            Log.e("----> ", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        if(mItems != null) {
            return mItems.size();
        }
        return 0;
    }

    public void setItems(List<HourDetail> list) {
        mItems = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView temp;
        public ImageView icon;
        public TextView des;
        public TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            temp = (TextView) itemView.findViewById(R.id.temp);
            des = (TextView) itemView.findViewById(R.id.des);
            time = (TextView) itemView.findViewById(R.id.time);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
