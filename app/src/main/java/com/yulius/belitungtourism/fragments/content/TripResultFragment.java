package com.yulius.belitungtourism.fragments.content;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.yulius.belitungtourism.Constans;
import com.yulius.belitungtourism.FormattingUtil;
import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.activities.HotelListActivity;
import com.yulius.belitungtourism.activities.PoiListActivity;
import com.yulius.belitungtourism.activities.RestaurantListActivity;
import com.yulius.belitungtourism.activities.SouvenirListActivity;
import com.yulius.belitungtourism.algorithm.PoiAlgorithm;
import com.yulius.belitungtourism.algorithm.PoiIndividual;
import com.yulius.belitungtourism.algorithm.PoiPopulation;
import com.yulius.belitungtourism.algorithm.RestaurantAlgorithm;
import com.yulius.belitungtourism.algorithm.RestaurantIndividual;
import com.yulius.belitungtourism.algorithm.RestaurantPopulation;
import com.yulius.belitungtourism.algorithm.TSPNearestNeighbour;
import com.yulius.belitungtourism.api.CarAPI;
import com.yulius.belitungtourism.api.HotelListAPI;
import com.yulius.belitungtourism.api.PoiListAPI;
import com.yulius.belitungtourism.api.RestaurantListAPI;
import com.yulius.belitungtourism.api.RestaurantNearbyPoiAPI;
import com.yulius.belitungtourism.api.SouvenirListAPI;
import com.yulius.belitungtourism.entity.Car;
import com.yulius.belitungtourism.entity.Hotel;
import com.yulius.belitungtourism.entity.Poi;
import com.yulius.belitungtourism.entity.Restaurant;
import com.yulius.belitungtourism.entity.Souvenir;
import com.yulius.belitungtourism.fragments.base.BaseFragment;
import com.yulius.belitungtourism.listeners.OnMessageActionListener;
import com.yulius.belitungtourism.realm.Trip;
import com.yulius.belitungtourism.response.CarResponseData;
import com.yulius.belitungtourism.response.HotelListResponseData;
import com.yulius.belitungtourism.response.PoiListResponseData;
import com.yulius.belitungtourism.response.RestaurantListResponseData;
import com.yulius.belitungtourism.response.RestaurantNearbyPoiResponseData;
import com.yulius.belitungtourism.response.SouvenirListResponseData;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.exceptions.RealmException;

public class TripResultFragment extends BaseFragment {
    private static final String PARAM_TOTAL_NIGHT = "param_total_night";
    private static final String PARAM_POI_MAX_BUDGET = "param_poi_budget";
    private static final String PARAM_RESTAURANT_MAX_BUDGET = "param_restaurant_budget";
    private static final String PARAM_HOTEL_MAX_BUDGET = "param_hotel_budget";
    private static final String PARAM_POI_MIN_BUDGET = "param_poi_min_budget";
    private static final String PARAM_RESTAURANT_MIN_BUDGET = "param_restaurant_min_budget";
    private static final String PARAM_HOTEL_MIN_BUDGET = "param_hotel_min_budget";
    private static final int POPULATION_SIZE = 5;
    private static final int POI_REQUEST_CODE = 1;
    private static final int RESTAURANT_REQUEST_CODE = 2;
    private static final int HOTEL_REQUEST_CODE = 3;
    private static final int SOUVENIR_REQUEST_CODE = 4;
    public static final String EXTRA_RESPONSE_DATA = "RESPONSE DATA";

    private RestaurantListAPI mRestaurantListAPI;
    private PoiListAPI mPoiListAPI;
    private HotelListAPI mHotelListAPI;
    private SouvenirListAPI mSouvenirListAPI;
    private RestaurantListResponseData mRestaurantListResponseData;
    private HotelListResponseData mHotelListResponseData;
    private PoiListResponseData mPoiListResponseData;
    private SouvenirListResponseData mSouvenirListResponseData;
    private int mPoiMaxBudget;
    private int mRestaurantMaxBudget;
    private int mHotelMaxBudget;
    private int mTotalNight;
    private int mTotalPrice;
    private ArrayList<Restaurant>[] mRestaurantResultList;
    private ArrayList<Poi>[] mPoiResultList;
    private Hotel[] mSelectedHotel;
    private Car[] mSelectedCar;
    private Souvenir[] mSelectedSouvenir;
    private ArrayList<Integer> mSouvenirLotteryList;
//    private LinearLayout mRestaurantListFrame;
//    private LinearLayout mPoiListFrame;
//    private LinearLayout mHotelListFrame;
//    private LinearLayout mSouvenirListFrame;
//    private Button mSaveTripButton;
//    private TextView mTotalPriceTextView;
//    private EditText mTotalGuestEditText;
//    private EditText mTripNameEditText;
//    private LinearLayout tripListFrame;
//    private LinearLayout mTransportationListFrame;
//    private LinearLayout mCurrentPackage;
    private int mPoiMinBudget;
    private int mRestaurantMinBudget;
    private int mHotelMinBudget;
    private RestaurantNearbyPoiResponseData mRestaurantNearbyPoiResponseData;
    private RestaurantNearbyPoiAPI mRestaurantNearbyPoiAPI;
    private ArrayList<Integer> mHotelLotteryList;
    private CarAPI mCarAPI;
    private CarResponseData mCarResponseData;
    private int mNumGuests;
    private int mSelectedPoiIndex;
    private int mSelectedRestaurantIndex;
    private LinearLayout mPackageListFrame;
    private int mOptionNumber;
    private int mSelectedPackage;
    private double[] tspDistance;

    public static TripResultFragment newInstance(int poiMinBudget, int poiMaxBudget, int restaurantMinBudget, int restaurantMaxBudget, int hotelMinBudget, int hotelMaxBudget, int totalNight) {
        TripResultFragment fragment = new TripResultFragment();
        Bundle args = new Bundle();

        args.putInt(PARAM_POI_MIN_BUDGET, poiMinBudget);
        args.putInt(PARAM_RESTAURANT_MIN_BUDGET, restaurantMinBudget);
        args.putInt(PARAM_HOTEL_MIN_BUDGET, hotelMinBudget);

        args.putInt(PARAM_POI_MAX_BUDGET, poiMaxBudget);
        args.putInt(PARAM_RESTAURANT_MAX_BUDGET, restaurantMaxBudget);
        args.putInt(PARAM_HOTEL_MAX_BUDGET, hotelMaxBudget);
        args.putInt(PARAM_TOTAL_NIGHT, totalNight);
        fragment.setArguments(args);
        return fragment;
    }

    public TripResultFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.trip_result_fragment_tag);
        mTitle = "Trip Recommendation";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mTotalNight = getArguments().getInt(PARAM_TOTAL_NIGHT);
            mPoiMaxBudget = getArguments().getInt(PARAM_POI_MAX_BUDGET);
            mRestaurantMaxBudget = getArguments().getInt(PARAM_RESTAURANT_MAX_BUDGET);
            mHotelMaxBudget = getArguments().getInt(PARAM_HOTEL_MAX_BUDGET);

            mPoiMinBudget = getArguments().getInt(PARAM_POI_MIN_BUDGET);
            mRestaurantMinBudget = getArguments().getInt(PARAM_RESTAURANT_MIN_BUDGET);
            mHotelMinBudget = getArguments().getInt(PARAM_HOTEL_MIN_BUDGET);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayoutView = inflater.inflate(R.layout.fragment_trip_planner_result, container, false);

        setUpView();
        setUpViewState();
        setUpListener();
        setUpRequestAPI();
        setUpMessageListener();

        return mLayoutView;
    }

    private void setUpView() {
        mPackageListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_package_list);
//        mPoiListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_poi_list);
//        mRestaurantListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_restaurant_list);
    }

    private void setUpViewState() {
    }

    private void setUpListener() {

    }

    private String avoidNull(String str) {
        if(str == null){
            return "";
        } else{
            return str;
        }
    }

    private int findCarPrice(int numGuests, int packageNum) {
        int selectedCapacity = 99;
        for(int i=0;i<mCarResponseData.entries.length;i++){
            if(mCarResponseData.entries[i].carCapacity >= numGuests && mCarResponseData.entries[i].carCapacity < selectedCapacity){
                selectedCapacity = mCarResponseData.entries[i].carCapacity;

                mSelectedCar[packageNum] = new Car();
                mSelectedCar[packageNum].carId = mCarResponseData.entries[i].carId;
                mSelectedCar[packageNum].carName = mCarResponseData.entries[i].carName;
                mSelectedCar[packageNum].carPrice = mCarResponseData.entries[i].carPrice;
                mSelectedCar[packageNum].carCapacity = mCarResponseData.entries[i].carCapacity;
            }
        }

        Log.d("test", "harga mobil : " + mSelectedCar[packageNum].carPrice);
        return mSelectedCar[packageNum].carPrice;
    }

    private void setUpRequestAPI() {
        mCarAPI = new CarAPI(mContext);
        mCarAPI.setOnResponseListener(new CarAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(CarResponseData carResponseData) {
                mCarResponseData = carResponseData;
                findCarPrice(1, mOptionNumber);

                refreshFragment();
                if(mOptionNumber < 3){
                    mOptionNumber++;
                    findBestPoi();
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            hideMessageScreen();
                        }
                    });
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

        mPoiListAPI = new PoiListAPI(mContext);
        mPoiListAPI.setOnResponseListener(new PoiListAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(PoiListResponseData poiListResponseData) {
                mPoiListResponseData = poiListResponseData;
                if(poiListResponseData != null) {
//                    findBestPoi();
                    resetPackage();
                    mPackageListFrame.removeAllViews();
//                    new CalcluatePoi().execute();
                    findBestPoi();
//                    hideMessageScreen();
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

        mRestaurantListAPI = new RestaurantListAPI(mContext);
        mRestaurantListAPI.setOnResponseListener(new RestaurantListAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(RestaurantListResponseData restaurantListResponseData) {
                mRestaurantListResponseData = restaurantListResponseData;
                if(mRestaurantListResponseData != null) {
//                    findBestRestaurant();
                    new CalcluateRestaurant().execute();
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

        mHotelListAPI = new HotelListAPI(mContext);
        mHotelListAPI.setOnResponseListener(new HotelListAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(HotelListResponseData hotelListResponseData) {
                mHotelListResponseData = hotelListResponseData;
                if(hotelListResponseData != null) {
//                    findBestHotel();
                    findBestHotelUsingLottery();
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

        mSouvenirListAPI = new SouvenirListAPI(mContext);
        mSouvenirListAPI.setOnResponseListener(new SouvenirListAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(SouvenirListResponseData souvenirListResponseData) {
                mSouvenirListResponseData = souvenirListResponseData;
                if (souvenirListResponseData != null) {
                    findBestSouvenir();
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

        if(mPoiListResponseData == null || mRestaurantListResponseData == null) {
//            new StartCalculation().execute();

            mPoiListAPI.requestPoiList();
            showLoadingMessage(TripResultFragment.this.TAG);
        }
//        else {
//            refreshFragment();
//        }

        mRestaurantNearbyPoiAPI = new RestaurantNearbyPoiAPI(mContext);
        mRestaurantNearbyPoiAPI.setOnResponseListener(new RestaurantNearbyPoiAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(RestaurantNearbyPoiResponseData restaurantNearbyPoiResponseData) {
                mRestaurantNearbyPoiResponseData = restaurantNearbyPoiResponseData;
                if (restaurantNearbyPoiResponseData != null) {
//                    findBestRestaurant();
                    new CalcluateRestaurant().execute();
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

        if(mPoiListResponseData == null || mRestaurantListResponseData == null) {
//            new StartCalculation().execute();
            mPoiListAPI.requestPoiList();
            showLoadingMessage(TAG);
        } else {
            refreshFragment();
        }
    }

    private void resetPackage() {
        mOptionNumber = 0;
        mSelectedPackage = 0;
        mSelectedHotel = new Hotel[3];
        mSelectedSouvenir = new Souvenir[3];
        mSelectedCar = new Car[3];
        mPoiResultList = new ArrayList[3];
        mRestaurantResultList = new ArrayList[3];
        tspDistance = new double[3];
    }

    private void setUpMessageListener() {
        setOnMessageActionListener(new OnMessageActionListener() {
            @Override
            public void onMessageActionTryAgain() {
                super.onMessageActionTryAgain();

                hideMessageScreen();
//                if(mPoiListResponseData == null || mRestaurantListResponseData == null) {
//                    new StartCalculation().execute();
                mPoiListAPI.requestPoiList();
                showLoadingMessage(TAG);
//                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == POI_REQUEST_CODE){
                long id = data.getExtras().getLong("result");
                Poi selectedPoi = getPoiFromId(id);
                mPoiResultList[mSelectedPackage].set(mSelectedPoiIndex, selectedPoi);
                refreshFragment();
            } else if(requestCode == RESTAURANT_REQUEST_CODE){
                long id = data.getExtras().getLong("result");
                Restaurant selected = getRestaurantFromId(id);
                mRestaurantResultList[mSelectedPackage].set(mSelectedRestaurantIndex, selected);
                refreshFragment();
            } else if(requestCode == HOTEL_REQUEST_CODE){
                long id = data.getExtras().getLong("result");
                Hotel selected = getHotelFromId(id);
                mSelectedHotel[mSelectedPackage] = selected;
                refreshFragment();
            } else if(requestCode == SOUVENIR_REQUEST_CODE){
                long id = data.getExtras().getLong("result");
                Souvenir selected = getSouvenirFromId(id);
                mSelectedSouvenir[mSelectedPackage] = selected;
                refreshFragment();
            }
        }
    }

    private Poi getPoiFromId(long id) {
        for(int i = 0;i<mPoiListResponseData.entries.length;i++){
            if(mPoiListResponseData.entries[i].poiId == id){
                Poi poi = new Poi();
                poi.id = mPoiListResponseData.entries[i].poiId;
                poi.longitude = mPoiListResponseData.entries[i].poiLongitude;
                poi.latitude = mPoiListResponseData.entries[i].poiLatitude;
                poi.name = mPoiListResponseData.entries[i].poiName;
                poi.price = mPoiListResponseData.entries[i].poiPrice;
                poi.rating = mPoiListResponseData.entries[i].poiRating;
                poi.imageUrl = mPoiListResponseData.entries[i].assets[0].url;
                return poi;
            }
        }
        return null;
    }

    private Restaurant getRestaurantFromId(long id) {
        for(int i = 0;i<mRestaurantListResponseData.entries.length;i++){
            if(mRestaurantListResponseData.entries[i].restaurantId == id){
                Restaurant restaurant = new Restaurant();
                restaurant.id = mRestaurantListResponseData.entries[i].restaurantId;
                restaurant.name = mRestaurantListResponseData.entries[i].restaurantName;
                restaurant.price = mRestaurantListResponseData.entries[i].restaurantPrice;
                restaurant.rating = mRestaurantListResponseData.entries[i].restaurantRating;
                restaurant.imageUrl = mRestaurantListResponseData.entries[i].assets[0].url;
                return restaurant;
            }
        }
        return null;
    }

    private Hotel getHotelFromId(long id) {
        for(int i = 0;i<mHotelListResponseData.entries.length;i++){
            if(mHotelListResponseData.entries[i].hotelId == id){
                Hotel hotel = new Hotel();
                hotel.id = mHotelListResponseData.entries[i].hotelId;
                hotel.name = mHotelListResponseData.entries[i].hotelName;
                hotel.price = mHotelListResponseData.entries[i].hotelPrice;
                hotel.rating = mHotelListResponseData.entries[i].hotelRating;
                hotel.star = mHotelListResponseData.entries[i].hotelStar;
                hotel.imageUrl = mHotelListResponseData.entries[i].assets[0].url;
                return hotel;
            }
        }
        return null;
    }

    private Souvenir getSouvenirFromId(long id) {
        for(int i = 0;i<mSouvenirListResponseData.entries.length;i++){
            if(mSouvenirListResponseData.entries[i].souvenirId == id){
                Souvenir souvenir = new Souvenir();
                souvenir.id = mSouvenirListResponseData.entries[i].souvenirId;
                souvenir.name = mSouvenirListResponseData.entries[i].souvenirName;
                souvenir.price = mSouvenirListResponseData.entries[i].souvenirPrice;
                souvenir.rating = mSouvenirListResponseData.entries[i].souvenirRating;
                souvenir.imageUrl = mSouvenirListResponseData.entries[i].assets[0].url;
                return souvenir;
            }
        }
        return null;
    }

    @Override
    public void refreshFragment() {
//        super.refreshFragment();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPackageListFrame.removeAllViews();
            }
        });
        for (int k = 0; k <= mOptionNumber; k++) {
//            inflateNewTrip();
            final LinearLayout currentPackage = (LinearLayout) mLayoutInflater.inflate(R.layout.item_package, mPackageListFrame, false);
            LinearLayout tripListFrame = (LinearLayout) currentPackage.findViewById(R.id.frame_trip_list);
            LinearLayout mHotelListFrame = (LinearLayout) currentPackage.findViewById(R.id.frame_hotel_list);
            final LinearLayout mTransportationListFrame = (LinearLayout) currentPackage.findViewById(R.id.frame_transportation_list);
            LinearLayout mSouvenirListFrame = (LinearLayout) currentPackage.findViewById(R.id.frame_souvenir_list);
            final TextView mTotalPriceTextView = (TextView) currentPackage.findViewById(R.id.text_view_total_price);
            final EditText mTotalGuestEditText = (EditText) currentPackage.findViewById(R.id.edit_text_total_guest);
            final EditText tripNameEditText = (EditText) currentPackage.findViewById(R.id.edit_text_trip_name);
            TextView tripNumber = (TextView) currentPackage.findViewById(R.id.text_view_trip_number);
            Button saveTripButton = (Button) currentPackage.findViewById(R.id.button_save_trip);

            tripListFrame.removeAllViews();
            mHotelListFrame.removeAllViews();
            mSouvenirListFrame.removeAllViews();
            mTransportationListFrame.removeAllViews();
            tripNumber.setText("Option Number " + (k + 1));
            for (int i = 0; i < mRestaurantResultList[k].size(); i++) {
                if (i % 3 == 0) {
                    TextView textView = new TextView(getParentActivity());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 50, 0, 30);
                    lp.gravity = Gravity.CENTER;
                    textView.setLayoutParams(lp);
                    textView.setText("DAY " + ((i / 3) + 1));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    textView.setGravity(Gravity.CENTER);

                    tripListFrame.addView(textView);
                }

                final Poi poi = mPoiResultList[k].get(i);

                View poiRow = mLayoutInflater.inflate(R.layout.row_poi_list, tripListFrame, false);
                ((TextView) poiRow.findViewById(R.id.text_view_poi_name)).setText(poi.name);
                ((TextView) poiRow.findViewById(R.id.text_view_poi_rating)).setText("Rating " + poi.rating + "/100");
                ((TextView) poiRow.findViewById(R.id.text_view_poi_price)).setText("Rp " + FormattingUtil.formatDecimal(poi.price));
                Picasso.with(mContext).load(poi.imageUrl).into((ImageView) poiRow.findViewById(R.id.image_view_poi_image));
                ((TextView) poiRow.findViewById(R.id.text_view_region)).setVisibility(View.GONE);
//            if(i%3 == 0){
//                ((TextView) poiRow.findViewById(R.id.text_view_region)).setText("Day " + ((i/3)+1));
//            } else if(i%3 == 1){
//                ((TextView) poiRow.findViewById(R.id.text_view_region)).setText("Day " + ((i/3)+1));
//            } else {
//                ((TextView) poiRow.findViewById(R.id.text_view_region)).setText("Day " + ((i/3)+1));
//            }
                poiRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        replaceContentFragment(PoiDetailFragment.newInstance(Integer.toString(poi.id)), getResources().getString(R.string.poi_detail_fragment_tag));
                    }
                });

                final int finalI = i;
                final int finalK3 = k;
                poiRow.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("Change entry")
                                .setMessage("Are you sure you want to change this point of interest?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mContext, PoiListActivity.class);
                                        intent.putExtra(EXTRA_RESPONSE_DATA, mPoiListResponseData);
                                        mSelectedPoiIndex = finalI;
                                        mSelectedPackage = finalK3;
                                        getParentActivity().startActivityForResult(intent, POI_REQUEST_CODE);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                        dialog.cancel();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        return true;
                    }
                });
                if (i % 3 == 0) {
                    tripListFrame.addView(poiRow);
                } else {
                    tripListFrame.addView(poiRow, tripListFrame.getChildCount() - 1);
                }

                final Restaurant restaurant = mRestaurantResultList[k].get(i);

                View restaurantRow = mLayoutInflater.inflate(R.layout.row_restaurant_list, tripListFrame, false);
                ((TextView) restaurantRow.findViewById(R.id.text_view_restaurant_name)).setText(restaurant.name);
                ((TextView) restaurantRow.findViewById(R.id.text_view_restaurant_rating)).setText("Rating " + restaurant.rating + "/100");
                ((TextView) restaurantRow.findViewById(R.id.text_view_restaurant_price)).setText("Rp " + FormattingUtil.formatDecimal(restaurant.price));
                Picasso.with(mContext).load(restaurant.imageUrl).into((ImageView) restaurantRow.findViewById(R.id.image_view_restaurant_image));
                if (i % 3 == 0) {
                    ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Lunch");
                } else if (i % 3 == 1) {
                    ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Dinner");
                } else {
                    ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Midnight snack");
                }
                restaurantRow.setScaleX(0.8f);
                restaurantRow.setScaleY(0.8f);
                restaurantRow.setAlpha(0.8f);

                restaurantRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        replaceContentFragment(RestaurantDetailFragment.newInstance(Integer.toString(restaurant.id)), getResources().getString(R.string.restaurant_detail_fragment_tag));
                    }
                });

                restaurantRow.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("Change entry")
                                .setMessage("Are you sure you want to change this restaurant?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mContext, RestaurantListActivity.class);
                                        intent.putExtra(EXTRA_RESPONSE_DATA, mRestaurantListResponseData);
                                        mSelectedRestaurantIndex = finalI;
                                        mSelectedPackage = finalK3;
                                        getParentActivity().startActivityForResult(intent, RESTAURANT_REQUEST_CODE);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                        dialog.cancel();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        return true;
                    }
                });
                tripListFrame.addView(restaurantRow);
            }

            View hotelRow = mLayoutInflater.inflate(R.layout.row_hotel_list, mHotelListFrame, false);
            ((TextView) hotelRow.findViewById(R.id.text_view_hotel_name)).setText(mSelectedHotel[k].name);
            //((TextView) hotelRow.findViewById(R.id.text_view_region)).setText("Total price : " + mSelectedHotel.price);
            ((TextView) hotelRow.findViewById(R.id.text_view_region)).setVisibility(View.GONE);
            ((TextView) hotelRow.findViewById(R.id.text_view_hotel_rating)).setText("Rating " + mSelectedHotel[k].rating + "/100");
            ((TextView) hotelRow.findViewById(R.id.text_view_hotel_price)).setText("Rp " + FormattingUtil.formatDecimal(mSelectedHotel[k].price));

            LayerDrawable stars = (LayerDrawable) ((RatingBar) hotelRow.findViewById(R.id.star_bar_hotel_list)).getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.rgb(255, 199, 0), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            ((RatingBar) hotelRow.findViewById(R.id.star_bar_hotel_list)).setRating(mSelectedHotel[k].star);

            Picasso.with(mContext).load(mSelectedHotel[k].imageUrl).into((ImageView) hotelRow.findViewById(R.id.image_view_hotel_image));
            final int finalK2 = k;
            hotelRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceContentFragment(HotelDetailFragment.newInstance(Integer.toString(mSelectedHotel[finalK2].id)), getResources().getString(R.string.hotel_detail_fragment_tag));
                }
            });
            hotelRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Change entry")
                            .setMessage("Are you sure you want to change this hotel?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, HotelListActivity.class);
                                    intent.putExtra(EXTRA_RESPONSE_DATA, mHotelListResponseData);
                                    mSelectedPackage = finalK2;
                                    getParentActivity().startActivityForResult(intent, HOTEL_REQUEST_CODE);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    return true;
                }
            });
            mHotelListFrame.addView(hotelRow);

            View souvenirRow = mLayoutInflater.inflate(R.layout.row_souvenir_list, mSouvenirListFrame, false);
            ((TextView) souvenirRow.findViewById(R.id.text_view_souvenir_name)).setText(mSelectedSouvenir[k].name);
            ((TextView) souvenirRow.findViewById(R.id.text_view_region)).setVisibility(View.GONE);
            ((TextView) souvenirRow.findViewById(R.id.text_view_souvenir_rating)).setText("Rating " + mSelectedSouvenir[k].rating + "/100");
            Picasso.with(mContext).load(mSelectedSouvenir[k].imageUrl).into((ImageView) souvenirRow.findViewById(R.id.image_view_souvenir_image));

            souvenirRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceContentFragment(SouvenirDetailFragment.newInstance(Integer.toString(mSelectedSouvenir[finalK2].id)), getResources().getString(R.string.souvenir_detail_fragment_tag));
                }
            });
            souvenirRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Change entry")
                            .setMessage("Are you sure you want to change this souvenir shop?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, SouvenirListActivity.class);
                                    intent.putExtra(EXTRA_RESPONSE_DATA, mSouvenirListResponseData);
                                    mSelectedPackage = finalK2;
                                    getParentActivity().startActivityForResult(intent, SOUVENIR_REQUEST_CODE);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    return true;
                }
            });
            mSouvenirListFrame.addView(souvenirRow);

            View carRow = mLayoutInflater.inflate(R.layout.row_car_list, mTransportationListFrame, false);
            ((TextView) carRow.findViewById(R.id.text_view_car_name)).setText(mSelectedCar[k].carName);
            ((TextView) carRow.findViewById(R.id.text_view_car_detail)).setText("Cost : Rp " + FormattingUtil.formatDecimal(mSelectedCar[k].carPrice) + ", for " + mSelectedCar[k].carCapacity + " person");
            mTransportationListFrame.addView(carRow);

            final int finalK = k;
            mTotalGuestEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        return;
                    }
                    int numGuests;
                    try {
                        numGuests = Integer.parseInt(s.toString());
                        if (numGuests < 1 || numGuests > 45) {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Please insert a valid total passenger number", Toast.LENGTH_LONG).show();
                        mTotalPriceTextView.setText("Please insert a valid total passenger number");
                        return;
                    }

                    mNumGuests = numGuests;
                    mTotalPrice = 0;
                    for (Restaurant restaurant : mRestaurantResultList[finalK]) {
                        mTotalPrice += numGuests * restaurant.price;
                    }
                    for (Poi poi : mPoiResultList[finalK]) {
                        mTotalPrice += numGuests * poi.price;
                    }
//                mTotalPrice += numGuests * mSelectedSouvenir.price;
                    int numberOfRoom = (1 + numGuests) / 2;
                    Log.d("test", "jmlh kmr : " + numberOfRoom);
                    mTotalPrice += numberOfRoom * (mTotalNight - 1) * mSelectedHotel[finalK].price;

                    mTotalPrice += findCarPrice(numGuests, finalK) * mTotalNight;

                    mTransportationListFrame.removeAllViews();
                    View carRow = mLayoutInflater.inflate(R.layout.row_car_list, mTransportationListFrame, false);
                    ((TextView) carRow.findViewById(R.id.text_view_car_name)).setText(mSelectedCar[finalK].carName);
                    ((TextView) carRow.findViewById(R.id.text_view_car_detail)).setText("Cost : Rp " + FormattingUtil.formatDecimal(mSelectedCar[finalK].carPrice) + ", for " + mSelectedCar[finalK].carCapacity + " person");
                    mTransportationListFrame.addView(carRow);

                    mTotalPriceTextView.setText("Rp " + FormattingUtil.formatDecimal(mTotalPrice));
                }
            });
            mTotalGuestEditText.setText("1");

            final int finalK1 = k;
            saveTripButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tripName = tripNameEditText.getText().toString();
                    if (tripName.isEmpty()) {
                        Toast.makeText(mContext, "Please insert your trip name", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Realm realm = Realm.getInstance(mContext);
                    try {
                        realm.beginTransaction();
                        Trip trip = realm.createObject(Trip.class); // Create a new object

                        try {
                            mNumGuests = Integer.parseInt(mTotalGuestEditText.getText().toString());
                            if (mNumGuests < 1 || mNumGuests > 45) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            Toast.makeText(mContext, "Please insert a valid total passenger number", Toast.LENGTH_LONG).show();
                            mTotalPriceTextView.setText("Please insert a valid total passenger number");
                            return;
                        }

                        mTotalPrice = 0;
                        for (Restaurant restaurant : mRestaurantResultList[finalK]) {
                            mTotalPrice += mNumGuests * restaurant.price;
                        }
                        for (Poi poi : mPoiResultList[finalK]) {
                            mTotalPrice += mNumGuests * poi.price;
                        }

                        int numberOfRoom = (1 + mNumGuests) / 2;
                        Log.d("test", "jmlh kmr : " + numberOfRoom);
                        mTotalPrice += numberOfRoom * (mTotalNight - 1) * mSelectedHotel[finalK].price;
                        mTotalPrice += findCarPrice(mNumGuests, finalK) * mTotalNight;

                        trip.setNumGuests(mNumGuests);
                        trip.setTripName(tripName);
                        trip.setTotalNight(mTotalNight);
                        trip.setTotalPrice(mTotalPrice);

                        com.yulius.belitungtourism.realm.Hotel hotel = realm.createObject(com.yulius.belitungtourism.realm.Hotel.class);
                        hotel.setHotelId(mSelectedHotel[finalK1].id);
                        hotel.setHotelName(mSelectedHotel[finalK1].name);
                        hotel.setHotelPrice(mSelectedHotel[finalK1].price);
                        hotel.setHotelRating(mSelectedHotel[finalK1].rating);
                        hotel.setHotelStar(mSelectedHotel[finalK1].star);
                        hotel.setHotelImageUrl(avoidNull(mSelectedHotel[finalK1].imageUrl));
                        trip.setHotel(hotel);

                        com.yulius.belitungtourism.realm.Souvenir souvenir = realm.createObject(com.yulius.belitungtourism.realm.Souvenir.class);
                        souvenir.setSouvenirId(mSelectedSouvenir[finalK1].id);
                        souvenir.setSouvenirName(mSelectedSouvenir[finalK1].name);
                        souvenir.setSouvenirPrice(mSelectedSouvenir[finalK1].price);
                        souvenir.setSouvenirRating(mSelectedSouvenir[finalK1].rating);
                        souvenir.setSouvernirImageUrl(avoidNull(mSelectedSouvenir[finalK1].imageUrl));
                        trip.setSouvenir(souvenir);

                        com.yulius.belitungtourism.realm.Car car = realm.createObject(com.yulius.belitungtourism.realm.Car.class);
                        car.setCarId(mSelectedCar[finalK1].carId);
                        car.setCarName(mSelectedCar[finalK1].carName);
                        car.setCarPrice(mSelectedCar[finalK1].carPrice);
                        car.setCarCapacity(mSelectedCar[finalK1].carCapacity);
                        trip.setCar(car);

                        RealmList<com.yulius.belitungtourism.realm.Restaurant> restaurants = new RealmList<>();
                        for (Restaurant currentRestaurant : mRestaurantResultList[finalK1]) {
                            com.yulius.belitungtourism.realm.Restaurant restaurant = realm.createObject(com.yulius.belitungtourism.realm.Restaurant.class);
                            restaurant.setRestaurantId(currentRestaurant.id);
                            restaurant.setRestaurantName(currentRestaurant.name);
                            restaurant.setRestaurantPrice(currentRestaurant.price);
                            restaurant.setRestaurantRating(currentRestaurant.rating);
                            restaurant.setRestaurantType(currentRestaurant.type);
                            restaurant.setRestaurantImageUrl(avoidNull(currentRestaurant.imageUrl));
                            restaurants.add(restaurant);
                        }
                        trip.setRestaurants(restaurants);

                        RealmList<com.yulius.belitungtourism.realm.Poi> pois = new RealmList<>();
                        for (Poi currentPoi : mPoiResultList[finalK1]) {
                            com.yulius.belitungtourism.realm.Poi poi = realm.createObject(com.yulius.belitungtourism.realm.Poi.class);
                            poi.setPoiId(currentPoi.id);
                            poi.setPoiName(currentPoi.name);
                            poi.setPoiPrice(currentPoi.price);
                            poi.setPoiRating(currentPoi.rating);
                            poi.setPoiImageUrl(avoidNull(currentPoi.imageUrl));
                            Log.d("imageUrlAtRealmSaving", poi.getPoiImageUrl());
                            pois.add(poi);
                        }
                        trip.setPois(pois);
                        realm.commitTransaction();

                        showMessage("Your trip has been save", Constans.MessageType.MESSAGE_SUCCESS, Constans.Duration.LONG);

                        getFragmentManager().popBackStack();
                    } catch (RealmException e) {
                        realm.cancelTransaction();
                        showMessage(tripName + " has been used for other trip name, please use other name", Constans.MessageType.MESSAGE_ERROR, Constans.Duration.LONG);
                    }
                }
            });

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mPackageListFrame.addView(currentPackage);
                }
            });
        }
    }

    private int findTotalRating() {
        int maxValue = Integer.MIN_VALUE;
        int highestIndex = -1;

        for(int i=0;i<3;i++) {
            int totalPrice = 0;
            for (Restaurant restaurant : mRestaurantResultList[i]) {
                totalPrice += restaurant.rating;
            }
            for (Poi poi : mPoiResultList[i]) {
                totalPrice += poi.rating;
            }

            totalPrice += mSelectedHotel[i].rating;
            if(totalPrice > maxValue){
                maxValue = totalPrice;
                highestIndex = i;
            }
        }

        return highestIndex;
    }

    private int findTotalPrice() {
        int minPrice = Integer.MAX_VALUE;
        int cheapestIndex = -1;

        for(int i=0;i<3;i++) {
            int totalPrice = 0;
            for (Restaurant restaurant : mRestaurantResultList[i]) {
                totalPrice += restaurant.price;
            }
            for (Poi poi : mPoiResultList[i]) {
                totalPrice += poi.price;
            }

            totalPrice += mSelectedHotel[i].price;
            if(totalPrice < minPrice){
                minPrice = totalPrice;
                cheapestIndex = i;
            }
        }

        return cheapestIndex;
    }

    private void findBestPoi() {
        PoiPopulation poiPopulation = new PoiPopulation(POPULATION_SIZE, true, mPoiMinBudget, mPoiMaxBudget, mTotalNight, mPoiListResponseData);

        int generationCount = 0;
        int numberSameResult = 0;
        double lastFitnessResult = 0;
        while (true) {
            // Evolve our population until we reach an optimum solution
            if(numberSameResult >=5){//5x berturut" hasilnya sama break aj
                break;
            }

            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + poiPopulation.getFittest().getFitness());
            poiPopulation = PoiAlgorithm.evolvePoiPopulation(poiPopulation);
            if(lastFitnessResult == poiPopulation.getFittest().getFitness()){
                numberSameResult++;
            } else{
                numberSameResult = 0;
            }

            lastFitnessResult = poiPopulation.getFittest().getFitness();
        }
        Log.d("test algo", "Solution found!");
        Log.d("test algo", "Generation: " + generationCount);
        Log.d("test algo", "Genes:");
        Log.d("test algo", "pemenang poi: " + poiPopulation.getFittest());
        Log.d("test algo", "total price poi: " + poiPopulation.getFittest().getTotalPrice());

        PoiIndividual bestPoiIndividual = poiPopulation.getFittest();
        mPoiResultList[mOptionNumber] = new ArrayList<>();
        for(int i=0;i<bestPoiIndividual.size();i++){
            mPoiResultList[mOptionNumber].add(bestPoiIndividual.getGene(i));
        }

        applyTspAlgorithm();

//        mRestaurantListAPI.requestRestaurantList();
        if(mRestaurantNearbyPoiResponseData == null || mRestaurantListResponseData == null) {
            mRestaurantListAPI.requestRestaurantList();
            mRestaurantNearbyPoiAPI.requestRestaurantNearbyPoiList();
        } else {
//            findBestRestaurant();
            new CalcluateRestaurant().execute();

        }
    }

    private void applyTspAlgorithm() {
        //todo
        double[][] matrix = new double[mPoiResultList[mOptionNumber].size()][mPoiResultList[mOptionNumber].size()];
        for(int i=0;i<mPoiResultList[mOptionNumber].size();i++){
            for(int j=0;j<mPoiResultList[mOptionNumber].size();j++){
                if(i == j){
                    matrix[i][j] = 0;
                } else {
                    double selisihLat = Math.pow(mPoiResultList[mOptionNumber].get(i).latitude - mPoiResultList[mOptionNumber].get(j).latitude, 2);
                    double selisihLong = Math.pow(mPoiResultList[mOptionNumber].get(i).longitude - mPoiResultList[mOptionNumber].get(j).longitude, 2);
                    double selisih = Math.sqrt(selisihLat + selisihLong);
                    matrix[i][j] = selisih;
                    matrix[j][i] = selisih;
                }

            }
        }
        String out ="";
        for (int i = 0; i < mPoiResultList[mOptionNumber].size(); i++) {
            out += "\n";
            for (int j = 0; (j) < mPoiResultList[mOptionNumber].size(); j++) {
                out += matrix[i][j] + "  ";
            }
        }
        Log.d("test", out);

        for (int i = 0; i < mPoiResultList[mOptionNumber].size(); i++) {
            Log.d("test before shuffle", mPoiResultList[mOptionNumber].get(i).id + "\t");
        }
        TSPNearestNeighbour tspNearestNeighbour = new TSPNearestNeighbour();
        ArrayList<Integer> result = tspNearestNeighbour.tsp(matrix);
        tspDistance[mOptionNumber] = tspNearestNeighbour.totalLength;

        ArrayList<Poi> resultClone = (ArrayList<Poi>) mPoiResultList[mOptionNumber].clone();
        for(int i=0;i<mPoiResultList[mOptionNumber].size();i++){
            mPoiResultList[mOptionNumber].set(i, resultClone.get(result.get(i)));
        }
        for (int i = 0; i < mPoiResultList[mOptionNumber].size(); i++) {
            Log.d("test after shuffle", mPoiResultList[mOptionNumber].get(i).id + "\t");
        }
    }

    private void findBestRestaurant() {
        if(mRestaurantListResponseData != null && mRestaurantNearbyPoiResponseData != null) {
            RestaurantPopulation restaurantPopulation = new RestaurantPopulation(POPULATION_SIZE, true, mRestaurantMinBudget, mRestaurantMaxBudget, mTotalNight, mRestaurantListResponseData, mRestaurantNearbyPoiResponseData, mPoiResultList[mOptionNumber]);

            int generationCount = 0;
            int numberSameResult = 0;
            double lastFitnessResult = 0;
            while (true) {
                // Evolve our population until we reach an optimum solution
                if (numberSameResult >= 5) {//5x berturut" hasilnya sama break aj
                    break;
                }

                generationCount++;
                System.out.println("Generation: " + generationCount + " Fittest: " + restaurantPopulation.getFittest().getFitness());
                restaurantPopulation = RestaurantAlgorithm.evolveRestaurantpopulation(restaurantPopulation);
                if (lastFitnessResult == restaurantPopulation.getFittest().getFitness()) {
                    numberSameResult++;
                } else {
                    numberSameResult = 0;
                }

                lastFitnessResult = restaurantPopulation.getFittest().getFitness();
            }
            Log.d("test algo", "Solution found!");
            Log.d("test algo", "Generation: " + generationCount);
            Log.d("test algo", "Genes:");
            Log.d("test algo", "pemenang restaurant: " + restaurantPopulation.getFittest());
            Log.d("test algo", "total price restaurant: " + restaurantPopulation.getFittest().getTotalPrice());
            //show hasilnya
            RestaurantIndividual bestRestaurantIndividual = restaurantPopulation.getFittest();
            mRestaurantResultList[mOptionNumber] = new ArrayList<>();

            for (int i = 0; i < bestRestaurantIndividual.size(); i++) {
                mRestaurantResultList[mOptionNumber].add(bestRestaurantIndividual.getGene(i));
            }

            if(mHotelListResponseData == null) {
                mHotelListAPI.requestHotelList();
            } else {
                findBestHotelUsingLottery();
            }
        }
    }

//    private void findBestHotel() {
//        for(int i=0;i<mHotelListResponseData.entries.length;i++){
//            int totalPrice = mHotelListResponseData.entries[i].hotelPrice * (mTotalNight-1);
//            if(totalPrice <= mHotelMaxBudget && totalPrice >= mHotelMinBudget){
//                Hotel hotel = new Hotel();
//                hotel.id = mHotelListResponseData.entries[i].hotelId;
//                hotel.name = mHotelListResponseData.entries[i].hotelName;
//                hotel.price = mHotelListResponseData.entries[i].hotelPrice;
//                hotel.rating = mHotelListResponseData.entries[i].hotelRating;
//                hotel.star = mHotelListResponseData.entries[i].hotelStar;
//                Log.d("starAtResult", String.valueOf(mHotelListResponseData.entries[i].hotelStar));
//
//                if(mHotelListResponseData.entries[i].assets != null)
//                {hotel.imageUrl = mHotelListResponseData.entries[i].assets[0].url;}
//                else
//                {hotel.imageUrl = null;}
//                mSelectedHotel = hotel;
//
//                break;
//            }
//        }
//
//        if(mSelectedHotel == null){//fallback klo null, ambil yg lbh murah aj gpp
//            for(int i=0;i<mHotelListResponseData.entries.length;i++){
//                int totalPrice = mHotelListResponseData.entries[i].hotelPrice * (mTotalNight-1);
//                if(totalPrice <= mHotelMaxBudget){
//                    Hotel hotel = new Hotel();
//                    hotel.id = mHotelListResponseData.entries[i].hotelId;
//                    hotel.name = mHotelListResponseData.entries[i].hotelName;
//                    hotel.price = mHotelListResponseData.entries[i].hotelPrice;
//                    hotel.rating = mHotelListResponseData.entries[i].hotelRating;
//
//                    hotel.star = mHotelListResponseData.entries[i].hotelStar;
//                    Log.d("starAtResult", String.valueOf(mHotelListResponseData.entries[i].hotelStar));
//
//                    if(mHotelListResponseData.entries[i].assets != null)
//                    {hotel.imageUrl = mHotelListResponseData.entries[i].assets[0].url;}
//                    else
//                    {hotel.imageUrl = null;}
//
//                    mSelectedHotel = hotel;
//                    break;
//                }
//            }
//        }
//        if(mSouvenirListResponseData == null) {
//            mSouvenirListAPI.requestSouvenirList();
//        } else {
//            findBestSouvenir();
//        }
//    }

    private void findBestHotelUsingLottery() {
        mHotelLotteryList = new ArrayList<Integer>();
        for(int i=0;i<mHotelListResponseData.entries.length;i++){
            int totalPrice = mHotelListResponseData.entries[i].hotelPrice * (mTotalNight-1);
            if(totalPrice <= mHotelMaxBudget && totalPrice >= mHotelMinBudget){
                for(int j=0;j<mHotelListResponseData.entries[i].hotelRating;j++){
                    mHotelLotteryList.add(mHotelListResponseData.entries[i].hotelId);
                }
            }
        }
        int selectedIndex = new Random().nextInt(mHotelLotteryList.size());
        int selectedHotelId = mHotelLotteryList.get(selectedIndex);
        for(int i=0;i<mHotelListResponseData.entries.length;i++){
            if(mHotelListResponseData.entries[i].hotelId == selectedHotelId){
                Hotel hotel = new Hotel();
                hotel.id = mHotelListResponseData.entries[i].hotelId;
                hotel.name = mHotelListResponseData.entries[i].hotelName;
                hotel.price = mHotelListResponseData.entries[i].hotelPrice;
                hotel.rating = mHotelListResponseData.entries[i].hotelRating;
                hotel.star = mHotelListResponseData.entries[i].hotelStar;

                if(mHotelListResponseData.entries[i].assets != null)
                {hotel.imageUrl = mHotelListResponseData.entries[i].assets[0].url;}
                else
                {hotel.imageUrl = null;}

                mSelectedHotel[mOptionNumber] = hotel;
                break;
            }
        }

        if(mSouvenirListResponseData == null) {
            mSouvenirListAPI.requestSouvenirList();
        } else {
            findBestSouvenir();
        }
    }

    private void findBestSouvenir() {
        mSouvenirLotteryList = new ArrayList<Integer>();
        for(int i=0;i<mSouvenirListResponseData.entries.length;i++){
            for(int j=0;j<mSouvenirListResponseData.entries[i].souvenirRating;j++){
                mSouvenirLotteryList.add(mSouvenirListResponseData.entries[i].souvenirId);
            }
        }
        int selectedIndex = new Random().nextInt(mSouvenirLotteryList.size());
        int selectedSouvenirId = mSouvenirLotteryList.get(selectedIndex);
        for(int i=0;i<mSouvenirListResponseData.entries.length;i++){
            if(mSouvenirListResponseData.entries[i].souvenirId == selectedSouvenirId){
                mSelectedSouvenir[mOptionNumber] = new Souvenir();
                mSelectedSouvenir[mOptionNumber].id = mSouvenirListResponseData.entries[i].souvenirId;
                mSelectedSouvenir[mOptionNumber].name = mSouvenirListResponseData.entries[i].souvenirName;
                mSelectedSouvenir[mOptionNumber].price = mSouvenirListResponseData.entries[i].souvenirPrice;
                mSelectedSouvenir[mOptionNumber].rating = mSouvenirListResponseData.entries[i].souvenirRating;

                if(mSouvenirListResponseData.entries[i].assets != null)
                {mSelectedSouvenir[mOptionNumber].imageUrl = mSouvenirListResponseData.entries[i].assets[0].url;}
                else
                {mSelectedSouvenir[mOptionNumber].imageUrl = null;}
                break;
            }
        }

        if(mCarResponseData == null) {
            mCarAPI.requestCarList();
        } else {
            findCarPrice(1, mOptionNumber);

            refreshFragment();

            mOptionNumber++;

            if(mOptionNumber < 3){
                findBestPoi();
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        hideMessageScreen();
                            LinearLayout footer = (LinearLayout) mLayoutInflater.inflate(R.layout.item_most_package, mPackageListFrame, false);
                            TextView cheapest = (TextView) footer.findViewById(R.id.text_view_cheapest);
                            TextView highestRating = (TextView) footer.findViewById(R.id.text_view_highest_rating);
                            TextView minDistance = (TextView) footer.findViewById(R.id.text_view_min_distance);
                            cheapest.setText("Package " + (findTotalPrice() + 1));
                            highestRating.setText("Package " + (findTotalRating() + 1));
                            minDistance.setText("Package " + (findMinDistance() + 1));
                            mPackageListFrame.addView(footer);

                    }
                });
            }
        }
    }

    private int findMinDistance() {
        double minDistance = Integer.MAX_VALUE;
        int cheapestIndex = -1;

        for(int i=0;i<3;i++) {
            if(tspDistance[i] < minDistance){
                minDistance = tspDistance[i];
                cheapestIndex = i;
            }
        }

        return cheapestIndex;
    }

    @Override
    protected void restoreCustomActionBar(ActionBar actionBar) {
        super.restoreCustomActionBar(actionBar);

        actionBar.setDisplayHomeAsUpEnabled(true);
        getParentActivity().setDrawerIndicatorEnabled(false);
    }

    private class CalcluatePoi extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            findBestPoi();
            return null;
        }

    }

    private class CalcluateRestaurant extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            findBestRestaurant();
            return null;
        }

    }
}
