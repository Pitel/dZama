package vutbr.checkers.protocol.reply;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolReplyUserOutGame
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolReplyUserOutGame extends ProtocolReply {
    protected String login;

    /**
     * Konstruktor
     *
     * @param login Login hrace
     */
    public ProtocolReplyUserOutGame(String login) {
	setLogin(login);
    }
    
    /**
     * Nastavi login hrace
     *
     * @param login Login hrace
     */
    private void setLogin(String login) {
	this.login = login;
    }
    
    /**
     * Vrati login hrace
     *
     * @return Login hrace
     */
    private String getLogin() {
	return this.login;
    }

    // Abstract from ProtocolLine
    /**
     * Vrati jmeno prikazu
     *
     * @return Jmeno prikazu
     */
    protected String getCommand() {
	return "USER_OUT_GAME";
    }
    
    /**
     * Vrati parametry prikazu
     *
     * @return Parametry prikazu
     */
    protected String getParam() {
	return getLogin();
    }
}
