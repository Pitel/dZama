package vutbr.checkers.client.swing;

import java.net.*;
import java.io.*;
import javax.swing.*;
import vutbr.checkers.client.*;
import vutbr.checkers.game.*;
import vutbr.checkers.protocol.*;
import vutbr.checkers.protocol.request.*;
import vutbr.checkers.protocol.reply.*;

/**
 * Trida ClientThread - vlakno komunikace klientske strany
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ClientThread extends ProtocolClient {
    private Socket clientSocket;
    private MainWindow mainWindow;
    private String host;
    private int port;
    private String login;
    private ClientGame clientGame;
    private boolean register;
    
    /**
     * Konstruktor
     *
     * @param host Host
     * @param port Port serveru
     * @param login Login hrace
     */
    public ClientThread(String host, int port, String login, boolean register) {
	setClientSocket(null);
	setHost(host);
	setPort(port);
	setLogin(login);
	setRegister(register);
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
     * Nastavi hlavni okno
     *
     * @param mainWindow Hlavni okno hry
     */
    protected void setMainWindow(MainWindow mainWindow) {
	this.mainWindow = mainWindow;
    }
    
    /**
     * Nastavi host
     *
     * @param host Host
     */
    public void setHost(String host) {
	this.host = host;
    }
    
    /**
     * Nastavi port
     *
     * @param port Port serveru
     */
    public void setPort(int port) {
	this.port = port;
    }
    
    /**
     * Nastavi login
     *
     * @param login Login hrace
     */
    public void setLogin(String login) {
	this.login = login;
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
     * Vrati hlavni okno
     *
     * @return Hlavni okno hry
     */
    public MainWindow getMainWindow() {
	return this.mainWindow;
    }
    
    /**
     * Vrati host
     *
     * @return Host
     */
    public String getHost() {
	return this.host;
    }
    
    /**
     * Vrati port
     *
     * @return Port serveru
     */
    private int getPort() {
	return this.port;
    }
    
    /**
     * Vrati login
     *
     * @return Login hrace
     */
    public String getLogin() {
	return this.login;
    }
    
    /**
     * Nastavi klientskou cast hry
     *
     * @param clientGame Klientska cast hry
     */
    private void setClientGame(ClientGame clientGame) {
	this.clientGame = clientGame;
    }
    
    /**
     * Nastavi priznak registrace
     *
     * @param register Priznak registrace
     */
    private void setRegister(boolean register) {
	this.register = register;
    }
    
    /**
     * Vrati priznak registrace
     *
     * @return Priznak registrace
     */
    private boolean getRegister() {
	return this.register;
    }
    
    /**
     * Vrati klientskou cast hry
     *
     * @return Klientska cast hry
     */
    protected ClientGame getClientGame() {
	return this.clientGame;
    }
    
    /**
     * Pripoji se k serveru
     */
    public void connect() throws IOException {
	setClientSocket(new Socket(getHost(), getPort()));

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

	if (getRegister()) {
	    getProtocolOutput().send(new ProtocolRequestRegister(getLogin()));
	    setRegister(false);
	}
	
	// Login request
	getProtocolOutput().send(new ProtocolRequestLogin(getLogin()));
	getMainWindow().enableRequestGameButton();
    }
    
    /**
     * Odpoji se od serveru
     */
    public void disconnect() {
	if (getClientSocket() != null) {
	    getProtocolOutput().send(new ProtocolRequestExit());
	    getProtocolOutput().waitEmpty();
	    setProtocolInput(null);
	    setProtocolOutput(null);
	
	    try {
		getClientSocket().close();
	    } catch (IOException e) {
	    }
	
	    setClientSocket(null);
	}
    }

    /**
     * Odesle RequestGame zpravu
     *
     * @param login Login protihrace
     */
    public void game(String login) {
        getProtocolOutput().send(new ProtocolRequestGame(login));
    }

    /**
     * Odesle ReplyGameAccept zpravu a vytvori klientskou hru
     *
     * @param login Login protihrace
     */
    public void gameAccepted(String login) {
        getProtocolOutput().send(new ProtocolReplyGameAccept(login));
        setClientGame(new ClientGame(this, Game.GameColor.BLACK, getMainWindow()));
        getMainWindow().getBoard().setGame(getClientGame());
	getMainWindow().getBoard().getGame().getGameHistory().setOpponent(login);
	getMainWindow().getBoard().getGame().getGameHistory().setMyColor(getClientGame().getMyColor());
        getMainWindow().disableTurnButton();
        getMainWindow().disableRequestGameButton();
	getMainWindow().disablePlaybackButtons();
        getMainWindow().clearTurns();
    }

    /**
     * Odesle ReplyGameReject zpravu
     *
     * @param login Login protihrace
     */
    public void gameRejected(String login) {
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
    public void turn(int x, int y, int toX, int toY) {
        getProtocolOutput().send(new ProtocolRequestTurn(x, y, toX, toY));
    }

    /**
     * Odesle RequestTurnEnd zpravu
     */    
    public void turnEnd() {
        if (getClientGame().getMyColor() != getClientGame().getOnTurn()) {
            getProtocolOutput().send(new ProtocolRequestTurnEnd());
        }
    }
    
    /**
     * Odesle RequestGameEnd zpravu
     */
    public void gameEnd() {
	getProtocolOutput().send(new ProtocolRequestGameEnd());
    }

    /**
     * Rozsynchronizovani hry
     */    
    public void gameDesync(String error) {
	getMainWindow().showError("Došlo k rozsynchronizování hry, oveřte že máte aktuální verzi klienta i serveru: " + error);
    }
    
    // OnFunctions
    public void onRegister(String login) {
	denyMethod();
    }
    
    public void onLogin(String login) {
	denyMethod();
    }
    
    public void onExit() {
	// TODO
    }
    
    public void onGame(String login) {
        getMainWindow().showGameRequest(login);
    }
    
    public void onGameEnd() {
	getMainWindow().gameEnd();
    }
    
    public void onTurn(int x, int y, int toX, int toY) {
        if (getClientGame().turn(x, y, toX, toY)) {
            getProtocolOutput().send(new ProtocolReplyTurnOk());
        } else {
            getProtocolOutput().send(new ProtocolReplyTurnError("Game desync, impossible turn!"));
        }

        getMainWindow().getBoard().repaint();
    }

    public void onTurnEnd() {
	getClientGame().turnEnd();
        getMainWindow().enableTurnButton();
    }
    
    public void onBadCommand() {
	gameDesync("");
        getMainWindow().showError("Chybný příkaz");
    }
    
    public void onLoginOk() {
    }
    
    public void onLoginError(String error) {
	getMainWindow().showError("Chyba přihlášení: " + error);
    }
    
    public void onRegisterOk() {
	getMainWindow().showInfo("Registrace byla úspěšná");
    }
    
    public void onRegisterError(String error) {
	getMainWindow().showError("Registrace selhala: " + error);
    }

    public void onUserLogin(String login) {
	getMainWindow().userLogin(login);
    }
    
    public void onUserLogout(String login) {
	getMainWindow().userLogout(login);
    }
    
    public void onUserInGame(String login) {
	getMainWindow().userInGame(login);
    }
    
    public void onUserOutGame(String login) {
	getMainWindow().userOutGame(login);
    }
    
    public void onGameAccept(String login) {
        setClientGame(new ClientGame(this, Game.GameColor.WHITE, getMainWindow()));
        getMainWindow().getBoard().setGame(getClientGame());
	getMainWindow().getBoard().getGame().getGameHistory().setOpponent(login);
	getMainWindow().getBoard().getGame().getGameHistory().setMyColor(getClientGame().getMyColor());
        getMainWindow().enableTurnButton();
        getMainWindow().disableRequestGameButton();
	getMainWindow().disablePlaybackButtons();
        getMainWindow().clearTurns();
    }
    
    public void onGameReject(String login) {
	getMainWindow().showInfo(login + " odmítl Vaši hru.");
    }
    
    public void onGameError(String error) {
	gameDesync(error);
    }
    
    public void onTurnError(String error) {
	gameDesync(error);
    }

    public void onTurnEndError(String error) {
        getMainWindow().enableTurnButton();
    }

    public void onTurnEndOk() {
	// TODO
    }
    
    public void onTurnOk() {
    }
}
