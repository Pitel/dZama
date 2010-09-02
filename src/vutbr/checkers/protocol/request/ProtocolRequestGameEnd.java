package vutbr.checkers.protocol.request;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolRequestGameEnd
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolRequestGameEnd extends ProtocolRequest {
    // Abstract from ProtocolLine
    /**
     * Vrati jmeno prikazu
     *
     * @return Jmeno prikazu
     */
    protected String getCommand() {
	return "GAME_END";
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
