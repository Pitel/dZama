package vutbr.checkers.server;

import vutbr.checkers.game.*;

public class ServerGame extends Game {
    Server server;
    String loginWhite;
    String loginBlack;

    /**
     * Konstruktor
     *
     * @param server Server
     * @param loginWhite Login bileho hrace
     * @param loginBlack Login cerneho hrace
     */
    public ServerGame(Server server, String loginWhite, String loginBlack) {
	super();
    
	setServer(server);
	setLoginWhite(loginWhite);
	setLoginBlack(loginBlack);
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
	    String login = "";
	    switch (getBoard().getPiece(toX, toY).getColor()) {
		case WHITE:
		    login = getLoginBlack();
		break;
		case BLACK:
		    login = getLoginWhite();
		break;
	    }
	    
	    getServer().turn(login, x, y, toX, toY);
	    return true;
	}
	
	return false;
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
	
	String login = "";
	switch (getOnTurn()) {
	    case WHITE:
	        login = getLoginWhite();
	    break;
	    case BLACK:
	        login = getLoginBlack();
	    break;
	}
	
	getServer().turnEnd(login);

	return true;
    }
    
    /**
     * Nastavi server
     *
     * @param server Server
     */
    public void setServer(Server server) {
	this.server = server;
    }
    
    /**
     * Vrati server
     *
     * @return Server
     */
    public Server getServer() {
	return this.server;
    }
    
    /**
     * Nastavi login bileho hrace
     *
     * @param loginWhite Login bileho hrace
     */
    public void setLoginWhite(String loginWhite) {
	this.loginWhite = loginWhite;
    }
    
    /**
     * Nastavi login cerneho hrace
     *
     * @param loginBlack Login cerneho hrace
     */
    public void setLoginBlack(String loginBlack) {
	this.loginBlack = loginBlack;
    }
    
    /**
     * Vrati login bileho hrace
     *
     * @return Login bileho hrace
     */
    public String getLoginWhite() {
	return this.loginWhite;
    }
    
    /**
     * Vrati login cerneho hrace
     *
     * @return Login cerneho hrace
     */
    public String getLoginBlack() {
	return this.loginBlack;
    }
}
