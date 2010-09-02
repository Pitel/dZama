package vutbr.checkers.protocol.reply;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolReplyLoginError
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolReplyLoginError extends ProtocolReply {
    protected String message;

    /**
     * Konstruktor
     *
     * @param message Chybova zprava
     */
    public ProtocolReplyLoginError(String message) {
	setMessage(message);
    }
    
    /**
     * Nastavi chybovou zpravu
     *
     * @param message Chybova zprava
     */
    private void setMessage(String message) {
	this.message = message;
    }
    
    /**
     * Vrati chybovou zpravu
     *
     * @return Chybova zprava
     */
    private String getMessage() {
	return this.message;
    }

    // Abstract from ProtocolLine
    /**
     * Vrati jmeno prikazu
     *
     * @return Jmeno prikazu
     */
    protected String getCommand() {
	return "LOGIN_ERROR";
    }
    
    /**
     * Vrati parametry prikazu
     *
     * @return Parametry prikazu
     */
    protected String getParam() {
	return getMessage();
    }
}
