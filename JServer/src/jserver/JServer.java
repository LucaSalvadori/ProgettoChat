/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver;

/**
 *
 * @author angelo.arena
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



public class JServer{
    public static String host="";//lista dei client (da mettere in un posto migliore)
    
    public static void main(String[] args) throws Exception {
        Server s = new Server();
    }
    
}
