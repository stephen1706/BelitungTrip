package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.adapters.CarListAdapter;
import com.yulius.belitungtrip.api.CarAPI;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;
import com.yulius.belitungtrip.listeners.RecyclerItemClickListener;
import com.yulius.belitungtrip.response.CarResponseData;

public class CarListFragment extends BaseFragment{

    private RecyclerView mCarList;
    private CarListAdapter mCarListAdapter;
    private CarAPI mCarAPI;
    private CarResponseData mCarResponseData;

    public static CarListFragment newInstance() {
        CarListFragment fragment = new CarListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public CarListFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.car_list_fragment_tag);
        mTitle = "Car List";
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

        mLayoutView = inflater.inflate(R.layout.fragment_car_list, container, false);

        setUpView();
        setUpViewState();
        setUpListener();
        setUpRequestAPI();
        setUpMessageListener();

        return mLayoutView;
    }

    private void setUpView() {
        mCarList = (RecyclerView) mLayoutView.findViewById(R.id.list_car);
    }

    private void setUpViewState() {
        mCarList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCarList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpListener() {
        mCarList.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String url = mCarResponseData.entries[position].carLink;
                        if (!url.startsWith("https://") && !url.startsWith("http://")) {//hrs mulai pk http
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
        mCarAPI = new CarAPI(mContext);
        mCarAPI.setOnResponseListener(new CarAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(CarResponseData carResponseData) {
                mCarResponseData = carResponseData;
                if (mCarResponseData != null) {
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
        startRequestCarList();
    }

    private void startRequestCarList() {
        if(mCarResponseData == null) {//biar wkt back ga usah request ulang
            showLoadingMessage(TAG);
            mCarAPI.requestCarList();
        } else {
            refreshFragment();
        }
    }

    @Override
    protected void refreshFragment() {
        super.refreshFragment();
        mCarListAdapter = new CarListAdapter(mCarResponseData.entries, R.layout.row_car_list, mContext);
        mCarList.setAdapter(mCarListAdapter);
    }

    private void setUpMessageListener() {
        setOnMessageActionListener(new OnMessageActionListener() {
            @Override
            public void onMessageActionTryAgain() {
                super.onMessageActionTryAgain();
                startRequestCarList();
            }
        });
    }
    @Override
    protected void restoreCustomActionBar(ActionBar actionBar) {
        super.restoreCustomActionBar(actionBar);

        actionBar.setDisplayHomeAsUpEnabled(true);
        getParentActivity().setDrawerIndicatorEnabled(false);
    }
}
