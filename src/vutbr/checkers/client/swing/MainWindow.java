package vutbr.checkers.client.swing;

import vutbr.checkers.client.*;
import vutbr.checkers.game.Game; // FIX
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.io.*;

/**
 * Trida MainWindow - okno hry
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 * @author Jan Kalab <xkalab00@stud.fit.vutbr.cz>
 * @author Ondrej Choleva <xchole00@stud.fit.vutbr.cz>
 */
public class MainWindow extends JFrame implements ActionListener, ItemListener {
    private JMenuItem quitGameMenuItem, saveGameMenuItem, loadGameMenuItem, helpMenuItem, aboutMenuItem, setupMenuItem;
    private JCheckBoxMenuItem onlineGameMenuItem;
    private JButton requestGameButton, turnButton, beginPlaybackButton, backwardPlaybackButton, previousPlaybackButton, pausePlaybackButton, nextPlaybackButton, forwardPlaybackButton, endPlaybackButton;
    private DefaultListModel waitingPlayers, playingPlayers, turns;
    private JList waitingPlayersList, playingPlayersList;
    private Board board;
    private JFileChooser fc;
    private ClientThread clientThread;
    private ClientConfig clientConfig;
    private Timer timerForw;
    private Timer timerBack;

    /**
     * Konstruktor
     *
     * @param clientThread Vlakno klienta
     */
     public MainWindow(ClientThread clientThread, ClientConfig clientConfig) {
        super("dŽáma – " + clientConfig.getLogin() + "@" + clientConfig.getHost());

	setClientThread(clientThread);
	setClientConfig(clientConfig);

	clientThread.setMainWindow(this);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            System.err.println("Error setting native LAF: " + e);
        }
        this.setIconImage(new ImageIcon(
	    getClass().getResource("/resources/icon.png")).getImage());
        this.setJMenuBar(createMenuBar());

	fc.addChoosableFileFilter(new XMLFilter());
	fc.setAcceptAllFileFilterUsed(false);
	
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
            JPanel playersPanel = new JPanel();
            playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.PAGE_AXIS));
            playersPanel.setAlignmentY(TOP_ALIGNMENT);
                JLabel waitingPlayersLabel = new JLabel("Čekající hráči");
                playersPanel.add(waitingPlayersLabel);
                waitingPlayers = new DefaultListModel();
                waitingPlayersList = new JList(waitingPlayers);
                waitingPlayersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		MouseListener mouseListener = new MouseAdapter() {
		    public void mouseClicked(MouseEvent mouseEvent) {
			JList theList = (JList) mouseEvent.getSource();
			if (mouseEvent.getClickCount() == 2) {
			    int index = theList.locationToIndex(mouseEvent.getPoint());
			    if (index >= 0) {
				Object opponent = theList.getModel().getElementAt(index);
				getClientThread().game(opponent.toString());
			    }
			}
		    }
		};
		waitingPlayersList.addMouseListener(mouseListener);
                JScrollPane scrollWaitingPlayersList = new JScrollPane(waitingPlayersList);
                scrollWaitingPlayersList.setPreferredSize(new Dimension(100, 200));
                playersPanel.add(scrollWaitingPlayersList);

		JLabel playingPlayersLabel = new JLabel("Hrající hráči");
		playersPanel.add(playingPlayersLabel);
		playingPlayers = new DefaultListModel();
		playingPlayersList = new JList(playingPlayers);
		playingPlayersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPlayingPlayersList = new JScrollPane(playingPlayersList);
		scrollPlayingPlayersList.setPreferredSize(new Dimension(100, 200));
		playersPanel.add(scrollPlayingPlayersList);

                requestGameButton = new JButton("Vyzvat!");
                requestGameButton.setEnabled(false);
                requestGameButton.addActionListener(this);
                playersPanel.add(requestGameButton);

            JPanel boardPanel = new JPanel();
            boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.PAGE_AXIS));
            boardPanel.setAlignmentY(TOP_ALIGNMENT);
            board = new Board();
            boardPanel.add(board);
            JPanel playbackPanel = new JPanel();
            playbackPanel.setLayout(new BoxLayout(playbackPanel, BoxLayout.LINE_AXIS));
                beginPlaybackButton = new JButton("|<");
                backwardPlaybackButton = new JButton("<<");
                previousPlaybackButton = new JButton("<");
                pausePlaybackButton = new JButton("||");
                nextPlaybackButton = new JButton(">");
                forwardPlaybackButton = new JButton(">>");
                endPlaybackButton = new JButton(">|");
                beginPlaybackButton.setEnabled(false);
                backwardPlaybackButton.setEnabled(false);
                previousPlaybackButton.setEnabled(false);
                pausePlaybackButton.setEnabled(false);
                nextPlaybackButton.setEnabled(false);
                forwardPlaybackButton.setEnabled(false);
                endPlaybackButton.setEnabled(false);
                beginPlaybackButton.addActionListener(this);
                forwardPlaybackButton.addActionListener(this);
                previousPlaybackButton.addActionListener(this);
                pausePlaybackButton.addActionListener(this);
                nextPlaybackButton.addActionListener(this);
                backwardPlaybackButton.addActionListener(this);
                endPlaybackButton.addActionListener(this);
		timerForw = new Timer(1000, this);
		timerForw.stop();
		timerBack = new Timer(1000, this);
		timerBack.stop();
                playbackPanel.add(beginPlaybackButton);
                playbackPanel.add(backwardPlaybackButton);
                playbackPanel.add(previousPlaybackButton);
                playbackPanel.add(pausePlaybackButton);
                playbackPanel.add(nextPlaybackButton);
                playbackPanel.add(forwardPlaybackButton);
                playbackPanel.add(endPlaybackButton);
                boardPanel.add(playbackPanel);

            JPanel turnsPanel = new JPanel();
            turnsPanel.setLayout(new BoxLayout(turnsPanel, BoxLayout.PAGE_AXIS));
            turnsPanel.setAlignmentY(TOP_ALIGNMENT);
                JLabel turnsLabel = new JLabel("Tahy");
                turnsPanel.add(turnsLabel);
                turns = new DefaultListModel();
                JList turnsList = new JList(turns);
                turnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                JScrollPane scrollTurnsList = new JScrollPane(turnsList);
                scrollTurnsList.setPreferredSize(new Dimension(100, 500));
                turnsPanel.add(scrollTurnsList);
                turnButton = new JButton("Předat tah!");
                turnButton.setEnabled(false);
                turnButton.addActionListener(this);
                turnsPanel.add(turnButton);
        mainPanel.add(playersPanel);
        mainPanel.add(boardPanel);
        mainPanel.add(turnsPanel);
        this.setContentPane(mainPanel);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.repaint();
    }

    /**
     * Nastavi klienstske vlakno
     *
     * @param clientThread Vlakno klienta
     */
    private void setClientThread(ClientThread clientThread) {
	this.clientThread = clientThread;
    }

    /**
     * Vrati klienstske vlakno
     *
     * @return Vlakno klienta
     */
    private ClientThread getClientThread() {
	return this.clientThread;
    }

    /**
     * Pridani pripojeneho hrace do seznamu hracu
     *
     * @param login Prezdivka hrace
     */
    protected void userLogin(String login) {
        this.waitingPlayers.addElement(login);
    }

    /**
     * Odebrani odpojeneho hrace ze seznamu hracu
     *
     * @param login Prezdivka hrace
     */
    protected void userLogout(String login) {
        this.waitingPlayers.removeElement(login);
    }

    /**
     * Presun hrace z cekajicich do hrajicich
     *
     * @param login Prezdivka hrace
     */
    protected void userInGame(String login) {
	this.waitingPlayers.removeElement(login);
	this.playingPlayers.addElement(login);
    }

    /**
     * Presun hrace z hrajicich do cekajich
     *
     * @param login Prezdivka hrace
     */
    protected void userOutGame(String login) {
	this.playingPlayers.removeElement(login);
	this.waitingPlayers.addElement(login);
    }

    /**
     * Vymazani seznamu tahu
     */
    public void clearTurns() {
        turns.clear();
    }

    /**
     * Pridani tahu do seznamu tahu
     *
     * @param color Barva hrace
     * @param x Souradnice x odkud se tah provede
     * @param y Souradnice y odkud se tah provede
     * @param toX Souradnice x kam se tah provede
     * @param toY Souradnice y kam se tah provede
     */
    public void addTurn(Game.GameColor color, int x, int y, int toX, int toY) {
	String turn = new String();
	char letters[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
	if (color == Game.GameColor.BLACK) {
	    turn += "č";
	} else {
	    turn = "b";
	}
	turn += ' ';
	turn += letters[x];
	turn += 8 - y;
	turn += " → ";
	turn += letters[toX];
	turn += 8 - toY;
        turns.addElement(turn);
    }

    /**
     * Pristup k sachovnici
     *
     * @return Hraci plocha
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Povoli tlacitka pro prehravani partie dozadu
     */
    public void enableBackPlaybackButtons() {
	beginPlaybackButton.setEnabled(true);
	backwardPlaybackButton.setEnabled(true);
	previousPlaybackButton.setEnabled(true);
    }
    
    /**
     * Povoli tlacitka pro prehravani partie dopredu
     */
    public void enableForwPlaybackButtons() {
	nextPlaybackButton.setEnabled(true);
	forwardPlaybackButton.setEnabled(true);
	endPlaybackButton.setEnabled(true);
    }

    /**
     * Zakaze tlacitka pro prehravani partie dozadu
     */
    public void disableBackPlaybackButtons() {
	beginPlaybackButton.setEnabled(false);
	backwardPlaybackButton.setEnabled(false);
	previousPlaybackButton.setEnabled(false);
    }

    /**
     * Zakaze vsechna tlacitka pro prehravani partie
     */
    public void disablePlaybackButtons() {
	beginPlaybackButton.setEnabled(false);
	backwardPlaybackButton.setEnabled(false);
	previousPlaybackButton.setEnabled(false);
	nextPlaybackButton.setEnabled(false);
	pausePlaybackButton.setEnabled(false);
	forwardPlaybackButton.setEnabled(false);
	endPlaybackButton.setEnabled(false);
    }

    /**
     * Zakaze tlacitka pro prehravani partie dopredu
     */
    public void disableForwPlaybackButtons(){
	nextPlaybackButton.setEnabled(false);
	forwardPlaybackButton.setEnabled(false);
	endPlaybackButton.setEnabled(false);
    }

    /**
     * Povoli tlacitko s vyzvou pro hru
     */
    public void enableRequestGameButton() {
        requestGameButton.setEnabled(true);
    }

    /**
     * Zakaze lacitko s vyzvou pro hru
     */
    public void disableRequestGameButton() {
        requestGameButton.setEnabled(false);
    }

    /**
     * Povoli lacitko pro odeslani tahu
     */
    public void enableTurnButton() {
        turnButton.setEnabled(true);
    }

    /**
     * Zakaze lacitko pro odeslani tahu
     */
    public void disableTurnButton() {
        turnButton.setEnabled(false);
    }

    /**
     * Herní nabídka
     *
     * @return Herni menu
     */
    private JMenuBar createMenuBar() {
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Hra");
        menuBar.add(gameMenu);
        JMenu helpMenu = new JMenu("Nápověda");
        menuBar.add(helpMenu);

        saveGameMenuItem = new JMenuItem("Uložit partii");
        saveGameMenuItem.addActionListener(this);
        gameMenu.add(saveGameMenuItem);
        loadGameMenuItem = new JMenuItem("Nahrát partii");
        loadGameMenuItem.addActionListener(this);
        gameMenu.add(loadGameMenuItem);
        gameMenu.addSeparator();
        onlineGameMenuItem = new JCheckBoxMenuItem("On-line");
        onlineGameMenuItem.addItemListener(this);
        gameMenu.add(onlineGameMenuItem);
        gameMenu.addSeparator();
	setupMenuItem = new JMenuItem("Nastavení");
	setupMenuItem.addActionListener(this);
	gameMenu.add(setupMenuItem);
	gameMenu.addSeparator();
        quitGameMenuItem = new JMenuItem("Konec");
        quitGameMenuItem.addActionListener(this);
        gameMenu.add(quitGameMenuItem);

        helpMenuItem = new JMenuItem("Nápověda");
        helpMenuItem.addActionListener(this);
        helpMenu.add(helpMenuItem);
        aboutMenuItem = new JMenuItem("O programu");
        aboutMenuItem.addActionListener(this);
        helpMenu.add(aboutMenuItem);

        return menuBar;
    }

    /**
     * Obsluha stisku tlacitek apod.
     *
     * @param e Udalost
     */
    public void actionPerformed(ActionEvent e) {
        // Pozadavek na hru
        if (e.getSource() == requestGameButton) {
            Object opponent = waitingPlayersList.getSelectedValue();
            if (opponent != null) {
                getClientThread().game(opponent.toString());
            } else {
                showError("Nevybral jsi žádného protihráče!");
            }
        }

        // Odeslani tahu
        if (e.getSource() == turnButton) {
	    turnEnd();
        }

        // Konec hry
        if (e.getSource() == quitGameMenuItem) {
            System.exit(0);
        }

        // Nacteni partie
        if (e.getSource() == loadGameMenuItem) {
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

		ReplayGame replayGame = new ReplayGame(this);
		replayGame.getGameHistory().setHistoryFile(file.getPath());
		replayGame.getGameHistory().load();
		replayGame.setMyColor(replayGame.getGameHistory().getMyColor());
		getBoard().setGame(replayGame);
		while(replayGame.getGameHistory().replayTurnBack()){}
		enableForwPlaybackButtons();
		disableBackPlaybackButtons();
            }
        }

        // Ulozeni partie
        if (e.getSource() == saveGameMenuItem) {
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
		String name = file.getPath();
		if (!name.endsWith(".xml")) {
		    name = name + ".xml";
		}
		// nelze ulozit hru ktera neexistuje
		if (getClientThread().getClientGame() != null) {
		    getClientThread().getClientGame().getGameHistory().
			    setHistoryFile(name);
		    getClientThread().getClientGame().getGameHistory().
			    save();
		}
            }
        }

	// Nastaveni
	if (e.getSource() == setupMenuItem) {
	    new SetupWindow(getClientConfig(), this, getClientThread());
	}

        // Napoveda
        if (e.getSource() == helpMenuItem) {
            new HelpWindow(getClientConfig().getHelpUrl());
        }

        // O programu
        if (e.getSource() == aboutMenuItem) {
            new AboutWindow();
        }

	// Prehravani partie
	//pretoceni na zacatek
	if (e.getSource() == beginPlaybackButton) {
	    while(getBoard().getGame().getGameHistory().replayTurnBack()){}
	    enableForwPlaybackButtons();
	    disableBackPlaybackButtons();
	    pausePlaybackButton.setEnabled(false);
	    timerForw.stop();
	    timerBack.stop();
	    getBoard().repaint();
	}
	
	// automaticke prehravani dozadu - spusteni
	if (e.getSource() == backwardPlaybackButton) {
	    pausePlaybackButton.setEnabled(true);
	    backwardPlaybackButton.setEnabled(false);
	    timerBack.start();
	    timerForw.stop();
	}
	
	// automaticke prehravani dozadu - krokovani
	if (e.getSource() == timerBack) {
	    if (getBoard().getGame().getGameHistory().replayTurnBack())
		enableForwPlaybackButtons();
	    else {
		timerBack.stop();
		disableBackPlaybackButtons();
		pausePlaybackButton.setEnabled(false);
	    }
	    getBoard().repaint();
	}
	
	// rucni krokovani dozadu
	if (e.getSource() == previousPlaybackButton) {
	    timerBack.stop();
	    timerForw.stop();
	    pausePlaybackButton.setEnabled(false);
	    backwardPlaybackButton.setEnabled(true);
	    if (getBoard().getGame().getGameHistory().replayTurnBack())
		enableForwPlaybackButtons();
	    else {
		disableBackPlaybackButtons();
	    }
	    getBoard().repaint();
	}
	
	//pauza
	if (e.getSource() == pausePlaybackButton) {
	    timerBack.stop();
	    timerForw.stop();
	    pausePlaybackButton.setEnabled(false);
	    forwardPlaybackButton.setEnabled(true);
	    backwardPlaybackButton.setEnabled(true);
	}
	
	// rucni krokovani dopredu
	if (e.getSource() == nextPlaybackButton) {
	    timerBack.stop();
	    timerForw.stop();
	    pausePlaybackButton.setEnabled(false);
	    forwardPlaybackButton.setEnabled(true);
	    if (getBoard().getGame().getGameHistory().replayTurnForw())
		enableBackPlaybackButtons();
	    else {
		disableForwPlaybackButtons();
	    }
	    getBoard().repaint();
	}
	// automaticke prehravani dopredu - spusteni
	if (e.getSource() == forwardPlaybackButton) {
	    pausePlaybackButton.setEnabled(true);
	    forwardPlaybackButton.setEnabled(false);
	    timerForw.start();
	    timerBack.stop();
	}
	
	// automaticke prehravani dopredu - krokovani
	if (e.getSource() == timerForw) {
	    if (getBoard().getGame().getGameHistory().replayTurnForw())
		enableBackPlaybackButtons();
	    else {
		timerForw.stop();
		disableForwPlaybackButtons();
		pausePlaybackButton.setEnabled(false);
	    }
	    getBoard().repaint();
	}
	
	//pretoceni nakonec
	if (e.getSource() == endPlaybackButton) {
	    while(getBoard().getGame().getGameHistory().replayTurnForw()){}
	    disableForwPlaybackButtons();
	    enableBackPlaybackButtons();
	    pausePlaybackButton.setEnabled(false);
	    timerForw.stop();
	    timerBack.stop();
	    getBoard().repaint();
	}
    }

    /**
     * Zmena stavu prepinase On/Off-line v nabidce
     *
     * @param e Udalost
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == onlineGameMenuItem) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                waitingPlayers.clear();
		playingPlayers.clear();
                turns.clear();
		try {
        	    getClientThread().connect();
		} catch (IOException ie) {
		    showError("Chyba připojování: " + ie.getMessage());
		    onlineGameMenuItem.setState(false);
		}
            } else {
                disableRequestGameButton();
                getClientThread().disconnect();
                waitingPlayers.clear();
		playingPlayers.clear();
                turns.clear();
            }
	}
    }

    /**
     * Oskrtne polozku On-line v menu
     */
    public void uncheckOnlineMenuItem() {
        onlineGameMenuItem.setState(false);
    }

    /**
     * Zobrazeni dotazu na hru
     *
     * @param opponent Prezdivka soupere
     */
    public void showGameRequest(String opponent) {
        int answer = JOptionPane.showConfirmDialog(this, "Chcete hrát s " + opponent + "?", "Chcete hrát?", JOptionPane.YES_NO_OPTION);
        if (answer == 0) {
            getClientThread().gameAccepted(opponent);
        } else {
            getClientThread().gameRejected(opponent);
        }
    }

    /**
     * Konec tahu
     */
    public void turnEnd() {
        if (getClientThread().getClientGame().turnEnd()) {
            disableTurnButton();
        }
    }

    /**
     * Zobrazi chybove okno
     *
     * @param message Zprava
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "D'oh!", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Zobrazi informacni okno
     *
     * @param message Zprava
     */
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Vrati klientskou konfiguraci
     *
     * @return Klientska konfigurace
     */
    private ClientConfig getClientConfig() {
        return this.clientConfig;
    }

    /**
     * Nastavi klientskou konfiguraci
     *
     * @param config Klientska konfigurace
     */
    private void setClientConfig(ClientConfig config) {
        this.clientConfig = config;
    }

    /**
     * Konec hry
     */    
    public void gameEnd() {
        this.disableTurnButton();
        this.enableRequestGameButton();
        showInfo("Konec hry");
    }
}
