package vutbr.checkers.protocol.request;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolRequestTurn
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolRequestTurn extends ProtocolRequest {
    private int x;
    private int y;
    private int toX;
    private int toY;

    /**
     * Konstruktor
     *
     * @param x Zdrojova souradnice X
     * @param y Zdrojova souradnice Y
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     */
    public ProtocolRequestTurn(int x, int y, int toX, int toY) {
	setX(x);
	setY(y);
	setToX(toX);
	setToY(toY);
    }

    /**
     * Nastavi zdrojovou souradnici X
     *
     * @param x Zdrojova souradnice X
     */
    private void setX(int x) {
	this.x = x;
    }
    
    /**
     * Nastavi zdrojovou souradnici Y
     *
     * @param x Zdrojova souradnice Y
     */
    private void setY(int y) {
	this.y = y;
    }
    
    /**
     * Nastavi cilovou souradnici X
     *
     * @param y Cilova souradnice X
     */
    private void setToX(int toX) {
	this.toX = toX;
    }
    
    /**
     * Nastavi cilovou souradnici Y
     *
     * @param y Cilova souradnice Y
     */
    private void setToY(int toY) {
	this.toY = toY;
    }

    /**
     * Vrati zdrojovou souradnici X
     *
     * @param Zdrojova souradnice X
     */
    private int getX() {
	return this.x;
    }

    /**
     * Vrati zdrojovou souradnici Y
     *
     * @param Zdrojova souradnice Y
     */
    private int getY() {
	return this.y;
    }

    /**
     * Vrati cilovou souradnici X
     *
     * @param Cilova souradnice X
     */
    private int getToX() {
	return this.toX;
    }

    /**
     * Vrati cilovou souradnici Y
     *
     * @param Cilova souradnice Y
     */
    private int getToY() {
	return this.toY;
    }

    // Abstract from ProtocolLine
    /**
     * Vrati jmeno prikazu
     *
     * @return Jmeno prikazu
     */
    protected String getCommand() {
	return "TURN";
    }
    
    /**
     * Vrati parametry prikazu
     *
     * @return Parametry prikazu
     */
    protected String getParam() {
	return getX() + " " + getY() + " " + getToX() + " " + getToY();
    }
}
