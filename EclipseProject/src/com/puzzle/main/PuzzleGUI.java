package com.puzzle.main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JRadioButton;
import java.util.Random;
import javax.swing.JOptionPane;
import java.util.Arrays;

/////////////////////////////////////////////////// class PuzzleGUI
// This class contains all the parts of the GUI interface
class PuzzleGUI extends JPanel {
    //=============================================== instance variables
    private GraphicsPanel    _puzzleGraphics;
    private PuzzleController _puzzleCtrl = new PuzzleController();
    private JLabel moveLabel;
    private JLabel timerLabel;
    private Timer timer;
    private int sizeSelection = 3;
    private int diffSelection = 1;  //1 = Easy, 2 = Medium, 3 = Hard
    //end instance variables

    //====================================================== constructor
    public PuzzleGUI() {
        //--- Create buttons.  Add listeners to them.
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new NewGameAction());
        JRadioButton _3x3 = new JRadioButton("3 x 3", true);
        JRadioButton _4x4 = new JRadioButton("4 x 4", false);
        JRadioButton _5x5 = new JRadioButton("5 x 5", false);
        ButtonGroup sizeGroup = new ButtonGroup();
        sizeGroup.add(_3x3);
        sizeGroup.add(_4x4);
        sizeGroup.add(_5x5);
        _3x3.setActionCommand("3");
        _4x4.setActionCommand("4");
        _5x5.setActionCommand("5");
        _3x3.addActionListener(new SizeAction());
        _4x4.addActionListener(new SizeAction());
        _5x5.addActionListener(new SizeAction());
        JRadioButton easy = new JRadioButton("Easy", true);
        JRadioButton medium = new JRadioButton("Medium", false);
        JRadioButton hard = new JRadioButton("Hard", false);
        ButtonGroup diffGroup = new ButtonGroup();
        diffGroup.add(easy);
        diffGroup.add(medium);
        diffGroup.add(hard);
        easy.setActionCommand("1");
        medium.setActionCommand("2");
        hard.setActionCommand("3");
        easy.addActionListener(new DifficultyAction());
        medium.addActionListener(new DifficultyAction());
        hard.addActionListener(new DifficultyAction());
        hard.addActionListener(new DifficultyAction());*/
        
        //create timer to record time elapsed till when puzzle is solved.
        timer = new Timer();

        //--- Create control panel
        JPanel controlPanel = new JPanel();
        JPanel sizePanel = new JPanel();
        JPanel difficultyPanel = new JPanel();
              
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(newGameButton);
        sizePanel.setLayout(new GridLayout(3,1));
        difficultyPanel.setLayout(new GridLayout());
        sizePanel.add(_3x3);
        sizePanel.add(_4x4);
        sizePanel.add(_5x5);
        controlPanel.add(sizePanel);
        difficultyPanel.setLayout(new GridLayout(3,1));
        difficultyPanel.add(easy);
        difficultyPanel.add(medium);
        difficultyPanel.add(hard);
        controlPanel.add(difficultyPanel);
        
        
        moveLabel = new JLabel("Moves:  " + _puzzleCtrl.getMoves());
        timerLabel = new JLabel("Elapsed Time:  " + _puzzleCtrl.getTime());
        timer.schedule(new UpdateTime(), 0, 1000);
        //--- Create graphics panel
        _puzzleGraphics = new GraphicsPanel();
        
        //--- Set the layout and add the components
        this.setLayout(new BorderLayout());
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(_puzzleGraphics, BorderLayout.CENTER);
    }//end constructor


    //////////////////////////////////////////////// class GraphicsPanel
    // This is defined inside the outer class so that
    // it can use the outer class instance variables.
    class GraphicsPanel extends JPanel implements MouseListener {
        private int ROWS = 6;
        private int COLS = 6;
        
        private int CELL_SIZE = 80; // Pixels
        private Font _biggerFont;
        
        
        //================================================== constructor
        public GraphicsPanel() {
            _biggerFont = new Font("SansSerif", Font.BOLD, CELL_SIZE/2);
            this.setPreferredSize(
                   new Dimension(CELL_SIZE * COLS, CELL_SIZE*ROWS));
            this.setBackground(Color.black);
            this.addMouseListener(this);  // Listen own mouse events.
        }//end constructor
        
        //=======================================x method paintComponent
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //fill board for easy difficulty
            if(diffSelection == 1 ){
                for (int r=0; r<ROWS; r++) {
                    for (int c=0; c<COLS; c++) {
                        int x = c * CELL_SIZE;
                        int y = r * CELL_SIZE;
                        String text = _puzzleCtrl.getFace(r, c);
                        if (text != null) {
                            g.setColor(Color.gray);
                            g.fillRect(x+2, y+2, CELL_SIZE-4, CELL_SIZE-4);
                            g.setColor(Color.black);
                            g.setFont(_biggerFont);
                            g.drawString(text, x+20, y+(3*CELL_SIZE)/4);
                        }
                    }
                }
            }
            //fill board for medium difficulty
            if(diffSelection == 2){
                int[] numbers = new int[ROWS * COLS - 1];
                boolean check = true;
                Random randomGenerator = new Random();
                //method to fill board with sorted random numbers from 1 to 100, and ensure there are no duplicates
                for(int t = 0; t < (ROWS * COLS) - 1; t++ ){
                    int tempRandom = randomGenerator.nextInt(100) + 1;
                    if(t > 0){
                        do{
                            check = true;
                            for(int i = 0; i < numbers.length; i++){
                                if(tempRandom == numbers[i]){
                                    check = false;
                                }
                            }
                            if(!check) {
                                tempRandom = randomGenerator.nextInt(100) + 1;
                            }
                        } while(!check); 
                    }
                    numbers[t] = tempRandom;
                }
                Arrays.sort(numbers);
                for (int r = 0; r<ROWS; r++){
                    for (int c = 0; c<COLS; c++){
                        int x = c * CELL_SIZE;
                        int y = r * CELL_SIZE;
                        String text;
                        if((r != ROWS-1) && (c != COLS-1)){
                            text = Integer.toString(numbers[r*ROWS + c]);
                        }
                        else {
                            text = _puzzleCtrl.getFace(r, c);
                        }
                        if( text != null ){
                            g.setColor(Color.gray);
                            g.fillRect(x+2, y+2, CELL_SIZE-4, CELL_SIZE-4);
                            g.setColor(Color.black);
                            g.setFont(_biggerFont);
                            g.drawString(text, x+20, y+(3*CELL_SIZE)/4);
                        }
		}
            }
        }//end paintComponent
        
        //======================================== listener mousePressed
        public void mousePressed(MouseEvent e) {
            //--- map x,y coordinates into a row and col.
            int col = e.getX()/CELL_SIZE;
            int row = e.getY()/CELL_SIZE;
            
            if (!_puzzleCtrl.moveTile(row, col)) {
                // moveTile moves tile if legal, else returns false.
                Toolkit.getDefaultToolkit().beep();
            }
            //increments the label when a valid move is made
            moveLabel.setText("Moves:  " + _puzzleCtrl.getMoves());
            this.repaint();  // Show any updates to model.
        }//end mousePressed
        
        
        //========================================== ignore these events
        public void mouseClicked (MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered (MouseEvent e) {}
        public void mouseExited  (MouseEvent e) {}
    }//end class GraphicsPanel
    
    private class UpdateTime extends TimerTask {
        public void run(){
            _puzzleCtrl.incTime();
            long totalTime = _puzzleCtrl.getTime();
            long minutes = totalTime / 60;
            long seconds = totalTime % 60;
            timerLabel.setText("Elapsed Time:  " + minutes + " minutes, " + seconds + " seconds.");
        }
    }
    
    ////////////////////////////////////////// inner class NewGameAction
    public class NewGameAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            _puzzleCtrl.reset();
            //reset move counter
            moveLabel.setText("Moves:  " + _puzzleCtrl.getMoves());
            _puzzleGraphics.repaint();
        }
    }//end inner class NewGameAction
    
    ////////////////////////////////////////// inner class game size action listener
    public class SizeAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
                sizeSelection = Integer.parseInt(e.getActionCommand());
        }
    }
    
    ////////////////////////////////////////// inner class difficuly action listener
    public class DifficultyAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
             diffSelection = Integer.parseInt(e.getActionCommand());
        }
    }
}//end class PuzzleGUI