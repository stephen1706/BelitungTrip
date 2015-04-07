package com.yulius.belitungtrip.alogirthm;

import com.yulius.belitungtrip.objectTransfer.PoiObjectTransfer;
import com.yulius.belitungtrip.response.PoiListResponseData;

public class PoiPopulation {
    private static PoiListResponseData mPoiListResponseData;
    PoiIndividual[] individuals;

    public PoiPopulation(int populationSize, boolean initialise, int maxBudget, int totalNight, PoiListResponseData poiListResponseData) {
        individuals = new PoiIndividual[populationSize];
        mPoiListResponseData = poiListResponseData;
        PoiObjectTransfer rot = PoiObjectTransfer.getInstance();

        rot.maxBudget = maxBudget;
        rot.totalNight = totalNight;
        rot.mPoiListResponseData = poiListResponseData;

        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < size(); i++) {
                PoiIndividual newIndividual = new PoiIndividual(maxBudget, totalNight, mPoiListResponseData);
                newIndividual.generateIndividual();
                saveIndividual(i, newIndividual);
            }
        }
    }

    public PoiIndividual getIndividual(int index) {
        return individuals[index];
    }

    public PoiIndividual getFittest() {
        PoiIndividual fittest = individuals[0];
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

    public void saveIndividual(int index, PoiIndividual indiv) {
        individuals[index] = indiv;
    }
}
