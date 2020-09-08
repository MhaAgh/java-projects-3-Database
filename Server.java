/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpit305project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;

/**
 *
 * @author VIP
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket server = new ServerSocket(8800);
            System.out.println("\n-Server-");
            System.out.println("\n-----------------------------------------------------");
            System.out.println("Server waiting Connection...");
            int counter = 1;
            while (server != null) {
                // accept a socket 
                Socket client = server.accept();
                DBthread thread = new DBthread(client, counter);
                thread.start();
                counter++;
            }
            server.close();

        } catch (IllegalThreadStateException e) {
            System.err.println("Error: " + e.getStackTrace());
        } catch (UnknownHostException e) {
            System.err.println("Host not found");
        } catch (java.net.ConnectException e) {
            System.err.println("There are no connection at this port");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Exception exp) {
            System.err.println(exp.getMessage());
        }

    }
}
