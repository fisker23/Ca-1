package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import shared.ProtocolStrings;

public class Client extends Thread {

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    String name;
    List<Listener> listeners = new ArrayList();

    public void connect(String address, int port) throws UnknownHostException, IOException {
        name = JOptionPane.showInputDialog("Indtast dit navn!");
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        output.println("CONNECT#"+name);
        //   start();
    }

    public void send(String msg, String names) {
        output.println("SEND#"+names+"#"+msg);
    }

//    public void stopConnection() throws IOException {
//        output.println(ProtocolStrings.STOP);
//    }
    public void registerListener(Listener l) {
        listeners.add(l);
    }

    public void unRegisterListener(Listener l) {
        listeners.remove(l);
    }

    private void notifyListeners(String msg) {
        for (Listener l : listeners) {
            l.messageArrived(msg);

        }
    }

    public void run() {
        String msg = input.nextLine();
        while (!msg.equals(ProtocolStrings.CLOSE)) {
            notifyListeners(msg);
            msg = input.nextLine();
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        int port = 9090;
        String ip = "192.168.1.2";
        if (args.length == 2) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
        }
        Client cl = new Client();
        try {
            cl.connect(ip, port);
            cl.send("SKER DER FISSER", "BobK");
            cl.output.println("CLOSE#");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//public void startup(){
//    
//     int port = 9090;
//        String ip = "192.168.1.2";
////        if (args.length == 2) {
////            port = Integer.parseInt(args[0]);
////            ip = args[1];
////        }
//        try {
//            Client tester = new Client();
//            tester.connect(ip, port);
//            System.out.println("Sending 'Hello world'");
//            tester.send("Hello World");
//            System.out.println("Waiting for a reply");
//            //System.out.println("Received: " + tester.receive()); //Important Blocking call         
//           // tester.stop();
//            //System.in.read();      
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//}
//    public static void main(String[] args) {
//        int port = 9090;
//        String ip = "192.168.1.2";
//        if (args.length == 2) {
//            port = Integer.parseInt(args[0]);
//            ip = args[1];
//        }
//     //   try {
//            Listener tester = new Listener() {
//
//                @Override
//                public void messageArrived(String data) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//            };
////           registerListener(tester);
////            tester.connect(ip, port);
////            System.out.println("Sending 'Hello world'");
////            tester.send("Hello World");
////            System.out.println("Waiting for a reply");
////            System.out.println("Received: " + tester.receive()); //Important Blocking call         
////            tester.stop();
//            //System.in.read();      
////        } catch (UnknownHostException ex) {
//  //          Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//    //    } catch (IOException ex) {
//      //      Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//       // }
//    }
}
