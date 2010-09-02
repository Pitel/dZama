package vutbr.checkers.client;

import vutbr.checkers.game.*;
import vutbr.checkers.client.swing.*;

/**
 * Trida ClientGame - klientska cast hry
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ClientGame extends Game {
    private ClientThread clientThread;
    private Game.GameColor myColor;
    private MainWindow mainWindow;

    /**
     * Konstruktor
     *
     * @param clientThread Trida clientThread
     * @param myColor Barva hrace
     */
    public ClientGame(ClientThread clientThread, Game.GameColor myColor, MainWindow mainWindow) {
	super();
	
	setClientThread(clientThread);
	setMyColor(myColor);
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
	if (super.turn(x, y, toX, toY)) {
	    getMainWindow().addTurn(getOnTurn(), x, y, toX, toY);
	    if (getOnTurn() == getMyColor()) {
		// My turn, send to oponent
		getClientThread().turn(x, y, toX, toY);
	    }
	    return true;
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
	if (!super.turnEnd()) {
	    return false;
	}

	if (getOnTurn() != getMyColor()) {
		// My turn end, send to oponent
		getClientThread().turnEnd();
	}
	
	return true;
    }
    
    /**
     * Overi zda mohu tahnout a pripadne zavola turn()
     *
     * @param x Zdrojova souradnice X
     * @param y Zdrojova souradnice Y
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     * @return True pokud se tah povedl
     */
    public boolean myTurn(int x, int y, int toX, int toY) {
	if (getOnTurn() == getMyColor()) {
	    return turn(x, y, toX, toY);
	} else {
	    return false;
	}
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
    private void setMyColor(Game.GameColor myColor) {
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
    
    /**
     * Nastavi vlakno komunikace klientske strany
     *
     * @param clientThread Vlakno komunikace klientske strany
     */
    private void setClientThread(ClientThread clientThread) {
	this.clientThread = clientThread;
    }
    
    /**
     * Vrati vlakno komunikace klientske strany
     *
     * @return Vlakno komunikace klientske strany
     */
    public ClientThread getClientThread() {
	return this.clientThread;
    }
}
