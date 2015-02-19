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
import shared.ProtocolStrings;

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
        msg = "";
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
        assertEquals("MESSAGE#"+client.getClientName()+"#Vi tester nu send metoden!", msg);
        
    }
    @Test
    public void closeClient() throws IOException{
        Client client = new Client();
        client.registerListener(this);
        client.connect("localhost", 9090);
        client.start();
        String firstName = client.getClientName();
        client.stopConnection();
        Client client2 = new Client();
        client2.registerListener(this);
        client2.connect("localhost", 9090);
        client2.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(test123.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("First name: "+firstName+"\tSecond name: "+client2.getClientName());
        System.out.println(msg);
        assertFalse(msg.contains(firstName));
        
    }
    

    @Override
    public void messageArrived(String data) {
        msg = data;
    }
}
