class PuzzleController {
    private static final int ROWS = 6;
    private static final int COLS = 6;
    
    private Tile[][] _contents;  // All tiles.
    private Tile     _emptyTile; // The empty space.
    
    private int moves;
    private long timeElapsed;
    
    
    //================================================= constructor
    public PuzzleController() {
        _contents = new Tile[ROWS][COLS];
        reset();               // Initialize and shuffle tiles.
    }//end constructor
    
    
    //===================================================== getFace
    // Return the string to display at given row, col.
    String getFace(int row, int col) {
        return _contents[row][col].getFace();
    }//end getFace
    
    
    //======================================================= reset
    // Initialize and shuffle the tiles.
    public void reset() {
        moves = 0;
        setWatch();
        for (int r=0; r<ROWS; r++) {
            for (int c=0; c<COLS; c++) {
                _contents[r][c] = new Tile(r, c, "" + (r*COLS+c+1));
            }
        }
        //--- Set last tile face to null to mark empty space
        _emptyTile = _contents[ROWS-1][COLS-1];
        _emptyTile.setFace(null);
        
        //-- Shuffle - Exchange each tile with random tile.
        for (int r=0; r<ROWS; r++) {
            for (int c=0; c<COLS; c++) {
                exchangeTiles(r, c, (int)(Math.random()*ROWS)
                                  , (int)(Math.random()*COLS));
            }
        }
    }//end reset
    
    
    //==================================================== moveTile
    // Move a tile to empty position beside it, if possible.
    // Return true if it was moved, false if not legal.
    public boolean moveTile(int r, int c) {
        //--- It's a legal move if the empty cell is next to it.
        return checkEmpty(r, c, -1, 0) || checkEmpty(r, c, 1, 0)
            || checkEmpty(r, c, 0, -1) || checkEmpty(r, c, 0, 1);
    }//end moveTile
    
    //================================================== returns moves variable
    //Returns the moves variable.
    public int getMoves(){
        return moves;
    }

    //================================================== sets Stop Watch at zero
    public void setWatch(){
        timeElapsed = System.nanoTime() - System.nanoTime();
    }
    
    //================================================== returns value of the watch
    public long getTime(){
        return timeElapsed;
    }
    
    //================================================== increments the timeElapsed variable by one
    public void incTime(){
        timeElapsed++;
    }
    
    //================================================== checkEmpty
    // Check to see if there is an empty position beside tile.
    // Return true and exchange if possible, else return false.
    private boolean checkEmpty(int r, int c, int rdelta, int cdelta) {
        int rNeighbor = r + rdelta;
        int cNeighbor = c + cdelta;
        //--- Check to see if this neighbor is on board and is empty.
        if (isLegalRowCol(rNeighbor, cNeighbor) 
                  && _contents[rNeighbor][cNeighbor] == _emptyTile) {
            exchangeTiles(r, c, rNeighbor, cNeighbor);
            moves++;
            return true;
        }
        return false;
    }//end checkEmpty
    
    
    //=============================================== isLegalRowCol
    // Check for legal row, col
    public boolean isLegalRowCol(int r, int c) {
        return r>=0 && r<ROWS && c>=0 && c<COLS;
    }//end isLegalRowCol
    
    
    //=============================================== exchangeTiles
    // Exchange two tiles.
    private void exchangeTiles(int r1, int c1, int r2, int c2) {
        Tile temp = _contents[r1][c1];
        _contents[r1][c1] = _contents[r2][c2];
        _contents[r2][c2] = temp;
    }//end exchangeTiles
        
    
    //=================================================== isGameOver
    public boolean isGameOver() {
        for (int r=0; r<ROWS; r++) {
            for (int c=0; c<ROWS; c++) {
                Tile trc = _contents[r][c];
                return trc.isInFinalPosition(r, c);
            }
        }
        
        //--- Falling thru loop means nothing out of place.
        return true;
    }//end isGameOver
}//end class PuzzleController
    
    
    
////////////////////////////////////////////////////////// class Tile
// Represents the individual "tiles" that slide in puzzle.
class Tile {
    //============================================ instance variables
    private int _row;     // row of final position
    private int _col;     // col of final position
    private String _face;  // string to display 
    //end instance variables
    
    
    //==================================================== constructor
    public Tile(int row, int col, String face) {
        _row = row;
        _col = col;
        _face = face;
    }//end constructor
    
    
    //======================================================== setFace
    public void setFace(String newFace) {
        _face = newFace;
    }//end getFace
    
    
    //======================================================== getFace
    public String getFace() {
        return _face;
    }//end getFace
   
   
    //=============================================== isInFinalPosition
    public boolean isInFinalPosition(int r, int c) {
        return r==_row && c==_col;
    }//end isInFinalPosition
}//end class Tile