package vutbr.checkers.game;

/**
 * Trida GameTurnState - stav tahu hry
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class GameTurnState {
    private boolean firstTurn;
    private boolean onlyJump;
    private int x;
    private int y;
    
    /**
     * Konstruktor
     */
    public GameTurnState() {
	this.firstTurn = true;
	this.onlyJump = false;
    }
    
    /**
     * Vypne priznak prvniho tahu
     */
    protected void unsetFirstTurn() {
	this.firstTurn = false;
    }
    
    /**
     * Zapne priznak pouze skakani
     *
     * @param x Poloha X
     * @param y Poloha Y
     */
    protected void setOnlyJump(int x, int y) {
	this.onlyJump = true;
	this.x = x;
	this.y = y;
    }
    
    /**
     * Vrati priznak prvniho tahu
     */
    protected boolean isFirstTurn() {
	return this.firstTurn;
    }
    
    /**
     * Vrati priznak pouze skakani
     */
    protected boolean isOnlyJump() {
	return this.onlyJump;
    }
    
    /**
     * Vrati priznak pouze skakani pokud zaroven sedi X a Y
     *
     * @param x Poloha X
     * @param y Poloha Y
     */
    protected boolean isOnlyJumpXY(int x, int y) {
	return (!this.onlyJump) || (this.onlyJump && this.x == x && this.y == y);
    }
}
