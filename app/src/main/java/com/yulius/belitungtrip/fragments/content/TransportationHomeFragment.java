package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.fragments.base.BaseFragment;

public class TransportationHomeFragment extends BaseFragment{
    public static TransportationHomeFragment newInstance() {
        TransportationHomeFragment fragment = new TransportationHomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public TransportationHomeFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.transportation_home_fragment_tag);
        mTitle = "Daftar Transportasi";
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

        mLayoutView = inflater.inflate(R.layout.fragment_transportation_home, container, false);

//        setUpView();
//        setUpViewState();
//        setUpListener();
//        setUpRequestAPI();
//        setUpMessageListener();

        return mLayoutView;
    }
}
