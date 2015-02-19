/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Server.Server;
import client.Client;
import client.ClientGUI;
import client.Listener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Uffe
 */
public class test123 implements Listener{
    private String msg = "";
    public test123() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable(){
      @Override
      public void run() {
        Server.main(new String[]{"localhost", "9090"});
      }
    }).start();
    }
    
    @AfterClass
    public static void tearDownClass() {
        Server.stopServer();
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
       
    }
    @Test
    public void online() throws IOException{
        Client client = new Client();
        client.registerListener(this);
        client.connect("localhost", 9090);
        client.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(test123.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(msg);
        assertTrue(msg.startsWith("ONLINE#"));
        
    }
    @Test
    public void send() throws IOException{
        Client client = new Client();
        client.registerListener(this);
        client.connect("localhost", 9090);
        client.start();
        client.send("Vi tester nu send metoden!", "*");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(test123.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(msg);
        assertEquals("MESSAGE#"+client.getClientName()+"#Vi tester nu send metoden!", msg);
        
    }
    @Test
    public void close() throws IOException{
        Client client = new Client();
        client.registerListener(this);
        client.connect("localhost", 9090);
        client.start();
        client.stopConnection();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(test123.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(msg);
        assertEquals("CLOSE#", msg);
        
    }
    

    @Override
    public void messageArrived(String data) {
        msg = data;
        System.out.println(msg + "   : DET HER ER BESKEDEN YO");
    }
}
