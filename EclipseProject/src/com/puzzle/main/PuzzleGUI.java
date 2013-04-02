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
    private JPanel picturePanel = new JPanel();
    private JPanel emptyPanel = new JPanel();
    private Timer timer;
    private JPanel controlPanel = new JPanel();
    private JPanel sizePanel = new JPanel();
    private JPanel difficultyPanel = new JPanel();
    private JPanel emptyPanel2 = new JPanel();
    private JLabel gameTypeLabel = new JLabel("Select Game Type");
    private JButton newGameButton = new JButton("New Game");
    private JButton startButton = new JButton("Start Game");
    private int sizeSelection = 3;
    private int diffSelection = 1; // 1 = Easy, 2 = Medium, 3 = Hard

    // end instance variables

    // ====================================================== constructor
    public PuzzleGUI() {
	// --- Create buttons. Add listeners to them.
	
        newGameButton.addActionListener(new NewGameAction());
        startButton.addActionListener(new StartButtonAction());
        
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
	hard.addActionListener(new DifficultyAction());
        JRadioButton ocean = new JRadioButton("Ocean", true);
        JRadioButton dogs = new JRadioButton("Dogs", false);
        JRadioButton cars = new JRadioButton("Cars", false);
        JRadioButton space = new JRadioButton("Space", false);
        ButtonGroup pictureGroup = new ButtonGroup();
        pictureGroup.add(ocean);
        pictureGroup.add(dogs);
        pictureGroup.add(cars);
        pictureGroup.add(space);
        ocean.setActionCommand("1");
        dogs.setActionCommand("2");
        cars.setActionCommand("3");
        space.setActionCommand("4");
        
	// create timer to record time elapsed till when puzzle is solved.
	timer = new Timer();

	// --- Create control panel
	

	controlPanel.setLayout(new FlowLayout());
	controlPanel.add(newGameButton);
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 100;
        c.ipady = 20;
        c.insets = new Insets(0, 260, 0, 0);
        c.gridwidth = 3;
        this.add(gameTypeLabel, c);
        c.insets = new Insets(0, 150, 0, 0);
        c.gridwidth = 1;
        this.add(newGameButton, c);
        newGameButton.setVisible(false);
        
        c.insets = new Insets(0, 50, 0, 0);
        c.ipady = 0;
	sizePanel.setLayout(new GridLayout(3,1));
	sizePanel.add(_3x3);
	sizePanel.add(_4x4);
	sizePanel.add(_5x5);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
	this.add(sizePanel, c);
        
        c.insets = new Insets(0, 100, 0, 0);
        c.gridx = 1;
        difficultyPanel.setLayout(new GridLayout(3, 1));
        difficultyPanel.add(easy);
	difficultyPanel.add(medium);
	difficultyPanel.add(hard);
        this.add(difficultyPanel, c);     
        
        c.gridx = 2;
        this.add(emptyPanel, c);
        
        c.insets = new Insets(0, 50, 0, 0);
        c.gridx = 2;
        picturePanel.setLayout(new GridLayout(4,1));
        picturePanel.add(ocean);
        picturePanel.add(cars);
        picturePanel.add(dogs);
        picturePanel.add(space);
        this.add(picturePanel, c);
        picturePanel.setVisible(false);
        
        c.gridx = 1;
        c.gridy = 3;
        c.ipady = 20;
        c.ipadx = 0;
        this.add(emptyPanel2, c);
        c.gridy = 4;
        c.gridheight = 2;
        c.insets = new Insets(0, 0, 0, 0);
        this.add(startButton, c);
        
	moveLabel = new JLabel("Moves:  " + _puzzleCtrl.getMoves());
	timerLabel = new JLabel("Elapsed Time:  " + _puzzleCtrl.getTime());
	timer.schedule(new UpdateTime(), 0, 1000);
	// --- Create graphics panel
	_puzzleGraphics = new GraphicsPanel();

	// --- Set the layout and add the components
	c.gridx = 0;
        c.gridy = 3;
        c.gridheight = 5;
        c.gridwidth = 2;
        c.insets = new Insets(0,0,0,0);
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
	    this.setPreferredSize(new Dimension(CELL_SIZE * COLS, CELL_SIZE * ROWS));
	    this.setBackground(Color.black);
	    this.addMouseListener(this); // Listen own mouse events.
	}// end constructor

	// ======================================= method to set dimensions
	private void setSize(int size) {
	    ROWS = size;
	    COLS = size;
	    CELL_SIZE = 480 / size;
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
			if (diffSelection != 3) {
				//g.setColor(Color.gray);
				g.setColor(new Color(230,230,250));
				//g.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
				g.fill3DRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4, true);
				//g.setColor(Color.black);
				g.setColor(new Color(178,34,34));
			    g.setFont(_biggerFont);
			    if (Integer.parseInt(text) == 100) {
				g.drawString(text, x + (CELL_SIZE / 4) - 10, y + (3 * CELL_SIZE) / 4);
			    } else {
				g.drawString(text, x + (CELL_SIZE / 4), y + (3 * CELL_SIZE) / 4);
			    }
			} else { // if selected Hard==3 show images
			    BufferedImage myPicture = null;
			    String imagePath = "images/MickeyMouse_" + text + ".jpg";
			    g.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
			    try {
				myPicture = ImageIO.read(PuzzleGUI.class.getResource(imagePath));
				g.drawImage(myPicture, x + 2, y + 2, null);
			    } catch (IOException ex) {
				System.out.println(ex.getMessage());
			    }

			}
		    }
		}
	    }// end paintComponent
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
                int reply = JOptionPane.showConfirmDialog(null, "Congratulations you have won.", "CONGRATULATIONS!!!", JOptionPane.OK_OPTION);
                if( reply == JOptionPane.OK_OPTION){
                    gameTypeLabel.setVisible(true);
                    sizePanel.setVisible(true);
                    difficultyPanel.setVisible(true);
                    startButton.setVisible(true);
                    newGameButton.setVisible(false);
                    emptyPanel2.setVisible(true);
                    _puzzleGraphics.setVisible(false);
                }
            };
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
	    long minutes = totalTime / 60;
	    long seconds = totalTime % 60;
	    timerLabel.setText("Elapsed Time:  " + minutes + " minutes, " + seconds + " seconds.");
	}
    }

    // //////////////////////////////////////// inner class NewGameAction
    public class NewGameAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    gameTypeLabel.setVisible(true);
            sizePanel.setVisible(true);
            difficultyPanel.setVisible(true);
            startButton.setVisible(true);
            newGameButton.setVisible(false);
            emptyPanel2.setVisible(true);
            _puzzleGraphics.setVisible(false);
	}
    }// end inner class NewGameAction

    public class StartButtonAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gameTypeLabel.setVisible(false);
            sizePanel.setVisible(false);
            difficultyPanel.setVisible(false);
            startButton.setVisible(false);
            newGameButton.setVisible(true);
            picturePanel.setVisible(false);
            emptyPanel2.setVisible(false);
            _puzzleGraphics.setVisible(true);
            _puzzleGraphics.setSize(sizeSelection);
            _puzzleCtrl = new PuzzleController(sizeSelection, diffSelection);
            moveLabel.setText("Moves:  " + _puzzleCtrl.getMoves());
            _puzzleGraphics.repaint();
        }
    }
    // //////////////////////////////////////// inner class game size action
    // listener
    public class SizeAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    sizeSelection = Integer.parseInt(e.getActionCommand());
	}
    }

    // //////////////////////////////////////// inner class difficuly action
    // listener
    public class DifficultyAction implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    diffSelection = Integer.parseInt(e.getActionCommand());
            if(diffSelection == 3) {
                emptyPanel.setVisible(false);
                picturePanel.setVisible(true);
            }
            else {
                picturePanel.setVisible(false);
                emptyPanel.setVisible(true);
            }
	}
    }
}// end class PuzzleGUI