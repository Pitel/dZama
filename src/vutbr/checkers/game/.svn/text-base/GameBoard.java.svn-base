package vutbr.checkers.game;

/**
 * Trida GameBoard - hraci plocha
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class GameBoard {
    private GamePiece[][] board;
    private int whiteManCount;
    private int blackManCount;
    private int whiteKingCount;
    private int blackKingCount;
    private GameHistory gameHistory;
    
    /**
     * Konstruktor
     */
    public GameBoard(GameHistory gameHistory) {
	setGameHistory(gameHistory);
	this.whiteManCount = 0;
	this.blackManCount = 0;
	this.whiteKingCount = 0;
	this.blackKingCount = 0;

	setBoard(new GamePiece[8][8]);
	for (int i = 0 ; i < 4 ; i++) {
	    // White side
	    new GamePieceMan(this, 2*i+1, 6, Game.GameColor.WHITE);
	    new GamePieceMan(this, 2*i,   7, Game.GameColor.WHITE);

	    // Black side
	    new GamePieceMan(this, 2*i+1, 0, Game.GameColor.BLACK);
	    new GamePieceMan(this, 2*i,   1, Game.GameColor.BLACK);
	}
    }
    
    /**
     * Nastavi hraci plochu
     *
     * @param board 8x8 pole GamePiece
     */
    private void setBoard(GamePiece[][] board) {
	this.board = board;
    }
    
    /**
     * Vrati hraci plochu
     *
     * @return 8x8 pole GamePiece
     */
    private GamePiece[][] getBoard() {
	return this.board;
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
    protected GameHistory getGameHistory() {
	return this.gameHistory;
    }
    
    /**
     * Zaktualizuje pocet figurek typu piece podle hodnoty count
     *
     * @param piece Figurka
     * @param count Pocet (+1 pro pricteni, -1 pro odecteni hodnoty)
     */
    private void updateCount(GamePiece piece, int count) {
	if (piece instanceof GamePieceMan) {
	    switch (piece.getColor()) {
		case WHITE: this.whiteManCount += count;
		break;
		case BLACK: this.blackManCount += count;
		break;
	    }
	} else if (piece instanceof GamePieceKing) {
	    switch (piece.getColor()) {
		case WHITE: this.whiteKingCount += count;
		break;
		case BLACK: this.blackKingCount += count;
		break;
	    }
	}
    }
    
    /**
     * Prida figurku do hraciho pole
     *
     * @param x Souradnice X
     * @param y Souradnice Y
     * @param piece Figurka
     */
    protected void addPiece(int x, int y, GamePiece piece) {
	updateCount(piece, 1);
	
	setPiece(x, y, piece);
    }
    
    /**
     * Vrati figurku na souradnicich
     *
     * @param x Souradnice X
     * @param y Souradnice Y
     * @return Figurka
     */
    public GamePiece getPiece(int x, int y) {
	return getBoard()[x][y];
    }
    
    /**
     * Nastavi hodnotu pole na souradnicich na figurku
     *
     * @param x Souradnice X
     * @param y Souradnice Y
     */
    private void setPiece(int x, int y, GamePiece piece) {
	getBoard()[x][y] = piece;
    }
    
    /**
     * Presune figurku z pole jine pole
     *
     * @param x Zdrojova souradnice X
     * @param y Zdrojova souradnice Y
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     */
    protected void movePiece(int x, int y, int toX, int toY) {
	getPiece(x, y).setX(toX);
	getPiece(x, y).setY(toY);
	
	setPiece(toX, toY, getPiece(x, y));
	setPiece(x, y, null);
    }

    /**
     * Odstrani figurku z pole
     *
     * @param x Souradnice X
     * @param y Souradnice Y
     */
    protected void removePiece(int x, int y) {
	updateCount(getPiece(x, y), -1);
	
	setPiece(x, y, null);
    }
    
    /**
     * Vymeni figurku v poli za jinou
     *
     * @param x Souradnice X
     * @param y Souradnice Y
     * @param newPiece Nova figurka
     */
    protected void switchPiece(int x, int y, GamePiece newPiece) {
	updateCount(getPiece(x, y), -1);
	updateCount(newPiece,  1);
	
	setPiece(x, y, newPiece);
    }
    
    /**
     * Provede tah figurkou
     *
     * @param x Zdrojova souradnice X
     * @param y Zdrojova souradnice Y
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     * @param gameTurnState Stav tahu hry
     * @return True pokud se tah povedl
     */
    protected boolean turn(int x, int y, int toX, int toY,
	    GameTurnState gameTurnState) {
	return getPiece(x, y).turn(toX, toY, gameTurnState);
    }
    
    /**
     * Je konec hry?
     *
     * @return Konec hry
     */
    public boolean isGameEnd() {
	return (whiteManCount == 0 && whiteKingCount == 0) ||
	    (blackManCount == 0 && blackKingCount == 0);
    }
}
