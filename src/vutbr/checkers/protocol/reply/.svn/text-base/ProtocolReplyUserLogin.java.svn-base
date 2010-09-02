package vutbr.checkers.protocol.reply;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolReplyUserLogin
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolReplyUserLogin extends ProtocolReply {
    private String login;

    /**
     * Konstruktor
     *
     * @param login Login hrace
     */
    public ProtocolReplyUserLogin(String login) {
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
	return "USER_LOGIN";
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
