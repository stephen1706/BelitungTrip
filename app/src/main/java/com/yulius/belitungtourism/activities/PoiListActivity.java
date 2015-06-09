package com.yulius.belitungtourism.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.adapters.PoiListAdapter;
import com.yulius.belitungtourism.fragments.content.TripResultFragment;
import com.yulius.belitungtourism.listeners.RecyclerItemClickListener;
import com.yulius.belitungtourism.response.PoiListResponseData;

public class PoiListActivity extends BaseActivity {
    private RecyclerView mPoiList;
    private PoiListAdapter mPoiListAdapter;
    private PoiListResponseData mPoiListResponseData;

    public PoiListActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "Select point of interest";

        if (getIntent().getExtras() != null) {
            mPoiListResponseData = getIntent().getExtras().getParcelable(TripResultFragment.EXTRA_RESPONSE_DATA);
        }

        setContentView(R.layout.fragment_poi_home);

        setUpView();
        setUpViewState();
        setUpListener();
        refreshFragment();
    }

    private void setUpView() {
        mPoiList = (RecyclerView) findViewById(R.id.list_poi);
    }

    private void setUpViewState() {
        mPoiList.setLayoutManager(new LinearLayoutManager(this));
        mPoiList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpListener() {
        mPoiList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", mPoiListAdapter.getItemId(position));
                        setResult(Activity.RESULT_OK, returnIntent);

                        finish();
                    }
                })
        );
    }

    protected void refreshFragment() {
        mPoiListAdapter = new PoiListAdapter(mPoiListResponseData.entries, R.layout.row_poi_list, this);
        mPoiList.setAdapter(mPoiListAdapter);
    }
}
