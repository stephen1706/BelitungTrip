package com.yulius.belitungtourism.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulius.belitungtourism.FormattingUtil;
import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.entity.Hotel;
import com.yulius.belitungtourism.entity.Poi;
import com.yulius.belitungtourism.entity.Restaurant;
import com.yulius.belitungtourism.entity.Souvenir;
import com.yulius.belitungtourism.fragments.base.BaseFragment;
import com.yulius.belitungtourism.realm.Car;
import com.yulius.belitungtourism.realm.Trip;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TripDetailFragment extends BaseFragment {
    private static final String PARAM_TRIP_NAME = "param_trip_name";
    private ArrayList<Restaurant> mRestaurantResultList;
    private ArrayList<Poi> mPoiResultList;
    private Hotel mSelectedHotel;
    private Souvenir mSelectedSouvenir;
    private LinearLayout mHotelListFrame;
    private LinearLayout mSouvenirListFrame;
    private String mTripName;
    private LinearLayout mTripListFrame;
    private LinearLayout mTransportationListFrame;
    private com.yulius.belitungtourism.entity.Car mSelectedCar;

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

        mLayoutView = inflater.inflate(R.layout.fragment_trip_detail, container, false);

        setUpAttribute();
        setUpView();
        setUpViewState();

        return mLayoutView;
    }

    private void setUpAttribute() {

    }

    private void setUpView() {
        mHotelListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_hotel_list);
        mTripListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_trip_list);
        mSouvenirListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_souvenir_list);
        mTransportationListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_transportation_list);
    }

    private void setUpViewState() {
        Realm realm = Realm.getInstance(mContext);

        RealmResults<Trip> tripResult = realm.where(Trip.class)
                .equalTo("tripName", mTripName)
                .findAll();

        com.yulius.belitungtourism.realm.Hotel hotel = tripResult.first().getHotel();
        convertHotelRealmToEntity(hotel);

        RealmList<com.yulius.belitungtourism.realm.Poi> pois = tripResult.first().getPois();
        convertPoiRealmToEntity(pois);

        RealmList<com.yulius.belitungtourism.realm.Restaurant> restaurants = tripResult.first().getRestaurants();
        convertRestaurantRealmToEntity(restaurants);

        com.yulius.belitungtourism.realm.Souvenir souvenir = tripResult.first().getSouvenir();
        convertSouvenirRealmToEntity(souvenir);

        com.yulius.belitungtourism.realm.Car car = tripResult.first().getCar();
        convertCarRealmToEntity(car);

        refreshFragment();
    }

    @Override
    public void refreshFragment(){
        super.refreshFragment();

        mTripListFrame.removeAllViews();
        mHotelListFrame.removeAllViews();
        mSouvenirListFrame.removeAllViews();
        mTransportationListFrame.removeAllViews();

        for(int i = 0 ;i < mRestaurantResultList.size();i++){
            if(i%3 == 0){
                TextView textView = new TextView(getParentActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 20, 0, 20);
                textView.setLayoutParams(lp);
                textView.setText("Day " + ((i / 3) + 1));

                mTripListFrame.addView(textView);
            }

            final Poi poi = mPoiResultList.get(i);

            View poiRow = mLayoutInflater.inflate(R.layout.row_poi_list, mTripListFrame, false);
            ((TextView) poiRow.findViewById(R.id.text_view_poi_name)).setText(poi.name);
            ((TextView) poiRow.findViewById(R.id.text_view_region)).setVisibility(View.GONE);
            poiRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceContentFragment(PoiDetailFragment.newInstance(Integer.toString(poi.id)), getResources().getString(R.string.poi_detail_fragment_tag));
                }
            });
            mTripListFrame.addView(poiRow);

            final Restaurant restaurant = mRestaurantResultList.get(i);

            View restaurantRow = mLayoutInflater.inflate(R.layout.row_restaurant_list, mTripListFrame, false);
            ((TextView) restaurantRow.findViewById(R.id.text_view_restaurant_name)).setText(restaurant.name);
            if(i%3 == 0){
                ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Lunch");
            } else if(i%3 == 1){
                ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Dinner");
            } else {
                ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Midnight snack");
            }

            restaurantRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceContentFragment(RestaurantDetailFragment.newInstance(Integer.toString(restaurant.id)), getResources().getString(R.string.restaurant_detail_fragment_tag));
                }
            });
            mTripListFrame.addView(restaurantRow);
        }
        View hotelRow = mLayoutInflater.inflate(R.layout.row_hotel_list, mHotelListFrame, false);
        ((TextView) hotelRow.findViewById(R.id.text_view_hotel_name)).setText(mSelectedHotel.name);
        ((TextView) hotelRow.findViewById(R.id.text_view_region)).setText("Total price : " + mSelectedHotel.price);
        hotelRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceContentFragment(HotelDetailFragment.newInstance(Integer.toString(mSelectedHotel.id)), getResources().getString(R.string.restaurant_detail_fragment_tag));
            }
        });
        mHotelListFrame.addView(hotelRow);

        View souvenirRow = mLayoutInflater.inflate(R.layout.row_souvenir_list, mSouvenirListFrame, false);
        ((TextView) souvenirRow.findViewById(R.id.text_view_souvenir_name)).setText(mSelectedSouvenir.name);
        ((TextView) souvenirRow.findViewById(R.id.text_view_region)).setText("Cost Estimation : " + mSelectedSouvenir.price);
        souvenirRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceContentFragment(SouvenirDetailFragment.newInstance(Integer.toString(mSelectedSouvenir.id)), getResources().getString(R.string.souvenir_detail_fragment_tag));
            }
        });
        mSouvenirListFrame.addView(souvenirRow);

        View carRow = mLayoutInflater.inflate(R.layout.row_car_list, mTransportationListFrame, false);
        ((TextView) carRow.findViewById(R.id.text_view_car_name)).setText(mSelectedCar.carName);
        ((TextView) carRow.findViewById(R.id.text_view_car_detail)).setText("Cost : Rp " + FormattingUtil.formatDecimal(mSelectedCar.carPrice) + ", for " + mSelectedCar.carCapacity + " person");

        mTransportationListFrame.addView(carRow);
    }

    @Override
    protected void restoreCustomActionBar(ActionBar actionBar) {
        super.restoreCustomActionBar(actionBar);

        actionBar.setTitle(mTripName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getParentActivity().setDrawerIndicatorEnabled(false);
    }

    private void convertRestaurantRealmToEntity(RealmList<com.yulius.belitungtourism.realm.Restaurant> restaurants) {
        mRestaurantResultList = new ArrayList<>();
        for(com.yulius.belitungtourism.realm.Restaurant currentRestaurant : restaurants){
            Restaurant restaurant = new Restaurant();
            restaurant.id = currentRestaurant.getRestaurantId();
            restaurant.name = currentRestaurant.getRestaurantName();
            restaurant.price = currentRestaurant.getRestaurantPrice();
            restaurant.rating = currentRestaurant.getRestaurantRating();
            restaurant.type = currentRestaurant.getRestaurantType();
            mRestaurantResultList.add(restaurant);
        }
    }


    private void convertPoiRealmToEntity(RealmList<com.yulius.belitungtourism.realm.Poi> pois) {
        mPoiResultList = new ArrayList<>();
        for(com.yulius.belitungtourism.realm.Poi currentPoi : pois){
            Poi poi = new Poi();
            poi.id = currentPoi.getPoiId();
            poi.name = currentPoi.getPoiName();
            poi.price = currentPoi.getPoiPrice();
            poi.rating = currentPoi.getPoiRating();
            mPoiResultList.add(poi);
        }
    }

    private void convertHotelRealmToEntity(com.yulius.belitungtourism.realm.Hotel hotel) {
        mSelectedHotel = new Hotel();
        mSelectedHotel.id = hotel.getHotelId();
        mSelectedHotel.name = hotel.getHotelName();
        mSelectedHotel.price = hotel.getHotelPrice();
        mSelectedHotel.rating = hotel.getHotelRating();
    }

    private void convertSouvenirRealmToEntity(com.yulius.belitungtourism.realm.Souvenir souvenir) {
        mSelectedSouvenir = new Souvenir();
        mSelectedSouvenir.id = souvenir.getSouvenirId();
        mSelectedSouvenir.name = souvenir.getSouvenirName();
        mSelectedSouvenir.price = souvenir.getSouvenirPrice();
        mSelectedSouvenir.rating = souvenir.getSouvenirRating();
    }

    private void convertCarRealmToEntity(Car car) {
        mSelectedCar = new com.yulius.belitungtourism.entity.Car();
        mSelectedCar.carId = car.getCarId();
        mSelectedCar.carName = car.getCarName();
        mSelectedCar.carPrice = car.getCarPrice();
        mSelectedCar.carCapacity = car.getCarCapacity();
    }
}
