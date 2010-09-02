package vutbr.checkers.server;

import java.net.*;
import java.io.*;
import java.util.*;
import vutbr.checkers.protocol.*;
import vutbr.checkers.protocol.request.*;
import vutbr.checkers.protocol.reply.*;

public class ServerThread extends ProtocolClient {
    private Socket clientSocket;
    private String login;
    private Server server;
    private boolean pingRequested;
    private ServerGame serverGame;

    /**
     * Konstruktor
     *
     * @param socket Soket
     */
    public ServerThread(Socket socket) {
	setClientSocket(socket);
    }
    
    /**
     * Spusti vlakno
     */
    public void run() {
	try {
	    BufferedReader in = new BufferedReader(
		new InputStreamReader(
		    getClientSocket().getInputStream()));
	    BufferedWriter out = new BufferedWriter(
		new OutputStreamWriter(
		    getClientSocket().getOutputStream()));

	    setProtocolInput(new ProtocolInput(in, this));
	    setProtocolOutput(new ProtocolOutput(out));
	
	    getProtocolOutput().start();
	    getProtocolInput().start();
	    setPingRequested(false);
	    try {
		boolean running = true;
		while (running) {
		    Thread.sleep(60000); // Ping every 60sec
		    if (getClientSocket() == null) {
			break;
		    }
		    setPingRequested(true);
		    getProtocolOutput().send(new ProtocolRequestPing());
		    Thread.sleep(5000); // Wait 5sec for reply
		    if (getPingRequested()) {
		        // No PONG reply received
			running = false;
		    }
		}
	    } catch (InterruptedException ie) {}
	} catch (IOException e) {
	    System.err.println("Chyba");
	}
	
	try {
	    close();
	} catch (IOException e) {
	    System.err.println("Chyba zavirani CS");
	}
    }
    
    /**
     * Uzavre spojeni s klientem
     */
    private void close() throws IOException {
	if (getClientSocket() != null) {
	    getClientSocket().close();
	    setClientSocket(null);
	}
    }

    /**
     * Nastavi klientsky soket
     *
     * @param clientSocket Klientsky soket
     */
    private void setClientSocket(Socket clientSocket) {
	this.clientSocket = clientSocket;
    }
    
    /**
     * Vrati klientsky soket
     *
     * @return Klientsky soket
     */
    private Socket getClientSocket() {
	return this.clientSocket;
    }
    
    /**
     * Nastavi server
     *
     * @param server Server
     */
    public void setServer(Server server) {
	this.server = server;
    }
    
    /**
     * Vrati server
     *
     * @return Server
     */
    public Server getServer() {
	return this.server;
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
    public String getLogin() {
	return this.login;
    }

    /**
     * Odesle ReplyUserLogin zpravu
     *
     * @param login Login hrace
     */
    public void userLogin(String login) {
	getProtocolOutput().send(new ProtocolReplyUserLogin(login));
    }
    
    /**
     * Odesle ReplyUserLogout zpravu
     *
     * @param login Login hrace
     */
    public void userLogout(String login) {
	getProtocolOutput().send(new ProtocolReplyUserLogout(login));
    }
    
    /**
     * Nastavi zda byl odeslana RequestPing zprava
     *
     * @param pingRequested Zda byla odeslana RequestPing zprava
     */
    private synchronized void setPingRequested(boolean pingRequested) {
	this.pingRequested = pingRequested;
    }
    
    /**
     * Vrati zda byla odeslana RequestPing zprava
     *
     * @return Zda byla odeslana RequestPing zprava
     */
    private synchronized boolean getPingRequested() {
	return this.pingRequested;
    }
    
    /**
     * Nastavi server game
     *
     * @param serverGame Server game
     */
    protected void setServerGame(ServerGame serverGame) {
	this.serverGame = serverGame;
    }
    
    /**
     * Vrati server game
     *
     * @return Server game
     */
    protected ServerGame getServerGame() {
	return this.serverGame;
    }
    
    /**
     * Odesle RequestGame zpravu
     *
     * @param login Login protihrace
     */
    protected void game(String login) {
	getProtocolOutput().send(new ProtocolRequestGame(login));
    }
    
    /**
     * Odesle ReplyGameAccept zpravu
     *
     * @param login Login protihrace
     */
    protected void gameAccept(String login) {
	getProtocolOutput().send(new ProtocolReplyGameAccept(login));
    }
    
    /**
     * Odesle ReplyGameReject zpravu
     *
     * @param login Login protihrace
     */
    protected void gameReject(String login) {
	getProtocolOutput().send(new ProtocolReplyGameReject(login));
    }

    /**
     * Odesle RequestTurn zpravu
     *
     * @param x Zdrojova souradnice X
     * @param y Zdrojova souradnice Y
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     */
    protected void turn(int x, int y, int toX, int toY) {
	getProtocolOutput().send(new ProtocolRequestTurn(x, y, toX, toY));
    }

    /**
     * Odesle RequestTurnEnd zpravu
     */    
    protected void turnEnd() {
	getProtocolOutput().send(new ProtocolRequestTurnEnd());
    }
    
    /**
     * Odesle RequestGameEnd zpravu
     */
    protected void gameEnd() {
	setServerGame(null);
	getProtocolOutput().send(new ProtocolRequestGameEnd());
    }
    
    /**
     * Odesle ReplyUserInGame zpravu
     *
     * @param login Login hrace
     */
    protected void userInGame(String login) {
	getProtocolOutput().send(new ProtocolReplyUserInGame(login));
    }
    
    /**
     * Odesle ReplyUserOutGame zpravu
     *
     * @param login Login hrace
     */
    protected void userOutGame(String login) {
	getProtocolOutput().send(new ProtocolReplyUserOutGame(login));
    }
    
    // OnFunctions
    public void onLogin(String login) {
	ProtocolReply reply;
	
	if (getLogin() != null) {
	    reply = new ProtocolReplyLoginError("Already logged in");
	} else {
	    if (login.length() > 0) {
		setLogin(login);
		if (getServer().login(this)) {
		    reply = new ProtocolReplyLoginOk();
		    // Set thread name
		    setName("Client-" + login);
		
		} else {
		    reply = new ProtocolReplyLoginError("Nick collision");
		    setLogin(null);
		}
	    } else {
		reply = new ProtocolReplyLoginError("Bad login");
	    }
	}
	getProtocolOutput().send(reply);
    }
    
    public void onRegister(String login) {
	// Registrace neni nutna, vsichni jsou registrovani
	getProtocolOutput().send(new ProtocolReplyRegisterOk());
    }
    
    public void onExit() {
	if (getServerGame() != null) {
	    onGameEnd();
	}
	getServer().logout(this);
	try {
	    close();
	} catch (IOException e) {
	    System.err.println("Chyba zavirani CS");
	}
    }
    
    public void onGame(String login) {
	if (getLogin() == null) {
	    denyMethod();
	    return;
	}
	
	if (getServerGame() != null) {
	    denyMethod();
	    return;
	}

	if (getLogin().toLowerCase().equals(login.toLowerCase())) {
	    getProtocolOutput().send(new ProtocolReplyGameError("Can't play with yourself"));
	    return;
	}
	
	if (!getServer().game(this, login)) {
	    getProtocolOutput().send(new ProtocolReplyGameError("Bad login"));
	}
    }
    
    public void onTurn(int x, int y, int toX, int toY) {
	if (this.getServerGame() == null) {
	    denyMethod();
	    return;
	}
	if (getServerGame().turn(x, y, toX, toY)) {
	    getProtocolOutput().send(new ProtocolReplyTurnOk());
	    if (getServerGame().isGameEnd()) {
		gameEnd();
		onGameEnd();
	    }
	} else {
	    getProtocolOutput().send(new ProtocolReplyTurnError("Non possible turn!"));
	}
    }

    public void onTurnEnd() {
	if (this.getServerGame() == null) {
	    denyMethod();
	    return;
	}
	
	switch (getServerGame().getOnTurn()) {
	    case WHITE:
		if (!getServerGame().getLoginWhite().toLowerCase().
			equals(getLogin().toLowerCase())) {
		    getProtocolOutput().send(
			new ProtocolReplyTurnError("You are not on turn!"));
		    return;
		}
	    break;
	    case BLACK:
		if (!getServerGame().getLoginBlack().toLowerCase().
			equals(getLogin().toLowerCase())) {
		    getProtocolOutput().send(
			new ProtocolReplyTurnError("You are not on turn!"));
		    return;
		}
	    break;
	}
	if (getServerGame().turnEnd()) {
	    getProtocolOutput().send(new ProtocolReplyTurnEndOk());
	} else {
	    getProtocolOutput().send(new ProtocolReplyTurnEndError("Turn end not possible!"));
	}
    }

    public void onBadCommand() {
    }
        
    public void onUserLogin(String login) {
	denyMethod();
    }
    
    public void onUserLogout(String login) {
	denyMethod();
    }
    
    public void onUserInGame(String login) {
	denyMethod();
    }
    
    public void onUserOutGame(String login) {
	denyMethod();
    }
    
    public void onLoginOk() {
	denyMethod();
    }
    
    public void onLoginError(String error) {
	denyMethod();
    }
    
    public void onRegisterOk() {
	denyMethod();
    }
    
    public void onRegisterError(String error) {
	denyMethod();
    }
    
    public synchronized void onPong() {
	setPingRequested(false);
    }
    
    public void onGameAccept(String login) {
	if (getLogin() == null) {
	    denyMethod();
	    return;
	}

	if (getLogin().toLowerCase().equals(login.toLowerCase())) {
	    getProtocolOutput().send(new ProtocolReplyGameError("Can't play with yourself"));
	    return;
	}
	
	if (!getServer().gameAccept(this, login)) {
	    getProtocolOutput().send(new ProtocolReplyGameError("Bad login"));
	}
	
	setServerGame(new ServerGame(getServer(), login, getLogin()));
	if (!getServer().serverGame(login, getServerGame())) {
	    getProtocolOutput().send(new ProtocolReplyGameError("Oops"));
	}
	getServer().userInGame(login);
	getServer().userInGame(getLogin());
    }
    
    public void onGameReject(String login) {
	if (getLogin() == null) {
	    denyMethod();
	    return;
	}

	if (getLogin().toLowerCase().equals(login.toLowerCase())) {
	    getProtocolOutput().send(new ProtocolReplyGameError("Can't play with yourself"));
	    return;
	}
	
	if (!getServer().gameReject(this, login)) {
	    getProtocolOutput().send(new ProtocolReplyGameError("Bad login"));
	}
    }
    
    public void onGameError(String error) {
	denyMethod();
    }

    public void onTurnError(String error) {
	// TODO
	denyMethod();
    }

    public void onTurnOk() {
    }
    
    public void onTurnEndError(String error) {
    }

    public void onTurnEndOk() {
    }
    
    public void onGameEnd() {
	String login;

	if (getServerGame() == null) {
	    denyMethod();
	    return;
	}
	
	if (getServerGame().getLoginWhite().toLowerCase().equals(
		getLogin().toLowerCase())) {
	    login = getServerGame().getLoginBlack();
	} else {
	    login = getServerGame().getLoginWhite();
	}
	
	getServer().gameEnd(login);
	getServer().userOutGame(login);
	getServer().userOutGame(getLogin());
    }
}
