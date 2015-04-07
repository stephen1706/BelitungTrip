package com.yulius.belitungtrip.fragments.content;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.yulius.belitungtrip.R;
import com.yulius.belitungtrip.adapters.TripResultAdapter;
import com.yulius.belitungtrip.alogirthm.PoiAlgorithm;
import com.yulius.belitungtrip.alogirthm.PoiPopulation;
import com.yulius.belitungtrip.alogirthm.RestaurantAlgorithm;
import com.yulius.belitungtrip.alogirthm.RestaurantPopulation;
import com.yulius.belitungtrip.api.PoiListAPI;
import com.yulius.belitungtrip.api.RestaurantListAPI;
import com.yulius.belitungtrip.fragments.base.BaseFragment;
import com.yulius.belitungtrip.response.PoiListResponseData;
import com.yulius.belitungtrip.response.RestaurantListResponseData;

public class TripResultFragment extends BaseFragment {
    private static final String PARAM_TOTAL_NIGHT = "param_total_night";
    private static final String PARAM_MAX_BUDGET = "param_max_budget";
    private static final int POPULATION_SIZE = 10;
    private RecyclerView mTripList;
    private TripResultAdapter mTripResultAdapter;
    private RestaurantListAPI mRestaurantListAPI;
    private PoiListAPI mPoiListAPI;
    private RestaurantListResponseData mRestaurantListResponseData;
    private PoiListResponseData mPoiListResponseData;
    private int mMaxBudget;
    private int mTotalNight;

    public static TripResultFragment newInstance(int maxBudget, int totalNight) {
        TripResultFragment fragment = new TripResultFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_MAX_BUDGET, maxBudget);
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
            mMaxBudget = getArguments().getInt(PARAM_MAX_BUDGET);
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
//        setUpListener();
//        setUpMessageListener();

        return mLayoutView;
    }


    private void setUpView() {
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

        mPoiListAPI.requestPoiList();
    }

    @Override
    public void refreshFragment(){
        super.refreshFragment();
    }

    private void findBestPoi() {
        PoiPopulation poiPopulation = new PoiPopulation(POPULATION_SIZE, true, mMaxBudget, mTotalNight, mPoiListResponseData);

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

        mRestaurantListAPI.requestRestaurantList();
    }

    private void findBestRestaurant() {
        RestaurantPopulation restaurantPopulation = new RestaurantPopulation(POPULATION_SIZE, true, mMaxBudget, mTotalNight, mRestaurantListResponseData);

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
        //show hasilnya
//        RestaurantIndividual bestRestaurantIndividual = restaurantPopulation.getFittest();
//        for(int i=0;i<bestRestaurantIndividual.size();i++){
//            bestRestaurantIndividual.getGene(i).name
//        }
    }
}
