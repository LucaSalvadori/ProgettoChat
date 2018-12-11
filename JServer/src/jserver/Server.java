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
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LS_Fisso
 */
public class Server extends Thread{
    private ServerSocket Server;
    protected ArrayList<Connect> conections= new ArrayList();
    protected ArrayList<String> conectionsDisconnected= new ArrayList();
    private ServerFrame sf;
    
    public Server(ServerFrame sf) throws Exception {
        this.sf = sf;
        Server = new ServerSocket(4000);
        sf.printLog("Server started on port 4000");
        this.start();
    }
    
    public void run() {

        while (true) {

            try {
                Socket client = Server.accept();
                sf.printLog(client.getInetAddress() + " is trying to connect");
                conections.add(new Connect(client,conections,conectionsDisconnected,this)); // dopo aver creato la classe che gestisce la connesione la salvo in una lista che sarà poi utile per fare il broadcast dei messagi
            } catch (IOException ex) {
                Logger.getLogger(JServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void clientConnected(String name){
        sf.addClient(name);
        sf.printLog(name + " has connected");
    }
    
    public void clientDisconnected(String name){
        sf.removeClient(name);
    }
    
    public void printLog(String log){
        sf.printLog(log);
    }
    
    public void kickClient(String name){
       for (Connect conection : conections) {
            if(conection.getClientName().equals(name)){
                conection.closeConection();
                return;
            }
        }
       sf.printLog(name + "kicked");
    }
    
    public void kickAll(){
        for (Connect conection : conections) {
            conection.closeConection();
        }
        sf.printLog("all client kicked");
    }
}
