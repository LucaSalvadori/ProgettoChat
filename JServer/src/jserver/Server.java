/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LS_Fisso
 */
public class Server extends Thread{
    private ServerSocket Server;
    String host="";
    
    ArrayList<Connect> conections= new ArrayList<Connect>();
    
    
    public Server() throws Exception {
        System.out.println("jserver.Server.<init>()");
        Server = new ServerSocket(4000);
        System.out.println("Il server e' in attesa sulla porta 4000");
        this.start();
    }
    
    public void run() {

        while (true) {

            try {
                System.out.println("In attesa di connessione...");
                Socket client = Server.accept();
                System.out.println("Connessione accettata da: " + client.getInetAddress());
                conections.add(new Connect(client,host,conections)); // dopo aver creato la classe che gestisce la connesione la salvo in una lista che sarà poi utile per fare il broadcastdei messagi
            } catch (IOException ex) {
                Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
