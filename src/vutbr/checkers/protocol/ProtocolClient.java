package vutbr.checkers.protocol;

import vutbr.checkers.protocol.reply.*;

/**
 * Trida ProtocolClient
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public abstract class ProtocolClient extends Thread {
    private ProtocolInput protocolInput;
    private ProtocolOutput protocolOutput;

    /**
     * Nastavi protokolovy vystup
     *
     * @param protocolOutput Protokolovy vystup
     */
    protected void setProtocolOutput(ProtocolOutput protocolOutput) {
	this.protocolOutput = protocolOutput;
    }
    
    /**
     * Vrati protokolovy vystup
     *
     * @return Protokolovy vystup
     */
    protected ProtocolOutput getProtocolOutput() {
	return this.protocolOutput;
    }

    /**
     * Nastavi protokolovy vstup
     *
     * @param protocolInput Protokolovy vstup
     */
    protected void setProtocolInput(ProtocolInput protocolInput) {
	this.protocolInput = protocolInput;
    }
    
    /**
     * Vrati protkolovy vstup
     *
     * @return Protokolovy vstup
     */
    protected ProtocolInput getProtocolInput() {
	return this.protocolInput;
    }

    /**
     * Odesle ReplyBadCommand zpravu
     */
    public void denyMethod() {
	getProtocolOutput().send(new ProtocolReplyBadCommand());
    }

    // Request
    public abstract void onRegister(String login);
    public abstract void onLogin(String login);
    public abstract void onExit();
    public void onPing() {
	getProtocolOutput().send(new ProtocolReplyPong());
    }
    public abstract void onGame(String login);
    public abstract void onGameEnd();
    public abstract void onTurn(int x, int y, int toX, int toY);
    public abstract void onTurnEnd();

    // Reply
    public abstract void onBadCommand();
    public abstract void onLoginOk();
    public abstract void onLoginError(String error);
    public abstract void onRegisterOk();
    public abstract void onRegisterError(String error);
    public abstract void onUserLogin(String login);
    public abstract void onUserLogout(String login);
    public abstract void onUserInGame(String login);
    public abstract void onUserOutGame(String login);
    public void onPong() {
    }
    public abstract void onGameAccept(String login);
    public abstract void onGameReject(String login);
    public abstract void onGameError(String error);
    public abstract void onTurnError(String error);
    public abstract void onTurnOk();
    public abstract void onTurnEndError(String error);
    public abstract void onTurnEndOk();
}
