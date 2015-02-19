/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

/**
 *
 * @author Andreas Fisker
 */
public class httpServer {

    static int port = 8080;
    static String ip = "127.0.0.1";
    static String contentFolder = "public/";

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            port = Integer.parseInt(args[1]);
            ip = args[0];
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Server.main(null);
            }
        }).start();
        
        HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);
        server.createContext("/OnlineUsers", new RequestUsersOnline());
        server.createContext("/ChatClient.jar", new RequestChatClient());
        server.createContext("/log", new RequestLogFile());
        server.createContext("/", new RequestWebsite());
        server.setExecutor(null); // Use the default executor
        server.start();
        System.out.println("Server started, listening on port: " + port);
    }
    
    static class RequestLogFile implements HttpHandler {

        public void handle(HttpExchange he) throws IOException {
            File file = new File("chatLog.txt");
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
        }
    }
    
    static class RequestUsersOnline implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String response;
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>Users Online</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            sb.append("<h2>There is "+Server.usersOnline()+" users online.</h2>\n");
            sb.append("</body>\n");
            sb.append("</html>\n");
            response = sb.toString();
            he.sendResponseHeaders(200, response.length());
            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(response); 
            }
        }
        
    }

    static class RequestWebsite implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            File file = new File(contentFolder + "index.html");
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                ie.printStackTrace();

            }
            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
        }
    }
    static class RequestChatClient implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            File file = new File(contentFolder + "ChatClient.jar");
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                ie.printStackTrace();

            }
            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
        }
    }

}
