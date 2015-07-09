package com.yulius.belitungtourism.algorithm;

import com.yulius.belitungtourism.entity.Poi;
import com.yulius.belitungtourism.response.PoiListResponseData;

import java.util.ArrayList;
import java.util.Random;

public class PoiIndividual {
    private static final int LENIENT_ADJUSTMENT = 50000;
    static int defaultGeneLength = 9;
    private int minBudget;
    private Poi[] genes;
    private ArrayList<Poi> poiList;
    private int maxBudget;
    private int totalNight;
    private int fitness = 0;

    public PoiIndividual(int minBudget, int maxBudget, int totalNight, PoiListResponseData poiListResponseData) {
        poiList = new ArrayList<>();
        for(int i=0 ; i < poiListResponseData.entries.length;i++){
            Poi poi = new Poi();
            poi.id = poiListResponseData.entries[i].poiId;
            poi.name = poiListResponseData.entries[i].poiName;
            poi.rating = poiListResponseData.entries[i].poiRating;
            poi.price = poiListResponseData.entries[i].poiPrice;
            poi.latitude = poiListResponseData.entries[i].poiLatitude;
            poi.longitude = poiListResponseData.entries[i].poiLongitude;

            if(poiListResponseData.entries[i].assets != null)
            {
                poi.imageUrl = poiListResponseData.entries[i].assets[0].url;
                //Log.d("imageUrlAtIndividual", poi.imageUrl);
            }

            poiList.add(poi);
        }
        this.minBudget = minBudget;
        this.maxBudget = maxBudget;
        this.totalNight = totalNight;
        defaultGeneLength = totalNight * 3;
        genes = new Poi[defaultGeneLength];
        generateIndividual();
    }

    public void generateIndividual() {
        do {
            for (int i = 0; i < size(); i++) {
                int poiIndex = new Random().nextInt(poiList.size());

                genes[i] = poiList.get(poiIndex);
            }
        } while (priceHigherOrLowerThanBudget() || anyRedundant());
    }

    public Poi getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, Poi value) {
        genes[index] = value;
        fitness = 0;
    }

    public int size() {
        return genes.length;
    }

    public int getFitness() {
        fitness = 0;
        for(int i=0;i<defaultGeneLength;i++){
            fitness += (genes[i].rating * 1000000/genes[i].price);
        }
        return fitness;
    }

    public void mutate(){
        int changeIndex = new Random().nextInt(defaultGeneLength);

        do {
            int newPoiIndex = new Random().nextInt(poiList.size());
            genes[changeIndex] = poiList.get(newPoiIndex);
        } while (priceHigherOrLowerThanBudget() || anyRedundant());
    }

    public boolean priceHigherOrLowerThanBudget() {
        int totalPrice = 0;
        for (int i = 0; i < size(); i++) {
            totalPrice += genes[i].price;
        }
        return ((totalPrice - LENIENT_ADJUSTMENT > maxBudget) || totalPrice + LENIENT_ADJUSTMENT < minBudget);
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (int i = 0; i < size(); i++) {
            totalPrice += genes[i].price;
        }
        return (totalPrice);
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
            geneString += " " + getGene(i).id;
        }
        return geneString;
    }
}
