package vutbr.checkers.protocol.request;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolRequestTurnEnd
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolRequestTurnEnd extends ProtocolRequest {
    // Abstract from ProtocolLine
    /**
     * Vrati jmeno prikazu
     *
     * @return Jmeno prikazu
     */
    protected String getCommand() {
	return "TURN_END";
    }
    
    /**
     * Vrati parametry prikazu
     *
     * @return Parametry prikazu
     */
    protected String getParam() {
	return "";
    }
}
