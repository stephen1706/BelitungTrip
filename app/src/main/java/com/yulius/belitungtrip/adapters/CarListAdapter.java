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
import com.yulius.belitungtrip.response.CarResponseData;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.ViewHolder> {

    private CarResponseData.Entry[] carList;
    private int rowLayout;
    private Context mContext;

    public CarListAdapter(CarResponseData.Entry[] carList, int rowLayout, Context context) {
        this.carList = carList;
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
        CarResponseData.Entry carEntry = carList[i];
        viewHolder.carName.setText(carEntry.carName);
        viewHolder.carWebsite.setText(carEntry.carLink);
        viewHolder.carWebsite.setMovementMethod(LinkMovementMethod.getInstance());
        loadImage(carEntry, viewHolder);
    }

    private void loadImage(final CarResponseData.Entry carEntry, final ViewHolder viewHolder ) {
        Picasso.with(mContext).load(carEntry.carLogo).placeholder(R.drawable.abc_btn_check_material).into(viewHolder.carLogo, new Callback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                Log.d("Test", "fail load car image from : " + carEntry.carLogo);
                loadImage(carEntry, viewHolder);//klo fail ulang lg
            }
        });
    }

    @Override
    public int getItemCount() {
        return carList == null ? 0 : carList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView carName;
        public TextView carWebsite;
        public ImageView carLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            carName = (TextView) itemView.findViewById(R.id.text_view_car_name);
            carWebsite = (TextView)itemView.findViewById(R.id.text_view_car_website);
            carLogo = (ImageView)itemView.findViewById(R.id.image_car);
        }
    }
}
