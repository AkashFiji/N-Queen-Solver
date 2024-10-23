/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nqueen;

/**
 * @author akash
 * @author george
 */

import java.awt.*;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.*;

public class SolverGUI {
        // Constructor that initializes the GUI
    public SolverGUI() throws IOException {
        JFrame frame = new JFrame("N-Queens Solver");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        DisplayPanel displayPanel = new DisplayPanel();
        ControlPanel controlPanel = new ControlPanel(displayPanel);

        frame.add(displayPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.WEST);

        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);


    }

    // DisplayPanel class for drawing the chessboard and queens
    public static class DisplayPanel extends JPanel {
        private int boardSize = 8;
        private int squareWidth;
        private final int width = 720;
        private final int height = 720;
        private boolean solved = false;
        private int[] queenPositions;
        private Image queenImage; // Image to represent the queen

        public DisplayPanel() {
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.DARK_GRAY);
            squareWidth = width / boardSize;

            // Load the queen image when initializing the panel
            queenImage = loadImage();
        }

        public void solveWithBacktracking(int boardSize) {
            this.boardSize = boardSize;
            squareWidth = width / boardSize;
            solved = true;

            BackTrackingAlgorithm.n = boardSize;
            BackTrackingAlgorithm.col = new int[boardSize + 1];
            BackTrackingAlgorithm.solutionFound = false;
            BackTrackingAlgorithm.queens(0);
    
            // Extract the solution from the BackTrackingAlgorithm class
            queenPositions = new int[boardSize];
            for (int i = 1; i <= boardSize; i++) {
                queenPositions[i - 1] = BackTrackingAlgorithm.col[i] - 1; // Convert 1-based index to 0-based
            }

            // Print the solution
           // System.out.println("Backtracking Solution: " + Arrays.toString(queenPositions));

            repaint();
        }

    public void solveWithGeneticAlgorithm(int boardSize) {

    this.boardSize = boardSize;
    squareWidth = width / boardSize;
    solved = true;

    // Reset any internal state of the genetic algorithm
    GeneticAlgorithm.solutionFound = false; // Reset solution found flag
    GeneticAlgorithm.functionCalls = 0; // Reset function call counter

    // Run the genetic algorithm and get the solution
    int[] solution = GeneticAlgorithm.runGeneticAlgorithm(boardSize);

    if (solution == null) {
        System.out.println("No Solutions found");
        JOptionPane.showMessageDialog(this, "No solution found.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    queenPositions = solution;
    System.out.println("Genetic Algorithm Solution: " + Arrays.toString(queenPositions));
    repaint();  // Repaint the GUI to show the solution


           
    }
        
        // Paint the chessboard and queens
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBoard(g);

            if (solved) {
                drawQueens(g);
            }
        }

        private void drawBoard(Graphics g) {
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    int x = j * squareWidth;
                    int y = i * squareWidth;
                    // Alternate colors based on the sum of row and column indices
                    g.setColor((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                    g.fillRect(x, y, squareWidth, squareWidth);
                }
            }
        }

        public Image loadImage() {
            // Read the queen.png file
            try {
                return ImageIO.read(this.getClass().getResource("queen.png"));
            } catch (IOException ex) {
                ex.printStackTrace(); // Print the stack trace for debugging
            }
            return null; // Return null if the image couldn't be loaded
        }
        public void resetBoard() {
    // Reset the board state
    this.solved = false;
    this.queenPositions = null; // Clear positions of queens
    System.out.println("Board Reset");
    repaint(); // Refresh the display
}


        private void drawQueens(Graphics g) {

      int offset = squareWidth / 8;
      int queenLength = squareWidth * 6 / 8;
      
            // Draw the queens at their respective positions
            if (queenPositions != null && queenImage != null) {
                for (int i = 0; i < boardSize; i++) {
                    // Ensure there is a queen to draw
                    if (queenPositions[i] != -1) {
                        // Draw the queen image
                        g.drawImage(queenImage, i * squareWidth + offset, queenPositions[i] * squareWidth + offset, queenLength, queenLength, null);
                    }
                }
            }
        }
    }

    public static class ControlPanel extends JPanel {
    private final DisplayPanel display;
    private final Font font = new Font("Tahoma", Font.BOLD, 24);
    private final JTextField boardInputField;
    private final String[] algorithmList = {"Backtracking", "Genetic Algorithm"};
    private final JComboBox<String> algorithmDropdown;
    private final JButton solveButton;
    private final JButton resetButton; // Reset button
    private final JPanel buttonPanel; // Panel to hold buttons

    public ControlPanel(DisplayPanel display) {
        this.display = display;
        setPreferredSize(new Dimension(300, 720));
        setBackground(Color.DARK_GRAY);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createEmptyBorder(150, 50, 150, 25));

        boardInputField = new JTextField(5); // Text field for input
        algorithmDropdown = new JComboBox<>(algorithmList);
        algorithmDropdown.setSelectedItem("Backtracking");

        JLabel sizeLabel = new JLabel("Enter Board Size (4-100):");
        JLabel algorithmLabel = new JLabel("Choose Algorithm:");

        // Initialize buttons
        solveButton = new JButton("Solve");
        resetButton = new JButton("Reset"); // Initialize reset button

        // Create a panel for buttons and set layout
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.add(solveButton);
        buttonPanel.add(resetButton); // Add reset button to the button panel

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAlgorithm = (String) algorithmDropdown.getSelectedItem();
                int boardSize = getBoardSize();
                if (boardSize >= 4 && boardSize <= 100) {
                    switch (selectedAlgorithm) {
                        case "Backtracking":
                            display.solveWithBacktracking(boardSize);
                            break;
                        case "Genetic Algorithm":
                            display.solveWithGeneticAlgorithm(boardSize);
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(display, "Please enter a valid board size between 4 and 100.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add action listener for reset button
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.resetBoard(); // Call reset method in DisplayPanel
            }
        });

        sizeLabel.setForeground(Color.WHITE);
        algorithmLabel.setForeground(Color.WHITE);

        // Add components to ControlPanel
        add(sizeLabel);
        add(boardInputField);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(algorithmLabel);
        add(algorithmDropdown);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(buttonPanel); // Add button panel with Solve and Reset buttons
    }

    public int getBoardSize() {
        try {
            return Integer.parseInt(boardInputField.getText());
        } catch (NumberFormatException e) {
            return -1;  // Invalid input
        }
    }
}
}
