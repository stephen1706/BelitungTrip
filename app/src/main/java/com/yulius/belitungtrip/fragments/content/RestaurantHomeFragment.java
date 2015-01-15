package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.fragments.base.BaseFragment;

public class RestaurantHomeFragment extends BaseFragment{
    public static RestaurantHomeFragment newInstance() {
        RestaurantHomeFragment fragment = new RestaurantHomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public RestaurantHomeFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.restaurant_home_fragment_tag);

        mTitle = "Daftar Kuliner";
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

        mLayoutView = inflater.inflate(R.layout.fragment_restaurant_home, container, false);

//        setUpView();
//        setUpViewState();
//        setUpListener();
//        setUpRequestAPI();
//        setUpMessageListener();

        return mLayoutView;
    }
}
