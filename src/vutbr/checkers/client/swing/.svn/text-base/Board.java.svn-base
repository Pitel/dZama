package vutbr.checkers.client.swing;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import vutbr.checkers.game.*;
import vutbr.checkers.client.*;

/**
 * Trida Board - hraci plocha
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 * @author Jan Kalab <xkalab00@stud.fit.vutbr.cz>
 */
public class Board extends JPanel implements MouseListener {
    private BufferedImage boardImg, notationWImg, notationBImg;
    private BufferedImage selectedImg;
    private BufferedImage[][] whiteManImg;
    private BufferedImage[][] blackManImg;
    private BufferedImage[][] whiteKingImg;
    private BufferedImage[][] blackKingImg;
    private Game game;
    private int selectedX, selectedY;


    /**
     * Sirka ramecku kolem sachovnice
     */
    private static final int BORDER = 32;
    /**
     * Sirka policka sachovnice
     */
    private static final int SQUARE = 67;
    /**
     * Posunuti pesaka aby vypadal uprostred policka
     */
    private static final int MANOFFSET = 18;
    /**
     * Posunuti damy aby vypadala uprostred policka
     */
    private static final int KINGOFFSET = 21;

    /**
     * Nastaveni stavu hry
     *
     * @param game Stav hry
     */
    public void setGame(Game game) {
	this.game = game;
	repaint();
    }

    /**
     * Zjisteni stavu hry
     *
     * @return Stav hry
     */
    public Game getGame() {
	return this.game;
    }

    /**
     * Vykresleni sachovnice, figurek a vybraneho ctverce
     *
     * @param g Kreslici plocha
     */
    public void paintComponent(Graphics g) {
	Game.GameColor myColor = null;
	
	if (getGame() instanceof ClientGame) {
	    myColor = ((ClientGame)getGame()).getMyColor();
	} else if (getGame() instanceof ReplayGame) {
	    myColor = ((ReplayGame)getGame()).getMyColor();
	}
    
        g.drawImage(boardImg, 0, 0, this);
        if (getGame() != null) {
            if (myColor == Game.GameColor.BLACK) {
                g.drawImage(notationBImg, 0, 0, this);
            } else {
                g.drawImage(notationWImg, 0, 0, this);
            }
            if (selectedX >= 0 && selectedY >= 0 && selectedX < 8 && selectedY < 8) {
                if (myColor == Game.GameColor.BLACK) {
                    g.drawImage(selectedImg, BORDER + (7 - selectedX) * SQUARE, BORDER + (7 - selectedY) * SQUARE, this);
                } else {
                    g.drawImage(selectedImg, BORDER + selectedX * SQUARE, BORDER + selectedY * SQUARE, this);
                }
            }
            for (int gameX = 0 ; gameX < 8 ; gameX++) {
                for (int gameY = 0 ; gameY < 8; gameY++) {
                    int x = gameX;
                    int y = gameY;
                    GamePiece piece = getGame().getBoard().getPiece(x, y);
                    if (piece != null) {
                        int posx, posy, centering = 0;
                        BufferedImage pieceImg = whiteManImg[x][y];

                        if (myColor == Game.GameColor.BLACK) {
                            x = 7 - x;
                            y = 7 - y;
                        }
                        if (piece instanceof GamePieceMan) {
                            centering = MANOFFSET;
                            if (piece.getColor() == Game.GameColor.WHITE) {
                                pieceImg = whiteManImg[x][y];
                            } else {
                                pieceImg = blackManImg[x][y];
                            }
                        }
                        if (piece instanceof GamePieceKing) {
                            centering = KINGOFFSET;
                            if (piece.getColor() == Game.GameColor.WHITE) {
                                pieceImg = whiteKingImg[x][y];
                            } else {
                                pieceImg = blackKingImg[x][y];
                            }
                        }

                        centering = SQUARE / 2 - centering;
                        if (x < 4) {
                            if (y < 4) {
                                posx = (x + 1) * SQUARE - pieceImg.getTileWidth() - centering;
                                posy = (y + 1) * SQUARE - pieceImg.getTileHeight() - centering;
                            } else {
                                posx = (x + 1) * SQUARE - pieceImg.getTileWidth() - centering;
                                posy = y * SQUARE + centering;
                            }
                        } else {
                            if (y < 4) {
                                posx = x * SQUARE + centering;
                                posy = (y + 1) * SQUARE - pieceImg.getTileHeight() - centering;
                            } else {
                                posx = x * SQUARE + centering;
                                posy = y * SQUARE + centering;
                            }
                        }
                        posx += BORDER;
                        posy += BORDER;

                        g.drawImage(pieceImg, posx, posy, this);
                    }
                }
            }
        }
    }

    /**
     * Konstruktor
     */
    public Board() {
        whiteManImg = new BufferedImage[8][8];
        blackManImg = new BufferedImage[8][8];
        whiteKingImg = new BufferedImage[8][8];
        blackKingImg = new BufferedImage[8][8];

        try {
            boardImg = ImageIO.read(getClass().getResource("/resources/board.png"));
            selectedImg = ImageIO.read(getClass().getResource("/resources/selected.png"));
            notationWImg = ImageIO.read(getClass().getResource("/resources/notation_w.png"));
            notationBImg = ImageIO.read(getClass().getResource("/resources/notation_b.png"));
        } catch (IOException e) {
            System.err.println("Image opening error!");
        }
            for (int x = 0; x < 8; x++) {
                int y = 0;
                if (x % 2 == 0) {
                    y = 1;
                }
                for (;y <= 7; y += 2) {
                    try {
                        whiteManImg[x][y] = ImageIO.read(
			    getClass().getResource("/resources/white/man/" + x + "_" + y + ".png"));
                        blackManImg[x][y] = ImageIO.read(
			    getClass().getResource("/resources/black/man/" + x + "_" + y + ".png"));
                        whiteKingImg[x][y] = ImageIO.read(
			    getClass().getResource("/resources/white/king/" + x + "_" + y + ".png"));
                        blackKingImg[x][y] = ImageIO.read(
			    getClass().getResource("/resources/black/king/" + x + "_" + y + ".png"));
                    } catch (IOException e) {
                        System.err.println("Image opening error!");
                    }
                }
            }

        setPreferredSize(new Dimension(600, 600));
    	selectedX = -1; selectedY = -1;
	addMouseListener(this);
    }

    /**
     * Obsluha kliknuti na sachovnici
     *
     * @param e Udalost kliku
     */
    public void mouseClicked(MouseEvent e) {
	if (getGame() == null) {
    	    return;
	}
	if (!(getGame() instanceof ClientGame)) {
	    return;
	}
	
	ClientGame clientGame = (ClientGame)getGame();
	
	if (e.getButton() == MouseEvent.BUTTON1) {
    	    int x, y;
    	    x = (e.getX() - BORDER) / SQUARE;
    	    y = (e.getY() - BORDER) / SQUARE;
    	    if (clientGame.getMyColor() == Game.GameColor.BLACK) {
        	x = 7 - x;
        	y = 7 - y;
    	    }
    	    if (x < 0 || y < 0 || x > 7 || y > 7) {
        	return;
    	    }

    	    GamePiece piece;
    	    if (this.selectedX >= 0 && this.selectedY >= 0) {
        	if ((this.selectedX == x && this.selectedY == y) || clientGame.myTurn(this.selectedX, this.selectedY, x, y)) {
            	    this.selectedX = -1;
            	    this.selectedY = -1;
        	}
    	    } else {
        	piece = clientGame.getBoard().getPiece(x, y);
    	        if (piece != null) {
            	    this.selectedX = x;
            	    this.selectedY = y;
        	}
    	    }
    	    this.repaint();
	} else {
	    clientGame.getClientThread().getMainWindow().turnEnd();
	}
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
