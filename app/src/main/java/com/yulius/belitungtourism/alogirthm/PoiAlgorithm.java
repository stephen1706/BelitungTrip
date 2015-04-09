package com.yulius.belitungtourism.alogirthm;

import android.util.Log;

import com.yulius.belitungtourism.objectTransfer.PoiObjectTransfer;

public class PoiAlgorithm {
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.01;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    public static PoiPopulation evolvePoiPopulation(PoiPopulation pop) {
        PoiObjectTransfer rot = PoiObjectTransfer.getInstance();
        PoiPopulation newPoipopulation = new PoiPopulation(pop.size(), false, rot.maxBudget, rot.totalNight, rot.mPoiListResponseData);

        if (elitism) {
            newPoipopulation.saveIndividual(0, pop.getFittest());
        }

        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }

        for (int i = elitismOffset; i < pop.size(); i++) {
            PoiIndividual indiv1 = tournamentSelection(pop);
            PoiIndividual indiv2 = tournamentSelection(pop);
            PoiIndividual newIndiv = crossover(indiv1, indiv2);
            newPoipopulation.saveIndividual(i, newIndiv);
        }

        for (int i = elitismOffset; i < newPoipopulation.size(); i++) {
            mutate(newPoipopulation.getIndividual(i));
        }

        return newPoipopulation;
    }

    private static PoiIndividual crossover(PoiIndividual indiv1, PoiIndividual indiv2) {
        PoiObjectTransfer rot = PoiObjectTransfer.getInstance();
        PoiIndividual newSol = new PoiIndividual(rot.maxBudget, rot.totalNight, rot.mPoiListResponseData);
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

    private static void mutate(PoiIndividual indiv) {
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                Log.d("test algo", "mutate poi");
                indiv.mutate();
            }
        }
    }

    private static PoiIndividual tournamentSelection(PoiPopulation pop) {
        PoiObjectTransfer rot = PoiObjectTransfer.getInstance();

        PoiPopulation tournament = new PoiPopulation(tournamentSize, false, rot.maxBudget, rot.totalNight, rot.mPoiListResponseData);
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        PoiIndividual fittest = tournament.getFittest();
        return fittest;
    }
}
