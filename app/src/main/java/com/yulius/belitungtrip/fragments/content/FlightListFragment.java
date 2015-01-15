package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.adapters.FlightListAdapter;
import com.yulius.belitungtrip.api.FlightAPI;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;
import com.yulius.belitungtrip.listeners.RecyclerItemClickListener;
import com.yulius.belitungtrip.response.FlightResponseData;

public class FlightListFragment extends BaseFragment {
    private RecyclerView mPoiList;
    private FlightListAdapter mFlightListAdapter;
    private FlightAPI mFlightAPI;
    private FlightResponseData mFlightResponseData;

    public static FlightListFragment newInstance() {
        FlightListFragment fragment = new FlightListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public FlightListFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.flight_list_fragment_tag);
        mTitle = "Daftar Pesawat";
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

        mLayoutView = inflater.inflate(R.layout.fragment_flight_list, container, false);

        setUpView();
        setUpViewState();
        setUpListener();
        setUpRequestAPI();
        setUpMessageListener();

        return mLayoutView;
    }

    private void setUpView() {
        mPoiList = (RecyclerView) mLayoutView.findViewById(R.id.list_flight);
    }

    private void setUpViewState() {
        mPoiList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPoiList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpListener() {
        mPoiList.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String url = mFlightResponseData.entries[position].flightLink;
                        if (!url.startsWith("https://") && !url.startsWith("http://")){//hrs mulai pk http
                            url = "http://" + url;
                        }

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                })
        );
    }

    private void setUpRequestAPI() {
        mFlightAPI = new FlightAPI(mContext);
        mFlightAPI.setOnResponseListener(new FlightAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(FlightResponseData flightResponseData) {
                mFlightResponseData = flightResponseData;
                if (mFlightResponseData != null) {
                    refreshFragment();
                }
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
                showConnectionProblemErrorMessage(volleyError, TAG);
            }

            @Override
            public void onRequestFailed(String message) {
                showRequestFailedErrorMessage(message);
            }
        });
        startRequestFlightList();
    }

    private void startRequestFlightList() {
        if(mFlightResponseData == null) {//biar wkt back ga usah request ulang
            showLoadingMessage(TAG);
            mFlightAPI.requestFlightList();
        } else {
            refreshFragment();
        }
    }

    @Override
    protected void refreshFragment() {
        super.refreshFragment();
        mFlightListAdapter = new FlightListAdapter(mFlightResponseData.entries, R.layout.row_flight_list, mContext);
        mPoiList.setAdapter(mFlightListAdapter);
    }

    private void setUpMessageListener() {
        setOnMessageActionListener(new OnMessageActionListener() {
            @Override
            public void onMessageActionTryAgain() {
                super.onMessageActionTryAgain();
                startRequestFlightList();
            }
        });
    }
}
