package com.yulius.belitungtourism.fragments.content;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yulius.belitungtourism.Constans;
import com.yulius.belitungtourism.FormattingUtil;
import com.yulius.belitungtourism.R;
import com.yulius.belitungtourism.alogirthm.PoiAlgorithm;
import com.yulius.belitungtourism.alogirthm.PoiIndividual;
import com.yulius.belitungtourism.alogirthm.PoiPopulation;
import com.yulius.belitungtourism.alogirthm.RestaurantAlgorithm;
import com.yulius.belitungtourism.alogirthm.RestaurantIndividual;
import com.yulius.belitungtourism.alogirthm.RestaurantPopulation;
import com.yulius.belitungtourism.api.HotelListAPI;
import com.yulius.belitungtourism.api.PoiListAPI;
import com.yulius.belitungtourism.api.RestaurantListAPI;
import com.yulius.belitungtourism.api.SouvenirListAPI;
import com.yulius.belitungtourism.entity.Hotel;
import com.yulius.belitungtourism.entity.Poi;
import com.yulius.belitungtourism.entity.Restaurant;
import com.yulius.belitungtourism.entity.Souvenir;
import com.yulius.belitungtourism.fragments.base.BaseFragment;
import com.yulius.belitungtourism.listeners.OnMessageActionListener;
import com.yulius.belitungtourism.realm.Trip;
import com.yulius.belitungtourism.response.HotelListResponseData;
import com.yulius.belitungtourism.response.PoiListResponseData;
import com.yulius.belitungtourism.response.RestaurantListResponseData;
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
    private static final int POPULATION_SIZE = 10;
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
    private ArrayList<Restaurant> mRestaurantResultList;
    private ArrayList<Poi> mPoiResultList;
    private Hotel mSelectedHotel;
    private Souvenir mSelectedSouvenir;
    private ArrayList<Integer> mSouvenirLotteryList;
    private LinearLayout mRestaurantListFrame;
    private LinearLayout mPoiListFrame;
    private LinearLayout mHotelListFrame;
    private LinearLayout mSouvenirListFrame;
    private Button mSaveTripButton;
    private TextView mTotalPriceTextView;
    private EditText mTotalGuestEditText;
    private EditText mTripNameEditText;
    private int mPoiMinBudget;
    private int mRestaurantMinBudget;
    private int mHotelMinBudget;

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
        mPoiListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_poi_list);
        mRestaurantListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_restaurant_list);
        mHotelListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_hotel_list);
        mSouvenirListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_souvenir_list);
        mSaveTripButton = (Button) mLayoutView.findViewById(R.id.button_save_trip);
        mTotalPriceTextView = (TextView) mLayoutView.findViewById(R.id.text_view_total_price);
        mTotalGuestEditText = (EditText) mLayoutView.findViewById(R.id.edit_text_total_guest);
        mTripNameEditText = (EditText) mLayoutView.findViewById(R.id.edit_text_trip_name);
    }

    private void setUpViewState() {
    }

    private void setUpListener() {
        mTotalGuestEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    return;
                }
                int numGuests;
                try {
                    numGuests = Integer.parseInt(s.toString());
                } catch (Exception e){
                    Toast.makeText(mContext, "Please insert a valid total passenger number", Toast.LENGTH_LONG).show();
                    return;
                }

                mTotalPrice = 0;
                for(Restaurant restaurant:mRestaurantResultList){
                    mTotalPrice += numGuests * restaurant.price;
                }
                for(Poi poi:mPoiResultList){
                    mTotalPrice += numGuests * poi.price;
                }
                mTotalPrice += numGuests * mSelectedSouvenir.price;
                int numberOfRoom = (1+numGuests)/2;
                Log.d("test","jmlh kmr : " + numberOfRoom);
                mTotalPrice += numberOfRoom * (mTotalNight-1) * mSelectedHotel.price;

                mTotalPriceTextView.setText("Rp " + FormattingUtil.formatDecimal(mTotalPrice));
            }
        });

        mSaveTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tripName = mTripNameEditText.getText().toString();
                if(tripName.isEmpty()){
                    Toast.makeText(mContext, "Please insert your trip name", Toast.LENGTH_LONG).show();
                    return;
                }

                Realm realm = Realm.getInstance(mContext);
                try {
                    realm.beginTransaction();
                    Trip trip = realm.createObject(Trip.class); // Create a new object
                    trip.setNumGuests(1);
                    trip.setTripName(tripName);
                    trip.setTotalNight(mTotalNight);
                    trip.setTotalPrice(mTotalPrice);

                    com.yulius.belitungtourism.realm.Hotel hotel = realm.createObject(com.yulius.belitungtourism.realm.Hotel.class);
                    hotel.setHotelId(mSelectedHotel.id);
                    hotel.setHotelName(mSelectedHotel.name);
                    hotel.setHotelPrice(mSelectedHotel.price);
                    hotel.setHotelRating(mSelectedHotel.rating);
                    trip.setHotel(hotel);

                    com.yulius.belitungtourism.realm.Souvenir souvenir = realm.createObject(com.yulius.belitungtourism.realm.Souvenir.class);
                    souvenir.setSouvenirId(mSelectedSouvenir.id);
                    souvenir.setSouvenirName(mSelectedSouvenir.name);
                    souvenir.setSouvenirPrice(mSelectedSouvenir.price);
                    souvenir.setSouvenirRating(mSelectedSouvenir.rating);
                    trip.setSouvenir(souvenir);

                    RealmList<com.yulius.belitungtourism.realm.Restaurant> restaurants = new RealmList<>();
                    for (Restaurant currentRestaurant : mRestaurantResultList) {
                        com.yulius.belitungtourism.realm.Restaurant restaurant = realm.createObject(com.yulius.belitungtourism.realm.Restaurant.class);
                        restaurant.setRestaurantId(currentRestaurant.id);
                        restaurant.setRestaurantName(currentRestaurant.name);
                        restaurant.setRestaurantPrice(currentRestaurant.price);
                        restaurant.setRestaurantRating(currentRestaurant.rating);
                        restaurant.setRestaurantType(currentRestaurant.type);
                        restaurants.add(restaurant);
                    }
                    trip.setRestaurants(restaurants);

                    RealmList<com.yulius.belitungtourism.realm.Poi> pois = new RealmList<>();
                    for (Poi currentPoi : mPoiResultList) {
                        com.yulius.belitungtourism.realm.Poi poi = realm.createObject(com.yulius.belitungtourism.realm.Poi.class);
                        poi.setPoiId(currentPoi.id);
                        poi.setPoiName(currentPoi.name);
                        poi.setPoiPrice(currentPoi.price);
                        poi.setPoiRating(currentPoi.rating);
                        pois.add(poi);
                    }
                    trip.setPois(pois);
                    realm.commitTransaction();

                    showMessage("Your trip has been save", Constans.MessageType.MESSAGE_SUCCESS, Constans.Duration.LONG);
                } catch (RealmException e){
                    realm.cancelTransaction();
                    showMessage(tripName + " has been used for other trip name, please use other name", Constans.MessageType.MESSAGE_ERROR, Constans.Duration.LONG);
                }

                getFragmentManager().popBackStack();
            }
        });
    }

    private void setUpRequestAPI() {
        mPoiListAPI = new PoiListAPI(mContext);
        mPoiListAPI.setOnResponseListener(new PoiListAPI.OnResponseListener() {
            @Override
            public void onRequestSuccess(PoiListResponseData poiListResponseData) {
                mPoiListResponseData = poiListResponseData;
                if(poiListResponseData != null) {
                    findBestPoi();
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
                    findBestRestaurant();
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
                    findBestHotel();
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
            new StartCalculation().execute();
            showLoadingMessage(TAG);
        } else {
            refreshFragment();
        }
    }

    private void setUpMessageListener() {
        setOnMessageActionListener(new OnMessageActionListener() {
            @Override
            public void onMessageActionTryAgain() {
                super.onMessageActionTryAgain();

                hideMessageScreen();
                if(mPoiListResponseData == null || mRestaurantListResponseData == null) {
                    new StartCalculation().execute();
                    showLoadingMessage(TAG);
                }
            }
        });
    }

    @Override
    public void refreshFragment(){
        super.refreshFragment();
        mRestaurantListFrame.removeAllViews();
        mPoiListFrame.removeAllViews();
        mHotelListFrame.removeAllViews();
        mSouvenirListFrame.removeAllViews();

        for(int i = 0 ;i < mRestaurantResultList.size();i++){
            final Restaurant restaurant = mRestaurantResultList.get(i);

            View restaurantRow = mLayoutInflater.inflate(R.layout.row_restaurant_list, mRestaurantListFrame, false);
            ((TextView) restaurantRow.findViewById(R.id.text_view_restaurant_name)).setText(restaurant.name);
            if(i%3 == 0){
                ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Day " + ((i/3)+1) + ", lunch");
            } else if(i%3 == 1){
                ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Day " + ((i/3)+1) + ", dinner");
            } else {
                ((TextView) restaurantRow.findViewById(R.id.text_view_region)).setText("Day " + ((i/3)+1) + ", midnight snack");
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
                ((TextView) poiRow.findViewById(R.id.text_view_region)).setText("Day " + ((i/3)+1));
            } else if(i%3 == 1){
                ((TextView) poiRow.findViewById(R.id.text_view_region)).setText("Day " + ((i/3)+1));
            } else {
                ((TextView) poiRow.findViewById(R.id.text_view_region)).setText("Day " + ((i/3)+1));
            }
            poiRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replaceContentFragment(PoiDetailFragment.newInstance(Integer.toString(poi.id)), getResources().getString(R.string.poi_detail_fragment_tag));
                }
            });
            mPoiListFrame.addView(poiRow);
        }

        View hotelRow = mLayoutInflater.inflate(R.layout.row_hotel_list, mHotelListFrame, false);
        ((TextView) hotelRow.findViewById(R.id.text_view_hotel_name)).setText(mSelectedHotel.name);
        ((TextView) hotelRow.findViewById(R.id.text_view_region)).setText("Total price : " + mSelectedHotel.price);
        hotelRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceContentFragment(HotelDetailFragment.newInstance(Integer.toString(mSelectedHotel.id)), getResources().getString(R.string.hotel_detail_fragment_tag));
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
        mTotalGuestEditText.setText("1");
    }

    private void findBestPoi() {
        PoiPopulation poiPopulation = new PoiPopulation(POPULATION_SIZE, true, mPoiMinBudget, mPoiMaxBudget, mTotalNight, mPoiListResponseData);

        int generationCount = 0;
        int numberSameResult = 0;
        int lastFitnessResult = 0;
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
        mPoiResultList = new ArrayList<>();
        for(int i=0;i<bestPoiIndividual.size();i++){
            mPoiResultList.add(bestPoiIndividual.getGene(i));
        }

        mRestaurantListAPI.requestRestaurantList();
    }

    private void findBestRestaurant() {
        RestaurantPopulation restaurantPopulation = new RestaurantPopulation(POPULATION_SIZE, true, mRestaurantMinBudget, mRestaurantMaxBudget, mTotalNight, mRestaurantListResponseData);

        int generationCount = 0;
        int numberSameResult = 0;
        int lastFitnessResult = 0;
//        while (restaurantPopulation.getFittest().getFitness() < FitnessCalculation.getMaxFitness()) {
        while (true) {
            // Evolve our population until we reach an optimum solution
            if(numberSameResult >=5){//5x berturut" hasilnya sama break aj
                break;
            }

            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + restaurantPopulation.getFittest().getFitness());
            restaurantPopulation = RestaurantAlgorithm.evolveRestaurantpopulation(restaurantPopulation);
            if(lastFitnessResult == restaurantPopulation.getFittest().getFitness()){
                numberSameResult++;
            } else{
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
        mRestaurantResultList = new ArrayList<>();

        for(int i=0;i<bestRestaurantIndividual.size();i++){
            mRestaurantResultList.add(bestRestaurantIndividual.getGene(i));
        }

        mHotelListAPI.requestHotelList();
    }

    private void findBestHotel() {
        for(int i=0;i<mHotelListResponseData.entries.length;i++){
            int totalPrice = mHotelListResponseData.entries[i].hotelPrice * (mTotalNight-1);
            if(totalPrice <= mHotelMaxBudget && totalPrice >= mHotelMinBudget){
                Hotel hotel = new Hotel();
                hotel.id = mHotelListResponseData.entries[i].hotelId;
                hotel.name = mHotelListResponseData.entries[i].hotelName;
                hotel.price = mHotelListResponseData.entries[i].hotelPrice;
                hotel.rating = mHotelListResponseData.entries[i].hotelRating;
                mSelectedHotel = hotel;
                break;
            }
        }

        if(mSelectedHotel == null){//fallback klo null, ambil yg lbh murah aj gpp
            for(int i=0;i<mHotelListResponseData.entries.length;i++){
                int totalPrice = mHotelListResponseData.entries[i].hotelPrice * (mTotalNight-1);
                if(totalPrice <= mHotelMaxBudget){
                    Hotel hotel = new Hotel();
                    hotel.id = mHotelListResponseData.entries[i].hotelId;
                    hotel.name = mHotelListResponseData.entries[i].hotelName;
                    hotel.price = mHotelListResponseData.entries[i].hotelPrice;
                    hotel.rating = mHotelListResponseData.entries[i].hotelRating;
                    mSelectedHotel = hotel;
                    break;
                }
            }
        }
        mSouvenirListAPI.requestSouvenirList();
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
                mSelectedSouvenir = new Souvenir();
                mSelectedSouvenir.id = mSouvenirListResponseData.entries[i].souvenirId;
                mSelectedSouvenir.name = mSouvenirListResponseData.entries[i].souvenirName;
                mSelectedSouvenir.price = mSouvenirListResponseData.entries[i].souvenirPrice;
                mSelectedSouvenir.rating = mSouvenirListResponseData.entries[i].souvenirRating;
                break;
            }
        }
        refreshFragment();
    }

    @Override
    protected void restoreCustomActionBar(ActionBar actionBar) {
        super.restoreCustomActionBar(actionBar);

        actionBar.setDisplayHomeAsUpEnabled(true);
        getParentActivity().setDrawerIndicatorEnabled(false);
    }

    private class StartCalculation extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            mPoiListAPI.requestPoiList();
            return null;
        }

    }
}
