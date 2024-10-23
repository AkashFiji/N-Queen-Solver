/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nqueen;

/**
 * @author akash
 * @author george
 */
import java.util.ArrayList;
import java.util.Arrays;

public class BackTrackingAlgorithm {

    static int n;  // Number of queens
    static int[] col;  // Column positions for queens in rows
    static boolean solutionFound = false;  // Flag to stop after first solution
    static int functionCalls = 0; // Counter for function calls

    // Lists to store NFC and fitness values for graphing
    static ArrayList<Integer> nfcValues = new ArrayList<>();
    static ArrayList<Integer> fitnessValues = new ArrayList<>();

    // Method to check if the current queen placement is promising (safe)
    public static boolean promising(int i) {
        for (int k = 1; k < i; k++) {
            if (col[k] == col[i] || Math.abs(col[i] - col[k]) == (i - k)) {
                return false;  // Conflict with another queen
            }
        }
        return true;  // No conflicts
    }

    // Recursive method to solve the N-Queens problem
    public static void queens(int i) {
        // Track fitness value every 50 function calls
        if (functionCalls % 50 == 0) {
            int queensSolved = solutionFound ? n : i; // Use n if solution found
            nfcValues.add(functionCalls);
            fitnessValues.add(queensSolved);
        }

        if (promising(i)) {
            if (i == n) {
                functionCalls++;
                // Convert 1-based index solution to 0-based and store it
                int[] solution = new int[n];
                for (int k = 1; k <= n; k++) {
                    solution[k - 1] = col[k] - 1;  // Convert 1-based to 0-based
                }
                System.out.println("Backtracking Solution: " + Arrays.toString(solution));
                solutionFound = true;  // Stop further recursion

                // Ensure that the fitness value doesn't drop after finding a solution
                if (!nfcValues.isEmpty()) {
                    int lastNFC = nfcValues.get(nfcValues.size() - 1);
                    if (lastNFC == functionCalls) {
                        fitnessValues.set(fitnessValues.size() - 1, n); // Set fitness to n
                    } else {
                        nfcValues.add(functionCalls);
                        fitnessValues.add(n); // Add new NFC and set fitness to n
                    }
                } else {
                    nfcValues.add(functionCalls);
                    fitnessValues.add(n); // First entry
                }

            } else {
                functionCalls++;
                // Try placing queens in each column of the next row
                for (int j = 1; j <= n; j++) {
                    col[i + 1] = j;  // Place queen in column j of row i+1
                    queens(i + 1);   // Recursively place next queen
                    if (solutionFound) {
                        return;  // Stop recursion once the first solution is found
                    }
                }
            }
        }
    }

    // Method to solve the N-Queens problem with a given number of queens
    public static void solveNQueens(int numberOfQueens) {
        functionCalls = 0;
        n = numberOfQueens;  // Set the number of queens
        col = new int[n + 1];  // Initialize the column array
        queens(0);  // Start solving from the 0th row

        // Check if no solution was found
        if (!solutionFound) {
            System.out.println("No solution found.");
        }
        nfcValues.add(functionCalls); // Record the final NFC value
    }
    
    public static int getFunctionCalls() {
        return functionCalls;
    }
}
