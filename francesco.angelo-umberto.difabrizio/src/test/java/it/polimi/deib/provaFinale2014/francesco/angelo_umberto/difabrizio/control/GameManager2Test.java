
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerThread;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Umberto
 */
public class GameManager2Test {
    
    public GameManager2Test() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of startGame method, of class GameManager2.
     */
    
    @Test
    public void testStartGame() {
        System.out.println("startGame");
        GameManager2 instance = new GameManager2(4, null);
        instance.startGame();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServer method, of class GameManager2.
     */
    @Ignore
    @Test
    public void testGetServer() {
        System.out.println("getServer");
        GameManager2 instance = null;
        ServerThread expResult = null;
        ServerThread result = instance.getServer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMap method, of class GameManager2.
     */
    @Ignore
    @Test
    public void testGetMap() {
        System.out.println("getMap");
        GameManager2 instance = null;
        Map expResult = null;
        Map result = instance.getMap();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
