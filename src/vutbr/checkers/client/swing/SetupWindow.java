package vutbr.checkers.client.swing;

import vutbr.checkers.client.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Trida SetuptWindow - Nastaveni
 *
 * @author Jan Kalab <xkalab00@stud.fit.vutbr.cz>
 */
public class SetupWindow extends JFrame implements ActionListener {
    private JTextField nickField, serverField, helpField;
    private JSpinner portSpinner;
    private JButton okButton, cancelButton;
    private ClientConfig clientConfig;
    private ClientThread clientThread;
    private MainWindow mainWindow;

    /**
     * Konstruktor
     *
     * @param clientConfig Konfigurace
     */
    public SetupWindow(ClientConfig clientConfig, MainWindow mainWindow, ClientThread clientThread) {
        super("dŽáma – Nastavení");
	setClientConfig(clientConfig);
	setClientThread(clientThread);
	setMainWindow(mainWindow);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            System.err.println("Error setting native LAF: " + e);
        }
        this.setIconImage(new ImageIcon(getClass().getResource("/resources/icon_setup.png")).getImage());

	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
	JPanel nickPanel = new JPanel();
	nickPanel.setLayout(new BoxLayout(nickPanel, BoxLayout.LINE_AXIS));
	JLabel nickLabel = new JLabel("Nick: ");
	nickLabel.setAlignmentY(CENTER_ALIGNMENT);
	nickPanel.add(nickLabel);
	nickField = new JTextField();
	nickField.setText(clientConfig.getLogin());
	nickField.setAlignmentY(CENTER_ALIGNMENT);
	nickPanel.add(nickField);
	mainPanel.add(nickPanel);
	JPanel serverPanel = new JPanel();
	serverPanel.setLayout(new BoxLayout(serverPanel, BoxLayout.LINE_AXIS));
	JLabel serverLabel = new JLabel("Server: ");
	serverLabel.setAlignmentY(CENTER_ALIGNMENT);
	serverPanel.add(serverLabel);
	serverField = new JTextField();
	serverField.setText(clientConfig.getHost());
	serverField.setAlignmentY(CENTER_ALIGNMENT);
	serverPanel.add(serverField);
	JLabel portLabel = new JLabel(" Port: ");
	portLabel.setAlignmentY(CENTER_ALIGNMENT);
	serverPanel.add(portLabel);
	portSpinner = new JSpinner(new SpinnerNumberModel(clientConfig.getPort(), 1024, 65535, 1));
	portSpinner.setAlignmentY(CENTER_ALIGNMENT);
	serverPanel.add(portSpinner);
	mainPanel.add(serverPanel);
	JPanel helpPanel = new JPanel();
	helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.LINE_AXIS));
	JLabel helpLabel = new JLabel("Nápověda: ");
	helpLabel.setAlignmentY(CENTER_ALIGNMENT);
	helpPanel.add(helpLabel);
	helpField = new JTextField();
	helpField.setText(clientConfig.getHelpUrl());
	helpField.setAlignmentY(CENTER_ALIGNMENT);
	helpPanel.add(helpField);
	mainPanel.add(helpPanel);
	JLabel noteLabel = new JLabel("Po potvrzení změn budete off-line!");
	mainPanel.add(noteLabel);
	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
	okButton = new JButton("Ok");
	okButton.addActionListener(this);
	buttonPanel.add(okButton);
	cancelButton = new JButton("Zrušit");
	cancelButton.addActionListener(this);
	buttonPanel.add(cancelButton);
	mainPanel.add(buttonPanel);
	this.add(mainPanel);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.repaint();
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
     * Nastavi hlavni okno
     *
     * @param mainWindow Hlavni okno hry
     */
    protected void setMainWindow(MainWindow mainWindow) {
	this.mainWindow = mainWindow;
    }

    /**
     * Vrati vlakno klienta
     *
     * @return Vlakno klienta
     */
    public ClientThread getClientThread() {
	return this.clientThread;
    }

    /**
     * Nastavi vlakno klienta
     *
     * @param clientThread Vlakno klienta
     */
    protected void setClientThread(ClientThread clientThread) {
	this.clientThread = clientThread;
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
     * Potvrzeni voleb
     */
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == okButton) {
	    getClientConfig().setLogin(nickField.getText());
	    getClientConfig().setHost(serverField.getText());
	    getClientConfig().setPort((Integer) portSpinner.getValue());
	    getClientConfig().setHelpUrl(helpField.getText());
	    getClientConfig().save();
	    getClientThread().disconnect();
	    getMainWindow().uncheckOnlineMenuItem();
	    getClientThread().setHost(clientConfig.getHost());
	    getClientThread().setPort((Integer) portSpinner.getValue());
	    getClientThread().setLogin(clientConfig.getLogin());
	    getMainWindow().setTitle("dŽáma – " + clientConfig.getLogin() + "@" + clientConfig.getHost());
	}
	dispose();
    }
}
