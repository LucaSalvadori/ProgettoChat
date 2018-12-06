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
import java.util.Scanner;
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

    protected ClientFrame cf;

    private BufferedReader in;
    private PrintStream out;
    private Socket socket;
    private Thread Tr;
    private String name;

    public ClientConnection(ClientFrame cf) {
        this.cf = cf;
    }

    public void startConnection(String IP, int port, String hostName) { //defaul localhost 4000

        name = hostName;
        try {
            socket = new Socket(IP, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            String messaggio = in.readLine();//messaggio di hello
            System.out.println("Messaggio ricevuto: " + messaggio);

            out.println("<root><name>" + name + "</name></root>");//send to the server the hostname

            Tr = new Thread(new reader(in));//creo e avvio un tread responsabile della lettura
            Tr.start();

            //per Test da console
//            Scanner s = new Scanner(System.in);
//
//            while (true) {//aggiungere condizione di uscita
//                String mess = s.nextLine();
//
//                if (mess.equals("/close")) {//comando per chiudere il client
//                    closeConection();
//                    return;//interromo l'esecuzione (non elegante)
//                }
//                brodcastMessage(mess);//mando quello in input da console
//            }


        } catch (IOException ex) {
            Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void brodcastMessage(String message) {
        sendMessage(message, "");//se l'host è vuoto va in brodcast
    }

    public void sendMessage(String message, String Host) {
        out.println("<root><message to='" + Host + "'>" + message + "</message></root>");//mando messaggio all'host indicato
    }

    public void closeConection() {
        try {
            out.println("<root><close>0</close></root>");//mando il messaggio per chiudere la parte server
            Tr.stop();//fermo il thread che legge i messaggi (soluzione non elegante)
            out.close();//chiudo il resto
            in.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class reader implements Runnable { //codice da eseguire in parallelo per avere le funzioni di lettura

        BufferedReader in;

        public reader(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String messaggio = in.readLine();
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

                                for (int i = 0; i < ListaHost.getLength(); i++) {//la scorro
                                    System.out.println(ListaHost.item(i).getTextContent());//stampo host
                                }
                            }

                            campiFigli = d.getElementsByTagName("message").item(0);
                            if (campiFigli != null) {//se è presente un messaggio
                                System.out.println(campiFigli.getTextContent());//stampo messaggio
                                System.out.println(campiFigli.getAttributes().getNamedItem("name"));//stampo nome
                            }

                            campiFigli = d.getElementsByTagName("closing").item(0);
                            if (campiFigli != null) {//se è presente una richiesta di chiusura
                                //da aggiungere un metodo di chiusura
                            }
                        }
                    } catch (ParserConfigurationException ex) {
                    } catch (SAXException ex) {
                        Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
