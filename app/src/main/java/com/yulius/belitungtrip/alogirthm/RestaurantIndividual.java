package com.yulius.belitungtrip.alogirthm;

import android.content.Context;

import com.yulius.belitungtrip.realm.Restaurant;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class RestaurantIndividual {

    static int defaultGeneLength = 9;
    private Restaurant[] genes = new Restaurant[defaultGeneLength];
    private Realm realm;
    private RealmResults<Restaurant> restaurantList;
    private int maxBudget;
    private int totalNight;
    // Cache
    private int fitness = 0;

    public RestaurantIndividual(Context context, int maxBudget, int totalNight) {
        realm = Realm.getInstance(context);
        restaurantList = realm.where(Restaurant.class).findAll();
        this.maxBudget = maxBudget;
        this.totalNight = totalNight;
        defaultGeneLength = totalNight * 3;
        generateIndividual();
    }

    public void generateIndividual() {
        int totalPrice = 0;
        do {
            for (int i = 0; i < size(); i++) {
                int restaurantIndex = new Random().nextInt(restaurantList.size());
                genes[i] = restaurantList.get(restaurantIndex);
                totalPrice += genes[i].getRestaurantPrice();
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
            fitness += genes[i].getRestaurantRating();
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
            totalPrice += genes[i].getRestaurantPrice();
        }
        return (totalPrice > maxBudget);
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            if(i%totalNight == 0){
                geneString += "\n";
            }
            geneString += getGene(i).getRestaurantRating();
        }
        return geneString;
    }
}
