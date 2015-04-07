package com.yulius.belitungtrip.alogirthm;

import com.yulius.belitungtrip.entity.Restaurant;
import com.yulius.belitungtrip.response.RestaurantListResponseData;

import java.util.ArrayList;
import java.util.Random;

public class RestaurantIndividual {
    static int defaultGeneLength = 9;
    private Restaurant[] genes;
    private ArrayList<Restaurant> restaurantList;
    private int maxBudget;
    private int totalNight;
    private int fitness = 0;

    public RestaurantIndividual(int maxBudget, int totalNight, RestaurantListResponseData restaurantListResponseData) {
        restaurantList = new ArrayList<>();
        for(int i=0 ; i < restaurantListResponseData.entries.length;i++){
            Restaurant restaurant = new Restaurant();
            restaurant.id = restaurantListResponseData.entries[i].restaurantId;
            restaurant.name = restaurantListResponseData.entries[i].restaurantName;
            restaurant.rating = restaurantListResponseData.entries[i].restaurantRating;
            restaurant.price = restaurantListResponseData.entries[i].restaurantPrice;
            restaurant.type = restaurantListResponseData.entries[i].restaurantType;

            restaurantList.add(restaurant);
        }
        this.maxBudget = maxBudget;
        this.totalNight = totalNight;
        defaultGeneLength = totalNight * 3;
        genes = new Restaurant[defaultGeneLength];
        generateIndividual();
    }

    public void generateIndividual() {
        do {
            for (int i = 0; i < size(); i++) {
                do {
                    int restaurantIndex = new Random().nextInt(restaurantList.size());
                    genes[i] = restaurantList.get(restaurantIndex);
                    if (i % 3 == 2) {//khusus yg mlm hrs type no 2
                        if (genes[i].type == 2) {
                            break;
                        }
                    } else {
                        if (genes[i].type == 1) {
                            break;
                        }
                    }
                } while (true);
            }
        } while (priceHigherThanBudget());
    }

    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }

    public Restaurant getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, Restaurant value) {
        genes[index] = value;
        fitness = 0;
    }

    public int size() {
        return genes.length;
    }

    public int getFitness() {
        fitness = 0;
        for(int i=0;i<defaultGeneLength;i++){
            fitness += genes[i].rating;
        }
        return fitness;
    }

    public void mutate(){
        int changeIndex = new Random().nextInt(defaultGeneLength);

        do {
            int newRestaurantIndex = new Random().nextInt(restaurantList.size());
            genes[changeIndex] = restaurantList.get(newRestaurantIndex);
        } while (priceHigherThanBudget());
    }

    private boolean priceHigherThanBudget() {
        int totalPrice = 0;
        for (int i = 0; i < size(); i++) {
            totalPrice += genes[i].price;
        }
        return (totalPrice > maxBudget);
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            if(i%3 == 0){
                geneString += "\n";
            }
            geneString += " " + getGene(i).rating + "-" + getGene(i).type;
        }
        return geneString;
    }
}
