package com.yulius.belitungtrip.alogirthm;

import com.yulius.belitungtrip.entity.Poi;
import com.yulius.belitungtrip.response.PoiListResponseData;

import java.util.ArrayList;
import java.util.Random;

public class PoiIndividual {
    static int defaultGeneLength = 9;
    private Poi[] genes;
    private ArrayList<Poi> poiList;
    private int maxBudget;
    private int totalNight;
    private int fitness = 0;

    public PoiIndividual(int maxBudget, int totalNight, PoiListResponseData poiListResponseData) {
        poiList = new ArrayList<>();
        for(int i=0 ; i < poiListResponseData.entries.length;i++){
            Poi poi = new Poi();
            poi.id = poiListResponseData.entries[i].poiId;
            poi.name = poiListResponseData.entries[i].poiName;
            poi.rating = poiListResponseData.entries[i].poiRating;
            poi.price = poiListResponseData.entries[i].poiPrice;

            poiList.add(poi);
        }
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
        } while (priceHigherThanBudget() || anyRedundant());
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
            fitness += genes[i].rating;
        }
        return fitness;
    }

    public void mutate(){
        int changeIndex = new Random().nextInt(defaultGeneLength);

        do {
            int newPoiIndex = new Random().nextInt(poiList.size());
            genes[changeIndex] = poiList.get(newPoiIndex);
        } while (priceHigherThanBudget() || anyRedundant());
    }

    public boolean priceHigherThanBudget() {
        int totalPrice = 0;
        for (int i = 0; i < size(); i++) {
            totalPrice += genes[i].price;
        }
        return (totalPrice > maxBudget);
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
