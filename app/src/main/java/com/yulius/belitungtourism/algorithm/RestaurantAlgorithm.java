package com.yulius.belitungtourism.algorithm;

import android.util.Log;

import com.yulius.belitungtourism.objectTransfer.RestaurantObjectTransfer;

public class RestaurantAlgorithm {

    /* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 3;
    private static final boolean elitism = true;

    /* Public methods */

    // Evolve a restaurantpopulation
    public static RestaurantPopulation evolveRestaurantpopulation(RestaurantPopulation pop) {
        RestaurantObjectTransfer rot = RestaurantObjectTransfer.getInstance();
        RestaurantPopulation newRestaurantpopulation = new RestaurantPopulation(pop.size(), false, rot.minBudget, rot.maxBudget, rot.totalNight, rot.mRestaurantListResponseData, rot.mRestaurantNearbyPoiResponseData, rot.mPoiResultList);

        // Keep our best restaurantindividual
        if (elitism) {
            newRestaurantpopulation.saveIndividual(0, pop.getFittest());
        }

        // Crossover restaurantpopulation
        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }
        // Loop over the restaurantpopulation size and create new restaurantindividuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i++) {
            RestaurantIndividual indiv1 = tournamentSelection(pop);
            RestaurantIndividual indiv2 = tournamentSelection(pop);
            RestaurantIndividual newIndiv = crossover(indiv1, indiv2);
            newRestaurantpopulation.saveIndividual(i, newIndiv);
        }

        // Mutate restaurantpopulation
        for (int i = elitismOffset; i < newRestaurantpopulation.size(); i++) {
            mutate(newRestaurantpopulation.getIndividual(i));
        }

        return newRestaurantpopulation;
    }

    // Crossover restaurantindividuals
    private static RestaurantIndividual crossover(RestaurantIndividual indiv1, RestaurantIndividual indiv2) {
        RestaurantObjectTransfer rot = RestaurantObjectTransfer.getInstance();
        //todo
        RestaurantIndividual newSol = new RestaurantIndividual(rot.minBudget, rot.maxBudget, rot.totalNight, rot.mRestaurantListResponseData, rot.mRestaurantNearbyPoiResponseData, rot.mPoiResultList);
        // Loop through genes
        int totalRetry = 0;
        newSol.resetLenientAdjustment();
        do {
            if(totalRetry % 5==0){
                newSol.increaseLenientAdjustment();
            }
            for (int i = 0; i < indiv1.size(); i++) {
                if (Math.random() <= uniformRate) {
                    newSol.setGene(i, indiv1.getGene(i));
                } else {
                    newSol.setGene(i, indiv2.getGene(i));
                }
            }
            totalRetry++;
        } while (newSol.anyRedundant() || newSol.priceHigherOrLowerThanBudget());
        return newSol;
    }

    // Mutate an individual
    private static void mutate(RestaurantIndividual indiv) {
        // Loop through genes
//        RestaurantObjectTransfer rot = RestaurantObjectTransfer.getInstance();

        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                Log.d("test algo","mutate restaurant");
                indiv.mutate();

//                int index = new Random().nextInt(rot.mRestaurantListResponseData.entries.length);//ambil lg dr response data
//
//                Restaurant restaurant = new Restaurant();
//                restaurant.id = rot.mRestaurantListResponseData.entries[index].restaurantId;
//                restaurant.rating = rot.mRestaurantListResponseData.entries[index].restaurantRating;
//                restaurant.name = rot.mRestaurantListResponseData.entries[index].souvenirName;
//                restaurant.type = rot.mRestaurantListResponseData.entries[index].restaurantType;
//                restaurant.price = rot.mRestaurantListResponseData.entries[index].restaurantPrice;
//                indiv.setGene(i, restaurant);
            }
        }
    }

    // Select restaurantindividuals for crossover
    private static RestaurantIndividual tournamentSelection(RestaurantPopulation pop) {
        // Create a tournament restaurantpopulation
        RestaurantObjectTransfer rot = RestaurantObjectTransfer.getInstance();

        RestaurantPopulation tournament = new RestaurantPopulation(tournamentSize, false, rot.minBudget, rot.maxBudget, rot.totalNight, rot.mRestaurantListResponseData, rot.mRestaurantNearbyPoiResponseData, rot.mPoiResultList);
        // For each place in the tournament get a random restaurantindividual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        RestaurantIndividual fittest = tournament.getFittest();
        return fittest;
    }
}
