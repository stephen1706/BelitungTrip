package com.yulius.belitungtourism.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yulius.belitungtourism.R;

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

        if(i == 1)
        {
            viewHolder.transportationContainer.setBackgroundColor(mContext.getResources().getColor(R.color.pink));
            viewHolder.transportationPicture.setImageResource(R.drawable.transport_ic_plane);
        }
    }

    @Override
    public int getItemCount() {
        return transportationList == null ? 0 : transportationList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView transportationName;
        public LinearLayout transportationContainer;
        public ImageView transportationPicture;

        public ViewHolder(View itemView) {
            super(itemView);
            transportationName = (TextView) itemView.findViewById(R.id.text_view_transportation_name);
            transportationContainer = (LinearLayout) itemView.findViewById(R.id.transportationContainer);
            transportationPicture = (ImageView) itemView.findViewById(R.id.image_view_transportation_picture);
        }
    }

}
