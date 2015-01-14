package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.fragments.base.BaseFragment;

public class TripPlannerHomeFragment extends BaseFragment {
    private ImageButton mAddButton;

    public static TripPlannerHomeFragment newInstance() {
        TripPlannerHomeFragment fragment = new TripPlannerHomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public TripPlannerHomeFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.trip_planner_home_fragment_tag);
        mTitle = "Trip Planner";
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

        mLayoutView = inflater.inflate(R.layout.fragment_trip_planner_home, container, false);

        setUpView();
//        setUpViewState();
//        setUpListener();
//        setUpRequestAPI();
//        setUpMessageListener();

        return mLayoutView;
    }

    private void setUpView() {
    }
}
