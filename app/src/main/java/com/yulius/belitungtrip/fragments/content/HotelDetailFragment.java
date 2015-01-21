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
import com.squareup.picasso.Target;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.activities.MainActivity;
import com.yulius.belitungtrip.api.HotelDetailAPI;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;
import com.yulius.belitungtrip.response.HotelDetailResponseData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private Button mPhotoSphereButton;

//    private NetworkImageView mPhotosphereImageView;
    private ImageView mPhotosphereImageView;
    private TextView mTextViewPhotosphere;
    private TextView mTextViewAddress;
    private TextView mTextViewWebsite;
    private TextView mTextViewTelephone;
    private ProgressBar mProgressBar;

    private String mHotelId;
    private int mImagePosition;
    private ImageLoader mImageLoader;

    //================================================================================
    // Current Fragment Variable
    //================================================================================

    private HotelDetailResponseData mHotelDetailResponseData;

    //================================================================================
    // API
    //================================================================================

    private HotelDetailAPI mHotelDetailAPI;
    private Target loadtarget;
    private File mPhotosphereFile;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImageLoader != null) {
            mImageLoader.cancelDisplayTask(mPhotosphereImageView);//cancel load gambar wkt diback ato home button pressed
            mImageLoader.destroy();
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
        mPhotoSphereButton = (Button) mLayoutView.findViewById(R.id.button_view_photosphere);
//        mPhotosphereImageView = (NetworkImageView) mLayoutView.findViewById(R.id.image);
        mPhotosphereImageView = (ImageView) mLayoutView.findViewById(R.id.image_photosphere);
        mTextViewPhotosphere = (TextView) mLayoutView.findViewById(R.id.text_view_photosphere);
        mTextViewAddress = (TextView) mLayoutView.findViewById(R.id.text_view_hotel_address);
        mTextViewWebsite = (TextView) mLayoutView.findViewById(R.id.text_view_hotel_website);
        mTextViewTelephone = (TextView) mLayoutView.findViewById(R.id.text_view_hotel_telephone);
        mProgressBar = (ProgressBar) mLayoutView.findViewById(R.id.progress_bar);
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

        Log.d("test","url:" + mHotelDetailResponseData.photosphere);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
        .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
        .defaultDisplayImageOptions(defaultOptions)
        .build();

        ImageLoader.getInstance().init(config);
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.displayImage(mHotelDetailResponseData.photosphere, mPhotosphereImageView, defaultOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.d("Test", "started");
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d("Test", "fail");

                mPhotoSphereButton.setVisibility(View.GONE);
                mTextViewPhotosphere.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.d("Test", "complete");
                mProgressBar.setVisibility(View.GONE);
                storeImage(loadedImage);
                mPhotosphereImageView.setImageBitmap(loadedImage);
                mPhotoSphereButton.setVisibility(View.VISIBLE);
                mTextViewPhotosphere.setVisibility(View.VISIBLE);

                mPhotoSphereButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File cachedFile = mImageLoader.getDiskCache().get(mHotelDetailResponseData.photosphere);//lgsg ambil file dari cache downloader biar ga kekompres dll
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
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                mProgressBar.setMax(total);
                mProgressBar.setProgress(current);
                Log.d("Test", "progress:" + current + "/" + total);
            }
        });

        mTextViewTelephone.setText(mHotelDetailResponseData.hotelTelephone);
        mTextViewTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHotelDetailResponseData.hotelTelephone != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + mHotelDetailResponseData.hotelTelephone));
                    startActivity(callIntent);
                }
            }
        });
        mTextViewAddress.setText(mHotelDetailResponseData.hotelAddress);
        mTextViewWebsite.setText(mHotelDetailResponseData.hotelWebsite);
        mTextViewWebsite.setMovementMethod(LinkMovementMethod.getInstance());

        double score = Double.parseDouble(mHotelDetailResponseData.hotelStars);
        mRatingLayout.removeAllViews();
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
        mMapboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/maps?q=loc:" + mHotelDetailResponseData.hotelLatitude + "," + mHotelDetailResponseData.hotelLongitude + " (" + mHotelDetailResponseData.hotelName + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
//                MapboxDialogFragment mapboxDialogFragment = MapboxDialogFragment.newInstance(hotelLocation);
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
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
                + "/Files/Hotel");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        Log.d("test","written to : " + mediaStorageDir.getPath() + File.separator + mImageName);

        return mediaFile;
    }
//    private void writeBitmapToMemory(Bitmap bitmap) {
//        Log.d("Test", "size rowbyte*height: " + (bitmap.getRowBytes() * bitmap.getHeight()));
//        Log.d("Test", "size allocation: " + bitmap.getAllocationByteCount());
//        Log.d("Test", "size byte count: " + bitmap.getByteCount());
//
//
////        ContentValues values = new ContentValues();
////        values.put(MediaStore.Images.Media.TITLE, "test.jpg");
////        values.put(MediaStore.Images.Media.DESCRIPTION, "deskripsi");
////        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
////        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
////        values.put(MediaStore.MediaColumns.DATA, Environment.getExternalStorageDirectory() + File.separator + "test.jpg");
////
////        mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        File f = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "test.jpg");
//        try {
//            f.createNewFile();
//            FileOutputStream fo = new FileOutputStream(f);
//            Log.d("Test", "size byte after write: " + bytes.size());
//            fo.write(bytes.toByteArray());
//
//            fo.close();
//            Log.d("test","written to : " + Environment.getExternalStorageDirectory().getPath() + File.separator + "test.jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("test","error : " + e.getMessage());
//        }
//
//
////        FileOutputStream out = null;
////        try {
////            out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "test.png");
////            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
////            FileInputStream fileInputStream = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/test.png"));
////            FileOutputStream fileOutputStream = mContext.openFileOutput(Environment.getExternalStorageDirectory() + "/test.png", Activity.MODE_WORLD_READABLE);
////            ByteStreams.copy(fileInputStream, fileOutputStream);
////        } catch (Exception e) {
////            e.printStackTrace();
////        } finally {
////            try {
////                if (out != null) {
////                    out.close();
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
//    }

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