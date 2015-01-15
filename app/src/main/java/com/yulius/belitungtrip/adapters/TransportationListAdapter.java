package com.yulius.belitungtrip.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yulius.belitungtrip.R;

public class TransportationListAdapter  extends RecyclerView.Adapter<TransportationListAdapter.ViewHolder> {

    private String[] transportationList;
    private int rowLayout;
    private Context mContext;

    public TransportationListAdapter(String[] transportationList, int rowLayout, Context context) {
        this.transportationList = transportationList;
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
        viewHolder.transportationName.setText(transportationList[i]);
    }

    @Override
    public int getItemCount() {
        return transportationList == null ? 0 : transportationList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView transportationName;

        public ViewHolder(View itemView) {
            super(itemView);
            transportationName = (TextView) itemView.findViewById(R.id.text_view_transportation_name);
        }
    }
}
