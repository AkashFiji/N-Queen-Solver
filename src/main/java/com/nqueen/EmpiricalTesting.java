/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nqueen;

/**
 * @author akash
 * @author george
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class EmpiricalTesting {

    public void runTests() {
        int[] testSizes = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int runs = 30; 

        // Create a CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("nqueens_EmphiricalResults.csv"))) {
            writer.write("Algorithm,Board Size,Best NFC,Mean NFC,Worst NFC,Total NFC,Success Rate\n");

            System.out.println("\n==========================");
            System.out.println("Genetic Algorithm");
            System.out.println("==========================");
            for (int boardSize : testSizes) {
                System.out.println("-------------------------------");
                System.out.println("Testing Board Size for GA = " + boardSize);
                System.out.println("-------------------------------");
                // Run Genetic Algorithm
                String gaResults = runGeneticAlgorithm(boardSize, runs);
                writer.write(gaResults + "\n"); // Save results to CSV
            }

            System.out.println("\n=========================================");
            System.out.println("Backtracking Algorithm");
            System.out.println("=========================================");
            for (int boardSize : testSizes) {
                System.out.println("-----------------------------------------");
                System.out.println("Testing Board Size for BT = " + boardSize);
                System.out.println("-----------------------------------------");
                // Run Backtracking Algorithm once
                String btResults = runBacktrackingAlgorithm(boardSize);
                writer.write(btResults + "\n"); // Save results to CSV
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("================================");
        System.out.println("'nqueens_EmphiricalResults.csv' Is Saved 'N-Queen-CS214/' Directory");
    }

    private static String runBacktrackingAlgorithm(int boardSize) {
        System.out.println("\nRunning Backtracking Algorithm");
        BackTrackingAlgorithm.functionCalls = 0; // Reset call counter
        BackTrackingAlgorithm.solutionFound = false; // Reset solution flag
        
        BackTrackingAlgorithm.solveNQueens(boardSize);
        int btFunctionCalls = BackTrackingAlgorithm.functionCalls;
        
        int BTSuccessCount = BackTrackingAlgorithm.solutionFound ? 1 : 0;

        // Prepare stats
        return String.format("Backtracking,%d,%d,%d,%d,%d,%.2f", boardSize, btFunctionCalls, btFunctionCalls, btFunctionCalls, btFunctionCalls, BTSuccessCount * 100.0);
    }

    private static String runGeneticAlgorithm(int boardSize, int numRuns) {
        long gaBestCalls = Integer.MAX_VALUE;
        long gaWorstCalls = Integer.MIN_VALUE;
        long gaTotalCalls = 0;
        int GAsuccessCount = 0;

        for (int i = 0; i < numRuns; i++) {
            System.out.println("\nGenetic Algorithm run: " + (i + 1));
            GeneticAlgorithm.solutionFound = false; // Reset the solution found flag
            GeneticAlgorithm.functionCalls = 0; // Reset function calls

            int[] result = GeneticAlgorithm.runGeneticAlgorithm(boardSize);
            long functionCalls = GeneticAlgorithm.functionCalls;
            
            if (result != null) {
                GAsuccessCount++;
                // System.out.println("Solution found in Run " + (i + 1));
            }
            
            System.out.println("Genetic Algorithm Solution: " + Arrays.toString(result));
            System.out.println("Function Calls in Run " + (i + 1) + ": " + functionCalls +"\n");

            gaBestCalls = Math.min(gaBestCalls, functionCalls);
            gaWorstCalls = Math.max(gaWorstCalls, functionCalls);
            gaTotalCalls += functionCalls;
        }

        double gaMeanCalls = (double) gaTotalCalls / numRuns;
        double GAsuccessRate = (double) GAsuccessCount / numRuns * 100;

        // Return results as a CSV line
        return String.format("Genetic Algorithm,%d,%d,%.2f,%d,%d,%.2f", boardSize, gaBestCalls, gaMeanCalls, gaWorstCalls, gaTotalCalls, GAsuccessRate);
    }
}
