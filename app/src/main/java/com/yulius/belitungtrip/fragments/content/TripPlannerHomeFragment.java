package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.adapters.TripListAdapter;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.listeners.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;

public class TripPlannerHomeFragment extends BaseFragment {
    private FloatingActionButton mAddButton;
    private RecyclerView mTripList;
    private TripListAdapter mTripListAdapter;
    private ArrayList<String> mItems;

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
        setUpViewState();
        setUpAdapter();
        setUpListener();
//        setUpMessageListener();

        return mLayoutView;
    }

    private void setUpAdapter() {
        mItems = new ArrayList<String>();
        mItems.add("aa");
        mItems.add("bb");
        mItems.add("aa");
        mItems.add("bb");
        mItems.add("aa");
        mItems.add("bb");
        mItems.add("aa");
        mItems.add("bb");
        mTripListAdapter = new TripListAdapter(mItems, R.layout.row_trip_list, mContext);
        mTripList.setAdapter(mTripListAdapter);
    }

    private void setUpView() {
        mTripList = (RecyclerView) mLayoutView.findViewById(R.id.list_trip);
        mAddButton = (FloatingActionButton) mLayoutView.findViewById(R.id.button_add_trip);
    }
    private void setUpViewState() {
        mTripList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTripList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpListener() {
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mTripList,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mItems.remove(position);
                                    mTripListAdapter.notifyItemRemoved(position);
                                }
                                mTripListAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mItems.remove(position);
                                    mTripListAdapter.notifyItemRemoved(position);
                                }
                                mTripListAdapter.notifyDataSetChanged();
                            }
                        });

        mTripList.addOnItemTouchListener(swipeTouchListener);
    }
}
