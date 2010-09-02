package vutbr.checkers.protocol;

import java.util.Vector;

/**
 * Trida ProtocolQueue
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class ProtocolQueue {
    private Vector<String> queue;

    /**
     * Konstruktor
     */
    public ProtocolQueue() {        
	setQueue(new Vector<String>());
    }
    
    /**
     * Prida do fronty retezec
     *
     * @param s Retezec pro pridani
     */    
    public void add(String s) {
        synchronized (queue) {
            queue.addElement(s);
            queue.notify();
        }
    }
    
    /**
     * Vrati retezec na zacatku fronty
     *
     * @return s Retezec
     */
    public String next() {        
        String s = null;
        
        synchronized (queue) {
            if (queue.size() == 0) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    return null;
                }
            }
        
            try {
                s = queue.firstElement();
                queue.removeElementAt(0);
            } catch (ArrayIndexOutOfBoundsException e) {
		return null;
            }
	    
	    synchronized (this) {
		this.notify();
	    }
        }

        return s;
    }
    
    
    /**
     * Vrati delku fronty
     *
     * @return Delka fronty
     */
    public int size() {
        return queue.size();
    }
    
    /**
     * Vycka na vyprazdneni fronty
     */
    public void waitEmpty() {
	synchronized (this) {
	    while (size() != 0) {
		try {
		    this.wait();
        	} catch (InterruptedException e) {
        	}
	    }
	}
    }
    
    /**
     * Nastavi vektor fronty
     *
     * @param Vektor fronty
     */
    private void setQueue(Vector<String> queue) {
	this.queue = queue;
    }
}
