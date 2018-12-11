/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
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

class Connect extends Thread {

    ArrayList<Connect> conections;
    ArrayList<String> conectionsDisconnected;
    private Socket client = null;
    private BufferedReader in = null;
    private PrintStream out = null;
    private String host;
    private String name;
    private Server s;

    public void print(String message) {
        out.println(message);
    }

    public String getClientName() {
        return name;
    }

    public Connect(Socket clienSocket, ArrayList<Connect> conections, ArrayList<String> conectionsDisconnected, Server s) {
        this.conections = conections;
        this.conectionsDisconnected = conectionsDisconnected;
        this.s = s;
        client = clienSocket;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintStream(client.getOutputStream(), true);
        } catch (IOException ex) {
            try {
                client.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            return;
        }
        name = "";
        this.start();
    }

    public void run() {
        String message;
        try {
            out.println("Hello");

            String NEWname = ((Element) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(in.readLine()))).getElementsByTagName("root").item(0)).getElementsByTagName("name").item(0).getTextContent();
            for (Connect c : conections) {
                if (c.getClientName().equals(NEWname)) {
                    s.printLog(NEWname + " is trying to connect with an already used name"); //bisogna fare qualcosa
                    out.println("<root><accepted>0</accepted></root>");
                    this.closeConection();
                    return;
                }
            }
            out.println("<root><accepted>1</accepted></root>");
            name = NEWname;
            s.clientConnected(name);
            
            for (int i = 0; i < conectionsDisconnected.size() ; i++) {
                if(conectionsDisconnected.get(i).equals(name)){
                    conectionsDisconnected.remove(i);
                    sendToAll("<root><connection><clientConnected>" + name + "</clientConnected></connection></root>");
                }
            }
            
            updateHostList();

            while (true) {//aggiungere condizione di uscita
                try {
                    String mess = in.readLine();
                    s.printLog(name + " says " + mess);
                    org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(mess)));
                    Element d = ((Element) doc.getElementsByTagName("root").item(0));//spachetto il tag root

                    if (d.hasChildNodes()) {//se il messaggio contiene qualcosa

                        Node campiFigli = d.getElementsByTagName("message").item(0);
                        if (campiFigli != null) {//se mi manda un messaggio per un client
                            String to = campiFigli.getAttributes().getNamedItem("to").getTextContent();//prendo dagli attributi a chi il messaggio è destinato
                            message = "<root>" + "<message " + "from='" + name + "' to='" + to + "' >" + campiFigli.getTextContent() + "</message>" + "</root>";

                            if (to.equals("Broadcast")) {
                                sendToAll(message); //se è in brodcast
                            } else {
                                sendTo(to, message);
                            }
                        }

                        campiFigli = d.getElementsByTagName("connection").item(0);
                        if (campiFigli != null) {//se mi manda un messaggio di gestione connessione
                            switch(campiFigli.getTextContent()){
                                case "close":
                                s.printLog(name + " asked to close connection. Closing connection");
                                closeConection();
                                return;
                            }
                        }

                    }
                } catch (ParserConfigurationException ex) {
                } catch (SAXException ex) {
                    Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullPointerException e) {//se qualche campo non è ben formattato.... (da migliorare)
                    s.printLog(name + " has send a message not well formatted");
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
            closeConection();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateHostList() {
        String message;
        message = "<root><host>";
        for (Connect conection : conections) {
            message += "<name>" + conection.getClientName() + "</name>";
        }
        message += "</host></root>";
        sendToAll(message);
    }

    public void sendToAll(String message) {
        conections.forEach((conection) -> {
            //lo mando a tutti i client connesi attraverso la lista delle classi conection
            conection.print(message);
        });
    }

    public void sendTo(String Host, String message) {
        conections.stream().filter((c) -> (c.getClientName().equals(Host))).forEachOrdered((c) -> {
            c.print(message);
        });
    }
    
    public synchronized void closeConection() {
        s.clientDisconnected(name);
        this.stop();
        sendToAll("<root><connection><clientDisconnected>" + name + "</clientDisconnected></connection></root>");
        conections.remove(this);
        conectionsDisconnected.add(name);
        out.flush();
        out.close();
        try {
            in.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
