package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.activities.MainActivity;
import com.yulius.belitungtrip.api.PoiDetailAPI;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;
import com.yulius.belitungtrip.response.PoiDetailResponseData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PoiDetailFragment extends BaseFragment {
    private static final String PARAM_IMAGE_URL = "param_image_url";
    private static final String PARAM_POI_ID = "param_poi_id";
    private static final int DEFAULT_ZOOM_LEVEL = 14;

    //================================================================================
    // Current Fragment Component
    //================================================================================

    private LinearLayout mNoPhotoFrame;
    private ViewPager mImageHeaderPager;
    private MapView mMapView;
    private Button mMapboxButton;
    private Button mPhotoSphereButton;

    private ImageView mPhotosphereImageView;
    private TextView mTextViewPhotosphere;
    private TextView mTextViewAddress;
    private TextView mTextViewWebsite;
    private TextView mTextViewTelephone;
    private ProgressBar mProgressBar;

    private String mPoiId;
    private ImageLoader mImageLoader;
    //================================================================================
    // Current Fragment Variable
    //================================================================================

    private PoiDetailResponseData mPoiDetailResponseData;

    //================================================================================
    // API
    //================================================================================

    private PoiDetailAPI mPoiDetailAPI;
    private File mPhotosphereFile;
    //================================================================================
    // Constructor
    //================================================================================

    public static PoiDetailFragment newInstance(String poiId) {
        PoiDetailFragment fragment = new PoiDetailFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_POI_ID, poiId);
        fragment.setArguments(args);
        return fragment;
    }

    public PoiDetailFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.poi_detail_fragment_tag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mPoiId = getArguments().getString(PARAM_POI_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayoutView = inflater.inflate(R.layout.fragment_poi_detail, container, false);

        setUpAttribute();
        setUpView();
        setUpViewState();
        setUpListener();
        setUpRequestAPI();
        setUpMessageListener();

        return mLayoutView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImageLoader != null) {
            mImageLoader.cancelDisplayTask(mPhotosphereImageView);//cancel load gambar wkt diback ato home button pressed
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mPoiDetailResponseData != null){
            setUpImageGallery();
        }
    }

    private void setUpAttribute(){
    }

    private void setUpView(){
        mNoPhotoFrame  = (LinearLayout) mLayoutView.findViewById(R.id.frame_no_photo_available);
        mImageHeaderPager = (ViewPager) mLayoutView.findViewById(R.id.pager_poi_image_header);
        mMapboxButton = (Button) mLayoutView.findViewById(R.id.button_mapview);
        mMapView = (MapView) mLayoutView.findViewById(R.id.mapid);
        mPhotoSphereButton = (Button) mLayoutView.findViewById(R.id.button_view_photosphere);
        mPhotosphereImageView = (ImageView) mLayoutView.findViewById(R.id.image_photosphere);
        mTextViewPhotosphere = (TextView) mLayoutView.findViewById(R.id.text_view_photosphere);
        mTextViewAddress = (TextView) mLayoutView.findViewById(R.id.text_view_poi_address);
        mTextViewWebsite = (TextView) mLayoutView.findViewById(R.id.text_view_poi_website);
        mTextViewTelephone = (TextView) mLayoutView.findViewById(R.id.text_view_poi_telephone);
        mProgressBar = (ProgressBar) mLayoutView.findViewById(R.id.progress_bar);
    }

    private void setUpViewState() {
        if(mPoiDetailResponseData != null) {
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
                if (mPoiDetailResponseData == null) {
                    startPoiDetailRequest();

                }
            }
        });
    }

    private void startPoiDetailRequest() {
        showLoadingMessage(TAG);
        mPoiDetailAPI.requestPoiDetail(mPoiId);
    }

    private void setUpImageGallery() {
        if(mPoiDetailResponseData.assets != null && mPoiDetailResponseData.assets.length > 0){
            mNoPhotoFrame.setVisibility(View.GONE);
            mImageHeaderPager.setVisibility(View.VISIBLE);

            String[] mImageUrl = new String [mPoiDetailResponseData.assets.length];
            for(int i = 0 ;i < mPoiDetailResponseData.assets.length;i++){
                mImageUrl[i] = mPoiDetailResponseData.assets[i].url;
            }

            mImageHeaderPager.setAdapter(new ImageHeaderAdapter(getChildFragmentManager(), mImageUrl));
            mImageHeaderPager.setCurrentItem(0);
            mImageHeaderPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int state) {
                }

                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                public void onPageSelected(int position) {

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

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(mPoiDetailResponseData.poiName);

        Log.d("test", "url:" + mPoiDetailResponseData.photosphere);

        final ImageLoadingProgressListener imageLoadingProgressListener = new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                mProgressBar.setMax(total);
                mProgressBar.setProgress(current);
                mProgressBar.setVisibility(View.VISIBLE);
                Log.d("Test","progress:" +current+"/"+total);
            }
        };

        final DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.displayImage(mPoiDetailResponseData.photosphere, mPhotosphereImageView, defaultOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d("Test", "fail");
//                mImageLoader.displayImage(mPoiDetailResponseData.photosphere, mPhotosphereImageView, defaultOptions, this, imageLoadingProgressListener);
                mPhotoSphereButton.setVisibility(View.GONE);
                mTextViewPhotosphere.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.GONE);
                storeImage(loadedImage);
                mPhotosphereImageView.setImageBitmap(loadedImage);
                mPhotoSphereButton.setVisibility(View.VISIBLE);
                mTextViewPhotosphere.setVisibility(View.VISIBLE);

                mPhotoSphereButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File cachedFile = mImageLoader.getDiskCache().get(mPoiDetailResponseData.photosphere);//lgsg ambil file dari cache downloader biar ga kekompres dll
//                        Uri uri = Uri.fromFile(mPhotosphereFile);
                        Uri uri = Uri.fromFile(cachedFile);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                        String mime = "*/*";
                        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                        if (mimeTypeMap.hasExtension(
                                mimeTypeMap.getFileExtensionFromUrl(uri.toString())))
                            mime = mimeTypeMap.getMimeTypeFromExtension(
                                    mimeTypeMap.getFileExtensionFromUrl(uri.toString()));
                        intent.setComponent(new ComponentName("com.google.android.gms", "com.google.android.gms.panorama.PanoramaViewActivity"));
                        intent.setDataAndType(uri, mime);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.d("Test", "cancel");
            }
        }, imageLoadingProgressListener);

        mTextViewTelephone.setText(mPoiDetailResponseData.poiTelephone);
        mTextViewTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPoiDetailResponseData.poiTelephone != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + mPoiDetailResponseData.poiTelephone));
                    startActivity(callIntent);
                }
            }
        });

        mTextViewAddress.setText(mPoiDetailResponseData.poiAddress);
        mTextViewWebsite.setText(mPoiDetailResponseData.poiWebsite);
        mTextViewWebsite.setMovementMethod(LinkMovementMethod.getInstance());

        final LatLng poiLocation = new LatLng(Double.parseDouble(mPoiDetailResponseData.poiLatitude), Double.parseDouble(mPoiDetailResponseData.poiLongitude));
        mMapView.setCenter(poiLocation);

        Marker hotelMarker = new Marker(mMapView, "", "", poiLocation);
        hotelMarker.setIcon(new Icon(getResources().getDrawable(R.drawable.green_pin)));
        mMapView.addMarker(hotelMarker);

        mMapView.setZoom(DEFAULT_ZOOM_LEVEL);
        mMapboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/maps?q=loc:" + mPoiDetailResponseData.poiLatitude + "," + mPoiDetailResponseData.poiLongitude + " (" + mPoiDetailResponseData.poiName + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
//                MapboxDialogFragment mapboxDialogFragment = MapboxDialogFragment.newInstance(poiLocation);
//                mapboxDialogFragment.show(((ActionBarActivity) mContext).getSupportFragmentManager(), null);
            }
        });
    }
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            mPhotosphereFile = pictureFile;
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
    private  File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + mContext.getPackageName()
                + "/Files/Poi");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        Log.d("test","written to : " + mediaStorageDir.getPath() + File.separator + mImageName);

        return mediaFile;
    }

    private void setUpRequestAPI() {
        mPoiDetailAPI = new PoiDetailAPI(mContext);

        mPoiDetailAPI.setOnResponseListener(new PoiDetailAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(PoiDetailResponseData poiDetailResponseData) {
                mPoiDetailResponseData = poiDetailResponseData;

                refreshFragment();
                setUpImageGallery();
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
                showConnectionProblemErrorMessage(volleyError, getResources().getString(R.string.poi_detail_fragment_tag));
            }

            @Override
            public void onRequestFailed(String message) {
                showRequestFailedErrorMessage(message);
            }
        });

        startPoiDetailRequest();
    }

    @Override
    protected void restoreCustomActionBar(ActionBar actionBar) {
        super.restoreCustomActionBar(actionBar);
        if(mPoiDetailResponseData != null){
            actionBar.setTitle(mPoiDetailResponseData.poiName);
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        getParentActivity().setDrawerIndicatorEnabled(false);
    }

    class ImageHeaderAdapter extends FragmentPagerAdapter {
        private static final String PARAM_IMAGE_URL = "param_image_url";
        private String[] mImageList;

        ImageHeaderAdapter(FragmentManager fm, String[] imageList) {
            super(fm);
            mImageList = imageList;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment imageFragment = new ImageHeaderFragment();

            Bundle b = new Bundle();
            b.putString(PARAM_IMAGE_URL, mImageList[i]);
            imageFragment.setArguments(b);

            return imageFragment;
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
    public static class ImageHeaderFragment extends Fragment {
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
                loadingImageView.setVisibility(View.INVISIBLE);
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
