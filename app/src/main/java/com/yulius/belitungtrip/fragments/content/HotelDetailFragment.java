package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yulius.belitungtrip.activities.MainActivity;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.api.HotelDetailAPI;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.request.HotelDetailRequestData;
import com.yulius.belitungtrip.response.HotelDetailResponseData;

public class HotelDetailFragment extends BaseFragment {
    private static final String PARAM_IMAGE_URL = "param_image_url";
    private static final String PARAM_IMAGE_POSITION = "param_image_position";
    private static final String PARAM_HOTEL_ID = "param_hotel_id";
    private static final int DEFAULT_ZOOM_LEVEL = 14;

    //================================================================================
    // Current Fragment Component
    //================================================================================

    private LinearLayout mPageContainer;
    private LinearLayout mNoPhotoFrame;
    private LinearLayout mRatingLayout;
    private ViewPager mImageHeaderPager;
    private MapView mMapView;
    private Button mMapboxButton;


    private String mHotelId;
    private int mImagePosition;

    //================================================================================
    // Current Fragment Variable
    //================================================================================

    private HotelDetailResponseData mHotelDetailResponseData;

    //================================================================================
    // API
    //================================================================================

    private HotelDetailAPI mHotelDetailAPI;
    //================================================================================
    // Constructor
    //================================================================================

    public static HotelDetailFragment newInstance(String hotelId) {
        HotelDetailFragment fragment = new HotelDetailFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_HOTEL_ID, hotelId);
        fragment.setArguments(args);
        return fragment;
    }

    public HotelDetailFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.hotel_detail_fragment_tag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mHotelId = getArguments().getString(PARAM_HOTEL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayoutView = inflater.inflate(R.layout.fragment_hotel_detail, container, false);

        setUpAttribute();
        setUpView();
        setUpViewState();
        setUpListener();
        setUpRequestAPI();
        setUpMessageListener();

        return mLayoutView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mHotelDetailResponseData != null){
            setUpImageGallery();
        }
    }

    private void setUpAttribute(){
    }

    private void setUpView(){
        mNoPhotoFrame  = (LinearLayout) mLayoutView.findViewById(R.id.frame_no_photo_available);
        mImageHeaderPager = (ViewPager) mLayoutView.findViewById(R.id.pager_hotel_image_header);
        mMapboxButton = (Button) mLayoutView.findViewById(R.id.button_mapview);
        mRatingLayout = (LinearLayout) mLayoutView.findViewById(R.id.rating_layout);
        mMapView = (MapView) mLayoutView.findViewById(R.id.mapid);
    }

    private void setUpViewState() {
        if(mHotelDetailResponseData != null) {
            refreshFragment();
        }
    }

    private void setUpListener() {

    }

    private void setUpMessageListener() {
        setOnMessageActionListener(new OnMessageActionListener() {
            @Override
            public void onMessageActionTryAgain() {
                super.onMessageActionTryAgain();

                hideMessageScreen();
                if (mHotelDetailResponseData == null) {
                    startHotelDetailRequest();

                }
            }
        });
    }

    private void startHotelDetailRequest() {
        showLoadingMessage(TAG);
        HotelDetailRequestData hotelDetailRequestData = new HotelDetailRequestData();
        hotelDetailRequestData.id = mHotelId;
//        mHotelDetailAPI.requestHotelDetail(hotelDetailRequestData);
        mHotelDetailAPI.requestHotelDetail(mHotelId);
    }

    private void setUpImageGallery() {
        if(mHotelDetailResponseData.assets != null && mHotelDetailResponseData.assets.length > 0){
            mNoPhotoFrame.setVisibility(View.GONE);
            mImageHeaderPager.setVisibility(View.VISIBLE);

            String[] mImageUrl = new String [mHotelDetailResponseData.assets.length];
            for(int i = 0 ;i<mHotelDetailResponseData.assets.length;i++){
                mImageUrl[i] = mHotelDetailResponseData.assets[i].url;
            }

            mImageHeaderPager.setAdapter(new ImageHeaderAdapter(getChildFragmentManager(), mImageUrl));
            mImageHeaderPager.setCurrentItem(0);
            mImagePosition = 0;
            mImageHeaderPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int state) {
                }

                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                public void onPageSelected(int position) {
                    mImagePosition = position;
                }
            });

//            final GestureDetector tapGestureDetector = new GestureDetector(getActivity(), new TapGestureListener());

//            mImageHeaderPager.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    tapGestureDetector.onTouchEvent(event);
//                    return false;
//                }
//            });
        } else {
            mNoPhotoFrame.setVisibility(View.VISIBLE);
            mImageHeaderPager.setVisibility(View.GONE);
        }
    }

    @Override
    protected void refreshFragment() {
        super.refreshFragment();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(mHotelDetailResponseData.hotelName);

        double score = Double.parseDouble(mHotelDetailResponseData.hotelStars);
        while (score >= 0.5) {
            ImageView iv = new ImageView(getActivity());
            if (score >= 1) {
                iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_star));
                mRatingLayout.addView(iv);
                score--;
            } else {
                iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_star_half));
                mRatingLayout.addView(iv);
                score -= 0.5;
            }
        }

        final LatLng hotelLocation = new LatLng(Double.parseDouble(mHotelDetailResponseData.hotelLatitude), Double.parseDouble(mHotelDetailResponseData.hotelLongitude));
        mMapView.setCenter(hotelLocation);

        Marker hotelMarker = new Marker(mMapView, "", "", hotelLocation);
        hotelMarker.setIcon(new Icon(getResources().getDrawable(R.drawable.green_pin)));
        mMapView.addMarker(hotelMarker);

        mMapView.setZoom(DEFAULT_ZOOM_LEVEL);
//                mMapboxButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        MapboxDialogFragment mapboxDialogFragment = MapboxDialogFragment.newInstance(hotelLocation, null, null);
//                        mapboxDialogFragment.show(((ActionBarActivity) mContext).getSupportFragmentManager(), null);
//                    }
//                });
    }

    private void setUpRequestAPI() {
        mHotelDetailAPI = new HotelDetailAPI(mContext);

        mHotelDetailAPI.setOnResponseListener(new HotelDetailAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(HotelDetailResponseData hotelDetailResponseData) {
                mHotelDetailResponseData = hotelDetailResponseData;

                refreshFragment();
                setUpImageGallery();
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
                showConnectionProblemErrorMessage(volleyError, getResources().getString(R.string.hotel_detail_fragment_tag));
            }

            @Override
            public void onRequestFailed(String message) {
                showRequestFailedErrorMessage(message);
            }
        });

        startHotelDetailRequest();
    }

    @Override
    protected void restoreCustomActionBar(ActionBar actionBar) {
        super.restoreCustomActionBar(actionBar);
        if(mHotelDetailResponseData != null){
            actionBar.setTitle(mHotelDetailResponseData.hotelName);
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        getParentActivity().setDrawerIndicatorEnabled(false);
    }

//    class TapGestureListener extends GestureDetector.SimpleOnGestureListener{//onclick listener for image pager
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            Intent i = new Intent(getParentActivity(), GalleryImageHotelActivity.class);
//            i.putExtra(PARAM_IMAGE_URL, mHotelDetailResponseData.assets);
//            i.putExtra(PARAM_IMAGE_POSITION, mImagePosition);
//            startActivity(i);
//            return false;
//        }
//    }

    class ImageHeaderAdapter extends FragmentPagerAdapter {
        private static final String PARAM_IMAGE_URL = "param_image_url";
        private String[] mImageList;

        ImageHeaderAdapter(FragmentManager fm, String[] imageList) {
            super(fm);
            mImageList = imageList;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment hotelImageFragment = new HotelImageHeaderFragment();

            Bundle b = new Bundle();
            b.putString(PARAM_IMAGE_URL, mImageList[i]);
            hotelImageFragment.setArguments(b);

            return hotelImageFragment;
        }

        @Override
        public int getCount() {
            if(mImageList != null) {
                return mImageList.length;
            } else {
                return 0;
            }
        }

    }
    public static class HotelImageHeaderFragment extends Fragment {
        private String mUrl;
        protected View mLayoutView;
        private ImageView mLoadingImageView;
        private ImageView mImageView;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            mLayoutView = inflater.inflate(R.layout.fragment_image_header_hotel, container, false);
            mUrl = getArguments().getString(PARAM_IMAGE_URL);
            setUpView();
            return mLayoutView;
        }
        private void setUpView() {
            mImageView = (ImageView) mLayoutView.findViewById(R.id.image_view_gallery);

            mLoadingImageView = (ImageView) mLayoutView.findViewById(R.id.image_view_loading_animation);
            AnimationDrawable loadingAnimation = (AnimationDrawable)mLoadingImageView.getBackground();
            loadingAnimation.start();

            if(mImageView.getDrawable() == null){
                loadImage();
            } else{
                ImageView loadingImageView = (ImageView) mLayoutView.findViewById(R.id.image_view_loading_animation);
                loadingImageView.setVisibility(View.INVISIBLE);//harus di findVieById lagi ga tau knp bru bisa di buat invisible
            }
        }

        private void loadImage() {
            mLoadingImageView.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(mUrl).into(mImageView, new Callback() {
                @Override
                public void onSuccess() {
                    ImageView loadingImageView = (ImageView) mLayoutView.findViewById(R.id.image_view_loading_animation);
                    loadingImageView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                    loadImage();
                }
            });
        }
    }
}