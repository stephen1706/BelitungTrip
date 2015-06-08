package com.yulius.belitungtourism.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yulius.belitungtourism.FormattingUtil;
import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.response.PoiListResponseData;

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
        viewHolder.restaurantRating.setText("Rating " + String.valueOf(poiEntry.poiRating) + "/100");

        String poiPriceWithDot = String.valueOf(poiEntry.poiPrice).substring(0, 2) + "." + String.valueOf(poiEntry.poiPrice).substring(2, 5);
        viewHolder.restaurantPrice.setText("Rp " + FormattingUtil.formatDecimal(poiEntry.poiPrice));

        if(poiEntry.assets != null)
        {Picasso.with(mContext).load(poiEntry.assets[0].url).into(viewHolder.restaurantImage);}
    }

    @Override
    public int getItemCount() {
        return poiList == null ? 0 : poiList.length;
    }

    @Override
    public long getItemId(int position) {
        return poiList[position].poiId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantName;
        public TextView restaurantAddress;
        public TextView restaurantRating;
        public TextView restaurantPrice;
        public ImageView restaurantImage;

        public ViewHolder(View itemView) {
            super(itemView);
            restaurantName = (TextView) itemView.findViewById(R.id.text_view_poi_name);
            restaurantAddress = (TextView)itemView.findViewById(R.id.text_view_region);
            restaurantRating = (TextView)itemView.findViewById(R.id.text_view_poi_rating);
            restaurantPrice = (TextView)itemView.findViewById(R.id.text_view_poi_price);
            restaurantImage = (ImageView)itemView.findViewById(R.id.image_view_poi_image);
        }
    }
}
