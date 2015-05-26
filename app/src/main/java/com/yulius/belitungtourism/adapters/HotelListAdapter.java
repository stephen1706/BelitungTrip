package com.yulius.belitungtourism.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.response.HotelListResponseData;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.ViewHolder> {

    private HotelListResponseData.Entry[] hotelList;
    private int rowLayout;
    private Context mContext;

    public HotelListAdapter(HotelListResponseData.Entry[] hotelList, int rowLayout, Context context) {
        this.hotelList = hotelList;//tampung data dr database ke variabel
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {//saatny untuk inflate layout dari xml jadi coding
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {//listview request data dari adapter
        HotelListResponseData.Entry hotelEntry = hotelList[position];
        viewHolder.hotelName.setText(hotelEntry.hotelName);//text view diisi textnya sesuai dari database
        viewHolder.hotelLocation.setText(hotelEntry.hotelLocation);
    }

    @Override
    public int getItemCount() {
        return hotelList == null ? 0 : hotelList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hotelName;
        public TextView hotelLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            hotelName = (TextView) itemView.findViewById(R.id.text_view_hotel_name);//buat nampung textview-nya di variabel
            hotelLocation = (TextView)itemView.findViewById(R.id.text_view_region);
        }
    }
}
