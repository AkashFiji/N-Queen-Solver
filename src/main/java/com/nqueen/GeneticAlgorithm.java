/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nqueen;

/**
 * @author akash
 * @author george
 */

import java.util.*;

public class GeneticAlgorithm {
    static int N; // Number of queens (dynamic based on input)
    static final int POPULATION_SIZE = 100;
    static final int MAX_GENERATIONS = 3000;
    static final double INITIAL_MUTATION_RATE = 0.01;
    static final int ELITISM_COUNT = 2;  // Preserve top 2 individuals each generation
    static boolean solutionFound = false;
    public static int functionCalls = 0; // To track the number of function calls
    public static List<Integer> nfcValues = new ArrayList<>();
    public static List<Integer> fitnessValues = new ArrayList<>();
    public static int queensSolved = 0;

    public static int[] runGeneticAlgorithm(int boardSize) {
        functionCalls = 0;
        N = boardSize;  // Set board size dynamically
        List<int[]> population = initializePopulation();
        int generation = 0;
        double mutationRate = INITIAL_MUTATION_RATE;
        int noImprovementGenerations = 0;
        int lastBestFitness = Integer.MAX_VALUE;

        while (generation < MAX_GENERATIONS && !solutionFound) {
            List<int[]> newPopulation = new ArrayList<>();
            int bestFitness = Integer.MAX_VALUE;
            int[] bestIndividual = null;

            functionCalls++;

            for (int[] individual : population) {
                int fitness = calculateFitness(individual);
                if (fitness < bestFitness) {
                    bestFitness = fitness;
                    bestIndividual = individual;
                }
                if (fitness == 0) { // Found a solution
                    solutionFound = true;
                    nfcValues.add(functionCalls);
                    fitnessValues.add(N - bestFitness);  // Record the final NFC and fitness
                    return bestIndividual;
                }
            }

            // Track progress every 100 NFCs
            if (functionCalls % 100 == 0) {
                int queensSolved = N - bestFitness; // Fitness indicates conflicts, N - conflicts gives queens solved
                nfcValues.add(functionCalls);  // Track NFCs every 100 calls
                fitnessValues.add(queensSolved);  // Track fitness (queens solved)
            }

            // Adjust mutation rate if there's no improvement in fitness
            if (bestFitness >= lastBestFitness) {
                noImprovementGenerations++;
                if (noImprovementGenerations > 50) {
                    mutationRate = Math.min(0.5, mutationRate + 0.05); // Increase mutation rate if no improvement
                }
            } else {
                noImprovementGenerations = 0;
                mutationRate = INITIAL_MUTATION_RATE;  // Reset mutation rate
            }

            lastBestFitness = bestFitness;

            // Elitism: Keep the top ELITISM_COUNT best individuals in the next generation
            population.sort(Comparator.comparingInt(GeneticAlgorithm::calculateFitness));
            for (int i = 0; i < ELITISM_COUNT; i++) {
                newPopulation.add(population.get(i));
            }

            // Generate the rest of the new population
            while (newPopulation.size() < POPULATION_SIZE) {
                int[] parent1 = tournamentSelection(population);
                int[] parent2 = tournamentSelection(population);
                int[] child = crossover(parent1, parent2);
                if (Math.random() < mutationRate) {
                    mutate(child);
                }
                newPopulation.add(child);
            }
            population = newPopulation;
            generation++;
        }

        // If no solution is found, add the final NFC and fitness values
        nfcValues.add(functionCalls);  // Ensure the final NFC is recorded
        fitnessValues.add(N - lastBestFitness);  // Record the final fitness (queens solved)

        return null;  // Return null if no solution found
    }

    static List<int[]> initializePopulation() {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[] individual = new int[N];
            for (int j = 0; j < N; j++) {
                individual[j] = (int) (Math.random() * N);  // Random row for each column
            }
            population.add(individual);
        }
        return population;
    }

    static int calculateFitness(int[] individual) {
        int conflicts = 0;
        for (int i = 0; i < individual.length; i++) {
            for (int j = i + 1; j < individual.length; j++) {
                if (individual[i] == individual[j] || Math.abs(individual[i] - individual[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    static int[] tournamentSelection(List<int[]> population) {
        Random rand = new Random();
        int tournamentSize = 10;
        List<int[]> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(population.get(rand.nextInt(POPULATION_SIZE)));
        }
        return Collections.min(tournament, Comparator.comparingInt(GeneticAlgorithm::calculateFitness));
    }

    static int[] crossover(int[] parent1, int[] parent2) {
        int crossoverPoint = (int) (Math.random() * N);
        int[] child = Arrays.copyOf(parent1, N);
        for (int i = crossoverPoint; i < N; i++) {
            child[i] = parent2[i];
        }
        return child;
    }

    static void mutate(int[] individual) {
        int index = (int) (Math.random() * N);
        int minConflicts = Integer.MAX_VALUE;
        int bestRow = individual[index];
        for (int row = 0; row < N; row++) {
            individual[index] = row;
            int conflicts = calculateFitness(individual);
            if (conflicts < minConflicts) {
                minConflicts = conflicts;
                bestRow = row;
            }
        }
        individual[index] = bestRow;
    }
}
