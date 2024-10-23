/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nqueen;

/**
 * @author akash
 * @author george
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.*;
import javax.swing.*;
import java.awt.*;


public class AlgorithmRace extends JFrame {
    private XYSeries btSeries;
    private XYSeries gaSeries;

    public AlgorithmRace(String title) {
        super(title);
        btSeries = new XYSeries("Backtracking Algorithm");
        gaSeries = new XYSeries("Genetic Algorithm");

        // Create dataset
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(btSeries);
        dataset.addSeries(gaSeries);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "N-Queens Algorithm Performance",
                "Number of Function Calls (NFC)",
                "Fitness (Non-attacking Queens)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Customize chart
        chart.setBackgroundPaint(Color.white);
        chart.getPlot().setBackgroundPaint(Color.lightGray);

        // Add chart to a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    public void updateBacktrackingData() {
        // Ensure both lists are of equal size
        int minSize = Math.min(BackTrackingAlgorithm.nfcValues.size(), BackTrackingAlgorithm.fitnessValues.size());

        // Loop using the smaller size
        for (int i = 0; i < minSize; i++) {
            int nfc = BackTrackingAlgorithm.nfcValues.get(i);
            int queensSolved = BackTrackingAlgorithm.fitnessValues.get(i);
            btSeries.add(nfc, queensSolved);  // Assuming btSeries is a valid JFreeChart series
        }
    }

    public void updateGeneticAlgorithmData() {
        // Ensure both lists are of equal size
        int minSize = Math.min(GeneticAlgorithm.nfcValues.size(), GeneticAlgorithm.fitnessValues.size());

        // Loop using the smaller size
        for (int i = 0; i < minSize; i++) {
            int nfc = GeneticAlgorithm.nfcValues.get(i);
            int queensSolved = GeneticAlgorithm.fitnessValues.get(i);
            gaSeries.add(nfc, queensSolved);  // Assuming gaSeries is a valid JFreeChart series
        }
    }

    public void startRace(int nQueens) {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        // Create threads for both algorithms
        Thread backtrackingThread = new Thread(() -> {
            BackTrackingAlgorithm.solveNQueens(nQueens);
            updateBacktrackingData();  // Update graph with backtracking data

            // Ensure NFC values are updated at the end if missing
            if (BackTrackingAlgorithm.nfcValues.isEmpty()) {
                BackTrackingAlgorithm.nfcValues.add(BackTrackingAlgorithm.functionCalls);
            }
        });

        Thread geneticThread = new Thread(() -> {
            int[] gaSolution = GeneticAlgorithm.runGeneticAlgorithm(nQueens);
            updateGeneticAlgorithmData();  // Update graph with genetic algorithm data

            // Display GA solution or failure
            if (gaSolution != null) {
                System.out.println("Genetic Algorithm Solution: " + Arrays.toString(gaSolution));  // Print solution in [1, 2, 3, 4] format
            } else {
                System.out.println("GA failed to provide a solution.");
            }

            // Ensure NFC values are updated at the end if missing
            if (GeneticAlgorithm.nfcValues.isEmpty()) {
                GeneticAlgorithm.nfcValues.add(GeneticAlgorithm.functionCalls);
            }
        });

        // Start both algorithms
        backtrackingThread.start();
        geneticThread.start();

        // Wait for both threads to finish
        try {
            backtrackingThread.join();
            geneticThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if both nfcValues lists have data before accessing them
        if (!BackTrackingAlgorithm.nfcValues.isEmpty() && !GeneticAlgorithm.nfcValues.isEmpty()) {
            int backtrackingNFC = BackTrackingAlgorithm.nfcValues.get(BackTrackingAlgorithm.nfcValues.size() - 1);
            int geneticNFC = GeneticAlgorithm.nfcValues.get(GeneticAlgorithm.nfcValues.size() - 1);

            if (backtrackingNFC < geneticNFC) {
                System.out.println("Backtracking Algorithm finished first with " + backtrackingNFC + " function calls.");
            } else if (geneticNFC < backtrackingNFC) {
                System.out.println("Genetic Algorithm finished first with " + geneticNFC + " function calls.");
            } else {
                System.out.println("Both algorithms finished with the same number of function calls: " + backtrackingNFC);
            }
        } else {
            System.out.println("Error: One or both algorithms did not produce any function call data.");
        }

        // Refresh the chart to show the updated data
        repaint();
    }
}
