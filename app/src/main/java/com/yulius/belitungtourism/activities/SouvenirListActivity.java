package com.yulius.belitungtourism.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.adapters.SouvenirListAdapter;
import com.yulius.belitungtourism.fragments.content.TripResultFragment;
import com.yulius.belitungtourism.listeners.RecyclerItemClickListener;
import com.yulius.belitungtourism.response.SouvenirListResponseData;

public class SouvenirListActivity extends BaseActivity {
    private RecyclerView mSouvenirList;
    private SouvenirListAdapter mSouvenirListAdapter;
    private SouvenirListResponseData mSouvenirListResponseData;

    public SouvenirListActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "Select souvenir";

        if (getIntent().getExtras() != null) {
            mSouvenirListResponseData = getIntent().getExtras().getParcelable(TripResultFragment.EXTRA_RESPONSE_DATA);
        }

        setContentView(R.layout.fragment_souvenir_home);

        setUpView();
        setUpViewState();
        setUpListener();
        refreshFragment();
    }

    private void setUpView() {
        mSouvenirList = (RecyclerView) findViewById(R.id.list_souvenir);
    }

    private void setUpViewState() {
        mSouvenirList.setLayoutManager(new LinearLayoutManager(this));
        mSouvenirList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpListener() {
        mSouvenirList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", mSouvenirListAdapter.getItemId(position));
                        setResult(Activity.RESULT_OK, returnIntent);

                        finish();
                    }
                })
        );
    }

    protected void refreshFragment() {
        mSouvenirListAdapter = new SouvenirListAdapter(mSouvenirListResponseData.entries, R.layout.row_souvenir_list, this);
        mSouvenirList.setAdapter(mSouvenirListAdapter);
    }
}
