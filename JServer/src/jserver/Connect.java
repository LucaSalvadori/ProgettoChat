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
        private BufferedReader in = null;
        private PrintStream out = null;
        private String host;
        private String name;
        private Server s;

        
        public void print(String message){
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
                    
                    name = in.readLine();//leggo l'hostname
                    
                    message = "<host>";
                    for (Connect conection : conections) {
                        message += conection.getHostName();
                    }
                    message += "</host>";
                    
                    out.println(message);//mando la lista al client (implementare aggiornamenti alla lista e invio di tali aggiornamenti ai client)
                    
                    while(true){//aggiungere condizione di uscita
                        message = name+"<message>"+in.readLine()+"</message>";//leggo un messaggio
                        System.out.println(message);//stampo a video
                        
                        
                        
                        
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
