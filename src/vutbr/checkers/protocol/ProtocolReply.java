package vutbr.checkers.protocol;

import vutbr.checkers.protocol.reply.*;

/**
 * Trida ProtocolReply
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public abstract class ProtocolReply extends ProtocolLine{
    /**
     * Vrati typ prikazu REP
     *
     * @return Typ prikazu REP
     */
    protected final String getType() {
	return "REP";
    }
}
