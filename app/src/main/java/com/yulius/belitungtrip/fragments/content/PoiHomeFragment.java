package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.fragments.base.BaseFragment;

public class PoiHomeFragment extends BaseFragment {
    public static PoiHomeFragment newInstance() {
        PoiHomeFragment fragment = new PoiHomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public PoiHomeFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.poi_home_fragment_tag);
        mTitle = "Daftar Objek Wisata";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayoutView = inflater.inflate(R.layout.fragment_poi_home, container, false);

//        setUpView();
//        setUpViewState();
//        setUpListener();
//        setUpRequestAPI();
//        setUpMessageListener();

        return mLayoutView;
    }
}
