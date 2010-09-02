package vutbr.checkers.protocol.reply;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolReplyTurnEndError
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolReplyTurnEndError extends ProtocolReply {
    protected String message;

    /**
     * Konstruktor
     *
     * @param message Chybova zprava
     */
    public ProtocolReplyTurnEndError(String message) {
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
	return "TURN_END_ERROR";
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
