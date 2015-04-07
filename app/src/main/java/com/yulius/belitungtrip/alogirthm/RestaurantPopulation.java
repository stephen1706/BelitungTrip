package com.yulius.belitungtrip.alogirthm;

import com.yulius.belitungtrip.objectTransfer.RestaurantObjectTransfer;
import com.yulius.belitungtrip.response.RestaurantListResponseData;

public class RestaurantPopulation {
    private static RestaurantListResponseData mRestaurantListResponseData;
    RestaurantIndividual[] individuals;

    public RestaurantPopulation(int populationSize, boolean initialise, int maxBudget, int totalNight, RestaurantListResponseData restaurantListResponseData) {
        individuals = new RestaurantIndividual[populationSize];
        // Initialise population
        mRestaurantListResponseData = restaurantListResponseData;
        RestaurantObjectTransfer rot = RestaurantObjectTransfer.getInstance();

        rot.maxBudget = maxBudget;
        rot.totalNight = totalNight;
        rot.mRestaurantListResponseData = restaurantListResponseData;

        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < size(); i++) {
                RestaurantIndividual newIndividual = new RestaurantIndividual(maxBudget, totalNight, mRestaurantListResponseData);
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
        // Loop through individuals to find fittest
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public int size() {
        return individuals.length;
    }

    // Save individual
    public void saveIndividual(int index, RestaurantIndividual indiv) {
        individuals[index] = indiv;
    }
}
