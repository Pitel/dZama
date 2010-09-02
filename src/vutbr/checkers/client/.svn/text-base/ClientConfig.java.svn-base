package vutbr.checkers.client;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.*;
import java.io.*;

public class ClientConfig {
    private String configFile;

    private String host;
    private int port;
    private String login;
    private String helpUrl;

    /**
     * Konstruktor
     *
     * @param configFile Jmeno konfiguracniho souboru
     */
    public ClientConfig(String configFile) {
	setConfigFile(configFile);
	
	// Implicitni hodnoty
	setHost("");
	setPort(5690);
	setLogin("");
	setHelpUrl("jar");
	
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
		if (node.getNodeName().equals("host")) {
		    setHost(node.getTextContent());
		} else if (node.getNodeName().equals("port")) {
		    setPort(Integer.valueOf(node.getTextContent()));
		} else if (node.getNodeName().equals("login")) {
		    setLogin(node.getTextContent());
		} else if (node.getNodeName().equals("helpUrl")) {
		    setHelpUrl(node.getTextContent());
		}
	    
		node = node.getNextSibling();
	    }
	} catch (ParserConfigurationException e) {
	    System.out.println("ParserConfigurationException: " + e.getMessage());	
	} catch (IOException e) {
	    System.out.println("Konfiguracni soubor nenalezen, vytvarim implicitni");
	    save();
	} catch (SAXException e) {
	    System.out.println("Sax exception: " + e.getMessage());
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
	    Element root = doc.createElementNS(null, "client");

	    Element host = doc.createElementNS(null, "host");
	    host.appendChild(doc.createTextNode(getHost()));
	    root.appendChild(host);

	    Element port = doc.createElementNS(null, "port");
	    port.appendChild(doc.createTextNode(Integer.toString(getPort())));
	    root.appendChild(port);
	    
	    Element login = doc.createElementNS(null, "login");
	    login.appendChild(doc.createTextNode(getLogin()));
	    root.appendChild(login);
	    
	    Element helpUrl = doc.createElementNS(null, "helpUrl");
	    helpUrl.appendChild(doc.createTextNode(getHelpUrl()));
	    root.appendChild(helpUrl);
	    
	    doc.appendChild(root);
	    
	    Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	    StreamResult result = new StreamResult(new FileWriter(getConfigFile()));
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
     * Nastavi host
     *
     * @param host Host
     */
    public void setHost(String host) {
	this.host = host;
    }
    
    /**
     * Vrati host
     *
     * @return Host
     */
    public String getHost() {
	return this.host;
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
     * Nastavi login hrace
     *
     * @param login Login hrace
     */
    public void setLogin(String login) {
	this.login = login;
    }
    
    /**
     * Vrati login hrace
     *
     * @return Login
     */
    public String getLogin() {
	return this.login;
    }

    /**
     * Nastavi URL napovedy
     *
     * @param helpUrl URL napovedy
     */
    public void setHelpUrl(String helpUrl) {
	this.helpUrl = helpUrl;
    }
    
    /**
     * Vrati URL napovedy
     *
     * @return URL napovedy
     */
    public String getHelpUrl() {
	return this.helpUrl;
    }
}
