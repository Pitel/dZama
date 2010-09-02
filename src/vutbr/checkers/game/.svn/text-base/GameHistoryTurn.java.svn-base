package vutbr.checkers.game;

/**
 * Jednotlive posunuti figurkou
 * 
 * @author Ondrej Choleva <xchole00@stud.fit.vutbr.cz>
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class GameHistoryTurn {
    private int x;
    private int y;
    private int toX;
    private int toY;
    private GamePiece removed;
    private int removedX;
    private int removedY;
    private boolean changedToKing;
    
    /**
     * Kontruktor
     */
    public GameHistoryTurn () {
	setX(-1);
	setY(-1);
	setToX(-1);
	setToY(-1);
	this.removed = null;
	this.changedToKing = false;
    }
    
    /**
     * Nastavi souradnice tahu
     *
     * @param x Vychozi souradnice x
     * @param y Vychozi souradnice y
     * @param toX Cilova souradnice x
     * @param toY Cilova souradnice y
     */
    protected void setTurn(int x, int y, int toX, int toY) {
	this.x = x;
	this.y = y;
	this.toX = toX;
	this.toY = toY;
    }
    
    /**
     * Nastavi souradnice a typ odebrane figurky
     * 
     * @param x Souradnice x
     * @param y Souradnice y
     * @param piece Typ figurky
     */
    protected void setRemoved(int x, int y, GamePiece piece) {
	this.removed = piece;
	this.removedX = x;
	this.removedY = y;
    }
    
    protected void setChangedToKing () {
	this.changedToKing = true;
    }

    /**
     * Nastavi zdrojovou souradnici X
     *
     * @param x Zdrojova souradnice X
     */
    protected void setX(int x) {
	this.x = x;
    }
    
    /**
     * Nastavi zdrojovou souradnici Y
     *
     * @param y Zdrojova souradnice Y
     */
    protected void setY(int y) {
	this.y = y;
    }
    /**
     * Nastavi cilovou souradnici X
     *
     * @param toX Cilova souradnice X
     */
    protected void setToX(int toX) {
	this.toX = toX;
    }
    
    /**
     * Nastavi cilovou souradnici Y
     *
     * @param toY Cilova souradnice Y
     */
    protected void setToY(int toY) {
	this.toY = toY;
    }
    
    /**
     * Vrati zdrojovou souradnici x
     *
     * @return Zdrojova souradnice x
     */
    protected int getX() {
	return this.x;
    }
    
    /**
     * Vrati zdrojovou souradnici y
     *
     * @return Zdrojova souradnice y
     */
    protected int getY() {
	return this.y;
    }
    
    /**
     * Vrati cilovou souradnici x
     *
     * @return Cilova souradnice x
     */
    protected int getToX() {
	return this.toX;
    }

    /**
     * Vrati cilovou souradnici y
     *
     * @return Cilova souradnice y
     */
    protected int getToY() {
	return this.toY;
    }
    
    /**
     * Vrati typ odebrane figurky
     * 
     * @return Pesec, dama nebo null
     */
    protected GamePiece getRemoved(){
	return this.removed;
    }
    
    /**
     * Vrati souradnici x odebrane figurky
     *
     * @return Souradnice x odebrane figurky
     */
    protected int getRemovedX() {
	return this.removedX;
    }

    /**
     * Vrati souradnici y odebrane figurky
     *
     * @return Souradnice x odebrane figurky
     */
    protected int getRemovedY() {
	return this.removedY;
    }
    
    /**
     * Vrati priznak zmeny na damu
     * 
     * @return True nebo false
     */
    protected boolean isChangedToKing (){
	return this.changedToKing;
    }
}
