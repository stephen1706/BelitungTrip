package com.yulius.belitungtourism.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.adapters.TransportationListAdapter;
import com.yulius.belitungtourism.fragments.base.BaseFragment;
import com.yulius.belitungtourism.listeners.RecyclerItemClickListener;

public class TransportationHomeFragment extends BaseFragment {
    private RecyclerView mTransportationList;
    private TransportationListAdapter mTransportationListAdapter;

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
        mTitle = "Transportation List";
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

        mLayoutView = inflater.inflate(R.layout.fragment_transportation_home, container, false);

        setUpView();
        setUpViewState();
        setUpListener();

        return mLayoutView;
    }

    private void setUpView() {
        mTransportationList = (RecyclerView) mLayoutView.findViewById(R.id.list_transportation);
    }

    private void setUpViewState() {
        mTransportationList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTransportationList.setItemAnimator(new DefaultItemAnimator());
        refreshFragment();
    }

    private void setUpListener() {
        mTransportationList.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(position == 0){//car
                            replaceContentFragment(CarListFragment.newInstance(), getResources().getString(R.string.car_list_fragment_tag));
                        } else if(position == 1){//flight
                            replaceContentFragment(FlightListFragment.newInstance(), getResources().getString(R.string.flight_list_fragment_tag));
                        }
                    }
                })
        );
    }


    @Override
    protected void refreshFragment() {
        super.refreshFragment();
        mTransportationListAdapter = new TransportationListAdapter(new String[]{"CAR","FLIGHT"}, R.layout.row_transportation_list, mContext);
        mTransportationList.setAdapter(mTransportationListAdapter);
    }


    @Override
    protected void restoreCustomActionBar(ActionBar actionBar) {
        super.restoreCustomActionBar(actionBar);

        actionBar.setDisplayHomeAsUpEnabled(true);
        getParentActivity().setDrawerIndicatorEnabled(true);
    }
}
