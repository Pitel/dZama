package vutbr.checkers.protocol.reply;

import vutbr.checkers.protocol.*;

/**
 * Trida ProtocolReplyRegisterOk
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolReplyRegisterOk extends ProtocolReply {
    // Abstract from ProtocolLine
    /**
     * Vrati jmeno prikazu
     *
     * @return Jmeno prikazu
     */
    protected String getCommand() {
	return "REGISTER_OK";
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
