/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import shared.ProtocolStrings;

/**
 *
 * @author Andreas Fisker
 */
public class clientHandler extends Thread {

    Scanner input;
    PrintWriter writer;
    Socket socket;
    Server serv;
    String name;

    public clientHandler(Socket socket, Server serv) {
        try {
            this.socket = socket;
            this.serv = serv;
            input = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(clientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void send(String message) {
        writer.println(message);
    }

    public void run() {

        String message = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(Server.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals(ProtocolStrings.CLOSE)) {
            if (message.contains(ProtocolStrings.CONNECT)) {
                String[] str = message.split("#");
                name = str[str.length - 1];
                serv.allOnlineUsers();
            } else if (message.contains(ProtocolStrings.SEND)) {
                String[] str = message.split("#");

                if (str[1].equalsIgnoreCase("*")) {
                    
                    serv.sendAll(message, name);
                } else {
                    serv.send("MESSAGE#" + str[1] + "#" + str[str.length - 1], str[1]);
                }
            }

            Logger.getLogger(Server.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
            message = input.nextLine(); //IMPORTANT blocking call
        }
//        writer.println(ProtocolStrings.CLOSE);//Echo the stop message back to the client for a nice closedown
        try {
            socket.close();
            serv.removeHandler(this);
            serv.allOnlineUsers();
        } catch (IOException ex) {
            Logger.getLogger(clientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(Server.class.getName()).log(Level.INFO, "Closed a Connection");
    }

}
