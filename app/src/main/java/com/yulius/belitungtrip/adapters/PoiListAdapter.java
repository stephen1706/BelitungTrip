package com.yulius.belitungtrip.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.response.PoiListResponseData;

public class PoiListAdapter extends RecyclerView.Adapter<PoiListAdapter.ViewHolder>{

    private PoiListResponseData.Entry[] poiList;
    private int rowLayout;
    private Context mContext;

    public PoiListAdapter(PoiListResponseData.Entry[] poiList, int rowLayout, Context context) {
        this.poiList = poiList;
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
        PoiListResponseData.Entry poiEntry = poiList[i];
        viewHolder.restaurantName.setText(poiEntry.poiName);
        viewHolder.restaurantAddress.setText(poiEntry.poiAddress);
    }

    @Override
    public int getItemCount() {
        return poiList == null ? 0 : poiList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantName;
        public TextView restaurantAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            restaurantName = (TextView) itemView.findViewById(R.id.text_view_poi_name);
            restaurantAddress = (TextView)itemView.findViewById(R.id.text_view_region);
        }
    }
}
