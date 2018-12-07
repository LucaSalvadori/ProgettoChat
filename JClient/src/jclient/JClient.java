/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jclient;

/**
 *
 * @author luca
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class JClient {

    // ClientFrame f = new ClientFrame(); //da implementare
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ClientConnection cc = new ClientConnection(new ClientFrame());
        
        cc.startConnection("localhost", 4000, ("Host"+((int)(Math.random()*10))));
        
        cc.brodcastMessage("prova");
        cc.closeConection(0);
    }

}
