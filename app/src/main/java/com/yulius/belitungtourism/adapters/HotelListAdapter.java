package com.yulius.belitungtourism.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
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
        viewHolder.hotelRating.setText("Rating " + String.valueOf(hotelEntry.hotelRating) + "/100");
        viewHolder.hotelStarBar.setRating(hotelEntry.hotelStar);

        LayerDrawable stars = (LayerDrawable) viewHolder.hotelStarBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(255, 199, 0), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        String hotelPriceWithDot = String.valueOf(hotelEntry.hotelPrice).substring(0, 3) + "." + String.valueOf(hotelEntry.hotelPrice).substring(3, 6);
        viewHolder.hotelPrice.setText("Rp " + hotelPriceWithDot);

        if(hotelEntry.assets != null)
        {Picasso.with(mContext).load(hotelEntry.assets[0].url).into(viewHolder.hotelImage);}
    }

    @Override
    public int getItemCount() {
        return hotelList == null ? 0 : hotelList.length;
    }

    @Override
    public long getItemId(int position) {
        return hotelList[position].hotelId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hotelName;
        public TextView hotelLocation;
        public ImageView hotelImage;
        public RatingBar hotelStarBar;
        public TextView hotelRating;
        public TextView hotelPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            hotelName = (TextView) itemView.findViewById(R.id.text_view_hotel_name);//buat nampung textview-nya di variabel
            hotelLocation = (TextView)itemView.findViewById(R.id.text_view_region);
            hotelImage = (ImageView)itemView.findViewById(R.id.image_view_hotel_image);
            hotelStarBar = (RatingBar)itemView.findViewById(R.id.star_bar_hotel_list);
            hotelRating = (TextView)itemView.findViewById(R.id.text_view_hotel_rating);
            hotelPrice = (TextView)itemView.findViewById(R.id.text_view_hotel_price);
        }
    }
}
