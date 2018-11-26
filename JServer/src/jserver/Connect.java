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
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



class Connect extends Thread {
    ArrayList<Connect> conections;
        private Socket client = null;
        BufferedReader in = null;
        PrintStream out = null;
        String host;
        String name;

        
        public void print(String message){
            out.println(message);
        }

        public Connect(Socket clienSocket, String host, ArrayList<Connect> conections) {
            this.conections = conections;
            this.host = host;
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
                    
                    name = in.readLine();//leggo l'hostname
                    
                    JServer.host += name;//lo aggiungo alla lista condivisa
                    
                    out.println("<host>"+JServer.host+"</host>");//mando la lista al client (implementare aggiornamenti alla lista e invio di tali aggiornamenti ai client)
                    
                    while(true){//aggiungere condizione di uscita
                        message = name+": "+in.readLine();//leggo un messaggio
                        System.out.println(message);//stmpo a video
                        
                        for (Connect conection : conections) {//lo mando a tutti i client connesi attraverso la lista delle classi conection
                            conection.print(message);
                        }
                        
                    }
                    
                    
//                    out.flush(); da implementare
//                    out.close();
//                    in.close();
//                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
