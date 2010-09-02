package vutbr.checkers.client.swing;

import vutbr.checkers.client.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Trida LoginWindow - prihlasovaci okno
 *
 * @author Jan Kalab <xkalab00@stud.fit.vutbr.cz>
 */
public class LoginWindow extends JFrame implements ActionListener {
    private JTextField nickField, serverField;
    private JSpinner portSpinner;
    private JButton loginButton, registerButton;
    private ClientConfig clientConfig;

    /**
     * Konstruktor
     *
     * @param clientConfig Konfigurace
     */
    public LoginWindow(ClientConfig clientConfig) {
        super("dŽáma – přihlášení");

        setClientConfig(clientConfig);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            System.err.println("Error setting native LAF: " + e);
        }
        this.setIconImage(new ImageIcon(getClass().getResource("/resources/icon_login.png")).getImage());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
            JLabel splashLabel = new JLabel(new ImageIcon(
		getClass().getResource("/resources/splash.png")));
            splashLabel.setAlignmentX(CENTER_ALIGNMENT);
            mainPanel.add(splashLabel);
            JPanel nickPanel = new JPanel();
            nickPanel.setLayout(new BoxLayout(nickPanel, BoxLayout.LINE_AXIS));
                JLabel nickLabel = new JLabel("Nick: ");
                nickLabel.setAlignmentY(CENTER_ALIGNMENT);
                nickPanel.add(nickLabel);
                nickField = new JTextField();
                nickField.setText(clientConfig.getLogin());
                nickField.addActionListener(this);
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
                serverField.addActionListener(this);
                serverField.setAlignmentY(CENTER_ALIGNMENT);
                serverPanel.add(serverField);
		JLabel portLabel = new JLabel(" Port: ");
                portLabel.setAlignmentY(CENTER_ALIGNMENT);
                serverPanel.add(portLabel);
                portSpinner = new JSpinner(new SpinnerNumberModel(clientConfig.getPort(), 1024, 65535, 1));
                portSpinner.setAlignmentY(CENTER_ALIGNMENT);
                serverPanel.add(portSpinner);
            mainPanel.add(serverPanel);
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
            loginButton = new JButton("Přihlásit");
            loginButton.addActionListener(this);
            registerButton = new JButton("Registrovat");
            registerButton.addActionListener(this);
            buttonsPanel.add(registerButton);
            buttonsPanel.add(loginButton);
            mainPanel.add(buttonsPanel);

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton ||
		e.getSource() == registerButton ||
		e.getSource() == nickField ||
		e.getSource() == serverField) {
            if (nickField.getText().equals("") || serverField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Nejsou vyplněna obě pole!", "D'oh!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            getClientConfig().setLogin(nickField.getText());
            getClientConfig().setHost(serverField.getText());
	    getClientConfig().setPort((Integer) portSpinner.getValue());
            getClientConfig().save();
            ClientThread clientThread = new ClientThread(serverField.getText(), 5690, nickField.getText(), e.getSource()==registerButton);
            new MainWindow(clientThread, getClientConfig());
            this.dispose();
        }
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
}
