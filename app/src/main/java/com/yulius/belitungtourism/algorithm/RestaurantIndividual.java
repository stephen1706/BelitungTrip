package com.yulius.belitungtourism.algorithm;

import com.yulius.belitungtourism.entity.Poi;
import com.yulius.belitungtourism.entity.Restaurant;
import com.yulius.belitungtourism.response.RestaurantListResponseData;
import com.yulius.belitungtourism.response.RestaurantNearbyPoiResponseData;

import org.apache.commons.collections15.multimap.MultiHashMap;

import java.util.ArrayList;
import java.util.Random;

public class RestaurantIndividual {
    private static final int LENIENT_ADJUSTMENT = 50000;
    static int defaultGeneLength = 9;
    private final MultiHashMap<Integer, Integer> mRestaurantNearbyPoiList;
    private final ArrayList<Poi> mPoiResultList;
    private int minBudget;
    private Restaurant[] genes;
    private ArrayList<Restaurant> mRestaurantList;
    private int maxBudget;
    private int totalNight;
    private int fitness = 0;

    public RestaurantIndividual(int minBudget, int maxBudget, int totalNight, RestaurantListResponseData restaurantListResponseData, RestaurantNearbyPoiResponseData restaurantNearbyPoiResponseData, ArrayList<Poi> poiResultList) {
        mRestaurantList = new ArrayList<>();
        for(int i=0 ; i < restaurantListResponseData.entries.length;i++){
            Restaurant restaurant = new Restaurant();
            restaurant.id = restaurantListResponseData.entries[i].restaurantId;
            restaurant.name = restaurantListResponseData.entries[i].restaurantName;
            restaurant.rating = restaurantListResponseData.entries[i].restaurantRating;
            restaurant.price = restaurantListResponseData.entries[i].restaurantPrice;
            restaurant.type = restaurantListResponseData.entries[i].restaurantType;

            mRestaurantList.add(restaurant);
        }
        this.minBudget = minBudget;
        this.maxBudget = maxBudget;
        this.totalNight = totalNight;
        mPoiResultList = poiResultList;
//        mRestaurantNearbyPoiList = ArrayListMultimap.create();
        mRestaurantNearbyPoiList = new MultiHashMap<>();
        for(int i=0;i<restaurantNearbyPoiResponseData.entries.length;i++){
            mRestaurantNearbyPoiList.put(restaurantNearbyPoiResponseData.entries[i].poiId, restaurantNearbyPoiResponseData.entries[i].restaurantId);
        }

        defaultGeneLength = totalNight * 3;
        genes = new Restaurant[defaultGeneLength];
        generateIndividual();
    }

    public void generateIndividual() {
        do {
            for (int i = 0; i < size(); i++) {
                if(i%3==2) {//pake cara lama buat snack mlm
                    do {
                        int restaurantIndex = new Random().nextInt(mRestaurantList.size());
                        genes[i] = mRestaurantList.get(restaurantIndex);
                        if (i % 3 == 2) {//khusus yg snack mlm hrs type no 3
                            if (genes[i].type == 3) {
                                break;
                            }
                        } else if (i % 3 == 1) {
                            if (genes[i].type == 1 || genes[i].type == 2) {
                                break;
                            }
                        } else {
                            if (genes[i].type == 1 || genes[i].type == 2) {
                                break;
                            }
                        }
                    } while (true);
                } else {//mkn biasa berdasarkan nearby poi
                    int poiId = mPoiResultList.get(i+1).id; // +1 krn makan pertama itu ke poi kedua
                    ArrayList<Integer> nearbyRestaurantList = (ArrayList<Integer>) mRestaurantNearbyPoiList.get(poiId);
                    int selectedIndex = new Random().nextInt(nearbyRestaurantList.size());
                    int selectedRestaurantId = nearbyRestaurantList.get(selectedIndex);
                    genes[i] = getRestaurantById(selectedRestaurantId);
                }
            }
        } while (priceHigherOrLowerThanBudget() || anyRedundant());
    }

    private Restaurant getRestaurantById(int selectedRestaurantId) {
        for(int i = 0;i<mRestaurantList.size();i++){
            if(mRestaurantList.get(i).id == selectedRestaurantId){
                return mRestaurantList.get(i);
            }
        }
        return null;
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
            int newRestaurantIndex = new Random().nextInt(mRestaurantList.size());

            if(changeIndex % 3 == 2 && mRestaurantList.get(newRestaurantIndex).type == 3){
            //biar grupin ga brantakan di mutasi, klo crossover ga bkl brantakan krn ambil gen dr urutan yg sama selalu
                genes[changeIndex] = mRestaurantList.get(newRestaurantIndex);
            }
//            else if (changeIndex % 3 == 0 && mRestaurantList.get(newRestaurantIndex).type == 1){
//                genes[changeIndex] = mRestaurantList.get(newRestaurantIndex);
//            } else if (changeIndex % 3 == 1 && mRestaurantList.get(newRestaurantIndex).type == 2){
//                genes[changeIndex] = mRestaurantList.get(newRestaurantIndex);
//            }
            else if (changeIndex % 3 != 2) {
//                int restaurantId = genes[changeIndex].id;
                int poiId = mPoiResultList.get(changeIndex + 1).id; // +1 krn makan pertama itu ke poi kedua
                ArrayList<Integer> nearbyRestaurantList = (ArrayList<Integer>) mRestaurantNearbyPoiList.get(poiId);
                int selectedIndex = new Random().nextInt(nearbyRestaurantList.size());
                int selectedRestaurantId = nearbyRestaurantList.get(selectedIndex);
                genes[changeIndex] = getRestaurantById(selectedRestaurantId);
            }

        } while (priceHigherOrLowerThanBudget() || anyRedundant());
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < size(); i++) {
            totalPrice += genes[i].price;
        }
        return (totalPrice);
    }

    public boolean priceHigherOrLowerThanBudget() {
        int totalPrice = 0;
        for (int i = 0; i < size(); i++) {
            totalPrice += genes[i].price;
        }
        return ((totalPrice - LENIENT_ADJUSTMENT > maxBudget) || totalPrice + LENIENT_ADJUSTMENT < minBudget);
    }

    public boolean anyRedundant(){
        for (int i = 0; i < size(); i++) {
            for(int j=i+1;j<size();j++) {
                if(genes[i].id == genes[j].id){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            if(i%3 == 0){
                geneString += "\n";
            }
            geneString += " " + getGene(i).id + "-" + getGene(i).type;
        }
        return geneString;
    }
}
