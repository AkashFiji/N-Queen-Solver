/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nqueen;

/**
 * @author akash
 * @author george
 */

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Display menu
            System.out.println("\nMenu:");
            System.out.println("1. Solver GUI");
            System.out.println("2. Empirical Testing");
            System.out.println("3. Race Algorithm");
            System.out.println("(Press 0 to exit)");
            System.out.print("\nEnter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear invalid input
            }
            choice = scanner.nextInt();
            System.out.print("\n");
            
            switch (choice) {
                case 1:
                    System.out.println("Launching Solver GUI...");
                    new SolverGUI();  // This will open the GUI directly
                    System.out.println("Plot window is open. Press Enter to close it.");
                    System.in.read(); // Wait for user input
                    break;

                case 2:
                    System.out.println("Running Empirical Testing...");
                    EmpiricalTesting empiricalTesting = new EmpiricalTesting();  // Create an instance of EmpiricalTesting
                    empiricalTesting.runTests();  // Call the non-static method
                    break;

                case 3:
                    System.out.println("Starting Algorithm Race...");

                    // Prompt for the number of queens
                    System.out.print("Enter the number of queens (N): ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Invalid input. Please enter a valid integer for N.");
                        scanner.next(); // Clear invalid input
                    }
                    int nQueens = scanner.nextInt();

                    // Validate the input for N
                    if (nQueens < 4) {
                        System.out.println("The number of queens should be 4 or greater.");
                        break;
                    }

                    // Instantiate and start the race
                    AlgorithmRace race = new AlgorithmRace("N-Queens Race: Backtracking vs Genetic Algorithm");
                    race.startRace(nQueens);

                    System.out.println("\nPlot window is open. Press Enter to close it.");
                    System.in.read(); // Wait for user input
                    break;

                case 0:
                    System.out.println("Exiting...");
                    System.exit(0);  // Exit the program
                    break;

                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        } while (choice != 0);

        scanner.close();
    }
}
