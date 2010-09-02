package vutbr.checkers.game;

/**
 * Trida GamePieceKing - figurka kralovny
 *
 * @author Ondřej Choleva <xchole00@stud.fit.vutbr.cz>
 */
public class GamePieceKing extends GamePiece {
    /**
     * Konstruktur
     *
     * @param board Hraci plocha
     * @param x Souradnice X
     * @param y Souradnice Y
     * @param color Barva figurky
     */
    public GamePieceKing(GameBoard board, int x, int y, Game.GameColor color) {
	super(board, x, y, color);
    }
    
    /**
     * Provede tah kralovnou
     *
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     * @param gameTurnState Stav tahu hry
     * @return True pokud se tah povedl
     */    
    protected boolean turn(int toX, int toY, GameTurnState gameTurnState) {
	int directionX, directionY;
	
	// Zjisti smer pohybu
	if (toX<getX()) {
	    directionX = -1;
	} else {
	    directionX = 1;
	}
	if (toY<getY()) {
	    directionY = -1;
	} else {
	    directionY = 1;
	}
	
	// Pokud je tah mozny, provede jej a ulozi do historie
	if (possibleTurn(toX, toY, gameTurnState)) {
	    // Historie
	    getBoard().getGameHistory().turn(getX(), getY(), toX, toY);
	    
	    // Posun na novou pozici
	    getBoard().movePiece(getX(), getY(), toX, toY);

	    // Pripadny skok a odebrani figurky
	    if (getBoard().getPiece(toX-directionX, toY-directionY) != null) {
		gameTurnState.setOnlyJump(toX, toY);
		int x = toX-directionX;
		int y = toY-directionY;

		// Historie
		getBoard().getGameHistory().setRemoved(x, y, getBoard().getPiece(x,y));
		
		// Odebrani
		getBoard().removePiece(x, y);
	    }
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Overi, zda je tento tah kralovnou mozny
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
	
	int directionX, directionY;
	int i, j;
	
	// Zjisti smer pohybu
	if (toX<getX()) {
	    directionX = -1;
	} else {
	    directionX = 1;
	}
	if (toY<getY()) {
	    directionY = -1;
	} else {
	    directionY = 1;
	}

	// Posun o jedno pole
        if ((toX-getX() == directionX) &&
		toY-getY() == directionY) {
    	    return !gameTurnState.isOnlyJump();
	}
	
	// preX, preY jsou souradnice predposledniho pole skoku
	int preX = toX - directionX;
	int preY = toY - directionY;
	
	// Skok, kontroluje za je prvnich n-2 poli prezdnych
	for(i = getX() + directionX, j = getY() + directionY;
	    i != preX && j != preY;
	    i += directionX, j += directionY) {
	    if (getBoard().getPiece(i, j) != null) {
		return false;
	    }
	}
	
	// Kontroluje predposleni pole skoku: musi byt bud prazdne nebo protihracova figurka
	if (getBoard().getPiece(preX, preY) != null) {
	    Game.GameColor color = getBoard().getPiece(preX, preY).getColor();
	    if (!gameTurnState.isOnlyJumpXY(getX(), getY())) {
		return false;
	    }
	    switch (color) {
		case WHITE: return (getColor() == Game.GameColor.BLACK);
		case BLACK: return (getColor() == Game.GameColor.WHITE);
	    }
	} else {
	    return !gameTurnState.isOnlyJump();
	}
	
	return false;
    }
}
