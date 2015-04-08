package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.entity.Hotel;
import com.yulius.belitungtrip.entity.Poi;
import com.yulius.belitungtrip.entity.Restaurant;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.realm.Trip;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TripDetailFragment extends BaseFragment {
    private static final String PARAM_TRIP_NAME = "param_trip_name";
    private ArrayList<Restaurant> mRestaurantResultList;
    private ArrayList<Poi> mPoiResultList;
    private Hotel mSelectedHotel;
    private LinearLayout mRestaurantListFrame;
    private LinearLayout mPoiListFrame;
    private LinearLayout mHotelListFrame;
    private Button mSaveTripButton;
    private String mTripName;

    public static TripDetailFragment newInstance(String tripName) {
        TripDetailFragment fragment = new TripDetailFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TRIP_NAME, tripName);
        fragment.setArguments(args);
        return fragment;
    }

    public TripDetailFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TAG = getResources().getString(R.string.trip_detail_fragment_tag);
        mTitle = "Trip Detail";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mTripName = getArguments().getString(PARAM_TRIP_NAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayoutView = inflater.inflate(R.layout.fragment_trip_planner_result, container, false);

        setUpAttribute();
        setUpView();
        setUpViewState();

        return mLayoutView;
    }

    private void setUpAttribute() {

    }

    private void setUpView() {
        mPoiListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_poi_list);
        mRestaurantListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_restaurant_list);
        mHotelListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_hotel_list);
        mSaveTripButton = (Button) mLayoutView.findViewById(R.id.button_save_trip);
    }

    private void setUpViewState() {
        mSaveTripButton.setVisibility(View.GONE);
        Realm realm = Realm.getInstance(mContext);

        RealmResults<Trip> tripResult = realm.where(Trip.class)
                .equalTo("tripName", mTripName)
                .findAll();

        com.yulius.belitungtrip.realm.Hotel hotel = tripResult.first().getHotel();
        convertHotelRealmToEntity(hotel);

        RealmList<com.yulius.belitungtrip.realm.Poi> pois = tripResult.first().getPois();
        convertPoiRealmToEntity(pois);

        RealmList<com.yulius.belitungtrip.realm.Restaurant> restaurants = tripResult.first().getRestaurants();
        convertRestaurantRealmToEntity(restaurants);

        refreshFragment();
    }

    @Override
    public void refreshFragment(){
        super.refreshFragment();
        mRestaurantListFrame.removeAllViews();
        mPoiListFrame.removeAllViews();
        mHotelListFrame.removeAllViews();

        for(int i = 0 ;i < mRestaurantResultList.size();i++){
            final Restaurant restaurant = mRestaurantResultList.get(i);

            View restaurantRow = mLayoutInflater.inflate(R.layout.row_restaurant_list, mRestaurantListFrame, false);
            ((TextView) restaurantRow.findViewById(R.id.text_view_restaurant_name)).setText(restaurant.name);
            if(i%3 == 0){
                ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Hari ke-" + ((i/3)+1) + ", makan pagi");
            } else if(i%3 == 1){
                ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Hari ke-" + ((i/3)+1) + ", makan siang");
            } else {
                ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Hari ke-" + ((i/3)+1) + ", makan malam");
            }

            restaurantRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceContentFragment(RestaurantDetailFragment.newInstance(Integer.toString(restaurant.id)), getResources().getString(R.string.restaurant_detail_fragment_tag));
                }
            });
            mRestaurantListFrame.addView(restaurantRow);
        }

        for(int i = 0 ;i < mPoiResultList.size();i++){
            final Poi poi = mPoiResultList.get(i);

            View poiRow = mLayoutInflater.inflate(R.layout.row_poi_list, mPoiListFrame, false);
            ((TextView) poiRow.findViewById(R.id.text_view_poi_name)).setText(poi.name);
            if(i%3 == 0){
                ((TextView) poiRow.findViewById(R.id.text_view_region)).setText("Hari ke-" + ((i/3)+1));
            } else if(i%3 == 1){
                ((TextView) poiRow.findViewById(R.id.text_view_region)).setText("Hari ke-" + ((i/3)+1));
            } else {
                ((TextView) poiRow.findViewById(R.id.text_view_region)).setText("Hari ke-" + ((i/3)+1));
            }
            poiRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceContentFragment(PoiDetailFragment.newInstance(Integer.toString(poi.id)), getResources().getString(R.string.restaurant_detail_fragment_tag));
                }
            });
            mPoiListFrame.addView(poiRow);
        }

        View hotelRow = mLayoutInflater.inflate(R.layout.row_hotel_list, mPoiListFrame, false);
        ((TextView) hotelRow.findViewById(R.id.text_view_hotel_name)).setText(mSelectedHotel.name);
        ((TextView) hotelRow.findViewById(R.id.text_view_region)).setText("Total biaya : " + mSelectedHotel.price);
        hotelRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceContentFragment(HotelDetailFragment.newInstance(Integer.toString(mSelectedHotel.id)), getResources().getString(R.string.restaurant_detail_fragment_tag));
            }
        });
        mHotelListFrame.addView(hotelRow);
    }

    @Override
    protected void restoreCustomActionBar(ActionBar actionBar) {
        super.restoreCustomActionBar(actionBar);

        actionBar.setTitle(mTripName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getParentActivity().setDrawerIndicatorEnabled(false);
    }

    private void convertRestaurantRealmToEntity(RealmList<com.yulius.belitungtrip.realm.Restaurant> restaurants) {
        mRestaurantResultList = new ArrayList<>();
        for(com.yulius.belitungtrip.realm.Restaurant currentRestaurant : restaurants){
            Restaurant restaurant = new Restaurant();
            restaurant.id = currentRestaurant.getRestaurantId();
            restaurant.name = currentRestaurant.getRestaurantName();
            restaurant.price = currentRestaurant.getRestaurantPrice();
            restaurant.rating = currentRestaurant.getRestaurantRating();
            restaurant.type = currentRestaurant.getRestaurantType();
            mRestaurantResultList.add(restaurant);
        }
    }

    private void convertPoiRealmToEntity(RealmList<com.yulius.belitungtrip.realm.Poi> pois) {
        mPoiResultList = new ArrayList<>();
        for(com.yulius.belitungtrip.realm.Poi currentPoi : pois){
            Poi poi = new Poi();
            poi.id = currentPoi.getPoiId();
            poi.name = currentPoi.getPoiName();
            poi.price = currentPoi.getPoiPrice();
            poi.rating = currentPoi.getPoiRating();
            mPoiResultList.add(poi);
        }
    }

    private void convertHotelRealmToEntity(com.yulius.belitungtrip.realm.Hotel hotel) {
        mSelectedHotel = new Hotel();
        mSelectedHotel.id = hotel.getHotelId();
        mSelectedHotel.name = hotel.getHotelName();
        mSelectedHotel.price = hotel.getHotelPrice();
        mSelectedHotel.rating = hotel.getHotelRating();
    }
}
