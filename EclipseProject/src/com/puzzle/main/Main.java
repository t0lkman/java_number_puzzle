package com.puzzle.main;

import javax.swing.JFrame;

class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Numbers Puzzle");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new PuzzleGUI());
        window.pack();
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
    }
}
