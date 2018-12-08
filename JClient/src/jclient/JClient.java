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

import java.util.*;

public class JClient {

    // ClientFrame f = new ClientFrame(); //da implementare
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        //test
        Scanner s = new Scanner(System.in);

        ClientConnection cc = new ClientConnection(null);

        cc.startConnection("localhost", 4000, ("Host" + ((int) (Math.random() * 10))));

        cc.brodcastMessage("prova");
        for (int i = 0; i < 5; i++) {
            System.out.print("Send test Message to :");
            cc.sendMessage("test", s.nextLine());
        }

        cc.closeConection(0);
    }

}
