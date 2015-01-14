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
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.adapters.RestaurantListAdapter;
import com.yulius.belitungtrip.api.RestaurantListAPI;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;
import com.yulius.belitungtrip.listeners.RecyclerItemClickListener;
import com.yulius.belitungtrip.response.RestaurantListResponseData;

public class RestaurantHomeFragment extends BaseFragment{
    private RecyclerView mRestaurantList;
    private RestaurantListAdapter mRestaurantListAdapter;
    private RestaurantListAPI mRestaurantListAPI;
    private RestaurantListResponseData mRestaurantListResponseData;

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
        mTitle = "Restaurant List";
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

        mLayoutView = inflater.inflate(R.layout.fragment_restaurant_home, container, false);

        setUpView();
        setUpViewState();
        setUpListener();
        setUpRequestAPI();
        setUpMessageListener();

        return mLayoutView;
    }

    private void setUpView() {
        mRestaurantList = (RecyclerView) mLayoutView.findViewById(R.id.list_restaurant);
    }

    private void setUpViewState() {
        mRestaurantList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRestaurantList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpListener() {
        mRestaurantList.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        replaceContentFragment(RestaurantDetailFragment.newInstance(mRestaurantListResponseData.entries[position].restaurantId), getResources().getString(R.string.restaurant_detail_fragment_tag));
                    }
                })
        );
    }

    private void setUpRequestAPI() {
        mRestaurantListAPI = new RestaurantListAPI(mContext);
        mRestaurantListAPI.setOnResponseListener(new RestaurantListAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(RestaurantListResponseData restaurantListResponseData) {
                mRestaurantListResponseData = restaurantListResponseData;
                if (mRestaurantListResponseData != null) {
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
        startRequestRestaurantList();
    }

    private void startRequestRestaurantList() {
        if(mRestaurantListResponseData == null) {//biar wkt back ga usah request ulang
            showLoadingMessage(TAG);
            mRestaurantListAPI.requestRestaurantList();
        } else {
            refreshFragment();
        }
    }

    @Override
    protected void refreshFragment() {
        super.refreshFragment();
        mRestaurantListAdapter = new RestaurantListAdapter(mRestaurantListResponseData.entries, R.layout.row_restaurant_list, mContext);
        mRestaurantList.setAdapter(mRestaurantListAdapter);
    }

    private void setUpMessageListener() {
        setOnMessageActionListener(new OnMessageActionListener() {
            @Override
            public void onMessageActionTryAgain() {
                super.onMessageActionTryAgain();
                startRequestRestaurantList();
            }
        });
    }
}
