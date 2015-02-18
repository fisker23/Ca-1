package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;
import utils.Utils;

public class Server {

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Utils.initProperties("server.properties");
    private List<clientHandler> chl = new ArrayList();

    public static void stopServer() {
        keepRunning = false;
    }
    
    public void allOnlineUsers(){
        String all = "";
        for (clientHandler ch : chl) {
            all += ch.name+",";
        }
        for (clientHandler ch : chl) {
            ch.send(ProtocolStrings.ONLINE+all);
        }
      
    }
    
    public void removeHandler(clientHandler ch){
        chl.remove(ch);
    }
    
    private void runServer() {
        
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");

        Logger.getLogger(Server.class.getName()).log(Level.INFO, "Sever started. Listening on: " + port + ", bound to: " + ip);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(Server.class.getName()).log(Level.INFO, "Connected to a client");
         //       handleClient(socket);
                clientHandler ch = new clientHandler(socket, this);
                ch.start();
                chl.add(ch);
            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void sendAll(String message, String sender){
         String[] str = message.split(ProtocolStrings.SEPERATOR);
         
         for (clientHandler chl1 : chl) {
             chl1.send(ProtocolStrings.MESSAGE+sender+ProtocolStrings.SEPERATOR+str[str.length-1]);
         }
    }
     public void send(String message, String names, String sender){
         
         for (clientHandler ch : chl) {
             if (names.contains(ch.name)) {
                 ch.send(ProtocolStrings.MESSAGE + sender + ProtocolStrings.SEPERATOR + message);
             }
         }
     }

    public static void main(String[] args) {
        try {
            String logFile = properties.getProperty("logFile");
            Utils.setLogFile(logFile, Server.class.getName());
            new Server().runServer();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            Utils.closeLogger(Server.class.getName());
        }
    }
}
