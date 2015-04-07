package com.yulius.belitungtrip.alogirthm;

public class FitnessCalculation {

    static int solution;

    // Set a candidate solution as a byte array
    public static void setSolution(int newSolution) {
        solution = newSolution;
    }

    // To make it easier we can use this method to set our candidate solution
    // with string of 0s and 1s
//    public static void setSolution(int maxSolution) {
//        solution = maxSolution;
//        // Loop through each character of our string and save it in our byte
//        // array
//        for (int i = 0; i < newSolution.length(); i++) {
//            String character = newSolution.substring(i, i + 1);
//            if (character.contains("0") || character.contains("1")) {
//                solution[i] = Byte.parseByte(character);
//            } else {
//                solution[i] = 0;
//            }
//        }
//    }

    // Calculate inidividuals fittness by comparing it to our candidate solution
    public static int getFitness(Individual individual) {
        int fitness = 0;
        // Loop through our individuals genes and compare them to our cadidates
        for (int i = 0; i < individual.size(); i++) {
//            if (individual.getGene(i) == solution[i]) {
//                fitness++;
//            }
            fitness += individual.getFitness();
        }
        return fitness;
    }

    // Get optimum fitness
    public static int getMaxFitness() {
        int maxFitness = solution;
        return maxFitness;
    }
}