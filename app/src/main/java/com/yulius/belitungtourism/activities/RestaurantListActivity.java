package com.yulius.belitungtourism.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.adapters.RestaurantListAdapter;
import com.yulius.belitungtourism.fragments.content.TripResultFragment;
import com.yulius.belitungtourism.listeners.RecyclerItemClickListener;
import com.yulius.belitungtourism.response.RestaurantListResponseData;

/**
 * Created by stephen on 5/29/15.
 */
public class RestaurantListActivity extends BaseActivity {
    private RecyclerView mRestaurantList;
    private RestaurantListAdapter mRestaurantListAdapter;
    private RestaurantListResponseData mRestaurantListResponseData;

    public RestaurantListActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "Select restaurant";

        if (getIntent().getExtras() != null) {
            mRestaurantListResponseData = getIntent().getExtras().getParcelable(TripResultFragment.EXTRA_RESPONSE_DATA);
        }

        setContentView(R.layout.fragment_restaurant_home);

        setUpView();
        setUpViewState();
        setUpListener();
        refreshFragment();
    }

    private void setUpView() {
        mRestaurantList = (RecyclerView) findViewById(R.id.list_restaurant);
    }

    private void setUpViewState() {
        mRestaurantList.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpListener() {
        mRestaurantList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", mRestaurantListAdapter.getItemId(position));
                        setResult(Activity.RESULT_OK, returnIntent);

                        finish();
                    }
                })
        );
    }

    protected void refreshFragment() {
        mRestaurantListAdapter = new RestaurantListAdapter(mRestaurantListResponseData.entries, R.layout.row_restaurant_list, this);
        mRestaurantList.setAdapter(mRestaurantListAdapter);
    }
}
