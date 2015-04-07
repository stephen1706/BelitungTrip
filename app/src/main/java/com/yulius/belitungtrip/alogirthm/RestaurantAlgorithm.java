package com.yulius.belitungtrip.alogirthm;

import android.util.Log;

import com.yulius.belitungtrip.objectTransfer.RestaurantObjectTransfer;

public class RestaurantAlgorithm {

    /* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    /* Public methods */

    // Evolve a restaurantpopulation
    public static RestaurantPopulation evolveRestaurantpopulation(RestaurantPopulation pop) {
        RestaurantObjectTransfer rot = RestaurantObjectTransfer.getInstance();
        RestaurantPopulation newRestaurantpopulation = new RestaurantPopulation(pop.size(), false, rot.maxBudget, rot.totalNight, rot.mRestaurantListResponseData);

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
        RestaurantIndividual newSol = new RestaurantIndividual(rot.maxBudget, rot.totalNight, rot.mRestaurantListResponseData);
        // Loop through genes
        do {
            for (int i = 0; i < indiv1.size(); i++) {
                if (Math.random() <= uniformRate) {
                    newSol.setGene(i, indiv1.getGene(i));
                } else {
                    newSol.setGene(i, indiv2.getGene(i));
                }
            }
        } while (newSol.anyRedundant() || newSol.priceHigherThanBudget());
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
//                restaurant.name = rot.mRestaurantListResponseData.entries[index].restaurantName;
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

        RestaurantPopulation tournament = new RestaurantPopulation(tournamentSize, false, rot.maxBudget, rot.totalNight, rot.mRestaurantListResponseData);
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
