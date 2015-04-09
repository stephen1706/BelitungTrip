package com.yulius.belitungtourism.fragments.containers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.fragments.base.BaseContainerFragment;
import com.yulius.belitungtourism.fragments.content.PoiHomeFragment;

public class PoiContainerFragment extends BaseContainerFragment {
    public PoiContainerFragment() {
        // Required empty public constructor
    }


    //================================================================================
    // Life Cycle
    //================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment(PoiHomeFragment.newInstance(), getResources().getString(R.string.poi_home_fragment_tag));
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
