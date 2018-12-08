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
    private Socket client = null;
    private BufferedReader in = null;
    private PrintStream out = null;
    private String host;
    private String name;
    private Server s;

    public void print(String message) {
        out.println(message);
    }

    public String getHostName() {
        return name;
    }

    public Connect(Socket clienSocket, ArrayList<Connect> conections, Server s) {
        this.conections = conections;
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
        this.start();
    }

    public void run() {
        String message;

        while (true) {
            try {
                out.println("Hello");
                name = "notSet " + conections.size();

//                name = in.readLine();//leggo l'hostname
//                updateHostList();
                while (true) {//aggiungere condizione di uscita
                    try {
                        
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputSource is = new InputSource(new StringReader(in.readLine()));
                        org.w3c.dom.Document doc = builder.parse(is);//faccio un parsing del messaggio

                        Element d = ((Element) doc.getElementsByTagName("root").item(0));//spachetto il tag root

                        if (d.hasChildNodes()) {//se il messaggio contiene qualcosa
                            NodeList CampiMessagio = d.getChildNodes();//estraggo i nodi

                            for (int i = 0; i < CampiMessagio.getLength(); i++) {
                                System.out.println(CampiMessagio.item(i).toString());
                            }

                            Node campiFigli = d.getElementsByTagName("name").item(0);
                            if (campiFigli != null) {//se mi manda il suo hostname
                                String tmp = campiFigli.getTextContent();

                                for (Connect c : conections) {
                                    if (c.getHostName().equals(tmp)) {
                                        System.out.println("Settato nome uguale ad un altra connessione"); //bisogna fare qualcosa
                                    }
                                }
                                name = tmp;
                                updateHostList();
                            }

                            campiFigli = d.getElementsByTagName("message").item(0);
                            if (campiFigli != null) {//se mi manda un messaggio
                                message = "<root>" + "<message " + "name='" + name + "' >" + campiFigli.getTextContent() + "</message>" + "</root>";
                                System.out.println("Message: " + message);//stampo a video
                                String to = campiFigli.getAttributes().getNamedItem("to").getTextContent();//prendo dagli attributi a chi il messaggio è destinato
                                System.out.println("From: " + name);//stampo a video
                                System.out.println("To: " + to);//stampo a video

                                if (to.equals("")) {
                                    sendToAll(message); //se è in brodcast
                                } else {
                                    sendTo(to, message);
                                }
                            }

                            campiFigli = d.getElementsByTagName("close").item(0);
                            if (campiFigli != null) {//se mi manda un messaggio
                                System.out.println("Closing conection " + name + "...");
                                closeConection();
                                return;
                            }

                        }
                    } catch (ParserConfigurationException ex) {
                    } catch (SAXException ex) {
                        Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NullPointerException e) {//se qualche campo non è ben formattato.... (da migliorare)
                        System.out.println("Message not well formatted");
                    }

                }

            } catch (IOException ex) {
                Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
                closeConection();
                break;
            }
        }

    }

    public void updateHostList() {
        String message;
        message = "<root><host>";
        for (Connect conection : conections) {
            message += "<name>" + conection.getHostName() + "</name>";
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
        conections.stream().filter((c) -> (c.getHostName().equalsIgnoreCase(Host))).forEachOrdered((c) -> {
            c.print(message);
        });
    }

    public synchronized void closeConection() {
        conections.remove(this);
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
