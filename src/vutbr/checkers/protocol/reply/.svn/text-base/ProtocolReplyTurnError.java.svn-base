package vutbr.checkers.protocol.reply;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolReplyTurnError
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolReplyTurnError extends ProtocolReply {
    protected String message;

    /**
     * Konstruktor
     *
     * @param message Chybova zprava
     */
    public ProtocolReplyTurnError(String message) {
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
	return "TURN_ERROR";
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
