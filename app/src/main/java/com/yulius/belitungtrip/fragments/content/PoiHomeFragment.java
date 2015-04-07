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
import com.yulius.belitungtrip.adapters.PoiListAdapter;
import com.yulius.belitungtrip.api.PoiListAPI;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;
import com.yulius.belitungtrip.listeners.RecyclerItemClickListener;
import com.yulius.belitungtrip.response.PoiListResponseData;

public class PoiHomeFragment extends BaseFragment {
    private RecyclerView mPoiList;
    private PoiListAdapter mPoiListAdapter;
    private PoiListAPI mPoiListAPI;
    private PoiListResponseData mPoiListResponseData;

    public static PoiHomeFragment newInstance() {
        PoiHomeFragment fragment = new PoiHomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public PoiHomeFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.poi_home_fragment_tag);
        mTitle = "Daftar Objek Wisata";
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

        mLayoutView = inflater.inflate(R.layout.fragment_poi_home, container, false);

        setUpView();
        setUpViewState();
        setUpListener();
        setUpRequestAPI();
        setUpMessageListener();

        return mLayoutView;
    }

    private void setUpView() {
        mPoiList = (RecyclerView) mLayoutView.findViewById(R.id.list_poi);
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
                        replaceContentFragment(PoiDetailFragment.newInstance(Integer.toString(mPoiListResponseData.entries[position].poiId)), getResources().getString(R.string.poi_detail_fragment_tag));
                    }
                })
        );
    }

    private void setUpRequestAPI() {
        mPoiListAPI = new PoiListAPI(mContext);
        mPoiListAPI.setOnResponseListener(new PoiListAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(PoiListResponseData poiListResponseData) {
                mPoiListResponseData = poiListResponseData;
                if (mPoiListResponseData != null) {
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
        startRequestPoiList();
    }

    private void startRequestPoiList() {
        if(mPoiListResponseData == null) {//biar wkt back ga usah request ulang
            showLoadingMessage(TAG);
            mPoiListAPI.requestPoiList();
        } else {
            refreshFragment();
        }
    }

    @Override
    protected void refreshFragment() {
        super.refreshFragment();
        mPoiListAdapter = new PoiListAdapter(mPoiListResponseData.entries, R.layout.row_poi_list, mContext);
        mPoiList.setAdapter(mPoiListAdapter);
    }

    private void setUpMessageListener() {
        setOnMessageActionListener(new OnMessageActionListener() {
            @Override
            public void onMessageActionTryAgain() {
                super.onMessageActionTryAgain();
                startRequestPoiList();
            }
        });
    }
}
