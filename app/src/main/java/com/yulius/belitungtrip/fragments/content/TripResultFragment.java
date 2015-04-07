package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.alogirthm.PoiAlgorithm;
import com.yulius.belitungtrip.alogirthm.PoiIndividual;
import com.yulius.belitungtrip.alogirthm.PoiPopulation;
import com.yulius.belitungtrip.alogirthm.RestaurantAlgorithm;
import com.yulius.belitungtrip.alogirthm.RestaurantIndividual;
import com.yulius.belitungtrip.alogirthm.RestaurantPopulation;
import com.yulius.belitungtrip.api.PoiListAPI;
import com.yulius.belitungtrip.api.RestaurantListAPI;
import com.yulius.belitungtrip.entity.Poi;
import com.yulius.belitungtrip.entity.Restaurant;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.listeners.OnMessageActionListener;
import com.yulius.belitungtrip.response.PoiListResponseData;
import com.yulius.belitungtrip.response.RestaurantListResponseData;

import java.util.ArrayList;

public class TripResultFragment extends BaseFragment {
    private static final String PARAM_TOTAL_NIGHT = "param_total_night";
    private static final String PARAM_POI_BUDGET = "param_poi_budget";
    private static final String PARAM_RESTAURANT_BUDGET = "param_restaurant_budget";
    private static final String PARAM_HOTEL_BUDGET = "param_hotel_budget";
    private static final int POPULATION_SIZE = 10;
    private RestaurantListAPI mRestaurantListAPI;
    private PoiListAPI mPoiListAPI;
    private RestaurantListResponseData mRestaurantListResponseData;
    private PoiListResponseData mPoiListResponseData;
    private int mPoiBudget;
    private int mRestaurantBudget;
    private int mHotelBudget;
    private int mTotalNight;
    private ArrayList<Restaurant> mRestaurantResultList;
    private ArrayList<Poi> mPoiResultList;
    private LinearLayout mRestaurantListFrame;
    private LinearLayout mPoiListFrame;

    public static TripResultFragment newInstance(int poiBudget, int restaurantBudget, int hotelBudget, int totalNight) {
        TripResultFragment fragment = new TripResultFragment();
        Bundle args = new Bundle();

        args.putInt(PARAM_POI_BUDGET, poiBudget);
        args.putInt(PARAM_RESTAURANT_BUDGET, restaurantBudget);
        args.putInt(PARAM_HOTEL_BUDGET, hotelBudget);
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
            mPoiBudget = getArguments().getInt(PARAM_POI_BUDGET);
            mRestaurantBudget = getArguments().getInt(PARAM_RESTAURANT_BUDGET);
            mHotelBudget = getArguments().getInt(PARAM_HOTEL_BUDGET);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayoutView = inflater.inflate(R.layout.fragment_trip_planner_result, container, false);

        setUpView();
        setUpViewState();
        setUpRequestAPI();
        setUpMessageListener();

        return mLayoutView;
    }

    private void setUpView() {
        mPoiListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_poi_list);
        mRestaurantListFrame = (LinearLayout) mLayoutView.findViewById(R.id.frame_restaurant_list);
    }

    private void setUpViewState() {
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

        if(mPoiListResponseData == null || mRestaurantListResponseData == null) {
            mPoiListAPI.requestPoiList();
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
                    mPoiListAPI.requestPoiList();
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
    }

    private void findBestPoi() {
        PoiPopulation poiPopulation = new PoiPopulation(POPULATION_SIZE, true, mPoiBudget, mTotalNight, mPoiListResponseData);

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
        RestaurantPopulation restaurantPopulation = new RestaurantPopulation(POPULATION_SIZE, true, mRestaurantBudget, mTotalNight, mRestaurantListResponseData);

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
        refreshFragment();
    }
}
