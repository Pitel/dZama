package vutbr.checkers.protocol.reply;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolReplyGameReject
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolReplyGameReject extends ProtocolReply {
    private String login;

    /**
     * Konstruktor
     *
     * @param login Login protihrace
     */
    public ProtocolReplyGameReject(String login) {
	setLogin(login);
    }

    /**
     * Nastavi login protihrace
     *
     * @param login Login protihrace
     */
    private void setLogin(String login) {
	this.login = login;
    }
    
    /**
     * Vrati login protihrace
     *
     * @return Login protihrace
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
	return "GAME_REJECT";
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
