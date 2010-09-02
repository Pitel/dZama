package vutbr.checkers.game;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.*;
import java.io.*;
import java.util.*;

/**
 * Historie tahu
 * 
 * @author Ondrej Choleva <xchole00@stud.fit.vutbr.cz>
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class GameHistory {
    private String historyFile;
    private String opponent;
    private Game.GameColor myColor;
    
    private LinkedList<GameHistoryMove> moveList;
    private GameHistoryMove currentMove;
    private GameHistoryTurn lastTurn;
    private Game game;
    private int turnNumber;
    
    /**
     * konstruktor
     * 
     * @param game Hra
     */
    public GameHistory (Game game) {
	setGame(game);
	moveList = new LinkedList<GameHistoryMove>();
	
	setMyColor(Game.GameColor.WHITE); //default
	setOpponent("");
	setHistoryFile(opponent + ".xml");
	setTurnNumber(0);
	
	currentMove = new GameHistoryMove();
    }
    
    /**
     * Ulozi tah
     * 
     * @param x Vychozi souradnice x
     * @param y Vychozi souradnice y
     * @param toX Cilova souradnice x
     * @param toY Cilova souradnice y
     */
    protected void turn(int x, int y, int toX, int toY) {
	this.lastTurn  = new GameHistoryTurn();
	this.lastTurn.setTurn(x, y, toX, toY);
	currentMove.addTurn(this.lastTurn);
    }
    
    /**
     * Ulozi konec tahu
     */
    protected void turnEnd() {
	moveList.add(currentMove);
	currentMove = new GameHistoryMove();
	setTurnNumber(getTurnNumber() + 1);
    }
    
    /**
     * Nastavi sachovnici podle tahu predeneho v turn smerem dopredu
     * Pokud neexistuje zadny nasledujici tah, vraci false
     * 
     * @return True, false
     */
    public boolean replayTurnForw() {
	int k = 0;
	int tempTurnNumber = getTurnNumber() + 1;
	LinkedList<GameHistoryTurn> tempTurnList = new LinkedList<GameHistoryTurn>();
	
	//pozadovany tah musi byt v platnem rozmezi
	if (tempTurnNumber <= 0 || tempTurnNumber > moveList.size()) {
	    return false;
	}
	
	//najde pozadovany tah
	for (Iterator<GameHistoryMove> i = moveList.iterator();
		i.hasNext() && k < tempTurnNumber; k++) {
	    GameHistoryMove tempMove = i.next();
	    tempTurnList = tempMove.getTurnList();
	}

	//nastavi sachovnici odpovidajicim zpusobem
	for (Iterator<GameHistoryTurn> j = tempTurnList.iterator();
		j.hasNext();) {
	    GameHistoryTurn turn = j.next();

	    //posune figurku
	    getGame().getBoard().movePiece(turn.getX(), turn.getY(),
		    turn.getToX(), turn.getToY());

	    //odstrani figurku
	    if (turn.getRemoved() != null) {
		getGame().getBoard().removePiece(turn.getRemovedX(), turn.getRemovedY());
	    }

	    //zmeni figurku
	    if (turn.isChangedToKing() == true) {
		getGame().getBoard().switchPiece(turn.getToX(), turn.getToY(),
			new GamePieceKing(getGame().getBoard(),
			turn.getToX(), turn.getToY(),
			getGame().getOnTurn()));
	    }
	}
	
	//zmena barvy
	switch (getGame().getOnTurn()) {
	    case WHITE: getGame().setOnTurn(Game.GameColor.BLACK); break;
	    case BLACK: getGame().setOnTurn(Game.GameColor.WHITE); break;
	}
	
	setTurnNumber(tempTurnNumber);
	return true;
    }
    
    
    /**
     * Nastavi sachovnici podle tahu predeneho v turn smerem dozadu
     * Pokud neexistuje zadny predchozi tah, vraci false
     * 
     * @return True, false
     */
    public boolean replayTurnBack() {
	int k = 0;
	int l = 0;
	LinkedList<GameHistoryTurn> tempTurnList = new LinkedList<GameHistoryTurn>();
	GameHistoryTurn turn = new GameHistoryTurn();

	//pozadovany tah musi byt v platnem rozmezi
	if (getTurnNumber() <= 0 || getTurnNumber() > moveList.size()) {
	    return false;
	}

	//najde pozadovany tah
	for (Iterator<GameHistoryMove> i = moveList.iterator();
		i.hasNext() && k < getTurnNumber(); k++) {
	    GameHistoryMove tempMove = i.next();
	    tempTurnList = tempMove.getTurnList();
	}

	//nastavi sachovnici odpovidajicim zpusobem
	//krokujeme dozadu proto musime i seznam tahy prochazet pozpatku.
	for (l = tempTurnList.size(); l > 0; l--) {
	    
	    int m = 0;
	    for (Iterator<GameHistoryTurn> j = tempTurnList.iterator();
		j.hasNext() && m < l; m++) {
		turn = j.next();
	    }

	    //zmeni figurku
	    if (turn.isChangedToKing() == true) {
		Game.GameColor color = null;
		switch (getGame().getOnTurn()) {
		    case WHITE: color = Game.GameColor.BLACK; break;
		    case BLACK: color = Game.GameColor.WHITE; break;
		}
		getGame().getBoard().switchPiece(turn.getToX(), turn.getToY(),
			new GamePieceMan(getGame().getBoard(),
			turn.getToX(), turn.getToY(), color));
	    }
	    
	    //posune figurku
	    getGame().getBoard().movePiece(turn.getToX(), turn.getToY(),
		    turn.getX(), turn.getY());

	    //prida figurku
	    if (turn.getRemoved() != null) {
		getGame().getBoard().addPiece(turn.getRemovedX(), turn.getRemovedY(),
			turn.getRemoved());
		getGame().getBoard().getPiece(turn.getRemovedX(), turn.getRemovedY()).setColor(
			getGame().getOnTurn());
	    }
	}
	
	//zmena barvy
	switch (getGame().getOnTurn()) {
	    case WHITE: getGame().setOnTurn(Game.GameColor.BLACK); break;
	    case BLACK: getGame().setOnTurn(Game.GameColor.WHITE); break;
	}
	
	setTurnNumber(getTurnNumber() - 1);
	return true;
    }
    
    /**
     * Nastavi vyhozenou figurku
     *
     * @param x Souradnice X vyhozene firuky
     * @param y Souradnice Y vyhozene figurky
     * @param piece Typ vyhozene figurky
     */
    protected void setRemoved(int x, int y, GamePiece piece) {
	this.lastTurn.setRemoved(x, y, piece);
    }
    
    /**
     * Nastavi priznak zmeny na damu
     */
    protected void setChangedToKing() {
	this.lastTurn.setChangedToKing();
    }
    
    /**
     * Nacte soubor historie
     */
    public void load() {
	try {
	    int tempTurnNumber = 0;
	    GameHistoryMove tempMove = new GameHistoryMove();
	    
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    
	    Document doc = db.parse(getHistoryFile());
	    Node root = doc.getDocumentElement();
	    setOpponent(root.getAttributes().getNamedItem("opponent").getNodeValue());
	    
	    if (root.getAttributes().getNamedItem("color") != null) {
		if (root.getAttributes().getNamedItem("color").getNodeValue().equalsIgnoreCase("BLACK")) {
		    setMyColor(Game.GameColor.BLACK);
		} else {
		    setMyColor(Game.GameColor.WHITE);
		}
	    }
	    
	    NodeList moveNodeList = root.getChildNodes();
	    
	    for (int i = 0; i < moveNodeList.getLength(); i++) {
		Node move = moveNodeList.item(i);
		if (move.getNodeType() != Node.ELEMENT_NODE) continue;
		   
		NodeList turnNodeList = move.getChildNodes();
		
		for (int j = 0; j < turnNodeList.getLength(); j++) {
		    Node turn = turnNodeList.item(j);
		    if (turn.getNodeType() != Node.ELEMENT_NODE) continue;
		    getGame().turn(Integer.valueOf(turn.getAttributes().getNamedItem("x").getNodeValue()),
			    Integer.valueOf(turn.getAttributes().getNamedItem("y").getNodeValue()),
			    Integer.valueOf(turn.getAttributes().getNamedItem("toX").getNodeValue()),
			    Integer.valueOf(turn.getAttributes().getNamedItem("toY").getNodeValue()));
		}
		getGame().turnEnd();
		
		tempTurnNumber++;

		tempMove.getTurnList().clear();
	    }
	    
	    setTurnNumber(tempTurnNumber);
	} catch (ParserConfigurationException e) {
	    System.out.println("ParserConfigurationException: " + e.getMessage());	
	} catch (IOException e) {
	    System.out.println("IOException: " + e.getMessage());
	} catch (SAXException e) {
	    System.out.println("Sax exception: " + e.getMessage());
	}
    }
    
    
    /**
     * Ulozi soubor historie
     */
    public void save() {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
	try {
    	    DocumentBuilder db = dbf.newDocumentBuilder();
    	    Document doc = db.newDocument();

	    // Korenovy element
	    Element root = doc.createElementNS(null, "game");
	    root.setAttributeNS(null, "opponent", getOpponent());
	    switch (getMyColor()) {
		case WHITE: root.setAttributeNS(null, "color", "WHITE"); break;
		case BLACK: root.setAttributeNS(null, "color", "BLACK"); break;
	    }
	    
	    // Elementy tahu
	    for (Iterator<GameHistoryMove> i = moveList.iterator();
		i.hasNext(); ) {
		GameHistoryMove tempMove = i.next();
		Element move = doc.createElementNS(null, "move");
		List<GameHistoryTurn> tempTurnList = tempMove.getTurnList();
		
		// Elementy posunuti figurkou
		for (Iterator<GameHistoryTurn> j = tempTurnList.iterator();
		j.hasNext(); ) {
		    GameHistoryTurn tempTurn = j.next();
		    
		    Element turn = doc.createElementNS(null, "turn");
		    turn.setAttributeNS(null, "x", Integer.toString(tempTurn.getX()));
		    turn.setAttributeNS(null, "y", Integer.toString(tempTurn.getY()));
		    turn.setAttributeNS(null, "toX", Integer.toString(tempTurn.getToX()));
		    turn.setAttributeNS(null, "toY", Integer.toString(tempTurn.getToY()));
		    
		    move.appendChild(turn);
		}
		
		root.appendChild(move);
	    }
	    
	    doc.appendChild(root);
	    
	    Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	    StreamResult result = new StreamResult(new FileWriter(getHistoryFile()));
	    DOMSource source = new DOMSource(doc);
	    transformer.transform(source, result);
	} catch (TransformerConfigurationException e) {
	    System.out.println("TransformerConfigurationException");	
	} catch (TransformerException e) {
	    System.out.println("TransformerException");	
	} catch (IOException e) {
	    System.out.println("IO exception: " + e.getMessage());
	} catch (ParserConfigurationException pce) {
	    System.out.println("ParserConfigurationException");
	}
    }
    
    /**
     * Nastavi jmeno souboru historie
     *
     * @param historyFile Jmeno souboru s historii
     */
    public void setHistoryFile(String historyFile) {
	this.historyFile = historyFile;
    }
    
    /**
     * Vrati jmeno souboru historie
     *
     * @return Jmeno souboru s historii
     */
    private String getHistoryFile() {
	return this.historyFile;
    }
    
    /**
     * Nastavi jmeno protihrace
     *
     * @param opponent Jmeno protihrace
     */
    public void setOpponent(String opponent) {
	this.opponent = opponent;
    }
    
    /**
     * Vrati jmeno protihrace
     *
     * @return Jmeno protihrace
     */
    public String getOpponent() {
	return this.opponent;
    }
    
    /**
     * Nastavi hru
     *
     * @param board Hra
     */
    private void setGame(Game game) {
	this.game = game;
    }
    
    /**
     * Vrati aktualni hru
     *
     * @return Aktualni hra
     */
    public Game getGame() {
	return this.game;
    }
    
    /**
     * Nastavi cislo tahu
     *
     * @param turnNumber Cislo tahu
     */
    private void setTurnNumber(int turnNumber) {
	this.turnNumber = turnNumber;
    }
    
    /**
     * Vrati cislo tahu
     *
     * @return Cislo tahu
     */
    public int getTurnNumber() {
	return this.turnNumber;
    }
    
    /**
     * Nastavi barvu hrace
     * 
     * @param myColor Barva
     */
    public void setMyColor(Game.GameColor myColor) {
	this.myColor = myColor;
    }
    
    /**
     * Vrati barvu hrace
     * 
     * @return Barva
     */
    public Game.GameColor getMyColor() {
	return this.myColor;
    }
}
