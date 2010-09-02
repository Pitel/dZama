package vutbr.checkers.client;

import vutbr.checkers.game.*;
import vutbr.checkers.client.swing.*;

/**
 * Trida ReplayGame - trida pro prehravani hry
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ReplayGame extends Game {
    private Game.GameColor myColor;
    private MainWindow mainWindow;

    /**
     * Konstruktor
     */
    public ReplayGame(MainWindow mainWindow) {
	super();
	setMainWindow(mainWindow);
    }
    
    /**
     * Provede tah figurkou
     *
     * @param x Zdrojova souradnice X
     * @param y Zdrojova souradnice Y
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     * @return True pokud se tah povedl
     */
    public boolean turn(int x, int y, int toX, int toY) {
	getMainWindow().addTurn(getOnTurn(), x, y, toX, toY);
	return super.turn(x, y, toX, toY);
    }
    
    /**
     * Ukonci tah
     *
     * @return True pokud se konec tahu povedl
     */
    public boolean turnEnd() {
	return super.turnEnd();
    }
    
    /**
     * Vrati hlavni okno hry
     * 
     * @return Okno hry
     */
    private MainWindow getMainWindow() {
	return this.mainWindow;
    }

    /**
     * Nastavi hlavni okno hry
     * 
     * @param Okno hry
     */
    private void setMainWindow(MainWindow mainWindow) {
	this.mainWindow = mainWindow;
    }

    /**
     * Nastavi barvu hrace
     *
     * @param myColor Barva hrace
     */
    public void setMyColor(Game.GameColor myColor) {
	this.myColor = myColor;
    }
    
    /**
     * Vrati barvu hrace
     *
     * @return Barva hrace
     */
    public Game.GameColor getMyColor() {
	return this.myColor;
    }
}
