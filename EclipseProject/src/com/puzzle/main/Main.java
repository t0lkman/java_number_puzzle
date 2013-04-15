package com.puzzle.main;

import javax.swing.JFrame;

class Main {
    private static JFrame window;
    
    public static void main(String[] args) {
	window = new JFrame("Numbers Puzzle");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new PuzzleGUI());
        Main.setWindowSize(400,400);
        window.setVisible(true);
        window.setResizable(false);
    }
    public static void setWindowSize(int x, int y){
	Main.window.setSize(x, y);
	Main.window.setLocationRelativeTo(null);
    }


}
