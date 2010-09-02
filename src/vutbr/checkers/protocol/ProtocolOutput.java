package vutbr.checkers.protocol;

import java.io.*;

/**
 * Trida ProtocolOutput
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolOutput extends Thread {
    private BufferedWriter bufferedWriter;
    private ProtocolQueue queue;

    /**
     * Konstruktor
     *
     * @param bufferedWriter Bufferovany vystup
     */
    public ProtocolOutput(BufferedWriter bufferedWriter) {
	this.bufferedWriter = bufferedWriter;
	this.queue = new ProtocolQueue();
    }
    
    /**
     * Hlavni smycka odesilani dat ve fronte
     */
    public void run() {
	try {
	    boolean running = true;
	    while (running) {
		String line = getQueue().next();
		if (line == null) {
		    running = false;
		} else {
		    getBufferedWriter().write(line + "\n");
		    getBufferedWriter().flush();
		}
	    }
	} catch (IOException ie) {
	}
    }
    
    /**
     * Vrati bufferovany vystup
     *
     * @return Bufferovany vystup
     */
    private BufferedWriter getBufferedWriter() {
	return this.bufferedWriter;
    }
    
    /**
     * Vrati frontu
     *
     * @return Fronta
     */
    private ProtocolQueue getQueue() {
	return this.queue;
    }
    
    /**
     * Prida pozadavek na odeslani radku do fronty
     *
     * @param line Radek k odeslani
     */
    public void send(ProtocolLine line) {
	getQueue().add(line.build());
    }
    
    /**
     * Ceka na vyprazdneni odchozi fronty
     */
    public void waitEmpty() {
	getQueue().waitEmpty();
    }
}