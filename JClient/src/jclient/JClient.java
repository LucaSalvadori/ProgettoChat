/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jclient;

/**
 *
 * @author angelo.arena
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JClient {

    
   // ClientFrame f = new ClientFrame(); //da implementare
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        BufferedReader in;
        PrintStream out;
        Socket socket;
        String messaggio;
        String name = "Host"+Math.random(); //solo  per test
        try {
            socket = new Socket("localhost", 4000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            messaggio = in.readLine();
            System.out.println("Messaggio ricevuto: " + messaggio);
            
            out.println("<name>"+name+"</name>");//send to the server the hostname
            
            System.out.println("Messaggio ricevuto: " + in.readLine());
            
            Scanner s = new Scanner(System.in);
            
            Thread Tr = new Thread(new reader(in));
            Tr.start();
            
            while(true){//aggiungere condizione di uscita
                out.println(s.nextLine());//mando quello in input da console
            }
            
//            out.close();
//            in.close();
        } catch (IOException ex) {
            Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static class reader implements Runnable{ //codice da eseguire in parallelo per avere le funzioni di lettura
        BufferedReader in;

        public reader(BufferedReader in) {
            this.in = in;
        }
        
        
        @Override
        public void run() {
            while(true){
                try {
                    System.out.println("Messaggio ricevuto: " + in.readLine());
                } catch (IOException ex) {
                    Logger.getLogger(JClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        
        
    }

}

