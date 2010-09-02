package vutbr.checkers.game;

/**
 * Trida Game - samotna hra
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public abstract class Game {
    public enum GameColor {BLACK, WHITE};
    
    private GameColor onTurn;
    private GameBoard board;
    private GameTurnState gameTurnState;
    private GameHistory gameHistory;
    
    /**
     * Konstruktor
     */
    public Game() {
	setGameHistory(new GameHistory(this));
	setBoard(new GameBoard(getGameHistory()));
	setOnTurn(GameColor.WHITE);
	setGameTurnState(new GameTurnState());
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
     * Vrati aktualni hraci plochu
     *
     * @return Aktualni hraci plocha
     */
    public GameBoard getBoard() {
	return this.board;
    }
    
    /**
     * Nastavi barvu na tahu
     *
     * @param onTurn Barva na tahu
     */
    protected void setOnTurn(GameColor onTurn) {
	this.onTurn = onTurn;
    }
    
    /**
     * Vrati barvu na tahu
     *
     * @return Aktualni barva na tahu
     */
    public GameColor getOnTurn() {
	return this.onTurn;
    }

    /**
     * Nastavi stav tahu hry
     *
     * @param gameTurnState Stav tahu hry
     */    
    private void setGameTurnState(GameTurnState gameTurnState) {
	this.gameTurnState = gameTurnState;
    }
    
    /**
     * Vrati stav tahu hry
     *
     * @return Stav tahu hry
     */
    private GameTurnState getGameTurnState() {
	return this.gameTurnState;
    }
    
    /**
     * Nastavi historii hry
     *
     * @param gameHistory Historie hry
     */
    private void setGameHistory(GameHistory gameHistory) {
	this.gameHistory = gameHistory;
    }
    
    /**
     * Vrati historii hry
     *
     * @return Historie hry
     */
    public GameHistory getGameHistory() {
	return this.gameHistory;
    }

    /**
     * Provede tah figurkou
     *
     * Tato funkce overuje, zda je tahnuto figurkou, jejiz barva je na tahu.
     * Samotna implementace tahu je v tride GameBoard a GamePiece.
     *
     * @param x Zdrojova souradnice X
     * @param y Zdrojova souradnice Y
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     * @return True pokud se tah povedl
     */
    public boolean turn(int x, int y, int toX, int toY) {
	GamePiece piece = getBoard().getPiece(x, y);
	if (piece == null) {
	    return false;
	} else {
	    if (piece.getColor() != getOnTurn()) {
		return false;
	    }
	}
	if (getGameTurnState().isFirstTurn() ||
		getGameTurnState().isOnlyJump()) {
	    boolean out = getBoard().turn(x, y, toX, toY, getGameTurnState());
	    if (out) {
		getGameTurnState().unsetFirstTurn();
	    }
	    return out;
	} else {
	    return false;
	}
    }
    
    /**
     * Ukonci tah
     *
     * @return True pokud se konec tahu povedl
     */
    public boolean turnEnd() {
	if (getGameTurnState().isFirstTurn()) {
	    // Neni mozne vzdat tah
	    return false;
	}
	
	setGameTurnState(new GameTurnState());
	
	switch (getOnTurn()) {
	    case WHITE:
		setOnTurn(Game.GameColor.BLACK);
	    break;
	    case BLACK:
		setOnTurn(Game.GameColor.WHITE);
	    break;
	}
	
	getGameHistory().turnEnd();
	
	return true;
    }
    
    /**
     * Je konec hry?
     *
     * @return Konec hry
     */
    public boolean isGameEnd() {
	return getBoard().isGameEnd();
    }
}
