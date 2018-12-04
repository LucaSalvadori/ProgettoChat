/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jclient;

/**
 * Matteo è mona
 *
 * @author angelo.arena
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jcabi.xml.*;
import javax.swing.text.Document;
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

            out.println("<name>" + name + "</name>");//send to the server the hostname

//            messaggio = in.readLine(); // da eliminare
//            System.out.println("Messaggio ricevuto: " + messaggio);

            Thread Tr = new Thread(new reader(in));
            Tr.start();
            
            
            
            
            

            Scanner s = new Scanner(System.in);

            

            while (true) {//aggiungere condizione di uscita
                out.println(s.nextLine());//mando quello in input da console
            }

//            out.close();
//            in.close();
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
                    
                    try{
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(messaggio));
                    org.w3c.dom.Document d = builder.parse(is);
                    
                    if(d.hasChildNodes()){
                        NodeList childNodes = d.getChildNodes();
                        
                        for (int i = 0; i < childNodes.getLength(); i++) {
                            System.out.println(childNodes.item(i).toString());
                        }
                        
                        
                        Node elementByTagName = d.getElementsByTagName("host").item(0);
                        if(elementByTagName!=null){
                            
                            Element eElement = (elementByTagName.getNodeType() == Node.ELEMENT_NODE)? (Element) elementByTagName :null;
                            
                            NodeList elementsByTagName = eElement.getElementsByTagName("name");

                            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                                System.out.println(elementsByTagName.item(i).getTextContent());
                            }
                        }
                        
                        
                        elementByTagName = d.getElementsByTagName("message").item(0);
                        if(elementByTagName!=null){
                            System.out.println(elementByTagName.getTextContent());
                        }
                        
                        
                        
                    }
                    
                    
                    
//                    
//                    Node nNode = d.getElementsByTagName("host").item(0);
//
//                    
                    
                    }catch(ParserConfigurationException ex){
                        
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
