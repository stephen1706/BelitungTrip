package com.yulius.belitungtourism.algorithm;

import com.yulius.belitungtourism.entity.Poi;
import com.yulius.belitungtourism.objectTransfer.RestaurantObjectTransfer;
import com.yulius.belitungtourism.response.RestaurantListResponseData;
import com.yulius.belitungtourism.response.RestaurantNearbyPoiResponseData;

import java.util.ArrayList;

public class RestaurantPopulation {
    private static RestaurantListResponseData mRestaurantListResponseData;
    private static RestaurantNearbyPoiResponseData mRestaurantNearbyPoiResponseData;
    private final ArrayList<Poi> mPoiResultList;
    RestaurantIndividual[] individuals;

    public RestaurantPopulation(int populationSize, boolean initialise, int minBudget, int maxBudget, int totalNight, RestaurantListResponseData restaurantListResponseData, RestaurantNearbyPoiResponseData restaurantNearbyPoiResponseData, ArrayList<Poi> poiResultList) {
        individuals = new RestaurantIndividual[populationSize];
        mRestaurantListResponseData = restaurantListResponseData;
        mRestaurantNearbyPoiResponseData = restaurantNearbyPoiResponseData;
        mPoiResultList = poiResultList;
        RestaurantObjectTransfer rot = RestaurantObjectTransfer.getInstance();

        rot.maxBudget = maxBudget;
        rot.minBudget = minBudget;
        rot.totalNight = totalNight;
        rot.mRestaurantListResponseData = restaurantListResponseData;
        rot.mRestaurantNearbyPoiResponseData = restaurantNearbyPoiResponseData;
        rot.mPoiResultList = poiResultList;

        if (initialise) {
            for (int i = 0; i < size(); i++) {
                RestaurantIndividual newIndividual = new RestaurantIndividual(minBudget, maxBudget, totalNight, mRestaurantListResponseData, mRestaurantNearbyPoiResponseData, mPoiResultList);
                newIndividual.generateIndividual();
                saveIndividual(i, newIndividual);
            }
        }
    }

    public RestaurantIndividual getIndividual(int index) {
        return individuals[index];
    }

    public RestaurantIndividual getFittest() {
        RestaurantIndividual fittest = individuals[0];
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    public int size() {
        return individuals.length;
    }

    public void saveIndividual(int index, RestaurantIndividual indiv) {
        individuals[index] = indiv;
    }
}
