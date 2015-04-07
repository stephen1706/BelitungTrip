package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.adapters.HotelListAdapter;
import com.yulius.belitungtrip.api.HotelListAPI;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.listeners.RecyclerItemClickListener;
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

        setUpView();
        setUpViewState();
        setUpListener();
        setUpRequestAPI();
        setUpMessageListener();

        return mLayoutView;
    }

    private void setUpView() {
        mHotelList = (RecyclerView) mLayoutView.findViewById(R.id.list_hotel);
    }

    private void setUpViewState() {
        mHotelList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHotelList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpListener() {
        mHotelList.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        replaceContentFragment(HotelDetailFragment.newInstance(Integer.toString(mHotelListResponseData.entries[position].hotelId)), getResources().getString(R.string.hotel_detail_fragment_tag));
                    }
                })
        );
    }

    private void setUpRequestAPI() {
        mHotelListAPI = new HotelListAPI(mContext);
        mHotelListAPI.setOnResponseListener(new HotelListAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(HotelListResponseData hotelListResponseData) {
                mHotelListResponseData = hotelListResponseData;
                if(mHotelListResponseData != null) {
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
        startRequestHotelList();
    }

    private void startRequestHotelList() {
        if(mHotelListResponseData == null) {//biar wkt back ga usah request ulang
            showLoadingMessage(TAG);
            mHotelListAPI.requestHotelList();
        } else {
            refreshFragment();
        }
    }

    @Override
    protected void refreshFragment() {
        super.refreshFragment();
        mHotelListAdapter = new HotelListAdapter(mHotelListResponseData.entries, R.layout.row_hotel_list, mContext);
        mHotelList.setAdapter(mHotelListAdapter);
    }

    private void setUpMessageListener() {
        setOnMessageActionListener(new OnMessageActionListener() {
            @Override
            public void onMessageActionTryAgain() {
                super.onMessageActionTryAgain();
                startRequestHotelList();
            }
        });
    }
}

