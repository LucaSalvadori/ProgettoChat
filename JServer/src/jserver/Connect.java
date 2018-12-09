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

                while (true) {//aggiungere condizione di uscita
                    try {
                        
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputSource is = new InputSource(new StringReader(in.readLine()));
                        org.w3c.dom.Document doc = builder.parse(is);//faccio un parsing del messaggio

                        Element d = ((Element) doc.getElementsByTagName("root").item(0));//spachetto il tag root

                        if (d.hasChildNodes()) {//se il messaggio contiene qualcosa
                            NodeList CampiMessagio = d.getChildNodes();//estraggo i nodi

                            /* DEBUG (STAMPA MESSAGGIO COMPLETO)
                            for (int i = 0; i < CampiMessagio.getLength(); i++) {
                                System.out.println(CampiMessagio.item(i).toString());
                            }*/

                            Node campiFigli = d.getElementsByTagName("name").item(0);
                            if (campiFigli != null) {//se mi manda il suo hostname (primo collegamento)
                                String tmp = campiFigli.getTextContent();

                                for (Connect c : conections) {
                                    if (c.getHostName().equals(tmp)) {
                                        s.printMessage(tmp +" si è connesso con lo stesso nome di un utente già online.");
                                        out.println("<root><connection>refused</connection><reason>Name already online</reason></root>"); //Mando un messaggio di connessione rifiutata
                                        closeConection(0);
                                    }
                                }
                                out.println("<root><connection>accepted</connection></root>"); //Mando un messaggio di connessione accettata
                                name = tmp;
                                updateHostList();
                            }

                            campiFigli = d.getElementsByTagName("message").item(0);
                            if (campiFigli != null) {//se mi manda un messaggio
                                message = "<root>" + "<message " + "name='" + name + "' >" + campiFigli.getTextContent() + "</message>" + "</root>";
                                String to = campiFigli.getAttributes().getNamedItem("to").getTextContent();//prendo dagli attributi a chi il messaggio è destinato
                                s.printMessage("From: " + name + " | To: " + to + " | Message: " + message);//Stampo a video il messaggio

                                if (to.equals("")) {
                                    sendToAll(message); //se è in brodcast lo mando a tutti
                                } else {
                                    sendTo(to, message);
                                }
                            }

                            campiFigli = d.getElementsByTagName("connection").item(0);
                            if (campiFigli != null) {//se mi manda un messaggio di gestione connessione per chiudere la connessione
                                
                                switch(campiFigli.getTextContent()){
                                    case "close":
                                        s.printMessage("Closing conection with client " + name + "...");
                                        closeConection(0);
                                        return;
                                }
                                
                            }

                        }
                    } catch (ParserConfigurationException ex) {
                    } catch (SAXException ex) {
                        Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NullPointerException e) {//se qualche campo non è ben formattato.... (da migliorare)
                        s.printMessage("Message not well formatted");
                    }

                }

            } catch (IOException ex) {
                Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
                closeConection(1);
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

     /**
     *
     * @param state 0 closing by the client, 1 closing by the server
     *
     *
     */
    public synchronized void closeConection(int state) {
        if(state == 1){
            out.println("<root><connection>close</connection></root>");//mando il messaggio per chiudere la parte client
        }
        
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
