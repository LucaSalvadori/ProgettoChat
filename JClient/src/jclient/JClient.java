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
    public static void main(String[] args) throws SAXException, ParserConfigurationException {

        BufferedReader in;
        PrintStream out;
        Socket socket;
        String messaggio;
        String name = "Host" + (int) (Math.random() * 10); //solo  per test
        try {
            socket = new Socket("localhost", 4000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            messaggio = in.readLine();//messaggio di hello
            System.out.println("Messaggio ricevuto: " + messaggio);

            out.println("<root><name>" + name + "</name></root>");//send to the server the hostname

//            messaggio = in.readLine(); // da eliminare
//            System.out.println("Messaggio ricevuto: " + messaggio);
            Thread Tr = new Thread(new reader(in));
            Tr.start();

            Scanner s = new Scanner(System.in);

            while (true) {//aggiungere condizione di uscita
                String mess = s.nextLine();

                if (mess.equals("/close")) {//comando per chiudere il client
                    out.println("<root><close>0</close></root>");//mando il messaggio per chiudere la parte server
                    Tr.stop();//fermo il thread che legge i messaggi (soluzione non elegante)
                    out.close();//chiudo il resto
                    in.close();
                    socket.close();
                    return;//interromo l'esecuzione (non elegante)
                }

                out.println("<root><message>" + mess + "</message></root>");//mando quello in input da console
            }

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
