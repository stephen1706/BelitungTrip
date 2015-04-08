package com.yulius.belitungtrip.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.realm.Trip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

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
        viewHolder.mTripNameTextView.setText(tripList.get(i).getTripName());
        viewHolder.mAdditionalInfoTextView.setText(tripList.get(i).getTotalNight() + " hari, "
                + tripList.get(i).getNumGuests() + " orang, Rp "
                + formatDecimal(tripList.get(i).getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return tripList == null ? 0 : tripList.size();
    }

    public String getTripName(int position){
        return tripList.get(position).getTripName();
    }
    public void removeItem(int position) {
        tripList.remove(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mAdditionalInfoTextView;
        private TextView mTripNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTripNameTextView = (TextView) itemView.findViewById(R.id.text_view_trip_name);
            mAdditionalInfoTextView = (TextView) itemView.findViewById(R.id.text_view_additional_info);
        }
    }

    public static String formatDecimal(int number){
        DecimalFormatSymbols decimalSymbol = new DecimalFormatSymbols(new Locale("in"));
        DecimalFormat decimalFormat = new DecimalFormat("#,###", decimalSymbol);
        String formattedDecimal = decimalFormat.format(number);
        return formattedDecimal;
    }
}
