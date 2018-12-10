/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jclient;

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

/**
 *
 * @author LS_Fisso
 */
public class ClientConnection {

    protected ClientFrame cf; //utilizzato per eseguire metodi del form (sarebbe da pensare qualcosa di migliore)

    private BufferedReader in;
    private PrintStream out;
    private Socket socket;
    private Thread Tr;
    private String name;

    private boolean conectionOpened;//is the conection opened

    private ArrayList<String> onlineHost;

    public ClientConnection(ClientFrame cf) {
        this.cf = cf;
        onlineHost = new ArrayList();
        conectionOpened = false;
    }

    /**
     *
     * @param IP
     * @param port
     * @param hostName
     * @return true if the connection has been established
     */
    public boolean startConnection(String IP, int port, String hostName) { //defaul localhost 4000

        name = hostName;
        try {
            socket = new Socket(IP, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            conectionOpened = true;
            in.readLine();//messaggio di hello

            out.println("<root><name>" + name + "</name></root>");//send to the server the hostname
            String result = ((Element) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(in.readLine()))).getElementsByTagName("root").item(0)).getElementsByTagName("accepted").item(0).getTextContent();
            if(result.equals("0")){
                return false;
            }
            
            Tr = new Thread(new reader(this));//creo e avvio un tread responsabile della lettura
            Tr.start();
            
            return true;
//            }
        } catch (IOException ex) {
            Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
            conectionOpened = false;
            return false;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
            conectionOpened = false;
            return false;
        } catch (SAXException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
            conectionOpened = false;
            return false;
        }
    }

    /**
     * send a message in broadcast to all the host
     *
     * @param message
     */
    public void brodcastMessage(String message) {
        if (conectionOpened) {
            out.println("<root><message to='Broadcast' from='" + name + "'>" + message + "</message></root>");//mando messaggio all'host indicato
        }
    }

    public boolean isConectionOpened() {
        return conectionOpened;
    }

    /**
     *
     * @param message text to send to the server
     * @param Host name of the host of destination (if name is "" the message
     * will be broadcasted)
     */
    public void sendMessage(String message, String Host) {
        if (conectionOpened) {
            out.println("<root><message to='" + Host + "' from='" + name + "'>" + message + "</message></root>");//mando messaggio all'host indicato
        }
    }

    public String getName() {
        return name;
    }

    /**
     *
     * @param state 0 closing by the host 1 closing by the server -1 closing for
     * error
     *
     */
    public void closeConection(int state) {
        if (conectionOpened) {
            try {
                conectionOpened = false;
                if (state == 0) {
                    out.println("<root><connection>close</connection></root>");//mando il messaggio per chiudere la parte server
                } else {
                    cf.connectionClosing(state);
                }
                Tr.stop();//fermo il thread che legge i messaggi (soluzione non elegante)
                out.close();//chiudo il resto
                in.close();
                socket.close();

            } catch (IOException ex) {
                Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public ArrayList<String> getOnlineHost() {
        return onlineHost;
    }

    public class reader implements Runnable { //codice da eseguire in parallelo per avere le funzioni di lettura

        ClientConnection cc;

        public reader(ClientConnection cc) {
            this.cc = cc;
        }

        @Override
        public void run() {
            while (cc.isConectionOpened()) {
                try {
                    String messaggio = cc.in.readLine();
                    System.out.println("Messaggio ricevuto: " + messaggio);
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputSource is = new InputSource(new StringReader(messaggio));
                        org.w3c.dom.Document doc = builder.parse(is);//faccio un parsing del messaggio

                        Element d = ((Element) doc.getElementsByTagName("root").item(0));//spachetto il tag root

                        if (d.hasChildNodes()) {//se il messaggio contiene qualcosa
                            NodeList CampiMessagio = d.getChildNodes();//estraggo i nodi

                            for (int i = 0; i < CampiMessagio.getLength(); i++) {
                                System.out.println(CampiMessagio.item(i).toString());
                            }

                            Node campiFigli = d.getElementsByTagName("host").item(0);
                            if (campiFigli != null) {//se è presente la lista degli host

                                Element eElement = (campiFigli.getNodeType() == Node.ELEMENT_NODE) ? (Element) campiFigli : null;

                                NodeList ListaHost = eElement.getElementsByTagName("name");//prendo la lista degli host

                                cc.onlineHost.clear();//pulisco la lista degli host online

                                for (int i = 0; i < ListaHost.getLength(); i++) {//la scorro
                                    //System.out.println(ListaHost.item(i).getTextContent());//stampo host
                                    cc.onlineHost.add(ListaHost.item(i).getTextContent());//aggiungo gli hos alla lista
                                    //aggiungere update form
                                    cf.updateRooms();

                                }
                            }

                            campiFigli = d.getElementsByTagName("message").item(0);
                            if (campiFigli != null) {//se è presente un messaggio
                                String to = campiFigli.getAttributes().getNamedItem("to").getTextContent();
                                String from = campiFigli.getAttributes().getNamedItem("from").getTextContent();
                                if (!from.equals(name)) {
                                    cf.addMessageIn(campiFigli.getTextContent().replace('~', '\n'), from, to);
                                }
                            }
                            
                            campiFigli = d.getElementsByTagName("connection").item(0);
                            if (campiFigli != null) {//Se è presente un messaggio di gestione connessione
                                String clientName;
                                
                                switch(campiFigli.getFirstChild().getNodeName()){
                                    case "clientConnected":
                                        clientName = campiFigli.getFirstChild().getTextContent();
                                        cf.connected(clientName);
                                        break;
                                    case "clientDisconnected":
                                        clientName = campiFigli.getFirstChild().getTextContent();
                                        if(clientName.equals(name)){
                                            cc.closeConection(1);
                                        }
                                        else{
                                            cf.disconected(clientName);
                                        }
                                        break;
                                }
                            }
                        }
                    } catch (ParserConfigurationException ex) {
                    } catch (SAXException ex) {
                        Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
                    cc.closeConection(-1);
                } catch (NullPointerException e) {//se qualche campo non è ben formattato.... (da migliorare)
                    System.out.println("Message not well formatted. NullPointerException: " + e);
                }
            }
        }

    }
}
