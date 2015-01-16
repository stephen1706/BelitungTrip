package com.yulius.belitungtrip.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.database.Trip;

import java.util.List;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder>  {

    private List<Trip>  tripList;
    private int rowLayout;
    private Context mContext;

    public TripListAdapter(List<Trip> tripList, int rowLayout, Context context) {
        this.tripList = tripList;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tripName.setText(tripList.get(i).name);
    }

    @Override
    public int getItemCount() {
        return tripList == null ? 0 : tripList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tripName;

        public ViewHolder(View itemView) {
            super(itemView);
            tripName = (TextView) itemView.findViewById(R.id.text_view_trip_name);
        }
    }
}
