package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.adapters.TripListAdapter;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.listeners.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.Calendar;

public class TripPlannerHomeFragment extends BaseFragment  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    private FloatingActionButton mAddButton;
    private RecyclerView mTripList;
    private TripListAdapter mTripListAdapter;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;

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

        setUpCalendar();
        return mLayoutView;
    }

    private void setUpCalendar() {
        final Calendar calendar = Calendar.getInstance();

        mDatePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        mTimePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

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

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.setVibrate(false);
                mDatePickerDialog.setYearRange(1985, 2028);
                mDatePickerDialog.setCloseOnSingleTapDay(true);
                mDatePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
            }
        });

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
                                    //todo remove dr db
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

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Toast.makeText(mContext, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();

        mTimePickerDialog.setCloseOnSingleTapMinute(false);
        mTimePickerDialog.show(getFragmentManager(), TIMEPICKER_TAG);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(mContext, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
    }
}
