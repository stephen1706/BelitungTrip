package com.yulius.belitungtrip.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

public class BaseActivity extends ActionBarActivity {

    public String TAG;
    protected String mTitle;

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected View mLayoutView;

    public BaseActivity() {
        super();

        TAG = "BaseActivity";
    }


    //================================================================================
    // Life Cycle
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setIcon(android.R.color.transparent);

        return true;
    }
    
}
