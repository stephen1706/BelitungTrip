package com.yulius.belitungtrip.fragments.containers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.fragments.content.HotelHomeFragment;
import com.yulius.belitungtrip.fragments.base.BaseContainerFragment;

public class HotelContainerFragment extends BaseContainerFragment{
    public HotelContainerFragment() {
        // Required empty public constructor
    }


    //================================================================================
    // Life Cycle
    //================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment(HotelHomeFragment.newInstance(), getResources().getString(R.string.hotel_home_fragment_tag));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
