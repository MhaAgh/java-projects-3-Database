/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpit305project;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author VIP
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Enter the number of your request: 1- Select. 2- Update. 3- Insert. 4- Delete. 5- Add bonus. 6- Get a description about the table. 0- Exit\n\n");
        try {
            Socket client = new Socket("127.0.0.1", 8800);
            Scanner in = new Scanner(client.getInputStream());
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            Scanner userInput = new Scanner(System.in);
            while (true) {
                String str2 = userInput.nextLine();
                out.println(str2);
                if (str2.trim().equalsIgnoreCase("0")) {
                    break;
                }

                String str = "";
                str = in.nextLine();
                System.out.println(str);
            }

            client.close();

        } catch (UnknownHostException e) {
            System.err.println("Host not found");
        } catch (java.net.ConnectException e) {
            System.err.println("There are no connection at this port");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
