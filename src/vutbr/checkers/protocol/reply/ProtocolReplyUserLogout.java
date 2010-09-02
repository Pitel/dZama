package vutbr.checkers.protocol.reply;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolReplyUserLogout
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolReplyUserLogout extends ProtocolReply {
    protected String login;

    /**
     * Konstruktor
     *
     * @param login Login hrace
     */
    public ProtocolReplyUserLogout(String login) {
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
	return "USER_LOGOUT";
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
