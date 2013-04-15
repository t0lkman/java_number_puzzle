package com.puzzle.main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JRadioButton;

/////////////////////////////////////////////////// class PuzzleGUI
// This class contains all the parts of the GUI interface
class PuzzleGUI extends JPanel {
    // =============================================== instance variables
    private GraphicsPanel _puzzleGraphics;
    private PuzzleController _puzzleCtrl = new PuzzleController(6, 1);
    private JLabel moveLabel;
    private JLabel timerLabel;
    private JLabel timeRemainLabel;
    private JPanel picturePanel = new JPanel();
    private JPanel timerPanel = new JPanel();
    private Timer timer;
    private JPanel sizePanel = new JPanel();
    private JPanel difficultyPanel = new JPanel();
    private JPanel emptyPanel2 = new JPanel();
    private JLabel gameTypeLabel = new JLabel("Select Game Type");
    private JButton newGameButton = new JButton("New Game");
    protected JButton showFinalImage = new JButton("Show Final Image");
    protected boolean showFlag = false;
    private JButton startButton = new JButton("Start Game");
    private int sizeSelection = 3;
    private int typSelection = 1; // 1 = Numbers, 2 = Images
    private int imageTypeSelection = 1; // 1 = dogs, 2 = architectures, 3 = cars, 4 = cartoon
    private int timeRemain = 7200;
    private long finalTime = 0;
    private boolean active = true;
    private JRadioButton dogs = new JRadioButton("Dogs", true);
    private JRadioButton architectures = new JRadioButton("Architecture", false);
    private JRadioButton cars = new JRadioButton("Cars", false);
    private JRadioButton cartoon = new JRadioButton("Cartoon", false);

    // end instance variables

    // ====================================================== constructor
    public PuzzleGUI() {
	// --- Create buttons. Add listeners to them.
	
        newGameButton.addActionListener(new NewGameAction());
        startButton.addActionListener(new StartButtonAction());
        showFinalImage.addActionListener(new ShowFinalImageButtonAction());
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
	JRadioButton numbers = new JRadioButton("Numbers", true);
	JRadioButton images = new JRadioButton("Pictures", false);
	ButtonGroup diffGroup = new ButtonGroup();
	diffGroup.add(numbers);
	diffGroup.add(images);
	numbers.setActionCommand("1");
	images.setActionCommand("2");
	numbers.addActionListener(new DifficultyAction());
	images.addActionListener(new DifficultyAction());
        ButtonGroup pictureGroup = new ButtonGroup();
        dogs.getModel().setEnabled(false);
        architectures.getModel().setEnabled(false);
        cars.getModel().setEnabled(false);
        cartoon.getModel().setEnabled(false);
        pictureGroup.add(dogs);
        pictureGroup.add(architectures);
        pictureGroup.add(cars);
        pictureGroup.add(cartoon);
        dogs.setActionCommand("1");
        architectures.setActionCommand("2");
        cars.setActionCommand("3");
        cartoon.setActionCommand("4");
        dogs.addActionListener(new ImageTypeAction());
        architectures.addActionListener(new ImageTypeAction());
        cars.addActionListener(new ImageTypeAction());
        cartoon.addActionListener(new ImageTypeAction());        
        
	// create timer to record time elapsed till when puzzle is solved.
	timer = new Timer();

	// --- Create control panel
	moveLabel = new JLabel("Moves:  " + _puzzleCtrl.getMoves());
	timerLabel = new JLabel("Elapsed Time: 00:00");
      //  timeRemainLabel = new JLabel("Time Remaining:");
	timer.schedule(new UpdateTime(), 0, 1000); 
        timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
        timerPanel.add(timerLabel);
        //timerPanel.add(timeRemainLabel); Why do we need to show it?
        timerPanel.add(moveLabel);
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        this.add(timerPanel);
        timerPanel.setVisible(false);
        
        c.ipadx = 0;
        c.ipady = 0;
        c.gridx = 1;
        c.gridy = 0;
        gameTypeLabel.setFont(new Font(gameTypeLabel.getFont().getName(),Font.BOLD,gameTypeLabel.getFont().getSize()));
        this.add(gameTypeLabel, c);
        
        c.insets = new Insets(0, 80, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.gridx = 2;
        c.gridy = 0;
        this.add(newGameButton, c);
        newGameButton.setVisible(false);
        
        
        c.insets = new Insets(0, 80, 0, 0);
        c.ipadx = 0;
        c.ipady = 1;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 1;
        this.add(showFinalImage, c);
        showFinalImage.setVisible(false);
        
        
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 3;
        difficultyPanel.setLayout(new GridLayout(3, 1));
        difficultyPanel.add(numbers);
	difficultyPanel.add(images);
        this.add(difficultyPanel, c); 

        c.insets = new Insets(0, 0, 0, 0);
	sizePanel.setLayout(new GridLayout(3,1));
	sizePanel.add(_3x3);
	sizePanel.add(_4x4);
	sizePanel.add(_5x5);
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
	this.add(sizePanel, c);
        

        
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 2;
        picturePanel.setLayout(new GridLayout(4,1));
        picturePanel.add(dogs);
        picturePanel.add(cars);
        picturePanel.add(architectures);
        picturePanel.add(cartoon);
        this.add(picturePanel, c);
        picturePanel.setVisible(true);
        
        c.gridx = 1;
        c.gridy = 3;
        c.ipady = 20;
        c.ipadx = 0;
        this.add(emptyPanel2, c);
        c.gridy = 4;
        c.insets = new Insets(0, 0, 0, 0);
        this.add(startButton, c);
        
	
	// --- Create graphics panel
	_puzzleGraphics = new GraphicsPanel();

	// --- Set the layout and add the components
	c.gridx = 0;
        c.gridy = 4;
        c.gridheight = 0;
        c.gridwidth = 0;
        c.ipady = 0;
	//this.add(controlPanel, BorderLayout.NORTH);
	this.add(_puzzleGraphics, c);
        _puzzleGraphics.setVisible(false);
    }// end constructor

    // ////////////////////////////////////////////// class GraphicsPanel
    // This is defined inside the outer class so that
    // it can use the outer class instance variables.
    class GraphicsPanel extends JPanel implements MouseListener {
	
	private int ROWS = 6;
	private int COLS = 6;

	private int CELL_SIZE = 80; // Pixels
	private Font _biggerFont;

	// ================================================== constructor
	public GraphicsPanel() {
	    _biggerFont = new Font("SansSerif", Font.BOLD, CELL_SIZE / 2);
	    this.setPreferredSize(new Dimension(CELL_SIZE * COLS + 4, CELL_SIZE * ROWS + 4));
	    this.setBackground(Color.black);
	    this.addMouseListener(this); // Listen own mouse events.
	}// end constructor

	// ======================================= method to set dimensions
	private void setSize(int size) {
	    ROWS = size;
	    COLS = size;
	    if (typSelection == 1) {
		CELL_SIZE = 480 / size;
	    }
	    else{
		CELL_SIZE = 480 / size +2;
	    }
	}

	// =======================================x method paintComponent
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    for (int r = 0; r < ROWS; r++) {
		for (int c = 0; c < COLS; c++) {
		    int x = c * CELL_SIZE;
		    int y = r * CELL_SIZE;
		    String text = _puzzleCtrl.getFace(r, c);
		    if (text != null) {
			if (typSelection == 1) {
				g.setColor(new Color(230,230,250));
				g.fill3DRect(x + 4 , y + 4, CELL_SIZE - 4, CELL_SIZE - 4, true);
				g.setColor(new Color(178,34,34));
				g.setFont(_biggerFont);
				if(this.COLS==3){
				    if(Integer.parseInt(text) < 10){
					g.drawString(text, x + (CELL_SIZE / 4) + 28, y + (3 * CELL_SIZE) / 4 - 25);
				    }
				    else{ // center double digit number differently
					g.drawString(text, x + (CELL_SIZE / 4) + 16, y + (3 * CELL_SIZE) / 4 - 25);
				    }
				}
				else if(this.COLS==4){
				    if(Integer.parseInt(text) < 10){
					g.drawString(text, x + (CELL_SIZE / 4) + 18, y + (3 * CELL_SIZE) / 4 - 15);
				    }
				    else{ // center double digit number differently
					g.drawString(text, x + (CELL_SIZE / 4) + 5, y + (3 * CELL_SIZE) / 4 - 15);
				    }
				}
				else{
				    if(Integer.parseInt(text) < 10){
					g.drawString(text, x + (CELL_SIZE / 4) + 14, y + (3 * CELL_SIZE) / 4 - 6);
				    }
				    else{ // center double digit number differently
					g.drawString(text, x + (CELL_SIZE / 4), y + (3 * CELL_SIZE) / 4 - 6);
				    }
				}				
			} else { // if selected typSelection==2 show images
			    BufferedImage myPicture = null;
			    String imagePath = "";
			    String titlesSize;
			    if(this.COLS==3){
				titlesSize = "3x3";
			    }
			    else if(this.COLS==4){
				titlesSize = "4x4"; 
			    }
			    else{
				titlesSize = "5x5"; 
			    }
			    if(imageTypeSelection == 1){
				imagePath = "images/dogs/" + titlesSize + "/dogs_" + text + ".jpg";
			    }
			    else if(imageTypeSelection == 2){
				imagePath = "images/architectures/" + titlesSize + "/architectures_" + text + ".jpg";
			    }
			    else if(imageTypeSelection == 3){
				imagePath = "images/cars/" + titlesSize + "/cars_" + text + ".jpg";
			    }
			    else {
				imagePath = "images/cartoon/" + titlesSize + "/cartoon_" + text + ".jpg";
			    }
			    try {
				myPicture = ImageIO.read(PuzzleGUI.class.getResource(imagePath));
				g.drawImage(myPicture, x, y , null);			
			    } catch (IOException ex) {
				System.out.println(ex.getMessage());
			    }

			}
		    }
		}
	    }// end paintComponent
	}
	public void showFinalImage(boolean showFlag){
	    Graphics g = this.getGraphics();
	    if(showFlag){
		showFinalImage.setText("Continue The Game");
    	    	BufferedImage myPicture = null;
    	    	String imagePath = "";	
    	    	if(imageTypeSelection == 1){
    	    	    imagePath = "images/dogs/dogs_hint.jpg";
    	    	}
    	    	else if(imageTypeSelection == 2){
    	    	    imagePath = "images/architectures/architectures_hint.jpg";
    	    	}
    	    	else if(imageTypeSelection == 3){
    	    	    imagePath = "images/cars/cars_hint.jpg";
    	    	}
    	    	else {
    	    	    imagePath = "images/cartoon/cartoon_hint.jpg";
    	    	}
    	    	try {
    	    	    myPicture = ImageIO.read(PuzzleGUI.class.getResource(imagePath));
    	    	    g.drawImage(myPicture, 0, 0, null);
    	   	} 
    	    	catch (IOException ex) {
    	   	    System.out.println(ex.getMessage());
    	   	}
	    }
	    else{
		showFinalImage.setText("Show Final Image");
		this.repaint();
	    }
	}	
	// ======================================== listener mousePressed
	public void mousePressed(MouseEvent e) {
	    // --- map x,y coordinates into a row and col.
	    int col = e.getX() / CELL_SIZE;
	    int row = e.getY() / CELL_SIZE;

	    if (!_puzzleCtrl.moveTile(row, col)) {
		// moveTile moves tile if legal, else returns false.
		Toolkit.getDefaultToolkit().beep();
	    }
	    // increments the label when a valid move is made
	    moveLabel.setText("Moves:  " + _puzzleCtrl.getMoves());
	    this.repaint(); // Show any updates to model.
            if(_puzzleCtrl.isGameOver()) {
                finalTime = _puzzleCtrl.getTime();
                long totalTime = _puzzleCtrl.getTime();
                long totalRemain = timeRemain - _puzzleCtrl.getTime();
                long minutes = totalTime / 60;
                long seconds = totalTime % 60;
                String message = "Congratulations you have won.\nYou used " + _puzzleCtrl.getMoves() + " moves.";
                if( minutes > 60 ) {
                    message = message + "\nYou used a total of " + minutes/60 + " hour, " + minutes%60 + " minutes, " + seconds + " seconds.";
                }
                else {
                    message = message + "\nYou used a total of " + minutes + " minutes, " + seconds + " seconds.";
                }
                minutes = totalRemain / 60;
                seconds = totalRemain % 60;
                if( minutes > 60 ) {
                    message = message + "\nYou had " + minutes/60 + " hour, " + minutes%60 + " minutes, " + seconds + " seconds remaining.";
                }
                else {
                    message = message + "\nYou had " + minutes + " minutes, " + seconds + " seconds remaining.";
                }
                int reply = JOptionPane.showConfirmDialog(null, message, "CONGRATULATIONS!!!", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null);
                if( reply == JOptionPane.OK_OPTION){
                    // The code bellow repeated like 4 times, maybe move to its own method?
        	    showFlag = false;
        	    showFinalImage.setText("Show Final Image");
        	    Main.setWindowSize(400,400);
        	    gameTypeLabel.setVisible(true);
                    sizePanel.setVisible(true);
                    difficultyPanel.setVisible(true);
                    startButton.setVisible(true);
                    picturePanel.setVisible(true);
                    newGameButton.setVisible(false);
                    showFinalImage.setVisible(false);
                    emptyPanel2.setVisible(true);
                    _puzzleGraphics.setVisible(false);
                    timerPanel.setVisible(false);
                }
            }
	}// end mousePressed

	// ========================================== ignore these events
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

    }// end class GraphicsPanel

    private class UpdateTime extends TimerTask {
	public void run() {
	    _puzzleCtrl.incTime();
	    long totalTime = _puzzleCtrl.getTime();
            long totalRemain = timeRemain - _puzzleCtrl.getTime();
	    int minutes = (int) (totalTime / 60);
	    int seconds = (int) (totalTime % 60);
            if( minutes == 3  && active) {
                int reply = JOptionPane.showConfirmDialog(null, "You did not complete the puzzle within the specified time limit.", 
                        "YOU HAVE LOST.", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null);
                if( reply == JOptionPane.OK_OPTION){
        	    showFlag = false;
        	    showFinalImage.setText("Show Final Image");
        	    Main.setWindowSize(400,400);
        	    gameTypeLabel.setVisible(true);
                    sizePanel.setVisible(true);
                    difficultyPanel.setVisible(true);
                    startButton.setVisible(true);
                    picturePanel.setVisible(true);
                    newGameButton.setVisible(false);
                    showFinalImage.setVisible(false);
                    emptyPanel2.setVisible(true);
                    _puzzleGraphics.setVisible(false);
                    timerPanel.setVisible(false);
                    active = false;
                }
            }
            timerLabel.setText(String.format("Elapsed Time: %02d:%02d:%02d", minutes/60, minutes%60, seconds));
            minutes = (int) (totalRemain / 60);
            seconds = (int) (totalRemain % 60);
//            if( minutes > 60 ) { Delete this block in future
//                timeRemainLabel.setText("Time Left:  " + minutes/60 + " hour, " + minutes%60 + " minutes, " + seconds + " seconds.");
//            }
//            else {
//                timeRemainLabel.setText("Time Left:  " + minutes + " minutes, " + seconds + " seconds.");
//            }
	}
    }

    // //////////////////////////////////////// inner class NewGameAction
    public class NewGameAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    showFlag = false;
	    showFinalImage.setText("Show Final Image");
	    Main.setWindowSize(400,400);
	    gameTypeLabel.setVisible(true);
            sizePanel.setVisible(true);
            difficultyPanel.setVisible(true);
            startButton.setVisible(true);
            picturePanel.setVisible(true);
            newGameButton.setVisible(false);
            showFinalImage.setVisible(false);
            emptyPanel2.setVisible(true);
            _puzzleGraphics.setVisible(false);
            timerPanel.setVisible(false);
	}
    }// end inner class NewGameAction

    public class ShowFinalImageButtonAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	     showFlag = showFlag ? false : true; 
	    _puzzleGraphics.showFinalImage(showFlag);
	}
    }

    
    public class StartButtonAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
            gameTypeLabel.setVisible(false);
            sizePanel.setVisible(false);
            difficultyPanel.setVisible(false);
            startButton.setVisible(false);
            newGameButton.setVisible(true);
            if(typSelection==1){
               showFinalImage.setVisible(false);
               Main.setWindowSize(500,550);
            }
            else if(typSelection==2){
                showFinalImage.setVisible(true);
                Main.setWindowSize(500,600);
             }            
            picturePanel.setVisible(false);
            emptyPanel2.setVisible(false);
            _puzzleGraphics.setVisible(true);
            _puzzleGraphics.setSize(sizeSelection);
            _puzzleCtrl = new PuzzleController(sizeSelection, typSelection);
            _puzzleCtrl.setWatch();
            moveLabel.setText("Moves:  " + _puzzleCtrl.getMoves());
            timerLabel.setText("Elapsed Time: 00:00:00");
            timerPanel.setVisible(true);
            _puzzleGraphics.repaint();
            active = true;
        }
    }
    // //////////////////////////////////////// inner class game size action
    // listener
    public class SizeAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    sizeSelection = Integer.parseInt(e.getActionCommand());
	}
    }

    // //////////////////////////////////////// inner class game image type selection action
    // listener
    public class ImageTypeAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    imageTypeSelection = Integer.parseInt(e.getActionCommand());
	}
    }

    
    // //////////////////////////////////////// inner class difficulty action
    // listener
    public class DifficultyAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    typSelection = Integer.parseInt(e.getActionCommand());
            if(typSelection == 2) {
                dogs.getModel().setEnabled(true);
                architectures.getModel().setEnabled(true);
                cars.getModel().setEnabled(true);
                cartoon.getModel().setEnabled(true);
            }
            else {
                dogs.getModel().setEnabled(false);
                architectures.getModel().setEnabled(false);
                cars.getModel().setEnabled(false);
                cartoon.getModel().setEnabled(false);
            }
	}
    }
}// end class PuzzleGUI