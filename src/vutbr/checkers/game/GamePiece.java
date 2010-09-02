package vutbr.checkers.game;

/**
 * Trida GamePiece - figurka
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public abstract class GamePiece {
    private Game.GameColor color;
    private GameBoard board;
    private int x;
    private int y;
    
    /**
     * Konstruktor
     *
     * @param board Hraci plocha
     * @param x Souradnice X
     * @param y Souradnice Y
     * @param color Barva figurky
     */
    public GamePiece(GameBoard board, int x, int y, Game.GameColor color) {
	setX(x);
	setY(y);
	setBoard(board);
	setColor(color);
	
	if (getBoard().getPiece(x, y) == null) {
	    getBoard().addPiece(x, y, this);
	} else {
	    getBoard().switchPiece(x, y, this);
	}
    }
    
    /**
     * Nastavi hraci plochu
     *
     * @param board Hraci plocha
     */
    private void setBoard(GameBoard board) {
	this.board = board;
    }
    
    /**
     * Vrati hraci plochu
     *
     * @return Hraci plocha
     */
    protected GameBoard getBoard() {
	return this.board;
    }
    
    /**
     * Nastavi souradnici X
     *
     * @param x Souradnice X
     */
    protected void setX(int x) {
	this.x = x;
    }
    
    /**
     * Vrati souradnici X
     *
     * @return Souradnice X
     */
    protected int getX() {
	return this.x;
    }
    
    /**
     * Nastavi souradnici Y
     *
     * @param y Souradnice Y
     */
    protected void setY(int y) {
	this.y = y;
    }
    
    /**
     * Vrati souradnici Y
     *
     * @return Souradnice Y
     */
    protected int getY() {
	return this.y;
    }
    
    /**
     * Nastavi barvu figurky
     *
     * @param color Barva figurky
     */
    protected void setColor(Game.GameColor color) {
	this.color = color;
    }
    
    /**
     * Vrati barvu figurky
     *
     * @return Barva figurky
     */
    public Game.GameColor getColor() {
	return this.color;
    }
    
    /**
     * Provede tah figurkou
     *
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     * @param gameTurnState Stav tahu hry
     * @return True pokud se tah povedl
     */
    protected abstract boolean turn(int toX, int toY,
	    GameTurnState gameTurnState);

    /**
     * Overi, zda je tento tah mozny
     *
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     * @return True pokud je tah mozny
     */
    protected boolean possibleTurn(int toX, int toY) {
	// No move
	if (getX() == toX && getY() == toY) {
	    return false;
	}
    
	// Out of board
	if (toX < 0 || toY < 0 || toX > 7 || toY > 7) {
	    return false;
	}
	// Only on black field
	if ((toX+toY) % 2 != 1) {
	    return false;
	}

	// The field where is jumped have to be empty
	if (getBoard().getPiece(toX, toY) != null) {
	    return false;
	}
	
	// Non-diagonal moves are disabled
	if (Math.abs(toX-getX()) != Math.abs(toY-getY())) {
    	    return false;
	}

	return true;
    }
}
