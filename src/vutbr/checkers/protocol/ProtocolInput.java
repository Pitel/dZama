package vutbr.checkers.protocol;

import java.io.*;
import vutbr.checkers.protocol.reply.*;

/**
 * Trida ProtocolInput
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolInput extends Thread {
    private BufferedReader bufferedReader;
    private ProtocolQueue queue;
    private ProtocolClient client;

    /**
     * Konstruktor
     *
     * @param bufferedReader Bufferovany vstup
     * @param protocolClient Klient
     */
    public ProtocolInput(BufferedReader bufferedReader, ProtocolClient protocolClient) {
	this.bufferedReader = bufferedReader;
	this.client = protocolClient;
    }
    
    /**
     * Hlavni smycka cteni dat
     */
    public void run() {
	boolean running = true;
	while (running) {
	    try {
		String line = getBufferedReader().readLine();
		if (line == null) {
		    running = false;
		} else {
		    parse(line);
		}
	    } catch (IOException ie) {
		running = false;
	    }
	}
	getClient().onExit();
    }
    
    /**
     * Vrati bufferovany vstup
     *
     * @return Bufferovany vstup
     */
    private BufferedReader getBufferedReader() {
	return this.bufferedReader;
    }
    
    /**
     * Vrati klienta
     *
     * @return Klient
     */
    private ProtocolClient getClient() {
	return this.client;
    }
    
    /**
     * Rozeber vstupni radek a zavolej patricne zpracujici funkce
     *
     * @param line Vstupni radek
     */
    private void parse(String line) {
	int pos = line.indexOf(" ");
	String type;
	String cmd;
	String par;
	if (pos <= 0) {
	    getClient().getProtocolOutput().send(new ProtocolReplyBadCommand());
	    return;
	}
	
	type = line.substring(0, pos);
	cmd = line.substring(pos + 1);
	pos = cmd.indexOf(" ");
	if (pos > 0) {
	    par = cmd.substring(pos + 1);
	    cmd = cmd.substring(0, pos);
	} else {
	    par = "";
	}
	
	// Request
	if (type.toLowerCase().equals("req")) {
	    if (cmd.toLowerCase().equals("login")) {
		getClient().onLogin(par);
	    } else if (cmd.toLowerCase().equals("register")) {
		getClient().onRegister(par);
	    } else if (cmd.toLowerCase().equals("exit")) {
		getClient().onExit();
	    } else if (cmd.toLowerCase().equals("ping")) {
		getClient().onPing();
	    } else if (cmd.toLowerCase().equals("game")) {
		getClient().onGame(par);
	    } else if (cmd.toLowerCase().equals("game_end")) {
		getClient().onGameEnd();
	    } else if (cmd.toLowerCase().equals("turn")) {
		String x, y, toX, toY;
		
		pos = par.indexOf(" ");
		x = par.substring(0, pos);
		par = par.substring(pos + 1);
		
		pos = par.indexOf(" ");
		y = par.substring(0, pos);
		par = par.substring(pos + 1);
		
		pos = par.indexOf(" ");
		toX = par.substring(0, pos);
		par = par.substring(pos + 1);
		
		toY = par;
		
		getClient().onTurn(Integer.parseInt(x), 
		    Integer.parseInt(y),
		    Integer.parseInt(toX),
		    Integer.parseInt(toY));
	    } else if (cmd.toLowerCase().equals("turn_end")) {
		getClient().onTurnEnd();
		
	    // Bad command
    	    } else {
		getClient().getProtocolOutput().send(new ProtocolReplyBadCommand());
	    }
	}
	
	// Reply
	else if (type.toLowerCase().equals("rep")) {
	    if (cmd.toLowerCase().equals("bad_command")) {
		getClient().onBadCommand();
	    } else if (cmd.toLowerCase().equals("login_ok")) {
		getClient().onLoginOk();
	    } else if (cmd.toLowerCase().equals("login_error")) {
		getClient().onLoginError(par);
	    } else if (cmd.toLowerCase().equals("register_ok")) {
		getClient().onRegisterOk();
	    } else if (cmd.toLowerCase().equals("register_error")) {
		getClient().onRegisterError(par);
	    } else if (cmd.toLowerCase().equals("user_login")) {
		getClient().onUserLogin(par);
	    } else if (cmd.toLowerCase().equals("user_logout")) {
		getClient().onUserLogout(par);
	    } else if (cmd.toLowerCase().equals("user_in_game")) {
		getClient().onUserInGame(par);
	    } else if (cmd.toLowerCase().equals("user_out_game")) {
		getClient().onUserOutGame(par);
	    } else if (cmd.toLowerCase().equals("pong")) {
		getClient().onPong();
	    } else if (cmd.toLowerCase().equals("game_accept")) {
		getClient().onGameAccept(par);
	    } else if (cmd.toLowerCase().equals("game_reject")) {
		getClient().onGameReject(par);
	    } else if (cmd.toLowerCase().equals("game_error")) {
		getClient().onGameError(par);
	    } else if (cmd.toLowerCase().equals("turn_error")) {
		getClient().onTurnError(par);
	    } else if (cmd.toLowerCase().equals("turn_ok")) {
		getClient().onTurnOk();
	    } else if (cmd.toLowerCase().equals("turn_end_error")) {
		getClient().onTurnEndError(par);
	    } else if (cmd.toLowerCase().equals("turn_end_ok")) {
		getClient().onTurnEndOk();
    	    
	    // Bad command
	    } else {
		getClient().getProtocolOutput().send(new ProtocolReplyBadCommand());
	    }

	// Bad command
	} else {
	    getClient().getProtocolOutput().send(new ProtocolReplyBadCommand());
	}
    }
}