package vutbr.checkers.client.swing;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * Trida XMLFilter - trida filtrovani XML zaznamu patrii
 *
 * @author Ondrej Novy <xnovyo01@stud.fit.vutbr.cz>
 */
public class XMLFilter extends FileFilter {
    /**
     * Overi zda tento soubor je typu XML zaznam partie
     *
     * @param f Soubor XML zaznamu partie
     */
    public boolean accept(File f) {
	if (f.isDirectory()) return true;
	String name = f.getName();
	int index = name.lastIndexOf('.');
	String extension = null;
	if (index > 0) {
	    extension = name.substring(index+1);
	}
	
	if (extension != null) {
	    if (extension.toLowerCase().equals("xml")) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }
    
    /**
     * Vrati popis souboroveho filtru
     *
     * @return Popis souboroveho filtru
     */
    public String getDescription() {
	return "XML záznam partie";
    }
}