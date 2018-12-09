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
            String messaggio = in.readLine();//messaggio di hello
            System.out.println("Messaggio ricevuto: " + messaggio);

            out.println("<root><name>" + name + "</name></root>");//send to the server the hostname

            Tr = new Thread(new reader(this));//creo e avvio un tread responsabile della lettura
            Tr.start();

            //per Test da console
//            Scanner s = new Scanner(System.in);
//
//            while (true) {//aggiungere condizione di uscita
//                String mess = s.nextLine();
//
//                if (mess.equals("/close")) {//comando per chiudere il client
//                    closeConection(0);
//                    return;//interromo l'esecuzione (non elegante)
//                }
//                brodcastMessage(mess);//mando quello in input da console
            return true;
//            }
        } catch (IOException ex) {
            Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
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
            sendMessage(message, "");//se l'host è vuoto va in brodcast
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
            out.println("<root><message to='" + Host + "'>" + message + "</message></root>");//mando messaggio all'host indicato
        }
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
                    out.println("<root><close>0</close></root>");//mando il messaggio per chiudere la parte server
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

    public static class reader implements Runnable { //codice da eseguire in parallelo per avere le funzioni di lettura

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
                                }
                            }

                            campiFigli = d.getElementsByTagName("message").item(0);
                            if (campiFigli != null) {//se è presente un messaggio
                                System.out.println(campiFigli.getTextContent());//stampo messaggio
                                System.out.println(campiFigli.getAttributes().getNamedItem("name"));//stampo nome

                            }

                            campiFigli = d.getElementsByTagName("close").item(0);
                            if (campiFigli != null) {//se è presente una richiesta di chiusura
                                cc.closeConection(1);
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
