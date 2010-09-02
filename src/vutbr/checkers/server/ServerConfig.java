package vutbr.checkers.server;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.*;
import java.io.*;

public class ServerConfig {
    private String configFile;

    private int port;
    private String bindAddress;

    /**
     * Konstruktor
     *
     * @param configFile Jmeno konfiguracniho souboru
     */
    public ServerConfig(String configFile) {
	setConfigFile(configFile);
	
	// Implicitni hodnoty
	setPort(5690);
	setBindAddress("0.0.0.0");
	
	// Nacteni XML konfigurace
	load();
    }
    
    /**
     * Nacte konfiguracni soubor
     */
    public void load() {
	try {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    
	    Document doc = db.parse(getConfigFile());
	    Node node = doc.getDocumentElement().getFirstChild();
	    while (node != null) {
		if (node.getNodeName().equals("port")) {
		    setPort(Integer.valueOf(node.getTextContent()));
		} else if (node.getNodeName().equals("bindAddress")) {
		    setBindAddress(node.getTextContent());
		}
	    
		node = node.getNextSibling();
	    }
	} catch (ParserConfigurationException e) {
	    System.err.println("ParserConfigurationException: " + e.getMessage());	
	} catch (IOException e) {
	    System.out.println("Konfiguracni soubor nenalezen, vytvarim implicitni");
	    save();
	} catch (SAXException e) {
	    System.err.println("Sax exception: " + e.getMessage());
	}
    }
    
    /**
     * Ulozi konfiguracni soubor
     */
    public void save() {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
	try {
    	    DocumentBuilder db = dbf.newDocumentBuilder();
    	    Document doc = db.newDocument();

	    // Root element
	    Element root = doc.createElementNS(null, "server");

	    Element port = doc.createElementNS(null, "port");
	    port.appendChild(doc.createTextNode(Integer.toString(getPort())));
	    root.appendChild(port);
	    
	    Element bindAddress = doc.createElementNS(null, "bindAddress");
	    bindAddress.appendChild(doc.createTextNode(getBindAddress()));
	    root.appendChild(bindAddress);
	    
	    doc.appendChild(root);
	    
	    Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	    StreamResult result = new StreamResult(new FileWriter(getConfigFile()));
	    DOMSource source = new DOMSource(doc);
	    transformer.transform(source, result);
	} catch (TransformerConfigurationException e) {
	    System.err.println("TransformerConfigurationException");	
	} catch (TransformerException e) {
	    System.err.println("TransformerException");	
	} catch (IOException e) {
	    System.err.println("IO exception: " + e.getMessage());
	} catch (ParserConfigurationException pce) {
	    System.err.println("ParserConfigurationException");
	}
    }
    
    /**
     * Nastavi jmeno konfiguracniho souboru
     *
     * @param configFile Jmeno konfiguracniho souboru
     */
    private void setConfigFile(String configFile) {
	this.configFile = configFile;
    }
    
    /**
     * Vrati jmeno konfiguracniho souboru
     *
     * @return Jmeno konfiguracniho souboru
     */
    private String getConfigFile() {
	return this.configFile;
    }
    
    /**
     * Nastavi port
     *
     * @param port Port
     */
    public void setPort(int port) {
	this.port = port;
    }
    
    /**
     * Vrati port
     *
     * @return Port
     */
    public int getPort() {
	return this.port;
    }
    
    /**
     * Nastavi bind adresu
     *
     * @param bindAddress Bind adresa
     */
    public void setBindAddress(String bindAddress) {
	this.bindAddress = bindAddress;
    }
    
    /**
     * Vrati bind adresu
     *
     * @return Bind adresa
     */
    public String getBindAddress() {
	return this.bindAddress;
    }
}
