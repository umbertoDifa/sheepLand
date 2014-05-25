
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Umberto
 */
public class GameManagerTest {
    ServerSocket server;
    Socket client;
    int port = 5050;
    
    public GameManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
       
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws IOException {
         //crea una connessione server client
        server = new ServerSocket(port);
        client = new Socket("127.0.0.1", port);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of startGame method, of class GameManager.
     */
    @Test
    public void testStartGame() {
        System.out.println("startGame");
        List<Socket> clients = new ArrayList<Socket>();
        GameManager instance = new GameManager(3, new ServerThread(clients));
        instance.startGame();
    }

    /**
     * Test of askStreet method, of class GameManager.
     */
    @Test
    public void testAskStreet() throws Exception {
        System.out.println("askStreet");
        int playerHashCode = 0;
        int idShepherd = 0;
        GameManager instance = null;
        Street expResult = null;
        Street result = instance.askStreet(playerHashCode, idShepherd);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of askAboutRegion method, of class GameManager.
     */
    @Test
    public void testAskAboutRegion() throws Exception {
        System.out.println("askAboutRegion");
        int playerHashCode = 0;
        String message = "";
        GameManager instance = null;
        Region expResult = null;
        Region result = instance.askAboutRegion(playerHashCode, message);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServer method, of class GameManager.
     */
    @Test
    public void testGetServer() {
        System.out.println("getServer");
        GameManager instance = null;
        ServerThread expResult = null;
        ServerThread result = instance.getServer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMap method, of class GameManager.
     */
    @Test
    public void testGetMap() {
        System.out.println("getMap");
        GameManager instance = null;
        Map expResult = null;
        Map result = instance.getMap();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of askCancelOrRetry method, of class GameManager.
     */
    @Test
    public void testAskCancelOrRetry() throws Exception {
        System.out.println("askCancelOrRetry");
        int hashCode = 0;
        String message = "";
        GameManager instance = null;
        instance.askCancelOrRetry(hashCode, message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of askIdShepherd method, of class GameManager.
     */
    @Test
    public void testAskIdShepherd() {
        System.out.println("askIdShepherd");
        int hashCode = 0;
        int numShepherd = 0;
        String message = "";
        GameManager instance = null;
        int expResult = 0;
        int result = instance.askIdShepherd(hashCode, numShepherd, message);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of askAndThrowDice method, of class GameManager.
     */
    @Test
    public void testAskAndThrowDice() {
        System.out.println("askAndThrowDice");
        int playerHashCode = 0;
        GameManager instance = null;
        int expResult = 0;
        int result = instance.askAndThrowDice(playerHashCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPlayerByShepherd method, of class GameManager.
     */
    @Test
    public void testGetPlayerByShepherd() {
        System.out.println("getPlayerByShepherd");
        Shepherd shepherd = null;
        GameManager instance = null;
        Player expResult = null;
        Player result = instance.getPlayerByShepherd(shepherd);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
