package vutbr.checkers.client.swing;

import javax.swing.*;
import java.awt.event.*;

/**
 * Trida AboutWindow - Okno O programu
 *
 * @author Jan Kalab <xkalab00@stud.fit.vutbr.cz>
 */
public class AboutWindow extends JFrame implements ActionListener {
    /**
     * Konstruktor
     */
    public AboutWindow() {
        super("dŽáma – O programu");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            System.err.println("Error setting native LAF: " + e);
        }
        this.setIconImage(new ImageIcon(getClass().getResource("/resources/icon.png")).getImage());

        JButton splashButton = new JButton(new ImageIcon(getClass().getResource("/resources/splash.png")));
        splashButton.addActionListener(this);
        splashButton.setBorder(null);
        this.add(splashButton);

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.repaint();
    }

    /**
     * Kliknuti na splash
     */
    public void actionPerformed(ActionEvent e) {
        dispose();
    }
}
