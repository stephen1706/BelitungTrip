package com.yulius.belitungtourism.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.adapters.HotelListAdapter;
import com.yulius.belitungtourism.fragments.content.TripResultFragment;
import com.yulius.belitungtourism.listeners.RecyclerItemClickListener;
import com.yulius.belitungtourism.response.HotelListResponseData;

public class HotelListActivity extends BaseActivity {
    private RecyclerView mHotelList;
    private HotelListAdapter mHotelListAdapter;
    private HotelListResponseData mHotelListResponseData;

    public HotelListActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "Select hotel";

        if (getIntent().getExtras() != null) {
            mHotelListResponseData = getIntent().getExtras().getParcelable(TripResultFragment.EXTRA_RESPONSE_DATA);
        }

        setContentView(R.layout.fragment_hotel_home);

        setUpView();
        setUpViewState();
        setUpListener();
        refreshFragment();
    }

    private void setUpView() {
        mHotelList = (RecyclerView) findViewById(R.id.list_hotel);
    }

    private void setUpViewState() {
        mHotelList.setLayoutManager(new LinearLayoutManager(this));
        mHotelList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpListener() {
        mHotelList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", mHotelListAdapter.getItemId(position));
                        setResult(Activity.RESULT_OK, returnIntent);

                        finish();
                    }
                })
        );
    }

    protected void refreshFragment() {
        mHotelListAdapter = new HotelListAdapter(mHotelListResponseData.entries, R.layout.row_hotel_list, this);
        mHotelList.setAdapter(mHotelListAdapter);
    }
}
