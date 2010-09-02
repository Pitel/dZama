package vutbr.checkers.server;

import vutbr.checkers.game.*;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Trida Server
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class Server {
    private int port;
    private InetAddress bindAddress;
    private ServerSocket serverSocket;
    private Hashtable<String,ServerThread> clients;
    private ServerConfig serverConfig;

    /**
     * Konstruktor
     *
     * @param configFile Jmeno konfiguracniho souboru
     */
    public Server(String configFile) {
	setServerConfig(new ServerConfig(configFile));
    
	setPort(getServerConfig().getPort());
	try {
	    setBindAddress(InetAddress.getByName(
		getServerConfig().getBindAddress()));
	} catch (UnknownHostException e) {
	    System.err.println("Bind adresa neni platna!");
	    System.exit(-1);
	}
    
	try {
	    setServerSocket(new ServerSocket(getPort(), 0, getBindAddress()));
	} catch (IOException e) {
	    System.err.println("Chyba startu ServerSocketu");
	    System.exit(-1);
	}
	
	System.out.println("Server spusten");
	
	clients = new Hashtable<String,ServerThread>();
	boolean running = true;
	while (running) {
	    try {
		accept();
	    } catch (IOException e) {
		System.err.println("Chyba acceptu");
		running = false;
	    }
	}
    }
    
    /**
     * Prijme spojeni od klienta
     */
    public ServerThread accept() throws IOException {
	ServerThread st;
	st = new ServerThread(getServerSocket().accept());
	st.setServer(this);
	st.start();
	return st;
    }
    
    /**
     * Uzavreni spojeni ke klientovi
     */
    public void close() throws IOException {
	try {
	    getServerSocket().close();
	} catch (IOException e) {
	    System.err.println("Chyba zavirani SS");
	}
    }
    
    /**
     * Nastavi konfiguraci serveru
     *
     * @param serverConfig Konfigurace serveru
     */
    private void setServerConfig(ServerConfig serverConfig) {
	this.serverConfig = serverConfig;
    }
    
    /**
     * Vrati konfiguraci serveru
     *
     * @return Konfigurace serveru
     */
    private ServerConfig getServerConfig() {
	return this.serverConfig;
    }
    
    /**
     * Nastavi port
     *
     * @param port Port
     */
    private void setPort(int port) {
	this.port = port;
    }
    
    /**
     * Vrati port
     *
     * @return Port
     */
    public int getPort() {
	return this.port;
    }
    
    /**
     * Nastavi bind adresu
     *
     * @param bindAddress Bind adresa
     */
    private void setBindAddress(InetAddress bindAddress) {
	this.bindAddress = bindAddress;
    }
    
    /**
     * Vrati bind adresu
     *
     * @return Bind adresa
     */
    private InetAddress getBindAddress() {
	return this.bindAddress;
    }
    
    /**
     * Nastavi soket serveru
     *
     * @param serverSocket soket serveru
     */
    private void setServerSocket(ServerSocket serverSocket) {
	this.serverSocket = serverSocket;
    }
    
    /**
     * Vrati soket serveru
     *
     * @return Soket serveru
     */
    private ServerSocket getServerSocket() {
	return this.serverSocket;
    }
    
    /**
     * Provede prihlaseni uzivatele
     * Odesle mu seznam aktualnich uzivatelu a r ozesle informaci o prihlaseni
     * ostatnim uzivatelum
     *
     * @param serverThread Vlakno serveru
     */
    public synchronized boolean login(ServerThread serverThread) {
	if (clients.containsKey(serverThread.getLogin().toLowerCase())) {
	    return false;
	}
	
	// Send login info to other clients
	Enumeration<ServerThread> e = getClients().elements();
    	while (e.hasMoreElements()) {
	    e.nextElement().userLogin(serverThread.getLogin());
	}

	// Send users list to client
	Enumeration<ServerThread> e2 = getClients().elements();
    	while (e2.hasMoreElements()) {
	    ServerThread serverThread2 = e2.nextElement();
	    serverThread.userLogin(serverThread2.getLogin());
	    if (serverThread2.getServerGame() != null) {
		serverThread.userInGame(serverThread2.getLogin());
	    }
	}

	// Append client to list
	clients.put(serverThread.getLogin().toLowerCase(), serverThread);
	
	return true;
    }
    
    /**
     * Provede odhlaseni uzivatele
     * Odesle informaci o odhlaseni ostatnim uzivatelum
     *
     * @param serverThread Vlakno serveru
     */
    public synchronized void logout(ServerThread serverThread) {
	if (serverThread.getLogin() != null) {
	    clients.remove(serverThread.getLogin().toLowerCase());
	    Enumeration<ServerThread> e = getClients().elements();
    	    while (e.hasMoreElements()) {
		e.nextElement().userLogout(serverThread.getLogin());
	    }
	}
    }
    
    /**
     * Odesle informaci ostatnim hracum, ze je tento hrac ve hre
     *
     * @param login Login hrace
     */
    public synchronized void userInGame(String login) {
	Enumeration<ServerThread> e = getClients().elements();
    	while (e.hasMoreElements()) {
	    ServerThread serverThread = e.nextElement();
	    if (!serverThread.getLogin().toLowerCase().
		    equals(login.toLowerCase())) {
		serverThread.userInGame(login);
	    }
	}
    }
    
    /**
     * Odesle informaci ostatnim hracum, ze je tento hrac uz neni ve hre
     *
     * @param login Login hrace
     */
    public synchronized void userOutGame(String login) {
	Enumeration<ServerThread> e = getClients().elements();
    	while (e.hasMoreElements()) {
	    ServerThread serverThread = e.nextElement();
	    if (!serverThread.getLogin().toLowerCase().
		    equals(login.toLowerCase())) {
		serverThread.userOutGame(login);
	    }
	}
    }
    
    /**
     * Vrati seznamu uzivatelu
     *
     * @return Seznam uzivatelu
     */
    public synchronized Hashtable<String,ServerThread> getClients() {
	return this.clients;
    }
    
    /**
     * Odesle zadost o hru
     *
     * @param serverThread Vlakno serveru
     * @param login Login protihrace
     */
    public synchronized boolean game(ServerThread serverThread, String login) {
	ServerThread st = getClients().get(login.toLowerCase());
	if (st == null) {
	    return false;
	} else {
	    st.game(serverThread.getLogin());
	    return true;
	}
    }

    /**
     * Odesle potvrzeni hry
     *
     * @param serverThread Vlakno serveru
     * @param login Login protihrace
     */     
    public synchronized boolean gameAccept(ServerThread serverThread, String login) {
	ServerThread st = getClients().get(login.toLowerCase());
	if (st == null) {
	    return false;
	} else {
	    st.gameAccept(serverThread.getLogin());
	    return true;
	}
    }

    /**
     * Odesle odmitnuti hry
     *
     * @param serverThread Vlakno serveru
     * @param login Login protihrace
     */     
    public synchronized boolean gameReject(ServerThread serverThread, String login) {
	ServerThread st = getClients().get(login.toLowerCase());
	if (st == null) {
	    return false;
	} else {
	    st.gameReject(serverThread.getLogin());
	    return true;
	}
    }

    /**
     * Provede tah figurkou
     *
     * @param x Zdrojova souradnice X
     * @param y Zdrojova souradnice Y
     * @param toX Cilova souradnice X
     * @param toY Cilova souradnice Y
     * @return True pokud se tah povedl
     */
    public synchronized boolean turn(String login, int x, int y, int toX, int toY) {
	ServerThread st = getClients().get(login.toLowerCase());
	if (st == null) {
	    return false;
	} else {
	    st.turn(x, y, toX, toY);
	    return true;
	}
    }

    public synchronized boolean turnEnd(String login) {
	ServerThread st = getClients().get(login.toLowerCase());
	if (st == null) {
	    return false;
	} else {
	    st.turnEnd();
	    return true;
	}
    }
    
    /**
     * Odesle konec hry
     *
     * @param login Login protihrace
     */     
    public synchronized boolean gameEnd(String login) {
	ServerThread st = getClients().get(login.toLowerCase());
	if (st == null) {
	    return false;
	} else {
	    st.gameEnd();
	    return true;
	}
    }
    
    /**
     * Nastavi server game protihraci
     *
     * @param login Login protihrace
     * @param serverGame Server game
     */
    public synchronized boolean serverGame(String login, ServerGame serverGame) {
	ServerThread st = getClients().get(login.toLowerCase());
	if (st == null) {
	    return false;
	} else {
	    st.setServerGame(serverGame);
	    return true;
	}
    }
}
