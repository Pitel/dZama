package vutbr.checkers.client.swing;


import javax.swing.*;
import javax.swing.event.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.net.*;
import java.io.*;
import java.awt.Dimension;

/**
 * Trida HelpWindow - okno s napovedou
 *
 * @author Jan Kalab <xkalab00@stud.fit.vutbr.cz>
 */
public class HelpWindow extends JFrame implements HyperlinkListener {
    JEditorPane htmlPane;

    /**
     * Konstruktor
     */
    public HelpWindow(String url) {
        super("dŽáma – nápověda");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            System.err.println("Error setting native LAF: " + e);
        }
        this.setIconImage(new ImageIcon(getClass().getResource("/resources/icon_help.png")).getImage());
	String ourl = "";
	if (url.equals("jar")) {
	    ourl = getClass().getResource("/resources/help/index.html").toString();
	} else {
	    ourl = url;
	}
        try {
            htmlPane = new JEditorPane(ourl);
            htmlPane.setEditable(false);
            htmlPane.addHyperlinkListener(this);
            this.add(new JScrollPane(htmlPane));
        } catch(IOException ioe) {
	    JOptionPane.showMessageDialog(this, "Soubor s nápovědou neexistuje", "D'oh!", JOptionPane.ERROR_MESSAGE);
        }

	this.setPreferredSize(new Dimension(600, 600));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.repaint();
    }

    /*
     * Obsluha kliknuti na odkaz
     *
     * @param event Udalost odkazu
     */
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                htmlPane.setPage(event.getURL());
            } catch(IOException ioe) {
                System.err.println("Page doesn't exist!");
            }
        }
    }
}
