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
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class GraphGA extends JFrame {

    public GraphGA(String title) {
        super(title);

        // Create dataset
        XYSeries series = new XYSeries("Fitness (Non-attacking Queens) over NFC");
        for (int i = 0; i < GeneticAlgorithm.nfcValues.size(); i++) {
            series.add(GeneticAlgorithm.nfcValues.get(i), GeneticAlgorithm.fitnessValues.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Genetic Algorithm Performance",
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

    public static void main(String[] args) {
        // Prompt user for the number of queens
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of queens (N): ");
        int nQueens = scanner.nextInt();
        
        // Validate the input for N (typically N should be >= 4)
        if (nQueens < 4) {
            System.out.println("The number of queens should be 4 or greater.");
            return;
        }

        // Run the Genetic Algorithm for the input number of queens
        GeneticAlgorithm.runGeneticAlgorithm(nQueens);

        // Show the graph
        SwingUtilities.invokeLater(() -> {
            GraphGA example = new GraphGA("Genetic Algorithm Performance Chart");
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}