package com.yulius.belitungtourism.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.adapters.SouvenirListAdapter;
import com.yulius.belitungtourism.api.SouvenirListAPI;
import com.yulius.belitungtourism.fragments.base.BaseFragment;
import com.yulius.belitungtourism.listeners.OnMessageActionListener;
import com.yulius.belitungtourism.listeners.RecyclerItemClickListener;
import com.yulius.belitungtourism.response.SouvenirListResponseData;

public class SouvenirHomeFragment extends BaseFragment {
    private RecyclerView mSouvenirList;
    private SouvenirListAdapter mSouvenirListAdapter;
    private SouvenirListAPI mSouvenirListAPI;
    private SouvenirListResponseData mSouvenirListResponseData;

    public static SouvenirHomeFragment newInstance() {
        SouvenirHomeFragment fragment = new SouvenirHomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public SouvenirHomeFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.souvenir_home_fragment_tag);
        mTitle = "Souvenir Shop List";
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

        mLayoutView = inflater.inflate(R.layout.fragment_souvenir_home, container, false);

        setUpView();
        setUpViewState();
        setUpListener();
        setUpRequestAPI();
        setUpMessageListener();

        return mLayoutView;
    }

    private void setUpView() {
        mSouvenirList = (RecyclerView) mLayoutView.findViewById(R.id.list_souvenir);
    }

    private void setUpViewState() {
        mSouvenirList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSouvenirList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpListener() {
        mSouvenirList.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        replaceContentFragment(SouvenirDetailFragment.newInstance(Integer.toString(mSouvenirListResponseData.entries[position].souvenirId)), getResources().getString(R.string.souvenir_detail_fragment_tag));
                    }
                })
        );
    }

    private void setUpRequestAPI() {
        mSouvenirListAPI = new SouvenirListAPI(mContext);
        mSouvenirListAPI.setOnResponseListener(new SouvenirListAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(SouvenirListResponseData souvenirListResponseData) {
                mSouvenirListResponseData = souvenirListResponseData;
                if (mSouvenirListResponseData != null) {
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
        startRequestSouvenirList();
    }

    private void startRequestSouvenirList() {
        if(mSouvenirListResponseData == null) {//biar wkt back ga usah request ulang
            showLoadingMessage(TAG);
            mSouvenirListAPI.requestSouvenirList();
        } else {
            refreshFragment();
        }
    }

    @Override
    protected void refreshFragment() {
        super.refreshFragment();
        mSouvenirListAdapter = new SouvenirListAdapter(mSouvenirListResponseData.entries, R.layout.row_souvenir_list, mContext);
        mSouvenirList.setAdapter(mSouvenirListAdapter);
    }

    private void setUpMessageListener() {
        setOnMessageActionListener(new OnMessageActionListener() {
            @Override
            public void onMessageActionTryAgain() {
                super.onMessageActionTryAgain();
                startRequestSouvenirList();
            }
        });
    }
}
