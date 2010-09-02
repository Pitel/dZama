package vutbr.checkers.protocol;

/**
 * Trida ProtocolLine
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public abstract class ProtocolLine {
    private String command;
    private String param;

    /**
     * Konstruktor
     */
    public ProtocolLine() {
    }
    
    /**
     * Vytvori radek z tridy
     *
     * @return Vystupni radek
     */
    protected String build() {
	String line;
	
	line = getType() + " ";
	line += getCommand();
	if (getParam().length() > 0) {
	    line += " " + getParam();
	}
	
	return line;
    }
    
    /**
     * Vrati typ prikazu
     *
     * @return Typ prikazu (REQ/REP)
     */
    protected abstract String getType();
    /**
     * Vrati jmeno prikazu
     *
     * @return Jmeno prikazu
     */
    protected abstract String getCommand();    
    /**
     * Vrati parametry prikazu
     *
     * @return Parametry prikazu
     */
    protected abstract String getParam();
}
