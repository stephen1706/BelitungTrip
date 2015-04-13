package com.yulius.belitungtourism.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.response.SouvenirListResponseData;

public class SouvenirListAdapter extends RecyclerView.Adapter<SouvenirListAdapter.ViewHolder>{

    private SouvenirListResponseData.Entry[] souvenirList;
    private int rowLayout;
    private Context mContext;

    public SouvenirListAdapter(SouvenirListResponseData.Entry[] souvenirList, int rowLayout, Context context) {
        this.souvenirList = souvenirList;
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
        SouvenirListResponseData.Entry souvenirEntry = souvenirList[i];
        viewHolder.souvenirName.setText(souvenirEntry.souvenirName);
        viewHolder.souvenirAddress.setText(souvenirEntry.souvenirAddress);
    }

    @Override
    public int getItemCount() {
        return souvenirList == null ? 0 : souvenirList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView souvenirName;
        public TextView souvenirAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            souvenirName = (TextView) itemView.findViewById(R.id.text_view_souvenir_name);
            souvenirAddress = (TextView)itemView.findViewById(R.id.text_view_region);
        }
    }
}
