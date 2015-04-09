package com.yulius.belitungtourism.fragments.content;

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
import com.yulius.belitungtourism.FormattingUtil;
import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.activities.MainActivity;
import com.yulius.belitungtourism.api.SouvenirDetailAPI;
import com.yulius.belitungtourism.fragments.base.BaseFragment;
import com.yulius.belitungtourism.listeners.OnMessageActionListener;
import com.yulius.belitungtourism.response.SouvenirDetailResponseData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SouvenirDetailFragment extends BaseFragment {
    private static final String PARAM_IMAGE_URL = "param_image_url";
    private static final String PARAM_SOUVENIR_ID = "param_souvenir_id";
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

    private String mSouvenirId;
    private ImageLoader mImageLoader;
    //================================================================================
    // Current Fragment Variable
    //================================================================================

    private SouvenirDetailResponseData mSouvenirDetailResponseData;

    //================================================================================
    // API
    //================================================================================

    private SouvenirDetailAPI mSouvenirDetailAPI;
    private File mPhotosphereFile;
    private LinearLayout mPhotosphereFrame;
    private TextView mPriceTextView;
    private TextView mRatingTextView;
    //================================================================================
    // Constructor
    //================================================================================

    public static SouvenirDetailFragment newInstance(String souvenirId) {
        SouvenirDetailFragment fragment = new SouvenirDetailFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_SOUVENIR_ID, souvenirId);
        fragment.setArguments(args);
        return fragment;
    }

    public SouvenirDetailFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.souvenir_detail_fragment_tag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mSouvenirId = getArguments().getString(PARAM_SOUVENIR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayoutView = inflater.inflate(R.layout.fragment_souvenir_detail, container, false);

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

        if(mSouvenirDetailResponseData != null){
            setUpImageGallery();
        }
    }

    private void setUpAttribute(){
    }

    private void setUpView(){
        mNoPhotoFrame  = (LinearLayout) mLayoutView.findViewById(R.id.frame_no_photo_available);
        mImageHeaderPager = (ViewPager) mLayoutView.findViewById(R.id.pager_souvenir_image_header);
        mMapboxButton = (Button) mLayoutView.findViewById(R.id.button_mapview);
        mMapView = (MapView) mLayoutView.findViewById(R.id.mapid);
        mPhotoSphereButton = (Button) mLayoutView.findViewById(R.id.button_view_photosphere);
        mPhotosphereImageView = (ImageView) mLayoutView.findViewById(R.id.image_photosphere);
        mPhotosphereFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_photosphere);
        mTextViewPhotosphere = (TextView) mLayoutView.findViewById(R.id.text_view_photosphere);
        mTextViewAddress = (TextView) mLayoutView.findViewById(R.id.text_view_souvenir_address);
        mTextViewWebsite = (TextView) mLayoutView.findViewById(R.id.text_view_souvenir_website);
        mTextViewTelephone = (TextView) mLayoutView.findViewById(R.id.text_view_souvenir_telephone);
        mPriceTextView = (TextView) mLayoutView.findViewById(R.id.text_view_price);
        mRatingTextView = (TextView) mLayoutView.findViewById(R.id.text_view_rating);
        mProgressBar = (ProgressBar) mLayoutView.findViewById(R.id.progress_bar);
    }

    private void setUpViewState() {
        if(mSouvenirDetailResponseData != null) {
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
                if (mSouvenirDetailResponseData == null) {
                    startSouvenirDetailRequest();

                }
            }
        });
    }

    private void startSouvenirDetailRequest() {
        showLoadingMessage(TAG);
        mSouvenirDetailAPI.requestSouvenirDetail(mSouvenirId);
    }

    private void setUpImageGallery() {
        if(mSouvenirDetailResponseData.assets != null && mSouvenirDetailResponseData.assets.length > 0){
            mNoPhotoFrame.setVisibility(View.GONE);
            mImageHeaderPager.setVisibility(View.VISIBLE);

            String[] mImageUrl = new String [mSouvenirDetailResponseData.assets.length];
            for(int i = 0 ;i < mSouvenirDetailResponseData.assets.length;i++){
                mImageUrl[i] = mSouvenirDetailResponseData.assets[i].url;
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

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(mSouvenirDetailResponseData.souvenirName);
        mPriceTextView.setText("Rp " + FormattingUtil.formatDecimal(mSouvenirDetailResponseData.souvenirPrice));
        mRatingTextView.setText(mSouvenirDetailResponseData.souvenirRating + "/100");

        Log.d("test", "url:" + mSouvenirDetailResponseData.photosphere);

        final ImageLoadingProgressListener imageLoadingProgressListener = new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setMax(total);
                mProgressBar.setProgress(current);
                Log.d("Test","progress:" +current+"/"+total);
            }
        };

        final DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions).threadPriority(Thread.MAX_PRIORITY)
                .build();

        ImageLoader.getInstance().init(config);
        mImageLoader = ImageLoader.getInstance();
        if(!mSouvenirDetailResponseData.photosphere.equals("http://yulius.webatu.com/yulius/")) {
            mImageLoader.displayImage(mSouvenirDetailResponseData.photosphere, mPhotosphereImageView, defaultOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    Log.d("Test", "fail");
                    mImageLoader.displayImage(mSouvenirDetailResponseData.photosphere, mPhotosphereImageView, defaultOptions, this, imageLoadingProgressListener);
                    mPhotoSphereButton.setVisibility(View.GONE);
                    mTextViewPhotosphere.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mProgressBar.setVisibility(View.GONE);
//                storeImage(loadedImage);
                    mPhotosphereImageView.setImageBitmap(loadedImage);
                    mPhotoSphereButton.setVisibility(View.VISIBLE);
                    mTextViewPhotosphere.setVisibility(View.VISIBLE);

                    mPhotoSphereButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File cachedFile = mImageLoader.getDiskCache().get(mSouvenirDetailResponseData.photosphere);//lgsg ambil file dari cache downloader biar ga kekompres dll
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
        } else {
            mPhotosphereFrame.setVisibility(View.GONE);
        }
        mTextViewTelephone.setText(mSouvenirDetailResponseData.souvenirTelephone);
        mTextViewTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSouvenirDetailResponseData.souvenirTelephone != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + mSouvenirDetailResponseData.souvenirTelephone));
                    startActivity(callIntent);
                }
            }
        });

        mTextViewAddress.setText(mSouvenirDetailResponseData.souvenirAddress);
        mTextViewWebsite.setText(mSouvenirDetailResponseData.souvenirWebsite);
        mTextViewWebsite.setMovementMethod(LinkMovementMethod.getInstance());

        final LatLng souvenirLocation = new LatLng(Double.parseDouble(mSouvenirDetailResponseData.souvenirLatitude), Double.parseDouble(mSouvenirDetailResponseData.souvenirLongitude));
        mMapView.setCenter(souvenirLocation);

        Marker hotelMarker = new Marker(mMapView, "", "", souvenirLocation);
        hotelMarker.setIcon(new Icon(getResources().getDrawable(R.drawable.green_pin)));
        mMapView.addMarker(hotelMarker);

        mMapView.setZoom(DEFAULT_ZOOM_LEVEL);
        mMapboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/maps?q=loc:" + mSouvenirDetailResponseData.souvenirLatitude + "," + mSouvenirDetailResponseData.souvenirLongitude + " (" + mSouvenirDetailResponseData.souvenirName + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
//                MapboxDialogFragment mapboxDialogFragment = MapboxDialogFragment.newInstance(souvenirLocation);
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
                + "/Files/Souvenir");

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
        mSouvenirDetailAPI = new SouvenirDetailAPI(mContext);

        mSouvenirDetailAPI.setOnResponseListener(new SouvenirDetailAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(SouvenirDetailResponseData souvenirDetailResponseData) {
                mSouvenirDetailResponseData = souvenirDetailResponseData;

                refreshFragment();
                setUpImageGallery();
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
                showConnectionProblemErrorMessage(volleyError, getResources().getString(R.string.souvenir_detail_fragment_tag));
            }

            @Override
            public void onRequestFailed(String message) {
                showRequestFailedErrorMessage(message);
            }
        });

        startSouvenirDetailRequest();
    }

    @Override
    protected void restoreCustomActionBar(ActionBar actionBar) {
        super.restoreCustomActionBar(actionBar);
        if(mSouvenirDetailResponseData != null){
            actionBar.setTitle(mSouvenirDetailResponseData.souvenirName);
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
