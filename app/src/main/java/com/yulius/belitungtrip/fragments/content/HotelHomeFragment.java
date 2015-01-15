package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.adapters.HotelListAdapter;
import com.yulius.belitungtrip.api.HotelListAPI;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.response.HotelListResponseData;

public class HotelHomeFragment extends BaseFragment {
    private RecyclerView mHotelList;
    private HotelListAdapter mHotelListAdapter;
    private HotelListAPI mHotelListAPI;
    private HotelListResponseData mHotelListResponseData;

    public static HotelHomeFragment newInstance() {
        HotelHomeFragment fragment = new HotelHomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public HotelHomeFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.hotel_home_fragment_tag);
        mTitle = "Daftar Penginapan";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayoutView = inflater.inflate(R.layout.fragment_hotel_home, container, false);

//        setUpView();
//        setUpViewState();
//        setUpListener();
//        setUpRequestAPI();
//        setUpMessageListener();

        return mLayoutView;
    }

}

