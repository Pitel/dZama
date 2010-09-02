package vutbr.checkers.game;

import java.util.*;

/**
 * Historie posunuti v kazdem tahu
 * 
 * @author Ondrej Choleva <xchole00@stud.fit.vutbr.cz>
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class GameHistoryMove {
    protected LinkedList<GameHistoryTurn> turnList;
    
    public GameHistoryMove() {
	turnList = new LinkedList<GameHistoryTurn>();
    }
    
    /**
     * Ulozi tah do seznamu
     *
     * @param turn Tah
     */
    protected void addTurn(GameHistoryTurn turn) {
	turnList.add(turn);
    }
    
    /**
     * Vraci seznam posunuti
     * 
     * @return Seznam posunuti
     */
    protected LinkedList<GameHistoryTurn> getTurnList(){
	return this.turnList;
    }
}
