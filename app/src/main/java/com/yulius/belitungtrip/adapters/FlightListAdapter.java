package com.yulius.belitungtrip.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.response.FlightResponseData;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.ViewHolder>{

    private FlightResponseData.Entry[] flightList;
    private int rowLayout;
    private Context mContext;

    public FlightListAdapter(FlightResponseData.Entry[] flightList, int rowLayout, Context context) {
        this.flightList = flightList;
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
        FlightResponseData.Entry flightEntry = flightList[i];
        viewHolder.flightName.setText(flightEntry.flightName);
        viewHolder.flightWebsite.setText(flightEntry.flightLink);
        viewHolder.flightWebsite.setMovementMethod(LinkMovementMethod.getInstance());
        loadImage(flightEntry, viewHolder);
    }

    private void loadImage(final FlightResponseData.Entry flightEntry, final ViewHolder viewHolder ) {
        Picasso.with(mContext).load(flightEntry.flightLogo).placeholder(R.drawable.abc_btn_check_material).into(viewHolder.flightLogo, new Callback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                Log.d("Test","fail load flight image from : " + flightEntry.flightLogo);
                loadImage(flightEntry, viewHolder);//klo fail ulang lg
            }
        });
    }

    @Override
    public int getItemCount() {
        return flightList == null ? 0 : flightList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView flightName;
        public TextView flightWebsite;
        public ImageView flightLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            flightName = (TextView) itemView.findViewById(R.id.text_view_flight_name);
            flightWebsite = (TextView)itemView.findViewById(R.id.text_view_flight_website);
            flightLogo = (ImageView)itemView.findViewById(R.id.image_flight);
        }
    }
}
