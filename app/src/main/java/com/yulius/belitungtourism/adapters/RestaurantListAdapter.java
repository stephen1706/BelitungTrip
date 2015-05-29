package com.yulius.belitungtourism.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.response.RestaurantListResponseData;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private RestaurantListResponseData.Entry[] restaurantList;
    private int rowLayout;
    private Context mContext;

    public RestaurantListAdapter(RestaurantListResponseData.Entry[] restaurantList, int rowLayout, Context context) {
        this.restaurantList = restaurantList;
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
        RestaurantListResponseData.Entry restaurantEntry = restaurantList[i];
        viewHolder.restaurantName.setText(restaurantEntry.restaurantName);
        viewHolder.restaurantAddress.setText(restaurantEntry.restaurantAddress);
    }

    @Override
    public int getItemCount() {
        return restaurantList == null ? 0 : restaurantList.length;
    }

    @Override
    public long getItemId(int position) {
        return restaurantList[position].restaurantId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantName;
        public TextView restaurantAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            restaurantName = (TextView) itemView.findViewById(R.id.text_view_restaurant_name);
            restaurantAddress = (TextView)itemView.findViewById(R.id.text_view_region);
        }
    }
}
