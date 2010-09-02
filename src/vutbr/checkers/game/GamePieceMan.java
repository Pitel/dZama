package vutbr.checkers.game;

/**
 * Trida GamePieceMan - figurka pesce
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class GamePieceMan extends GamePiece {
    /**
     * Konstruktur
     *
     * @param board Hraci plocha
     * @param x Souradnice X
     * @param y Souradnice Y
     * @param color Barva figurky
     */
    public GamePieceMan(GameBoard board, int x, int y, Game.GameColor color) {
	super(board, x, y, color);
    }
    
    /**
     * Provede tah pescem
     *
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     * @param gameTurnState Stav tahu hry
     * @return True pokud se tah povedl
     */    
    protected boolean turn(int toX, int toY, GameTurnState gameTurnState) {
	if (possibleTurn(toX, toY, gameTurnState)) {
	    // Historie
	    getBoard().getGameHistory().turn(getX(), getY(), toX, toY);
	    	    
	    // Skok + odebrani figurky
	    if (Math.abs(toX-getX()) == 2 || Math.abs(toY-getY()) == 2) {
		gameTurnState.setOnlyJump(toX, toY);
		int x = (getX()+toX)/2;
		int y = (getY()+toY)/2;
		
		// Historie
		getBoard().getGameHistory().setRemoved(x, y, getBoard().getPiece(x,y));
		
		// Odebrani
		getBoard().removePiece(x, y);
	    }
	    
	    // Posun figurky
	    getBoard().movePiece(getX(), getY(), toX, toY);

	    // Zmenit na damu?
	    int lY = 0;
	    switch (getColor()) {
		case WHITE:
		    lY = 0;
	        break;
		case BLACK:
		    lY = 7;
		break;
	    }
	    
	    if (toY == lY) {
		new GamePieceKing(getBoard(), getX(), getY(), getColor());

		// Historie
		getBoard().getGameHistory().setChangedToKing();
	    }
	    
	    return true;
	} else {
	    return false;
	}
    }
    
    /**
     * Overi, zda je tento tah pescem mozny
     *
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     * @param gameTurnState Stav tahu hry
     * @return True pokud je tah mozny
     */
    protected boolean possibleTurn(int toX, int toY,
	    GameTurnState gameTurnState) {
	if (!super.possibleTurn(toX, toY)) {
	    return false;
	}
    
	int directionY = 0;
	int directionX = 0;
	switch (getColor()) {
	    case WHITE:
		directionY = -1;
	    break;
	    case BLACK:
		directionY = 1;
	    break;
	}
	if (toX<getX()) {
	    directionX = -1;
	} else {
	    directionX = 1;
	}

	// Posun o jedno pole
        if ((toX-getX() == directionX) &&
		toY-getY() == directionY) {
	    return !gameTurnState.isOnlyJump();
	}
	
	// Skok
        if ((toX-getX() == 2*directionX) &&
		toY-getY() == 2*directionY && getBoard().
		getPiece(getX()+directionX, getY()+directionY) != null) {
	    Game.GameColor color = getBoard().
		getPiece(getX()+directionX, getY()+directionY).getColor();
	    if (!gameTurnState.isOnlyJumpXY(getX(), getY())) {
		return false;
	    }
	    switch (color) {
		case WHITE: return (getColor() == Game.GameColor.BLACK);
		case BLACK: return (getColor() == Game.GameColor.WHITE);
	    }
	}
	
	return false;
    }
}
